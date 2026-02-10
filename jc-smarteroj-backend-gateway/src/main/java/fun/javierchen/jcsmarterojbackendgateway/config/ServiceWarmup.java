package fun.javierchen.jcsmarterojbackendgateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;

/**
 * 服务预热组件
 * 
 * <p>
 * 在网关启动完成后，自动预热各个微服务的连接，
 * 解决首次请求因服务发现和连接建立导致的延迟问题。
 * 同时提供定时保活功能，防止长时间空闲导致连接失效。
 * </p>
 *
 * @author JavierChen
 */
@Component
@EnableScheduling
public class ServiceWarmup {

    private static final Logger log = LoggerFactory.getLogger(ServiceWarmup.class);

    @Resource
    private DiscoveryClient discoveryClient;

    @Resource
    private WarmupProperties warmupProperties;

    private final WebClient webClient = WebClient.builder()
            .build();

    /**
     * 应用启动完成后执行预热
     */
    @EventListener(ApplicationReadyEvent.class)
    public void warmupServices() {
        List<String> services = warmupProperties.getServices();

        // 如果未配置预热服务列表，跳过预热
        if (services == null || services.isEmpty()) {
            log.info("[服务预热] 未配置预热服务列表，跳过预热");
            return;
        }

        log.info("[服务预热] 开始预热微服务连接，服务数量: {}", services.size());
        long startTime = System.currentTimeMillis();

        Flux.fromIterable(services)
                .flatMap(this::warmupService)
                .collectList()
                .doOnSuccess(results -> {
                    long duration = System.currentTimeMillis() - startTime;
                    log.info("[服务预热] 预热完成，耗时: {}ms", duration);
                })
                .doOnError(error -> {
                    log.warn("[服务预热] 预热过程中出现错误: {}", error.getMessage());
                })
                .subscribe();
    }

    /**
     * 定期刷新服务发现缓存 - 每3分钟执行一次
     * 
     * <p>
     * LoadBalancer 缓存 TTL 设置为 35 秒，但我们每 3 分钟主动刷新一次，
     * 确保服务实例列表始终是最新的，同时保持连接活跃。
     * </p>
     */
    @Scheduled(fixedRate = 180000) // 3分钟 = 180000ms
    public void keepServicesAlive() {
        List<String> services = warmupProperties.getServices();
        if (services == null || services.isEmpty()) {
            return;
        }

        log.debug("[服务保活] 开始刷新服务发现缓存...");

        for (String serviceName : services) {
            try {
                List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
                if (!instances.isEmpty()) {
                    ServiceInstance instance = instances.get(0);
                    // 发起健康检查请求保持连接活跃
                    int managementPort = resolveManagementPort(serviceName);
                    String url = String.format("http://%s:%d/actuator/health",
                            instance.getHost(), managementPort);

                    webClient.get()
                            .uri(url)
                            .retrieve()
                            .bodyToMono(String.class)
                            .timeout(Duration.ofSeconds(3))
                            .doOnError(e -> log.trace("[服务保活] {} 健康检查失败(可忽略)", serviceName))
                            .onErrorResume(e -> Mono.empty())
                            .subscribe();
                }
            } catch (Exception e) {
                log.trace("[服务保活] 刷新 {} 失败: {}", serviceName, e.getMessage());
            }
        }
    }

    /**
     * 预热单个服务
     */
    private Mono<String> warmupService(String serviceName) {
        return Mono.fromCallable(() -> {
            try {
                // 1. 从服务发现获取实例（这会触发服务发现缓存）
                List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
                if (instances.isEmpty()) {
                    log.warn("[服务预热] 服务 {} 未找到实例", serviceName);
                    return serviceName + ": no instances";
                }

                // 2. 获取第一个实例信息
                ServiceInstance instance = instances.get(0);
                log.info("[服务预热] 服务 {} 发现 {} 个实例，首实例: {}:{}",
                        serviceName, instances.size(), instance.getHost(), instance.getPort());

                return serviceName + ": ok";
            } catch (Exception e) {
                log.warn("[服务预热] 服务 {} 预热失败: {}", serviceName, e.getMessage());
                return serviceName + ": error";
            }
        }).timeout(Duration.ofSeconds(5))
                .onErrorResume(e -> {
                    log.warn("[服务预热] 服务 {} 预热超时", serviceName);
                    return Mono.just(serviceName + ": timeout");
                })
                // 3. 尝试发起一个健康检查请求来预热 HTTP 连接
                .flatMap(result -> warmupHttpConnection(serviceName).thenReturn(result));
    }

    /**
     * 发起 HTTP 请求预热连接
     */
    private Mono<Void> warmupHttpConnection(String serviceName) {
        return Mono.defer(() -> {
            try {
                List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
                if (instances.isEmpty()) {
                    return Mono.empty();
                }
                ServiceInstance instance = instances.get(0);
                int managementPort = resolveManagementPort(serviceName);
                String url = String.format("http://%s:%d/actuator/health",
                        instance.getHost(), managementPort);

                return webClient.get()
                        .uri(url)
                        .retrieve()
                        .bodyToMono(String.class)
                        .timeout(Duration.ofSeconds(3))
                        .doOnSuccess(body -> log.info("[服务预热] HTTP 连接预热成功: {}", serviceName))
                        .doOnError(e -> log.debug("[服务预热] HTTP 连接预热失败(可忽略): {} - {}",
                                serviceName, e.getMessage()))
                        .onErrorResume(e -> Mono.empty())
                        .then();
            } catch (Exception e) {
                return Mono.empty();
            }
        });
    }

    private int resolveManagementPort(String serviceName) {
        Integer port = warmupProperties.getManagementPorts().get(serviceName);
        if (port != null) {
            return port;
        }
        Integer fallback = warmupProperties.getManagementPort();
        return fallback != null ? fallback : 9999;
    }
}

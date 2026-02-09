package fun.javierchen.jcsmarterojbackendgateway.sentinel;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayParamFlowItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Sentinel 网关限流配置类
 * 
 * 根据规划方案配置以下限流规则：
 * 1. 路由级限流：user-service(200 QPS)、question-service(150 QPS)、judge-service(100
 * QPS)
 * 2. API 分组限流：短信接口(10 QPS)、题目查询(80 QPS)、判题提交(100 QPS)
 * 3. 热点参数限流：单 IP/用户 细粒度控制
 * 
 * @author JavierChen
 * @since 2026-01-30
 */
@Configuration
@Slf4j
public class SentinelGatewayConfig {

    private final RateLimitMetricsCollector metricsCollector;

    public SentinelGatewayConfig(RateLimitMetricsCollector metricsCollector) {
        this.metricsCollector = metricsCollector;
    }

    // 注意：sentinelGatewayFilter 和 sentinelGatewayBlockExceptionHandler
    // 已由 SentinelSCGAutoConfiguration 自动配置，无需手动定义

    /**
     * 初始化限流规则和 API 分组
     * 在 Spring 容器初始化后执行
     */
    @PostConstruct
    public void initGatewayRules() {
        log.info("[Sentinel] 开始初始化网关限流规则...");

        // 1. 初始化 API 分组定义
        initApiDefinitions();

        // 2. 初始化限流规则
        initFlowRules();

        // 3. 配置自定义限流响应
        initBlockHandler();

        log.info("[Sentinel] 网关限流规则初始化完成");
    }

    /**
     * 定义 API 分组
     * 将相关接口归组以便统一配置限流策略
     */
    private void initApiDefinitions() {
        Set<ApiDefinition> definitions = new HashSet<>();

        // ====== 短信发送接口分组 ======
        // 路径: POST /api/user/sms/send
        // 风险: 调用第三方短信服务产生费用
        ApiDefinition smsApi = new ApiDefinition("sms_api")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {
                    {
                        add(new ApiPathPredicateItem()
                                .setPattern("/api/user/sms/**")
                                .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
                    }
                });
        definitions.add(smsApi);

        // ====== 题目查询接口分组 ======
        // 路径: POST /api/question/list/page/vo, GET /api/question/get/vo
        // 风险: 数据库查询压力，防止爬虫抓取
        ApiDefinition questionQueryApi = new ApiDefinition("question_query_api")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {
                    {
                        // 题目列表查询
                        add(new ApiPathPredicateItem()
                                .setPattern("/api/question/list/**")
                                .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
                        // 题目详情查询
                        add(new ApiPathPredicateItem()
                                .setPattern("/api/question/get/**")
                                .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
                    }
                });
        definitions.add(questionQueryApi);

        // ====== 判题提交接口分组 ======
        // 路径: POST /api/question/question_submit/do
        // 风险: 消耗计算资源，Judge0 调用限额
        ApiDefinition judgeSubmitApi = new ApiDefinition("judge_submit_api")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {
                    {
                        add(new ApiPathPredicateItem()
                                .setPattern("/api/question/question_submit/do")
                                .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_EXACT));
                    }
                });
        definitions.add(judgeSubmitApi);

        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
        log.info("[Sentinel] 已加载 {} 个 API 分组定义", definitions.size());
    }

    /**
     * 初始化网关限流规则
     * 根据压测数据配置合理的 QPS 阈值
     */
    private void initFlowRules() {
        Set<GatewayFlowRule> rules = new HashSet<>();

        // ==================== 路由级限流规则 ====================

        // 1. User Service 路由限流 (200 QPS)
        // 理由: 短信接口低频，用户相关操作余量充足
        rules.add(new GatewayFlowRule("jc-smarteroj-backend-user-service")
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_ROUTE_ID)
                .setCount(200) // QPS 上限
                .setIntervalSec(1)); // 统计时间窗口：1秒

        // 2. Question Service 路由限流 (150 QPS)
        // 理由: 实测查询 27 QPS + 提交 131 QPS ≈ 160，取 150 作为安全上限
        rules.add(new GatewayFlowRule("jc-smarteroj-backend-question-service")
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_ROUTE_ID)
                .setCount(150)
                .setIntervalSec(1));

        // 3. Judge Service 路由限流 (100 QPS)
        // 理由: 提交接口实测 131 QPS，保守取 100
        rules.add(new GatewayFlowRule("jc-smarteroj-backend-judge-service")
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_ROUTE_ID)
                .setCount(100)
                .setIntervalSec(1));

        // ==================== API 分组限流规则 ====================

        // 4. 短信接口全局限流 (10 QPS)
        // 理由: 短信通道整体吞吐上限，控制费用
        rules.add(new GatewayFlowRule("sms_api")
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME)
                .setCount(10)
                .setIntervalSec(1));

        // 5. 短信接口单 IP 限流 (5次/60秒)
        // 理由: 同一 IP 1分钟内最多发送 5 次短信
        rules.add(new GatewayFlowRule("sms_api")
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME)
                .setCount(5)
                .setIntervalSec(60) // 60秒时间窗口
                .setParamItem(new GatewayParamFlowItem()
                        .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)));

        // 6. 题目查询全局限流 (80 QPS)
        // 理由: 实测 27 QPS，取 3 倍余量
        rules.add(new GatewayFlowRule("question_query_api")
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME)
                .setCount(80)
                .setIntervalSec(1));

        // 7. 题目查询单 IP 限流 (20次/60秒)
        // 理由: 防止爬虫高频抓取题库数据
        rules.add(new GatewayFlowRule("question_query_api")
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME)
                .setCount(20)
                .setIntervalSec(60)
                .setParamItem(new GatewayParamFlowItem()
                        .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)));

        // 8. 判题提交全局限流 (100 QPS)
        // 理由: 实测 131 QPS，保守取 100
        rules.add(new GatewayFlowRule("judge_submit_api")
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME)
                .setCount(100)
                .setIntervalSec(1));

        // 9. 判题提交单用户限流 (3次/10秒)
        // 理由: 用户提交冷却，防止刷题攻击
        // 注意：这里使用请求头中的用户 ID (需要在 Filter 中注入)
        rules.add(new GatewayFlowRule("judge_submit_api")
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME)
                .setCount(3)
                .setIntervalSec(10)
                .setParamItem(new GatewayParamFlowItem()
                        .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_HEADER)
                        .setFieldName("X-User-Id"))); // 从请求头获取用户 ID

        GatewayRuleManager.loadRules(rules);
        log.info("[Sentinel] 已加载 {} 条限流规则", rules.size());
    }

    /**
     * 配置限流降级响应
     * 返回统一的 JSON 格式错误响应，并记录限流指标
     */
    private void initBlockHandler() {
        // 将 metricsCollector 引用捕获到 final 变量
        final RateLimitMetricsCollector collector = this.metricsCollector;

        GatewayCallbackManager.setBlockHandler(new BlockRequestHandler() {
            @Override
            public Mono<ServerResponse> handleRequest(ServerWebExchange exchange, Throwable t) {
                String path = exchange.getRequest().getURI().getPath();

                // 获取客户端 IP
                String clientIp = getClientIp(exchange);

                // 获取用户 ID（从请求头中获取）
                String userId = exchange.getRequest().getHeaders().getFirst("X-User-Id");

                log.warn("[Sentinel] 请求被限流: IP={}, User={}, Path={}, Reason={}",
                        clientIp, userId, path, t.getMessage());

                // 记录限流指标（用于自动加入黑名单判断）
                if (collector != null) {
                    // 根据路径判断限流维度
                    if (path.contains("/question_submit/do") && userId != null) {
                        // 判题接口以用户维度统计
                        collector.recordUserRateLimit(userId, path);
                    } else {
                        // 短信接口、题目查询以 IP 维度统计
                        collector.recordIpRateLimit(clientIp, path);
                    }
                }

                // 构建标准错误响应 (与文档规定格式一致)
                Map<String, Object> response = new HashMap<>();
                response.put("code", 429);
                response.put("message", "请求过于频繁，请稍后重试");
                response.put("data", null);

                return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(response));
            }

            /**
             * 获取客户端真实 IP
             */
            private String getClientIp(ServerWebExchange exchange) {
                // 优先从 X-Real-Client-IP 请求头获取（由 BlacklistFilter 注入）
                String ip = exchange.getRequest().getHeaders().getFirst("X-Real-Client-IP");
                if (ip != null && !ip.isEmpty()) {
                    return ip;
                }

                // 直接从连接获取
                if (exchange.getRequest().getRemoteAddress() != null) {
                    return exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
                }

                return "unknown";
            }
        });
        log.info("[Sentinel] 自定义限流响应处理器已配置（含指标收集）");
    }
}

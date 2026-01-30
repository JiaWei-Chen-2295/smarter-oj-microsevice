package fun.javierchen.jcsmarterojbackendgateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 网关预热配置属性
 *
 * @author JavierChen
 */
@Component
@ConfigurationProperties(prefix = "gateway.warmup")
public class WarmupProperties {

    /**
     * 需要预热的服务列表
     */
    private List<String> services = new ArrayList<>();

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }
}

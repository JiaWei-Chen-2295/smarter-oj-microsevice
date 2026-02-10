package fun.javierchen.jcsmarterojbackendgateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    /**
     * 默认管理端口
     */
    private Integer managementPort = 9999;

    /**
     * 指定服务的管理端口
     */
    private Map<String, Integer> managementPorts = new HashMap<>();

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public Integer getManagementPort() {
        return managementPort;
    }

    public void setManagementPort(Integer managementPort) {
        this.managementPort = managementPort;
    }

    public Map<String, Integer> getManagementPorts() {
        return managementPorts;
    }

    public void setManagementPorts(Map<String, Integer> managementPorts) {
        this.managementPorts = managementPorts;
    }
}

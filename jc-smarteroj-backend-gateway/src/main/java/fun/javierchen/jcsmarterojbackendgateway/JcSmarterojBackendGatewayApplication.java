package fun.javierchen.jcsmarterojbackendgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import fun.javierchen.jcsmarterojbackendgateway.config.WarmupProperties;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableDiscoveryClient
@EnableConfigurationProperties(WarmupProperties.class)
public class JcSmarterojBackendGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(JcSmarterojBackendGatewayApplication.class, args);
    }
}

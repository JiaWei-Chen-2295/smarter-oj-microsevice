package fun.javierchen.jcsmarterojbackendgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
public class JcSmarterojBackendGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(JcSmarterojBackendGatewayApplication.class, args);
    }
}

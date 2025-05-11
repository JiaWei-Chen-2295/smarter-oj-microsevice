package fun.javierchen.jcsmarterojbackenduserservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("fun.javierchen.jcsmarterojbackenduserservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
//@ComponentScan("fun.javierchen")
@EnableDiscoveryClient
public class JcSmarterojBackendUserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(JcSmarterojBackendUserServiceApplication.class, args);
    }
}

package fun.javierchen.jcojbackendpostservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("fun.javierchen.jcojbackendpostservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan({ "fun.javierchen.jcojbackendcommon", "fun.javierchen.jcojbackendpostservice" })
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"fun.javierchen.jcojbackendserverclient"})
public class JcSmarterojBackendPostServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(JcSmarterojBackendPostServiceApplication.class, args);
    }
}

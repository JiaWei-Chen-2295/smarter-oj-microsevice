package fun.javierchen.jcojbackendroomservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("fun.javierchen.jcojbackendroomservice.mapper")
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan({ "fun.javierchen.jcojbackendcommon", "fun.javierchen.jcojbackendroomservice" })
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "fun.javierchen.jcojbackendserverclient")
public class JcSmarterojBackendRoomServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JcSmarterojBackendRoomServiceApplication.class, args);
    }
}


package fun.javierchen.jcojbackendquestionservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("fun.javierchen.jcojbackendquestionservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
//@ComponentScan({"fun.javierchen.jcojbackendcommon", "fun.javierchen.jcojbackendquestionservice"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "fun.javierchen.jcojbackendserverclient")
public class JcSmarterojBackendQuestionServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(JcSmarterojBackendQuestionServiceApplication.class, args);
    }
}

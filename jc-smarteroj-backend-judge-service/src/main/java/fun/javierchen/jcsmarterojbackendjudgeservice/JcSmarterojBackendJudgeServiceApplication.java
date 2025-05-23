package fun.javierchen.jcsmarterojbackendjudgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
//@ComponentScan({"fun.javierchen.jcojbackendcommon", "fun.javierchen.jcsmarterojbackendjudgeservice"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "fun.javierchen.jcojbackendserverclient")
public class JcSmarterojBackendJudgeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JcSmarterojBackendJudgeServiceApplication.class, args);
    }

}

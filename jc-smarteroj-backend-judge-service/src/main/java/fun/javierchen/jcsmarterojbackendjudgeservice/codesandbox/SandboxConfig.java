package fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class SandboxConfig {
    @Value("${codesandbox.type}")
    private String type;

    @Value("${codesandbox.remote-url}")
    private String remoteUrl;
}
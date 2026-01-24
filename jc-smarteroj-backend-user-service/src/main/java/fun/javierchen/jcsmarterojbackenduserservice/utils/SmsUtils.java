package fun.javierchen.jcsmarterojbackenduserservice.utils;

import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dypnsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dypnsapi20170525.models.SendSmsVerifyCodeRequest;
import com.aliyun.sdk.service.dypnsapi20170525.models.SendSmsVerifyCodeResponse;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Component
@ConfigurationProperties(prefix = "aliyun.sms")
@Data
@Slf4j
public class SmsUtils {

    private String accessKeyId;
    private String accessKeySecret;
    private String endpoint = "dypnsapi.aliyuncs.com"; // Default endpoint for dypnsapi
    private String signName;
    private String templateCode;
    private boolean simulate = false;

    private AsyncClient client;

    public void initClient() {
        if (client == null && !simulate) {
            try {
                StaticCredentialProvider provider = StaticCredentialProvider.create(
                        com.aliyun.auth.credentials.Credential.builder()
                                .accessKeyId(accessKeyId)
                                .accessKeySecret(accessKeySecret)
                                .build());

                client = AsyncClient.builder()
                        .region("cn-hangzhou") // You might need to make this configurable or default to a region
                        .credentialsProvider(provider)
                        .overrideConfiguration(
                                ClientOverrideConfiguration.create()
                                        .setEndpointOverride(endpoint)
                                        .setConnectTimeout(Duration.ofSeconds(30)))
                        .build();

            } catch (Exception e) {
                log.error("Aliyun SMS Client Init Failed", e);
            }
        }
    }

    public boolean sendSms(String phone, String code) {
        if (simulate) {
            log.info("Simulate sending SMS to {}: {}", phone, code);
            return true;
        }

        initClient();
        if (client == null) {
            log.error("Aliyun SMS Client is not initialized.");
            return false;
        }

        try {
            // Using "##code##" as placeholder for the code parameter if templateParam has
            // ## around keys,
            // but standard templates usually use ${code}.
            // The user's example used `{\"code\":\"##code##\",\"min\":\"5\"}` and had
            // `templateParam` set explicitly.
            // Assuming standard usage: {"code":"123456"}

            // Adjusting based on user example:
            // .templateParam("{\"code\":\"##code##\",\"min\":\"5\"}")
            // Typically dynamic params are passed here.

            SendSmsVerifyCodeRequest sendSmsVerifyCodeRequest = SendSmsVerifyCodeRequest.builder()
                    .signName(signName)
                    .templateCode(templateCode)
                    .phoneNumber(phone)
                    .templateParam(String.format("{\"code\":\"%s\",\"min\":\"5\"}", code))
                    .build();

            CompletableFuture<SendSmsVerifyCodeResponse> response = client.sendSmsVerifyCode(sendSmsVerifyCodeRequest);
            SendSmsVerifyCodeResponse resp = response.get();

            if (resp.getBody().getSuccess()) {
                return true;
            } else {
                log.error("SMS Send Failed: Code={}, Message={}", resp.getBody().getCode(),
                        resp.getBody().getMessage());
                return false;
            }

        } catch (Exception e) {
            log.error("SMS Send Exception", e);
            return false;
        }
    }
}

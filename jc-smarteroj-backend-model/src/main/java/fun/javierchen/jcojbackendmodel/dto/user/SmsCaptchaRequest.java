package fun.javierchen.jcojbackendmodel.dto.user;

import lombok.Data;
import java.io.Serializable;

/**
 * 发送短信验证码请求
 */
@Data
public class SmsCaptchaRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 行为验证码校验数据
     */
    private String captchaVerification;
}

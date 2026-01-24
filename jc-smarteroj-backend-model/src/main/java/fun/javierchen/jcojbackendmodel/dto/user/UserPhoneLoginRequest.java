package fun.javierchen.jcojbackendmodel.dto.user;

import lombok.Data;
import java.io.Serializable;

/**
 * 手机号登录请求
 */
@Data
public class UserPhoneLoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 短信验证码
     */
    private String captcha;
}

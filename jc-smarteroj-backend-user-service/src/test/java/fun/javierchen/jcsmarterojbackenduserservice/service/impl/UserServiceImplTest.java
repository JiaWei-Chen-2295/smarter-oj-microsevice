package fun.javierchen.jcsmarterojbackenduserservice.service.impl;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import fun.javierchen.jcojbackendmodel.dto.user.SmsCaptchaRequest;
import fun.javierchen.jcojbackendmodel.dto.user.UserPhoneLoginRequest;
import fun.javierchen.jcojbackendmodel.vo.LoginUserVO;
import fun.javierchen.jcsmarterojbackenduserservice.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class UserServiceImplTest {

    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @MockBean
    private CaptchaService captchaService;

    @Test
    public void testSendSmsAndLogin() {
        // TODO: 请填写您的真实手机号进行测试
        String phone = "19726862329";
        if ("".equals(phone)) {
            System.out.println("请在 UserServiceImplTest.java 中填写手机号 phone 变量");
            return;
        }

        // 1. Mock CaptchaService (绕过行为验证码校验)
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRepCode("0000");
        Mockito.when(captchaService.verification(any(CaptchaVO.class))).thenReturn(responseModel);

        // 2. 构造发送验证码请求
        SmsCaptchaRequest smsRequest = new SmsCaptchaRequest();
        smsRequest.setPhone(phone);
        smsRequest.setCaptchaVerification("mock-token");

        // 3. 发送验证码 (如果 application.yml 中 aliyun.sms.simulate=false，则会真实发送)
        boolean sendResult = userService.sendSmsCaptcha(smsRequest);
        Assertions.assertTrue(sendResult, "短信发送失败，请检查配置或日志");

        // 4. 从 Redis 获取验证码 (无论是模拟还是真实发送，验证码都会存入 Redis)
        String captchaKey = "sms:captcha:" + phone;
        String code = stringRedisTemplate.opsForValue().get(captchaKey);
        Assertions.assertNotNull(code, "Redis 中未找到验证码");
        System.out.println("【测试】获取到的验证码: " + code);

        // 5. 使用验证码登录
        UserPhoneLoginRequest loginRequest = new UserPhoneLoginRequest();
        loginRequest.setPhone(phone);
        loginRequest.setCaptcha(code);

        LoginUserVO loginUserVO = userService.userLoginByPhone(loginRequest, null);

        // 验证登录结果
        Assertions.assertNotNull(loginUserVO, "登录返回为空");
        Assertions.assertEquals(phone, loginUserVO.getPhone(), "登录用户手机号不匹配");
        System.out.println("【测试】登录成功，用户 ID: " + loginUserVO.getId());

        // 清理数据
        stringRedisTemplate.delete(captchaKey);
        stringRedisTemplate.delete("sms:limit:phone:" + phone);
    }
}

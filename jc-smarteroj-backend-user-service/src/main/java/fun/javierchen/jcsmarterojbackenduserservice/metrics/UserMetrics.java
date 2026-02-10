package fun.javierchen.jcsmarterojbackenduserservice.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

/**
 * 用户服务自定义指标.
 */
@Component
public class UserMetrics {

    private static final String RESULT_SUCCESS = "success";
    private static final String RESULT_FAIL = "fail";

    private final Counter registerCounter;
    private final Counter loginSuccessCounter;
    private final Counter loginFailCounter;

    public UserMetrics(MeterRegistry meterRegistry) {
        this.registerCounter = meterRegistry.counter("oj.user.register.total");
        this.loginSuccessCounter = meterRegistry.counter("oj.user.login.total", "result", RESULT_SUCCESS);
        this.loginFailCounter = meterRegistry.counter("oj.user.login.total", "result", RESULT_FAIL);
    }

    public void recordRegister() {
        registerCounter.increment();
    }

    public void recordLoginSuccess() {
        loginSuccessCounter.increment();
    }

    public void recordLoginFail() {
        loginFailCounter.increment();
    }
}

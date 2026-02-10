package fun.javierchen.jcsmarterojbackendjudgeservice.metrics;

import fun.javierchen.jcojbackendmodel.dto.questionsubmit.JudgeInfo;
import fun.javierchen.jcojbackendmodel.enums.JudgeInfoMessageEnum;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

/**
 * 判题服务自定义指标.
 */
@Component
public class JudgeMetrics {

    private static final String RESULT_SUCCESS = "success";
    private static final String RESULT_FAIL = "fail";
    private static final String RESULT_ERROR = "error";

    private final MeterRegistry meterRegistry;
    private final Counter submitCounter;
    private final Counter successCounter;
    private final Counter failCounter;
    private final Counter errorCounter;
    private final Timer executionTimer;

    public JudgeMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.submitCounter = meterRegistry.counter("oj.judge.submit.total");
        this.successCounter = meterRegistry.counter("oj.judge.result.total", "result", RESULT_SUCCESS);
        this.failCounter = meterRegistry.counter("oj.judge.result.total", "result", RESULT_FAIL);
        this.errorCounter = meterRegistry.counter("oj.judge.result.total", "result", RESULT_ERROR);
        this.executionTimer = Timer.builder("oj.judge.execution.duration")
                .description("Judge execution duration")
                .publishPercentiles(0.5, 0.9, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);
    }

    public void incrementSubmit() {
        submitCounter.increment();
    }

    public void recordResultSuccess() {
        successCounter.increment();
    }

    public void recordResultFail() {
        failCounter.increment();
    }

    public void recordResultError() {
        errorCounter.increment();
    }

    public Timer.Sample startExecution() {
        return Timer.start(meterRegistry);
    }

    public void stopExecution(Timer.Sample sample) {
        sample.stop(executionTimer);
    }

    public boolean isAccepted(JudgeInfo judgeInfo) {
        if (judgeInfo == null || judgeInfo.getMessage() == null) {
            return false;
        }
        String message = judgeInfo.getMessage();
        return JudgeInfoMessageEnum.ACCEPTED.getText().equals(message)
                || JudgeInfoMessageEnum.ACCEPTED.getValue().equals(message)
                || "Accepted".equalsIgnoreCase(message);
    }
}

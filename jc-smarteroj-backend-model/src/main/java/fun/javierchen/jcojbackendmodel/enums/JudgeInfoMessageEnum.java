package fun.javierchen.jcojbackendmodel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum JudgeInfoMessageEnum {

    ACCEPTED("成功", "Accepted"),
    WRONG_ANSWER("答案错误", "Wrong Answer"),
    COMPILE_ERROR("编译错误", "Compile Error"),
    TIME_LIMIT_EXCEEDED("时间超限", "Time Limit Exceeded"),
    MEMORY_LIMIT_EXCEEDED("内存超限", "Memory Limit Exceeded"),
    PRESENTATION_ERROR("格式错误", "Presentation Error"),
    RUNTIME_ERROR("运行时错误", "Runtime Error"),
    SYSTEM_ERROR("系统错误", "System Error"),
    WAITING("等待中", "Waiting");

    private final String text;
    private final String value;
}

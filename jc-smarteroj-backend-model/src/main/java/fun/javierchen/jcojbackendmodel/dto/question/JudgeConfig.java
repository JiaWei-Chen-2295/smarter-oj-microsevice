package fun.javierchen.jcojbackendmodel.dto.question;

import lombok.Data;

@Data
public class JudgeConfig {
    /**
     * 题目时间限制 单位 ms
     */
    private Long timeLimit;
    /**
     * 题目内存限制 单位 KB
     */
    private Long memoryLimit;
    /**
     * 题目堆栈限制 单位 KB
     */
    private Long stackLimit;
}

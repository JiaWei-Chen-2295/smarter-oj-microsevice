package fun.javierchen.jcojbackendmodel.dto.question;

import lombok.Data;

import java.util.List;

/**
 * FPS XML 中单个题目的解析结果
 * 对应 DTD 中的 item 元素
 *
 * @author JavierChen
 */
@Data
public class FpsItem {
    /**
     * 题目标题
     */
    private String title;

    /**
     * 题目链接 (可选)
     */
    private String url;

    /**
     * 时间限制值
     */
    private Long timeLimit;

    /**
     * 时间限制单位 (s, ms)
     */
    private String timeLimitUnit;

    /**
     * 内存限制值
     */
    private Long memoryLimit;

    /**
     * 内存限制单位 (mb, kb)
     */
    private String memoryLimitUnit;

    /**
     * 题目描述
     */
    private String description;

    /**
     * 输入说明
     */
    private String input;

    /**
     * 输出说明
     */
    private String output;

    /**
     * 样例输入列表
     */
    private List<String> sampleInputs;

    /**
     * 样例输出列表
     */
    private List<String> sampleOutputs;

    /**
     * 测试输入列表 (用于评测)
     */
    private List<String> testInputs;

    /**
     * 测试输出列表 (用于评测)
     */
    private List<String> testOutputs;

    /**
     * 提示信息
     */
    private String hint;

    /**
     * 题目来源
     */
    private String source;

    /**
     * 参考答案列表 (可能有多种语言)
     */
    private List<FpsSolution> solutions;
}

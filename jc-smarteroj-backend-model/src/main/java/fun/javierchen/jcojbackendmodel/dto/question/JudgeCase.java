package fun.javierchen.jcojbackendmodel.dto.question;

import lombok.Data;

/**
 * 测试用例类
 */
@Data
public class JudgeCase {
    /**
     * 测试用例的输入
     */
    private String input;
    /**
     * 测试用例期待的输出
     */
    private String output;
}

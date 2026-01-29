package fun.javierchen.jcojbackendmodel.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 单个题目的导入结果
 *
 * @author JavierChen
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionImportResult {
    /**
     * 题目在 XML 中的序号 (从1开始)
     */
    private Integer index;

    /**
     * 题目标题
     */
    private String title;

    /**
     * 是否导入成功
     */
    private Boolean success;

    /**
     * 导入后的题目 ID (成功时有效)
     */
    private Long questionId;

    /**
     * 失败原因 (失败时有效)
     */
    private String errorMessage;

    /**
     * 创建成功结果
     */
    public static QuestionImportResult success(int index, String title, Long questionId) {
        return new QuestionImportResult(index, title, true, questionId, null);
    }

    /**
     * 创建失败结果
     */
    public static QuestionImportResult fail(int index, String title, String errorMessage) {
        return new QuestionImportResult(index, title, false, null, errorMessage);
    }
}

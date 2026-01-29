package fun.javierchen.jcojbackendmodel.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 批量导入题目的响应结果
 *
 * @author JavierChen
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionBatchImportResponse {
    /**
     * 总题目数
     */
    private Integer totalCount;

    /**
     * 成功导入数
     */
    private Integer successCount;

    /**
     * 失败数
     */
    private Integer failCount;

    /**
     * 每个题目的导入结果
     */
    private List<QuestionImportResult> results;
}

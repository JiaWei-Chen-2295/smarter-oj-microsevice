package fun.javierchen.jcojbackendmodel.dto.question;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新请求
 *
 * @author JavierChen
 */
@Data
public class QuestionUpdateRequest implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 判题配置(JSON 对象)
     */
    private JudgeConfig judgeConfig;

    /**
     * 测试用例(JSON 数组)
     */
    private List<JudgeCase> judgeCase;

    /**
     * 问题参考答案
     */
    private String answer;

    private static final long serialVersionUID = 1L;
}
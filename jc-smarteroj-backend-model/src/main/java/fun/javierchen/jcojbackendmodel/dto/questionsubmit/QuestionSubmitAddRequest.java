package fun.javierchen.jcojbackendmodel.dto.questionsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 帖子点赞请求
 *
 * @author JavierChen
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {
    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 提交的语言
     */
    private String language;

    /**
     * 用户提交的代码
     */
    private String code;



    private static final long serialVersionUID = 1L;
}
package fun.javierchen.jcojbackendmodel.dto.questionsubmit;


import fun.javierchen.jcojbackendcommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionSubmitQueryRequest extends PageRequest {
    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 提交的用户id
     */
    private Long userId;

    /**
     * 提交的语言
     */
    private String language;

    /**
     * 判题状态0-待判题 1-判题中 2-成功 3-失败
     */
    private Integer status;


}

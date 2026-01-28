package fun.javierchen.jcojbackendmodel.vo;

import cn.hutool.json.JSONUtil;
import fun.javierchen.jcojbackendmodel.dto.question.JudgeCase;
import fun.javierchen.jcojbackendmodel.dto.questionsubmit.JudgeInfo;
import fun.javierchen.jcojbackendmodel.entity.QuestionSubmit;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class QuestionSubmitVO {
    /**
     * id
     */
    private Long id;

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
     * 用户提交的代码
     */
    private String code;

    /**
     * 判题状态0-待判题 1-判题中 2-成功 3-失败
     */
    private Integer status;

    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;

    /**
     * 提交者信息
     */
    private UserVO userVO;

    /**
     * 提交的问题信息
     */
    private QuestionVO questionVO;

    /**
     * 运行后的结果
     */
    private String outputResult;

    /**
     * 题目测试用例
     */
    private java.util.List<JudgeCase> judgeCaseList;

    /**
     * 包装类转对象
     *
     * @param questionSubmitVO
     * @return
     */
    public static QuestionSubmit voToObj(QuestionSubmitVO questionSubmitVO) {
        if (questionSubmitVO == null) {
            return null;
        }
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitVO, questionSubmit);
        JudgeInfo judgeInfo = questionSubmitVO.getJudgeInfo();
        String judgeInfoStr = JSONUtil.toJsonStr(judgeInfo);
        questionSubmit.setJudgeInfo(judgeInfoStr);
        return questionSubmit;
    }

    /**
     * 对象转包装类
     *
     * @param questionSubmit
     * @return
     */
    public static QuestionSubmitVO objToVo(QuestionSubmit questionSubmit) {
        if (questionSubmit == null) {
            return null;
        }
        QuestionSubmitVO questionSubmitVO = new QuestionSubmitVO();
        BeanUtils.copyProperties(questionSubmit, questionSubmitVO);
        String judgeInfoStr = questionSubmit.getJudgeInfo();
        JudgeInfo judgeInfo = JSONUtil.toBean(judgeInfoStr, JudgeInfo.class);
        questionSubmitVO.setJudgeInfo(judgeInfo);
        return questionSubmitVO;
    }
}

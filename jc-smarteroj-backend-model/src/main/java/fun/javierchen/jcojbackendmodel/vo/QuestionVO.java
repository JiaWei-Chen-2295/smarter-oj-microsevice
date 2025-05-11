package fun.javierchen.jcojbackendmodel.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import fun.javierchen.jcojbackendmodel.dto.question.JudgeConfig;
import fun.javierchen.jcojbackendmodel.entity.Question;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * 题目封装类
 */
@Data
public class QuestionVO {
    /**
     * id
     */
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
     * 提交总次数
     */
    private Integer submitNum;

    /**
     * 通过人数
     */
    private Integer acceptedNum;

    /**
     * 判题配置(JSON 对象)
     */
    private JudgeConfig judgeConfig;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建这个题目的用户
     */
    private UserVO userVO;

    /**
     * 包装类转对象
     *
     * @param questionVO
     * @return
     */
    public static Question voToObj(QuestionVO questionVO) {
        if (questionVO == null) {
            return null;
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionVO, question);
        List<String> tags = questionVO.getTags();
        if (CollectionUtils.isNotEmpty(tags)) {
            question.setTags(JSONUtil.toJsonStr(tags));
        }

        JudgeConfig judgeConfig = questionVO.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
        }

        return question;
    }

    /**
     * 对象转包装类
     *
     * @param question
     * @return
     */
    public static QuestionVO objToVo(Question question) {
        if (question == null) {
            return null;
        }
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question, questionVO);

        String tagsStr = question.getTags();
        if (StringUtils.isNotBlank(tagsStr)) {
            questionVO.setTags(JSONUtil.toList(tagsStr, String.class));
        }
        String judgeConfig = question.getJudgeConfig();
        if (StringUtils.isNotBlank(judgeConfig)) {
            questionVO.setJudgeConfig(JSONUtil.toBean(judgeConfig, JudgeConfig.class));
        }
        return questionVO;
    }
}

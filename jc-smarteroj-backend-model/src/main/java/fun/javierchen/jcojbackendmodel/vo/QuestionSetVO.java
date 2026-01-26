package fun.javierchen.jcojbackendmodel.vo;

import cn.hutool.json.JSONUtil;
import fun.javierchen.jcojbackendmodel.entity.QuestionSet;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class QuestionSetVO implements Serializable {

    private Long id;

    private String title;

    private String description;

    private List<String> tags;

    private Integer questionNum;

    private Long userId;

    private Integer favourNum;

    private Date createTime;

    private UserVO userVO;

    private List<QuestionVO> questions;

    public static QuestionSetVO objToVo(QuestionSet questionSet) {
        if (questionSet == null) {
            return null;
        }
        QuestionSetVO questionSetVO = new QuestionSetVO();
        BeanUtils.copyProperties(questionSet, questionSetVO);
        String tagsStr = questionSet.getTags();
        if (StringUtils.isNotBlank(tagsStr)) {
            questionSetVO.setTags(JSONUtil.toList(tagsStr, String.class));
        }
        return questionSetVO;
    }

    public static QuestionSet voToObj(QuestionSetVO questionSetVO) {
        if (questionSetVO == null) {
            return null;
        }
        QuestionSet questionSet = new QuestionSet();
        BeanUtils.copyProperties(questionSetVO, questionSet);
        List<String> tags = questionSetVO.getTags();
        if (tags != null) {
            questionSet.setTags(JSONUtil.toJsonStr(tags));
        }
        return questionSet;
    }

    private static final long serialVersionUID = 1L;
}


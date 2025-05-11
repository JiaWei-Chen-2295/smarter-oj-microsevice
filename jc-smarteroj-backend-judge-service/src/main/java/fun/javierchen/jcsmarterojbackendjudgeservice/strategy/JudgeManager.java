package fun.javierchen.jcsmarterojbackendjudgeservice.strategy;


import fun.javierchen.jcojbackendcommon.enums.QuestionSubmitLanguageEnum;
import fun.javierchen.jcojbackendmodel.dto.questionsubmit.JudgeInfo;
import fun.javierchen.jcojbackendmodel.entity.QuestionSubmit;
import fun.javierchen.jcsmarterojbackendjudgeservice.strategy.impl.DefaultJudgeStrategy;
import fun.javierchen.jcsmarterojbackendjudgeservice.strategy.impl.JavaLanguageJudgeStrategy;
import org.springframework.stereotype.Service;

/**
 * 判题策略对象管理
 */
@Service
public class JudgeManager {
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeStrategy judgeStrategy = null;

        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();

        if (language.equals(QuestionSubmitLanguageEnum.JAVA.getText())) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }

        if (judgeStrategy == null) {
            judgeStrategy = new DefaultJudgeStrategy();
        }

        return judgeStrategy.doJudge(judgeContext);
    }
}

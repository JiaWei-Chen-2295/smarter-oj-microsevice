package fun.javierchen.jcsmarterojbackendjudgeservice.strategy;

import fun.javierchen.jcojbackendcommon.enums.QuestionSubmitLanguageEnum;
import fun.javierchen.jcojbackendmodel.dto.questionsubmit.JudgeInfo;
import fun.javierchen.jcojbackendmodel.entity.QuestionSubmit;
import fun.javierchen.jcsmarterojbackendjudgeservice.strategy.impl.*;
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

        if (language.equals(QuestionSubmitLanguageEnum.JAVA.getValue())) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        } else if (language.equals(QuestionSubmitLanguageEnum.CPLUSPLUS.getValue())) {
            judgeStrategy = new CppLanguageJudgeStrategy();
        } else if (language.equals(QuestionSubmitLanguageEnum.GOLANG.getValue())) {
            judgeStrategy = new GoLanguageJudgeStrategy();
        } else if (language.equals(QuestionSubmitLanguageEnum.JAVASCRIPT.getValue())) {
            judgeStrategy = new JsLanguageJudgeStrategy();
        } else if (language.equals(QuestionSubmitLanguageEnum.PYTHON.getValue())) {
            judgeStrategy = new PythonLanguageJudgeStrategy();
        }

        // 当检测到未通过，将沙箱返回保存在上下文中的错误信息，直接返回
        JudgeInfo originalJudgeInfo = judgeContext.getJudgeInfo();
        if (originalJudgeInfo != null && !"Accepted".equals(originalJudgeInfo.getMessage())) {
            return originalJudgeInfo;
        }

        if (judgeStrategy == null) {
            judgeStrategy = new DefaultJudgeStrategy();
        }

        return judgeStrategy.doJudge(judgeContext);
    }
}

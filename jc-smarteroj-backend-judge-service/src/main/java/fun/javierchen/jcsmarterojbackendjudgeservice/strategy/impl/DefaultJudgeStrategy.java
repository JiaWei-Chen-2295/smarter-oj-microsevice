package fun.javierchen.jcsmarterojbackendjudgeservice.strategy.impl;

import cn.hutool.json.JSONUtil;
import fun.javierchen.jcojbackendmodel.dto.question.JudgeCase;
import fun.javierchen.jcojbackendmodel.dto.question.JudgeConfig;
import fun.javierchen.jcojbackendmodel.dto.questionsubmit.JudgeInfo;
import fun.javierchen.jcojbackendmodel.entity.Question;
import fun.javierchen.jcojbackendmodel.enums.JudgeInfoMessageEnum;
import fun.javierchen.jcsmarterojbackendjudgeservice.strategy.JudgeContext;
import fun.javierchen.jcsmarterojbackendjudgeservice.strategy.JudgeStrategy;

import java.util.List;

/**
 * 默认判题策略
 */
public class DefaultJudgeStrategy implements JudgeStrategy {
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        // 先判断输出和预期输出数量是否一致
        List<String> outputList = judgeContext.getOutputList();
        List<String> inputList = judgeContext.getInputList();
        if (outputList.size() != inputList.size()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;

        }
        // 仔细判断是否和预期输出一致
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        for (int i = 0; i < judgeCaseList.size(); i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(outputList.get(i))) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                break;
            }
        }

        // 判断程序运行的时间 占用资源是否符合要求
        Question question = judgeContext.getQuestion();
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long realTime = judgeInfo.getTime();
        Long realMemory = judgeInfo.getMemory();
        Long timeLimit = judgeConfig.getTimeLimit();
        Long memoryLimit = judgeConfig.getMemoryLimit();
        if (realTime > timeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;

        }
        if (realMemory > memoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;

        }

        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getText());
        judgeInfoResponse.setTime(0L);
        judgeInfoResponse.setMemory(0L);
        return judgeInfoResponse;
    }
}

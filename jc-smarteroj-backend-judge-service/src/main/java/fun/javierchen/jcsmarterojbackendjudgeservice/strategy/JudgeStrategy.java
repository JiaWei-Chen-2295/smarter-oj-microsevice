package fun.javierchen.jcsmarterojbackendjudgeservice.strategy;


import fun.javierchen.jcojbackendmodel.dto.questionsubmit.JudgeInfo;

/**
 * 判题策略
 *
 * @author JavierChen
 * @date 2025/04/07
 */
public interface JudgeStrategy {

    JudgeInfo doJudge(JudgeContext judgeContext);

}

package fun.javierchen.jcsmarterojbackendjudgeservice.service.impl;

import cn.hutool.json.JSONUtil;

import fun.javierchen.jcojbackendcommon.common.ErrorCode;
import fun.javierchen.jcojbackendcommon.exception.BusinessException;
import fun.javierchen.jcojbackendmodel.codesandbox.CodeSandBoxRequest;
import fun.javierchen.jcojbackendmodel.codesandbox.CodeSandBoxResponse;
import fun.javierchen.jcojbackendmodel.dto.question.JudgeCase;
import fun.javierchen.jcojbackendmodel.dto.question.JudgeConfig;
import fun.javierchen.jcojbackendmodel.dto.questionsubmit.JudgeInfo;
import fun.javierchen.jcojbackendmodel.entity.Question;
import fun.javierchen.jcojbackendmodel.entity.QuestionSubmit;
import fun.javierchen.jcojbackendmodel.entity.User;
import fun.javierchen.jcojbackendmodel.enums.QuestionSubmitStatusEnum;

import fun.javierchen.jcojbackendmodel.dto.questionsubmit.QuestionSubmitVOGetRequest;
import fun.javierchen.jcojbackendmodel.vo.QuestionSubmitVO;

import fun.javierchen.jcojbackendserverclient.QuestionFeignClient;
import fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox.CodeSandBox;
import fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox.CodeSandBoxFactory;
import fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox.CodeSandBoxProxy;
import fun.javierchen.jcsmarterojbackendjudgeservice.metrics.JudgeMetrics;
import fun.javierchen.jcsmarterojbackendjudgeservice.service.JudgeService;
import fun.javierchen.jcsmarterojbackendjudgeservice.strategy.JudgeContext;
import fun.javierchen.jcsmarterojbackendjudgeservice.strategy.JudgeManager;
import lombok.Data;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private CodeSandBoxFactory codeSandBoxFactory;

    @Resource
    private QuestionFeignClient questionFeignClient;

    @Resource
    private JudgeManager judgeManager;

    @Resource
    private JudgeMetrics judgeMetrics;

    @Override
    public QuestionSubmitVO doJudge(Long questionSubmitId, User loginUser) {

        Timer.Sample timerSample = judgeMetrics.startExecution();
        judgeMetrics.incrementSubmit();

        try {
            // 校验
            if (questionSubmitId == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "提交的题目记录不存在");
            }

            QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
            if (questionSubmit == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "题目提交记录不存在");
            }

            String language = questionSubmit.getLanguage();
            String code = questionSubmit.getCode();

            Long questionId = questionSubmit.getQuestionId();
            Question question = questionFeignClient.getQuestionById(questionId);
            if (question == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
            }
            // 判断当前的提交状态 不是等待中 就不要执行 防止重复执行
            Integer status = questionSubmit.getStatus();
            QuestionSubmitStatusEnum questionSubmitStatus = QuestionSubmitStatusEnum.getEnumByValue(status);
            if (questionSubmitStatus == null) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "当前题目提交记录状态异常");
            }
            if (!QuestionSubmitStatusEnum.WAITING.equals(questionSubmitStatus)) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "已经进行判题");
            }

            // 设置题目为判题中
            QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
            questionSubmitUpdate.setId(questionSubmitId);
            questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
            boolean updateResult = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
            if (!updateResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新状态失败");
            }

            // 调用代码沙箱
            // 测试用例的输入用例
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        if (judgeCaseList == null || judgeCaseList.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题用例为空");
        }
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
            String judgeConfigStr = question.getJudgeConfig();
            JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
            CodeSandBoxRequest request = CodeSandBoxRequest.builder()
                    .code(code)
                    .inputCase(inputList)
                    .judgeConfig(judgeConfig)
                    .language(language)
                    .build();
            CodeSandBox codeSandBox = codeSandBoxFactory.getCodeSandBox();
            CodeSandBoxProxy codeSandBoxProxy = new CodeSandBoxProxy(codeSandBox);
            CodeSandBoxResponse codeSandBoxResponse = codeSandBoxProxy.runCode(request);

            // 根据沙箱结果判断
            List<String> outputList = codeSandBoxResponse.getOutputList();
            List<String> answerList = judgeCaseList.stream().map(JudgeCase::getOutput).collect(Collectors.toList());
            JudgeInfo judgeInfoResponse = codeSandBoxResponse.getJudgeInfo();

            JudgeContext judgeContext = new JudgeContext();
            judgeContext.setJudgeInfo(judgeInfoResponse);
            judgeContext.setInputList(inputList);
            judgeContext.setOutputList(outputList);
            judgeContext.setJudgeCaseList(judgeCaseList);
            judgeContext.setQuestion(question);
            judgeContext.setQuestionSubmit(questionSubmit);

            JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);

            // 修改数据库的判题结果
            questionSubmitUpdate = new QuestionSubmit();
            questionSubmitStatus = QuestionSubmitStatusEnum.SUCCEED;
            questionSubmitUpdate.setId(questionSubmitId);
            questionSubmitUpdate.setStatus(questionSubmitStatus.getValue());
            questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
            questionSubmitUpdate.setOutputResult(JSONUtil.toJsonStr(outputList));
            updateResult = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
            if (!updateResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新题目提交信息失败");
            }

            if (judgeMetrics.isAccepted(judgeInfo)) {
                judgeMetrics.recordResultSuccess();
            } else {
                judgeMetrics.recordResultFail();
            }

            // 获取新的提交对象返回

            QuestionSubmitVOGetRequest questionSubmitVOGetRequest = new QuestionSubmitVOGetRequest();
            questionSubmitVOGetRequest.setQuestionSubmit(questionFeignClient.getQuestionSubmitById(questionSubmitId));
            questionSubmitVOGetRequest.setLoginUser(loginUser);
            return questionFeignClient.getQuestionSubmitVO(questionSubmitVOGetRequest);
        } catch (Exception e) {
            judgeMetrics.recordResultError();
            throw e;
        } finally {
            judgeMetrics.stopExecution(timerSample);
        }
    }
}

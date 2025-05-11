package fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox.impl;


import fun.javierchen.jcojbackendmodel.codesandbox.CodeSandBoxRequest;
import fun.javierchen.jcojbackendmodel.codesandbox.CodeSandBoxResponse;
import fun.javierchen.jcojbackendmodel.dto.questionsubmit.JudgeInfo;
import fun.javierchen.jcojbackendmodel.enums.JudgeInfoMessageEnum;
import fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox.CodeSandBox;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ExampleCodeSandBox implements CodeSandBox {
    @Override
    public CodeSandBoxResponse runCode(CodeSandBoxRequest codeSandBoxRequest) {
        String code = codeSandBoxRequest.getCode();
        CodeSandBoxResponse codeSandBoxResponse = new CodeSandBoxResponse();
        codeSandBoxResponse.setMessage(String.format("现在是示例代码沙箱,code:%s", code));
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setTime(200L);
        judgeInfo.setMemory(1000L);
        codeSandBoxResponse.setStatus(1);
        codeSandBoxResponse.setJudgeInfo(judgeInfo);
        codeSandBoxResponse.setOutputList(Arrays.asList("aa aa", "vv vv"));
        return codeSandBoxResponse;
    }
}

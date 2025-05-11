package fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox.impl;


import fun.javierchen.jcojbackendmodel.codesandbox.CodeSandBoxRequest;
import fun.javierchen.jcojbackendmodel.codesandbox.CodeSandBoxResponse;
import fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox.CodeSandBox;
import org.springframework.stereotype.Component;

@Component
public class ThirdPartCodeSandBox implements CodeSandBox {
    @Override
    public CodeSandBoxResponse runCode(CodeSandBoxRequest codeSandBoxRequest) {
        String code = codeSandBoxRequest.getCode();
        CodeSandBoxResponse codeSandBoxResponse = new CodeSandBoxResponse();
        codeSandBoxResponse.setMessage(String.format("现在是第三方代码沙箱,code:%s", code));
        return codeSandBoxResponse;
    }
}

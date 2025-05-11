package fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox;

import fun.javierchen.jcojbackendmodel.codesandbox.CodeSandBoxRequest;
import fun.javierchen.jcojbackendmodel.codesandbox.CodeSandBoxResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 使用静态代理增强代码沙箱的日志功能
 */
@AllArgsConstructor
@Slf4j
public class CodeSandBoxProxy implements CodeSandBox {

    private CodeSandBox codeSandBox;
    @Override
    public CodeSandBoxResponse runCode(CodeSandBoxRequest codeSandBoxRequest) {
        log.info("判题代码[{}]语言[{}]", codeSandBoxRequest.getCode(), codeSandBoxRequest.getLanguage());
        CodeSandBoxResponse codeSandBoxResponse = codeSandBox.runCode(codeSandBoxRequest);
        log.info("判题结果是[{}]", codeSandBoxResponse.getMessage() + codeSandBoxResponse.getJudgeInfo());
        return codeSandBoxResponse;
    }
}

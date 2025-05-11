package fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox;


import fun.javierchen.jcojbackendmodel.codesandbox.CodeSandBoxRequest;
import fun.javierchen.jcojbackendmodel.codesandbox.CodeSandBoxResponse;

public interface CodeSandBox {

    CodeSandBoxResponse runCode(CodeSandBoxRequest codeSandBoxRequest);

}

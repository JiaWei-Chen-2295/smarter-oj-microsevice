package fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox;

import fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox.impl.ExampleCodeSandBox;
import fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox.impl.Judge0CodeSandBox;
import fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox.impl.RemoteCodeSandBox;
import fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox.impl.ThirdPartCodeSandBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 简单工厂模式
 * - 可以确保单例和线程安全
 */
@Component
public class CodeSandBoxFactory {
    @Resource
    private SandboxConfig sandboxConfig;

    private final RemoteCodeSandBox remoteCodeSandBox;
    private final ThirdPartCodeSandBox thirdPartCodeSandBox;
    private final ExampleCodeSandBox exampleCodeSandBox;
    private final Judge0CodeSandBox judge0CodeSandBox;

    private Map<String, CodeSandBox> codeSandBoxMap;

    @Autowired
    public CodeSandBoxFactory(RemoteCodeSandBox remoteCodeSandBox, ThirdPartCodeSandBox thirdPartCodeSandBox,
            ExampleCodeSandBox exampleCodeSandBox, Judge0CodeSandBox judge0CodeSandBox) {
        this.remoteCodeSandBox = remoteCodeSandBox;
        this.thirdPartCodeSandBox = thirdPartCodeSandBox;
        this.exampleCodeSandBox = exampleCodeSandBox;
        this.judge0CodeSandBox = judge0CodeSandBox;
    }

    @PostConstruct
    public void init() {
        codeSandBoxMap = new HashMap<>();
        codeSandBoxMap.put("remote", remoteCodeSandBox);
        codeSandBoxMap.put("third-part", thirdPartCodeSandBox);
        codeSandBoxMap.put("example", exampleCodeSandBox);
        codeSandBoxMap.put("judge0", judge0CodeSandBox);
    }

    public CodeSandBox getCodeSandBox() {
        String codeSandBoxType = sandboxConfig.getType();
        CodeSandBox codeSandBox = codeSandBoxMap.get(codeSandBoxType);
        if (codeSandBox == null) {
            throw new IllegalArgumentException("Unknown CodeSandBox type: " + codeSandBoxType);
        }
        return codeSandBox;
    }
}
package fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

import fun.javierchen.jcojbackendcommon.common.ErrorCode;
import fun.javierchen.jcojbackendcommon.exception.BusinessException;
import fun.javierchen.jcojbackendmodel.codesandbox.CodeSandBoxRequest;
import fun.javierchen.jcojbackendmodel.codesandbox.CodeSandBoxResponse;
import fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox.CodeSandBox;
import fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox.SandboxConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RemoteCodeSandBox implements CodeSandBox {

    private SandboxConfig sandboxConfig;

    @Resource
    public void setSandboxConfig(SandboxConfig sandboxConfig) {
        this.sandboxConfig = sandboxConfig;
    }

    public static final String AUTH_HEADER = "5525-19970329";
    public static final String secret = "MAYDAY-5525-ASHIN-MING-MASA-STONE-MONSTER";

    @Override
    public CodeSandBoxResponse runCode(CodeSandBoxRequest codeSandBoxRequest) {
        String remoteUrl = sandboxConfig.getRemoteUrl();
        String body = HttpUtil.createPost(remoteUrl)
                .header(AUTH_HEADER, secret)
                .body(JSONUtil.toJsonStr(codeSandBoxRequest)).execute().body();
        if (StringUtils.isBlank(body)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR);
        }
        return JSONUtil.toBean(body, CodeSandBoxResponse.class);
    }
}

package fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import fun.javierchen.jcojbackendcommon.common.ErrorCode;
import fun.javierchen.jcojbackendcommon.exception.BusinessException;
import fun.javierchen.jcojbackendmodel.codesandbox.CodeSandBoxRequest;
import fun.javierchen.jcojbackendmodel.codesandbox.CodeSandBoxResponse;
import fun.javierchen.jcojbackendmodel.dto.questionsubmit.JudgeInfo;
import fun.javierchen.jcojbackendmodel.enums.QuestionSubmitStatusEnum;
import fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox.CodeSandBox;
import fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox.SandboxConfig;
import fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox.model.judge0.Judge0BatchResponse;
import fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox.model.judge0.Judge0SubmissionRequest;
import fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox.model.judge0.Judge0SubmissionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class Judge0CodeSandBox implements CodeSandBox {

    @Resource
    private SandboxConfig sandboxConfig;

    @Resource
    private ObjectMapper objectMapper;

    private static final Map<String, Integer> LANGUAGE_MAP = new HashMap<>();

    static {
        // Mapping based on Judge0 standard language IDs
        LANGUAGE_MAP.put("java", 62);
        LANGUAGE_MAP.put("python", 71);
        LANGUAGE_MAP.put("cpp", 54);
        LANGUAGE_MAP.put("c", 50);
        LANGUAGE_MAP.put("go", 60);
        LANGUAGE_MAP.put("javascript", 63);
        LANGUAGE_MAP.put("js", 63);
    }

    @Override
    public CodeSandBoxResponse runCode(CodeSandBoxRequest codeSandBoxRequest) {
        String language = codeSandBoxRequest.getLanguage();
        String code = codeSandBoxRequest.getCode();
        List<String> inputCases = codeSandBoxRequest.getInputCase();

        Integer languageId = LANGUAGE_MAP.get(language.toLowerCase());
        if (languageId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不支持的编程语言: " + language);
        }

        // 1. Prepare Batch Request with Base64
        List<Judge0SubmissionRequest> batchRequests = inputCases.stream()
                .map(input -> Judge0SubmissionRequest.builder()
                        .sourceCode(Base64.encode(code))
                        .languageId(languageId)
                        .stdin(Base64.encode(input))
                        .base64Encoded(true)
                        .cpuTimeLimit(codeSandBoxRequest.getJudgeConfig().getTimeLimit() / 1000.0)
                        .memoryLimit(codeSandBoxRequest.getJudgeConfig().getMemoryLimit().doubleValue()) // Judge0 uses
                                                                                                         // KB
                        .stackLimit(codeSandBoxRequest.getJudgeConfig().getStackLimit().doubleValue()) // Judge0 uses KB
                        .build())
                .collect(Collectors.toList());

        try {
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("submissions", batchRequests);

            String batchUrl = sandboxConfig.getRemoteUrl() + "/submissions/batch?base64_encoded=true";
            String responseStr = HttpUtil.createPost(batchUrl)
                    .addHeaders(getAuthHeaders())
                    .body(objectMapper.writeValueAsString(bodyMap))
                    .execute()
                    .body();

            List<Map<String, Object>> tokensResponse = objectMapper.readValue(responseStr,
                    new TypeReference<List<Map<String, Object>>>() {
                    });

            if (tokensResponse == null || tokensResponse.isEmpty()) {
                throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "Judge0 Batch 提交失败: 响应为空");
            }

            // Check if it's an error response (values are List instead of String)
            if (tokensResponse.get(0).containsKey("token")) {
                List<String> tokens = tokensResponse.stream()
                        .map(m -> (String) m.get("token"))
                        .filter(StrUtil::isNotBlank)
                        .collect(Collectors.toList());

                if (tokens.size() < batchRequests.size()) {
                    log.error("Judge0 提交部分失败. 原始响应: {}", responseStr);
                    throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "Judge0 提交部分失败，请确认后端连接及参数");
                }
                // 2. Polling for Results
                List<Judge0SubmissionResponse> finalResults = pollResults(tokens);

                // 3. Map Results to CodeSandBoxResponse
                return processResults(finalResults);
            } else {
                log.error("Judge0 校验失败: {}", responseStr);
                throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "Judge0 参数校验失败: " + responseStr);
            }
        } catch (Exception e) {
            log.error("Judge0 核心链路异常", e);
            if (e instanceof BusinessException)
                throw (BusinessException) e;
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "判题沙箱交互异常: " + e.getMessage());
        }
    }

    private List<Judge0SubmissionResponse> pollResults(List<String> tokens) {
        String tokensStr = String.join(",", tokens);
        String pollUrl = sandboxConfig.getRemoteUrl() + "/submissions/batch?tokens=" + tokensStr
                + "&base64_encoded=true";

        int maxRetries = 30;
        int retryInterval = 1000; // 1s

        for (int i = 0; i < maxRetries; i++) {
            try {
                String pollResponseStr = HttpUtil.createGet(pollUrl)
                        .addHeaders(getAuthHeaders())
                        .execute()
                        .body();
                Judge0BatchResponse batchResponse = objectMapper.readValue(pollResponseStr, Judge0BatchResponse.class);

                if (batchResponse != null && batchResponse.getSubmissions() != null
                        && !batchResponse.getSubmissions().isEmpty()) {
                    List<Judge0SubmissionResponse> submissions = batchResponse.getSubmissions();
                    long doneCount = submissions.stream()
                            .filter(s -> s != null && s.getStatus() != null && s.getStatus().getId() >= 3)
                            .count();

                    Judge0SubmissionResponse sample = submissions.get(0);
                    String statusMsg = (sample != null && sample.getStatus() != null)
                            ? sample.getStatus().getDescription()
                            : "未知";
                    log.info("Judge0 判题进度: {}/{}, 当前状态: {}", doneCount, submissions.size(), statusMsg);

                    if ("未知".equals(statusMsg)) {
                        log.warn("Judge0 状态识别异常，原始响应: {}", pollResponseStr);
                    }

                    if (doneCount == submissions.size()) {
                        return submissions;
                    }
                } else {
                    log.error("Judge0 Batch 轮询响应解析为空: {}", pollResponseStr);
                }
            } catch (Exception e) {
                log.error("Judge0 轮询解析异常", e);
            }

            try {
                Thread.sleep(retryInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "轮询被中断");
            }
        }

        throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "Judge0 判题超时");
    }

    private CodeSandBoxResponse processResults(List<Judge0SubmissionResponse> results) {
        List<String> outputList = new ArrayList<>();
        long maxTime = 0L;
        long maxMemory = 0L;
        String finalMessage = "Accepted";
        int finalStatus = QuestionSubmitStatusEnum.SUCCEED.getValue();

        for (Judge0SubmissionResponse response : results) {
            if (response == null || response.getStatus() == null) {
                finalStatus = QuestionSubmitStatusEnum.FAILED.getValue();
                finalMessage = "Judge0 响应异常: 结果为空";
                outputList.add("");
                continue;
            }
            // Decode Base64 fields and trim
            String stdout = StrUtil.trim(decodeBase64(response.getStdout()));
            String stderr = StrUtil.trim(decodeBase64(response.getStderr()));
            String compileOutput = StrUtil.trim(decodeBase64(response.getCompileOutput()));

            if (response.getStatus().getId() != 3) {
                finalStatus = QuestionSubmitStatusEnum.FAILED.getValue();
                finalMessage = response.getStatus().getDescription();
                if (StrUtil.isNotBlank(compileOutput)) {
                    finalMessage += ": " + compileOutput;
                } else if (StrUtil.isNotBlank(stderr)) {
                    finalMessage += ": " + stderr;
                }
            }

            outputList.add(stdout != null ? stdout : "");

            double timeInSec = Double.parseDouble(response.getTime() != null ? response.getTime() : "0");
            maxTime = Math.max(maxTime, (long) (timeInSec * 1000));
            maxMemory = Math.max(maxMemory, response.getMemory() != null ? response.getMemory().longValue() : 0L);
        }

        CodeSandBoxResponse codeSandBoxResponse = new CodeSandBoxResponse();
        codeSandBoxResponse.setOutputList(outputList);
        codeSandBoxResponse.setMessage(finalMessage);
        codeSandBoxResponse.setStatus(finalStatus);

        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(finalMessage);
        judgeInfo.setTime(maxTime);
        judgeInfo.setMemory(maxMemory);
        codeSandBoxResponse.setJudgeInfo(judgeInfo);

        return codeSandBoxResponse;
    }

    private String decodeBase64(String encoded) {
        if (StrUtil.isBlank(encoded)) {
            return encoded;
        }
        try {
            return new String(Base64.decode(encoded), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Base64 解码失败: {}", encoded, e);
            return encoded;
        }
    }

    private Map<String, String> getAuthHeaders() {
        Map<String, String> headers = new HashMap<>();
        String apiKey = sandboxConfig.getApiKey();
        if (StrUtil.isNotBlank(apiKey)) {
            headers.put("X-Auth-Token", apiKey);
            headers.put("X-RapidAPI-Key", apiKey);
        }
        return headers;
    }
}

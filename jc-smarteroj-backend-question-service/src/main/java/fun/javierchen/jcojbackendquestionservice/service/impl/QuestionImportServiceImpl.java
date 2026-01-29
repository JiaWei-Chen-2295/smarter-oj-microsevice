package fun.javierchen.jcojbackendquestionservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import fun.javierchen.jcojbackendcommon.common.ErrorCode;
import fun.javierchen.jcojbackendcommon.exception.BusinessException;
import fun.javierchen.jcojbackendcommon.exception.ThrowUtils;
import fun.javierchen.jcojbackendmodel.dto.question.*;
import fun.javierchen.jcojbackendmodel.entity.Question;
import fun.javierchen.jcojbackendmodel.entity.User;
import fun.javierchen.jcojbackendquestionservice.service.FpsXmlParseService;
import fun.javierchen.jcojbackendquestionservice.service.QuestionImportService;
import fun.javierchen.jcojbackendquestionservice.service.QuestionService;
import fun.javierchen.jcojbackendserverclient.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 题目批量导入服务实现
 *
 * @author JavierChen
 */
@Service
@Slf4j
public class QuestionImportServiceImpl implements QuestionImportService {

    @Resource
    private FpsXmlParseService fpsXmlParseService;

    @Resource
    private QuestionService questionService;

    @Resource
    private UserFeignClient userFeignClient;

    /**
     * 默认堆栈限制 (KB)
     */
    private static final Long DEFAULT_STACK_LIMIT = 8192L;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QuestionBatchImportResponse importFromFpsXml(MultipartFile file, User loginUser) {
        // 1. 权限校验 - 只允许管理员导入
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);
        ThrowUtils.throwIf(!userFeignClient.isAdmin(loginUser), ErrorCode.NO_AUTH_ERROR, "只有管理员可以批量导入题目");

        // 2. 解析 XML 文件
        List<FpsItem> fpsItems;
        try {
            fpsItems = fpsXmlParseService.parseFpsXml(file);
        } catch (Exception e) {
            log.error("解析 FPS XML 文件失败", e);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "解析 XML 文件失败: " + e.getMessage());
        }

        if (CollUtil.isEmpty(fpsItems)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "XML 文件中没有找到任何题目");
        }

        // 3. 逐个导入题目
        List<QuestionImportResult> results = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        for (int i = 0; i < fpsItems.size(); i++) {
            FpsItem fpsItem = fpsItems.get(i);
            int index = i + 1;
            String title = fpsItem.getTitle();

            try {
                Question question = convertFpsItemToQuestion(fpsItem, loginUser.getId());
                boolean saved = questionService.save(question);

                if (saved) {
                    results.add(QuestionImportResult.success(index, title, question.getId()));
                    successCount++;
                    log.info("题目导入成功: [{}] {}", index, title);
                } else {
                    results.add(QuestionImportResult.fail(index, title, "保存到数据库失败"));
                    failCount++;
                    log.warn("题目导入失败: [{}] {} - 保存到数据库失败", index, title);
                }
            } catch (Exception e) {
                results.add(QuestionImportResult.fail(index, title, e.getMessage()));
                failCount++;
                log.warn("题目导入失败: [{}] {} - {}", index, title, e.getMessage());
            }
        }

        log.info("批量导入完成: 总计 {} 道题目, 成功 {} 道, 失败 {} 道",
                fpsItems.size(), successCount, failCount);

        return new QuestionBatchImportResponse(fpsItems.size(), successCount, failCount, results);
    }

    /**
     * 将 FpsItem 转换为 Question 实体
     */
    private Question convertFpsItemToQuestion(FpsItem fpsItem, Long userId) {
        Question question = new Question();

        // 标题
        String title = fpsItem.getTitle();
        ThrowUtils.throwIf(StringUtils.isBlank(title), ErrorCode.PARAMS_ERROR, "题目标题不能为空");
        question.setTitle(title.trim());

        // 组装题目内容 (描述 + 输入说明 + 输出说明 + 样例 + 提示)
        String content = buildQuestionContent(fpsItem);
        question.setContent(content);

        // 标签 - 从 source 解析
        List<String> tags = parseTagsFromSource(fpsItem.getSource());
        question.setTags(JSONUtil.toJsonStr(tags));

        // 判题配置
        JudgeConfig judgeConfig = buildJudgeConfig(fpsItem);
        question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));

        // 判题用例 - 使用 sample_input/sample_output 作为测试用例
        // 如果有 test_input/test_output 则优先使用
        List<JudgeCase> judgeCases = buildJudgeCases(fpsItem);
        question.setJudgeCase(JSONUtil.toJsonStr(judgeCases));

        // 参考答案 - 优先使用 Java，其次 C++，然后其他
        String answer = extractAnswer(fpsItem);
        question.setAnswer(answer);

        // 代码模板 - 使用默认模板
        CodeTemplate codeTemplate = CodeTemplate.getDefaultTemplate();
        question.setCodeTemplate(JSONUtil.toJsonStr(codeTemplate));

        // 其他字段
        question.setUserId(userId);
        question.setSubmitNum(0);
        question.setAcceptedNum(0);
        question.setFavourNum(0);
        question.setIsDelete(0);

        return question;
    }

    /**
     * 构建题目内容
     */
    private String buildQuestionContent(FpsItem fpsItem) {
        StringBuilder content = new StringBuilder();

        // 题目描述
        String description = fpsItem.getDescription();
        if (StringUtils.isNotBlank(description)) {
            content.append("## 题目描述\n\n");
            content.append(cleanHtmlTags(description));
            content.append("\n\n");
        }

        // 输入说明
        String input = fpsItem.getInput();
        if (StringUtils.isNotBlank(input)) {
            content.append("## 输入格式\n\n");
            content.append(cleanHtmlTags(input));
            content.append("\n\n");
        }

        // 输出说明
        String output = fpsItem.getOutput();
        if (StringUtils.isNotBlank(output)) {
            content.append("## 输出格式\n\n");
            content.append(cleanHtmlTags(output));
            content.append("\n\n");
        }

        // 样例输入输出
        List<String> sampleInputs = fpsItem.getSampleInputs();
        List<String> sampleOutputs = fpsItem.getSampleOutputs();

        if (CollUtil.isNotEmpty(sampleInputs) || CollUtil.isNotEmpty(sampleOutputs)) {
            int sampleCount = Math.max(
                    sampleInputs != null ? sampleInputs.size() : 0,
                    sampleOutputs != null ? sampleOutputs.size() : 0);

            for (int i = 0; i < sampleCount; i++) {
                if (sampleCount > 1) {
                    content.append("## 样例 ").append(i + 1).append("\n\n");
                } else {
                    content.append("## 样例\n\n");
                }

                if (sampleInputs != null && i < sampleInputs.size()) {
                    content.append("### 输入\n\n```\n");
                    content.append(sampleInputs.get(i));
                    content.append("\n```\n\n");
                }

                if (sampleOutputs != null && i < sampleOutputs.size()) {
                    content.append("### 输出\n\n```\n");
                    content.append(sampleOutputs.get(i));
                    content.append("\n```\n\n");
                }
            }
        }

        // 提示
        String hint = fpsItem.getHint();
        if (StringUtils.isNotBlank(hint)) {
            content.append("## 提示\n\n");
            content.append(cleanHtmlTags(hint));
            content.append("\n\n");
        }

        return content.toString();
    }

    /**
     * 构建判题配置
     */
    private JudgeConfig buildJudgeConfig(FpsItem fpsItem) {
        JudgeConfig config = new JudgeConfig();

        // 时间限制
        Long timeLimit = fpsItem.getTimeLimit();
        String timeLimitUnit = fpsItem.getTimeLimitUnit();

        if (timeLimit != null) {
            // 统一转换为毫秒
            if ("s".equalsIgnoreCase(timeLimitUnit)) {
                timeLimit = timeLimit * 1000;
            }
            // 如果单位是 ms 或没有单位，假设已经是毫秒
        } else {
            timeLimit = 1000L; // 默认 1000ms
        }
        config.setTimeLimit(timeLimit);

        // 内存限制
        Long memoryLimit = fpsItem.getMemoryLimit();
        String memoryLimitUnit = fpsItem.getMemoryLimitUnit();

        if (memoryLimit != null) {
            // 统一转换为 KB
            if ("mb".equalsIgnoreCase(memoryLimitUnit)) {
                memoryLimit = memoryLimit * 1024;
            }
            // 如果单位是 kb 或没有单位，假设已经是 KB
        } else {
            memoryLimit = 128 * 1024L; // 默认 128MB = 131072KB
        }
        config.setMemoryLimit(memoryLimit);

        // 堆栈限制 - 使用默认值
        config.setStackLimit(DEFAULT_STACK_LIMIT);

        return config;
    }

    /**
     * 构建判题用例
     */
    private List<JudgeCase> buildJudgeCases(FpsItem fpsItem) {
        List<JudgeCase> judgeCases = new ArrayList<>();

        // 优先使用 test_input/test_output
        List<String> testInputs = fpsItem.getTestInputs();
        List<String> testOutputs = fpsItem.getTestOutputs();

        if (CollUtil.isNotEmpty(testInputs) && CollUtil.isNotEmpty(testOutputs)) {
            int caseCount = Math.min(testInputs.size(), testOutputs.size());
            for (int i = 0; i < caseCount; i++) {
                JudgeCase judgeCase = new JudgeCase();
                judgeCase.setInput(testInputs.get(i));
                judgeCase.setOutput(testOutputs.get(i));
                judgeCases.add(judgeCase);
            }
        }

        // 如果没有 test_input/test_output，则使用 sample_input/sample_output
        if (judgeCases.isEmpty()) {
            List<String> sampleInputs = fpsItem.getSampleInputs();
            List<String> sampleOutputs = fpsItem.getSampleOutputs();

            if (CollUtil.isNotEmpty(sampleInputs) && CollUtil.isNotEmpty(sampleOutputs)) {
                int caseCount = Math.min(sampleInputs.size(), sampleOutputs.size());
                for (int i = 0; i < caseCount; i++) {
                    String input = sampleInputs.get(i);
                    String output = sampleOutputs.get(i);

                    // 过滤无效的样例 (如 "无")
                    if (isValidSample(input) && isValidSample(output)) {
                        JudgeCase judgeCase = new JudgeCase();
                        judgeCase.setInput(input);
                        judgeCase.setOutput(output);
                        judgeCases.add(judgeCase);
                    }
                }
            }
        }

        return judgeCases;
    }

    /**
     * 检查样例是否有效
     */
    private boolean isValidSample(String sample) {
        if (StringUtils.isBlank(sample)) {
            return false;
        }
        String trimmed = sample.trim().toLowerCase();
        return !trimmed.equals("无") && !trimmed.equals("none") && !trimmed.equals("n/a");
    }

    /**
     * 从 source 解析标签
     */
    private List<String> parseTagsFromSource(String source) {
        List<String> tags = new ArrayList<>();

        if (StringUtils.isNotBlank(source)) {
            // source 可能是空格分隔的标签，如 "教学题-C语言 模拟 模拟运算 初级表达式求值"
            String[] parts = source.trim().split("\\s+");
            for (String part : parts) {
                String tag = part.trim();
                if (StringUtils.isNotBlank(tag) && tag.length() <= 20) {
                    tags.add(tag);
                }
            }
        }

        // 如果没有解析出标签，添加一个默认标签
        if (tags.isEmpty()) {
            tags.add("导入题目");
        }

        return tags;
    }

    /**
     * 提取参考答案
     */
    private String extractAnswer(FpsItem fpsItem) {
        List<FpsSolution> solutions = fpsItem.getSolutions();
        if (CollUtil.isEmpty(solutions)) {
            return "";
        }

        // 优先级: Java > C++ > C > Python > 其他
        String[] preferredLanguages = { "Java", "C++", "C", "Python" };

        for (String lang : preferredLanguages) {
            for (FpsSolution solution : solutions) {
                if (lang.equalsIgnoreCase(solution.getLanguage())) {
                    return formatAnswer(solution);
                }
            }
        }

        // 返回第一个答案
        return formatAnswer(solutions.get(0));
    }

    /**
     * 格式化参考答案
     */
    private String formatAnswer(FpsSolution solution) {
        StringBuilder sb = new StringBuilder();
        sb.append("```").append(solution.getLanguage().toLowerCase()).append("\n");
        sb.append(solution.getCode());
        if (!solution.getCode().endsWith("\n")) {
            sb.append("\n");
        }
        sb.append("```");
        return sb.toString();
    }

    /**
     * 清理 HTML 标签
     */
    private String cleanHtmlTags(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }

        // 将 <br> 和 <br/> 替换为换行
        text = text.replaceAll("<br\\s*/?>", "\n");

        // 将 <p> 替换为换行
        text = text.replaceAll("<p[^>]*>", "\n");
        text = text.replaceAll("</p>", "\n");

        // 移除其他 HTML 标签但保留内容
        text = text.replaceAll("<[^>]+>", "");

        // 处理 HTML 实体
        text = text.replace("&nbsp;", " ");
        text = text.replace("&lt;", "<");
        text = text.replace("&gt;", ">");
        text = text.replace("&amp;", "&");
        text = text.replace("&quot;", "\"");
        text = text.replace("&hellip;", "...");

        // 清理多余的空行
        text = text.replaceAll("\n{3,}", "\n\n");

        return text.trim();
    }
}

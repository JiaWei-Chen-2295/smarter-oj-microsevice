package fun.javierchen.jcojbackendquestionservice.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import fun.javierchen.jcojbackendcommon.common.BaseResponse;
import fun.javierchen.jcojbackendcommon.common.DeleteRequest;
import fun.javierchen.jcojbackendcommon.common.ErrorCode;
import fun.javierchen.jcojbackendcommon.common.ResultUtils;
import fun.javierchen.jcojbackendcommon.exception.BusinessException;
import fun.javierchen.jcojbackendcommon.exception.ThrowUtils;
import fun.javierchen.jcojbackendmodel.dto.question.*;
import fun.javierchen.jcojbackendmodel.dto.questionsubmit.QuestionSubmitAddRequest;
import fun.javierchen.jcojbackendmodel.dto.questionsubmit.QuestionSubmitQueryRequest;
import fun.javierchen.jcojbackendmodel.dto.questionsubmit.SubmitHeatmapRequest;
import fun.javierchen.jcojbackendmodel.entity.Question;
import fun.javierchen.jcojbackendmodel.entity.QuestionSubmit;
import fun.javierchen.jcojbackendmodel.entity.User;
import fun.javierchen.jcojbackendmodel.vo.QuestionSubmitVO;
import fun.javierchen.jcojbackendmodel.vo.QuestionVO;
import fun.javierchen.jcojbackendmodel.vo.SubmitHeatmapVO;
import fun.javierchen.jcojbackendquestionservice.service.QuestionService;
import fun.javierchen.jcojbackendquestionservice.service.QuestionSubmitService;
import fun.javierchen.jcojbackendserverclient.UserFeignClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 题目接口
 *
 * @author JavierChen
 */
@RestController
@RequestMapping("/")
@Slf4j
@Tag(name = "题目接口")
public class QuestionController {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private QuestionSubmitService questionSubmitService;

    /**
     * 热力图缓存（简单内存缓存，生产环境建议使用Redis）
     * key: userId_startDate_endDate
     * value: CacheEntry(data, expireTime)
     */
    private final Map<String, CacheEntry> heatmapCache = new ConcurrentHashMap<>();

    private static class CacheEntry {
        SubmitHeatmapVO data;
        long expireTime;

        CacheEntry(SubmitHeatmapVO data, long expireTime) {
            this.data = data;
            this.expireTime = expireTime;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }
    }

    // region 增删改查

    /**
     * 创建
     *
     * @param questionAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request) {
        if (questionAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionAddRequest, question);
        List<String> tags = questionAddRequest.getTags();
        if (tags != null) {
            question.setTags(JSONUtil.toJsonStr(tags));
        }
        questionService.validQuestion(question, true);

        User loginUser = userFeignClient.getLoginUser(request);
        question.setUserId(loginUser.getId());

        question.setFavourNum(0);

        JudgeConfig judgeConfig = questionAddRequest.getJudgeConfig();
        String judgeConfigJsonStr = JSONUtil.toJsonStr(judgeConfig);
        question.setJudgeConfig(judgeConfigJsonStr);

        List<JudgeCase> judgeCases = questionAddRequest.getJudgeCase();
        if (judgeCases != null) {
            question.setJudgeCase(JSONUtil.toJsonStr(judgeCases));
        }

        CodeTemplate codeTemplate = questionAddRequest.getCodeTemplate();
        if (codeTemplate != null) {
            question.setCodeTemplate(JSONUtil.toJsonStr(codeTemplate));
        }

        boolean result = questionService.save(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newQuestionId = question.getId();
        return ResultUtils.success(newQuestionId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userFeignClient.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldQuestion.getUserId().equals(user.getId()) && !userFeignClient.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = questionService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param questionUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest) {
        if (questionUpdateRequest == null || questionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest, question);
        List<String> tags = questionUpdateRequest.getTags();
        if (tags != null) {
            question.setTags(JSONUtil.toJsonStr(tags));
        }

        CodeTemplate codeTemplate = questionUpdateRequest.getCodeTemplate();
        if (codeTemplate != null) {
            question.setCodeTemplate(JSONUtil.toJsonStr(codeTemplate));
        }

        // 参数校验
        questionService.validQuestion(question, false);
        long id = questionUpdateRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = questionService.updateById(question);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<QuestionVO> getQuestionVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = questionService.getById(id);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(questionService.getQuestionVO(question));
    }


    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/all")
    public BaseResponse<Question> getQuestionById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = questionService.getById(id);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(question);
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param questionQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<Question>> listQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionPage);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                               HttpServletRequest request) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listMyQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                                 HttpServletRequest request) {
        if (questionQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        questionQueryRequest.setUserId(loginUser.getId());
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }


    /**
     * 编辑（用户）
     *
     * @param questionEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editQuestion(@RequestBody QuestionEditRequest questionEditRequest, HttpServletRequest request) {
        if (questionEditRequest == null || questionEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionEditRequest, question);
        List<String> tags = questionEditRequest.getTags();
        if (tags != null) {
            question.setTags(JSONUtil.toJsonStr(tags));
        }
        List<JudgeCase> judgeCase = questionEditRequest.getJudgeCase();
        if (CollUtil.isNotEmpty(judgeCase)) {
            question.setJudgeCase(JSONUtil.toJsonStr(judgeCase));
        }

        CodeTemplate codeTemplate = questionEditRequest.getCodeTemplate();
        if (codeTemplate != null) {
            question.setCodeTemplate(JSONUtil.toJsonStr(codeTemplate));
        }

        // 参数校验
        questionService.validQuestion(question, false);
        User loginUser = userFeignClient.getLoginUser(request);
        long id = questionEditRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldQuestion.getUserId().equals(loginUser.getId()) && !userFeignClient.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = questionService.updateById(question);
        return ResultUtils.success(result);
    }

    @PostMapping("/list/admin-page")
    public BaseResponse<Page<Question>> getQuestionList(@RequestBody QuestionQueryRequest questionQueryRequest) {
        long current = questionQueryRequest.getCurrent();
        long pageSize = questionQueryRequest.getPageSize();
        Page<Question> page = questionService.page(new Page<>(current, pageSize), questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(page);
    }

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return
     */
    @PostMapping("/submit")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
                                               HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能提交题目
        final User loginUser = userFeignClient.getLoginUser(request);
        long result = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 分页获取题目提交列表
     *
     * @return
     */
    @PostMapping("/submit/admin/page")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
                                                                         HttpServletRequest request
    ) {
        long current = questionSubmitQueryRequest.getCurrent();
        long pageSize = questionSubmitQueryRequest.getPageSize();
        User loginUser = userFeignClient.getLoginUser(request);
        Page<QuestionSubmit> page = questionSubmitService.page(new Page<>(current, pageSize), questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(page, loginUser));
    }

    @GetMapping("/submit/getSubmitStatus")
    public BaseResponse<QuestionSubmitVO> getSubmit(long submitId) {
        QuestionSubmit questionSubmit = questionSubmitService.getById(submitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(QuestionSubmitVO.objToVo(questionSubmit));
    }

    @GetMapping("/submit/admin/list")
    public BaseResponse<List<QuestionSubmit>> getAllQuestionSubmitByList() {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        List<QuestionSubmit> list = questionSubmitService.list(queryWrapper);
        return ResultUtils.success(list);
    }

    /**
     * 获取当前用户提交热力图数据
     *
     * @param request HTTP请求
     * @param startDate 开始日期（可选，格式：yyyy-MM-dd，默认为最近365天）
     * @param endDate 结束日期（可选，格式：yyyy-MM-dd，默认为今天）
     * @return 热力图数据
     */
    @GetMapping("/submit/heatmap")
    @Operation(summary = "获取用户提交热力图", description = "获取当前登录用户在指定时间范围内的题目提交热力图数据，类似LeetCode/GitHub的贡献图")
    public BaseResponse<SubmitHeatmapVO> getSubmitHeatmap(
            HttpServletRequest request,
            @Parameter(description = "开始日期，格式：yyyy-MM-dd（可选，默认为最近365天）", example = "2024-01-01")
            @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期，格式：yyyy-MM-dd（可选，默认为今天）", example = "2024-12-31")
            @RequestParam(required = false) String endDate) {
        // 获取当前登录用户
        User loginUser = userFeignClient.getLoginUser(request);
        Long userId = loginUser.getId();

        // 构建请求参数
        SubmitHeatmapRequest heatmapRequest = new SubmitHeatmapRequest();
        if (startDate != null) {
            heatmapRequest.setStartDate(DateUtil.parse(startDate, "yyyy-MM-dd"));
        }
        if (endDate != null) {
            heatmapRequest.setEndDate(DateUtil.parse(endDate, "yyyy-MM-dd"));
        }

        // 生成缓存key
        String cacheKey = buildCacheKey(userId, heatmapRequest);
        
        // 检查是否包含今天的数据，包含则不缓存今天的部分
        boolean includeToday = isIncludeToday(heatmapRequest);
        
        // 尝试从缓存获取
        if (!includeToday) {
            CacheEntry cached = heatmapCache.get(cacheKey);
            if (cached != null && !cached.isExpired()) {
                log.debug("从缓存获取热力图数据, userId: {}", userId);
                return ResultUtils.success(cached.data);
            }
        }

        // 查询数据
        SubmitHeatmapVO result = questionSubmitService.getSubmitHeatmap(userId, heatmapRequest);

        // 缓存结果（历史数据缓存1小时）
        if (!includeToday) {
            long expireTime = System.currentTimeMillis() + 60 * 60 * 1000; // 1小时
            heatmapCache.put(cacheKey, new CacheEntry(result, expireTime));
            log.debug("缓存热力图数据, userId: {}, 过期时间: {}", userId, expireTime);
        }

        return ResultUtils.success(result);
    }

    /**
     * 构建缓存key
     */
    private String buildCacheKey(Long userId, SubmitHeatmapRequest request) {
        String start = request.getStartDate() != null ? 
                DateUtil.format(request.getStartDate(), "yyyyMMdd") : "default";
        String end = request.getEndDate() != null ? 
                DateUtil.format(request.getEndDate(), "yyyyMMdd") : "default";
        return userId + "_" + start + "_" + end;
    }

    /**
     * 判断查询范围是否包含今天
     */
    private boolean isIncludeToday(SubmitHeatmapRequest request) {
        Date today = DateUtil.beginOfDay(new Date());
        Date endDate = request.getEndDate();
        if (endDate == null) {
            return true; // 默认包含今天
        }
        return !DateUtil.beginOfDay(endDate).before(today);
    }
}

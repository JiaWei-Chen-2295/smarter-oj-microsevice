package fun.javierchen.jcojbackendquestionservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.javierchen.jcojbackendcommon.common.BaseResponse;
import fun.javierchen.jcojbackendcommon.common.DeleteRequest;
import fun.javierchen.jcojbackendcommon.common.ErrorCode;
import fun.javierchen.jcojbackendcommon.common.ResultUtils;
import fun.javierchen.jcojbackendcommon.exception.BusinessException;
import fun.javierchen.jcojbackendcommon.exception.ThrowUtils;
import fun.javierchen.jcojbackendmodel.dto.questionset.*;
import fun.javierchen.jcojbackendmodel.entity.QuestionSet;
import fun.javierchen.jcojbackendmodel.entity.User;
import fun.javierchen.jcojbackendmodel.vo.QuestionSetVO;
import fun.javierchen.jcojbackendquestionservice.cache.CacheInvalidator;
import fun.javierchen.jcojbackendquestionservice.cache.QuestionSetCacheService;
import fun.javierchen.jcojbackendquestionservice.service.QuestionSetService;
import fun.javierchen.jcojbackendserverclient.UserFeignClient;
import fun.javierchen.jcojbackendserverclient.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/questionSet")
@Slf4j
public class QuestionSetController {

    @Resource
    private QuestionSetService questionSetService;

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private CacheInvalidator cacheInvalidator;

    @Resource
    private QuestionSetCacheService questionSetCacheService;

    @GetMapping("/v3/api-docs")
    public void redirectOpenApiDocs(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/question/v3/api-docs");
    }

    @GetMapping("/doc.html")
    public void redirectDocHtml(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/question/doc.html");
    }

    @PostMapping("/add")
    public BaseResponse<Long> addQuestionSet(@RequestBody QuestionSetAddRequest questionSetAddRequest,
            HttpServletRequest request) {
        if (questionSetAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionSet questionSet = new QuestionSet();
        BeanUtils.copyProperties(questionSetAddRequest, questionSet);
        if (questionSetAddRequest.getTags() != null) {
            questionSet.setTags(cn.hutool.json.JSONUtil.toJsonStr(questionSetAddRequest.getTags()));
        }
        questionSetService.validQuestionSet(questionSet, true);
        User loginUser = UserUtils.getLoginUser();
        long newQuestionSetId = questionSetService.createQuestionSet(questionSet, loginUser);
        // 新增题目集 → 失效列表缓存
        cacheInvalidator.onQuestionSetAdded();
        return ResultUtils.success(newQuestionSetId);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestionSet(@RequestBody DeleteRequest deleteRequest,
            HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = UserUtils.getLoginUser();
        long id = deleteRequest.getId();
        QuestionSet oldQuestionSet = questionSetService.getById(id);
        ThrowUtils.throwIf(oldQuestionSet == null, ErrorCode.NOT_FOUND_ERROR);
        if (!oldQuestionSet.getUserId().equals(user.getId()) && !UserUtils.isAdmin()) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = questionSetService.removeById(id);
        // 删除题目集 → 失效详情 + 列表缓存
        cacheInvalidator.onQuestionSetUpdatedOrDeleted(id);
        return ResultUtils.success(b);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateQuestionSet(@RequestBody QuestionSetUpdateRequest questionSetUpdateRequest) {
        User loginUser = UserUtils.getLoginUser();
        ThrowUtils.throwIf(!UserUtils.isAdmin(loginUser), ErrorCode.NO_AUTH_ERROR);
        if (questionSetUpdateRequest == null || questionSetUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionSet questionSet = new QuestionSet();
        BeanUtils.copyProperties(questionSetUpdateRequest, questionSet);
        if (questionSetUpdateRequest.getTags() != null) {
            questionSet.setTags(cn.hutool.json.JSONUtil.toJsonStr(questionSetUpdateRequest.getTags()));
        }
        questionSetService.validQuestionSet(questionSet, false);
        long id = questionSetUpdateRequest.getId();
        QuestionSet oldQuestionSet = questionSetService.getById(id);
        ThrowUtils.throwIf(oldQuestionSet == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = questionSetService.updateById(questionSet);
        // 更新题目集 → 失效详情 + 列表缓存
        cacheInvalidator.onQuestionSetUpdatedOrDeleted(id);
        return ResultUtils.success(result);
    }

    @GetMapping("/get/vo")
    public BaseResponse<QuestionSetVO> getQuestionSetVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionSetVO questionSetVO = questionSetCacheService.getQuestionSetVOById(id, request);
        if (questionSetVO == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(questionSetVO);
    }

    @GetMapping("/get")
    public BaseResponse<QuestionSet> getQuestionSetById(long id) {
        User loginUser = UserUtils.getLoginUser();
        ThrowUtils.throwIf(!UserUtils.isAdmin(loginUser), ErrorCode.NO_AUTH_ERROR);
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionSet questionSet = questionSetService.getById(id);
        if (questionSet == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(questionSet);
    }

    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionSet>> listQuestionSetByPage(
            @RequestBody QuestionSetQueryRequest questionSetQueryRequest) {
        User loginUser = UserUtils.getLoginUser();
        ThrowUtils.throwIf(!UserUtils.isAdmin(loginUser), ErrorCode.NO_AUTH_ERROR);
        long current = questionSetQueryRequest.getCurrent();
        long size = questionSetQueryRequest.getPageSize();
        Page<QuestionSet> questionSetPage = questionSetService.page(new Page<>(current, size),
                questionSetService.getQueryWrapper(questionSetQueryRequest));
        return ResultUtils.success(questionSetPage);
    }

    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionSetVO>> listQuestionSetVOByPage(
            @RequestBody QuestionSetQueryRequest questionSetQueryRequest,
            HttpServletRequest request) {
        long size = questionSetQueryRequest.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 多级缓存查询
        Page<QuestionSetVO> result = questionSetCacheService.listQuestionSetVOByPageWithCache(questionSetQueryRequest, request);
        return ResultUtils.success(result);
    }

    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<QuestionSetVO>> listMyQuestionSetVOByPage(
            @RequestBody QuestionSetQueryRequest questionSetQueryRequest,
            HttpServletRequest request) {
        if (questionSetQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = UserUtils.getLoginUser();
        questionSetQueryRequest.setUserId(loginUser.getId());
        long current = questionSetQueryRequest.getCurrent();
        long size = questionSetQueryRequest.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<QuestionSet> questionSetPage = questionSetService.page(new Page<>(current, size),
                questionSetService.getQueryWrapper(questionSetQueryRequest));
        return ResultUtils.success(questionSetService.getQuestionSetVOPage(questionSetPage, request));
    }

    @PostMapping("/edit")
    public BaseResponse<Boolean> editQuestionSet(@RequestBody QuestionSetEditRequest questionSetEditRequest,
            HttpServletRequest request) {
        if (questionSetEditRequest == null || questionSetEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionSet questionSet = new QuestionSet();
        BeanUtils.copyProperties(questionSetEditRequest, questionSet);
        if (questionSetEditRequest.getTags() != null) {
            questionSet.setTags(cn.hutool.json.JSONUtil.toJsonStr(questionSetEditRequest.getTags()));
        }
        questionSetService.validQuestionSet(questionSet, false);
        User loginUser = UserUtils.getLoginUser();
        long id = questionSetEditRequest.getId();
        QuestionSet oldQuestionSet = questionSetService.getById(id);
        ThrowUtils.throwIf(oldQuestionSet == null, ErrorCode.NOT_FOUND_ERROR);
        if (!oldQuestionSet.getUserId().equals(loginUser.getId()) && !UserUtils.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = questionSetService.updateById(questionSet);
        // 编辑题目集 → 失效详情 + 列表缓存
        cacheInvalidator.onQuestionSetUpdatedOrDeleted(id);
        return ResultUtils.success(result);
    }

    @PostMapping("/item/add")
    public BaseResponse<Boolean> addQuestionToSet(@RequestBody QuestionSetItemAddRequest itemAddRequest,
            HttpServletRequest request) {
        if (itemAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = UserUtils.getLoginUser();
        boolean result = questionSetService.addQuestionToSet(
                itemAddRequest.getQuestionSetId(),
                itemAddRequest.getQuestionId(),
                itemAddRequest.getSortOrder(),
                loginUser);
        // 题目加入集合 → 失效该集详情缓存
        cacheInvalidator.onQuestionSetItemChanged(itemAddRequest.getQuestionSetId());
        return ResultUtils.success(result);
    }

    @PostMapping("/item/remove")
    public BaseResponse<Boolean> removeQuestionFromSet(@RequestBody QuestionSetItemRemoveRequest itemRemoveRequest,
            HttpServletRequest request) {
        if (itemRemoveRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = UserUtils.getLoginUser();
        boolean result = questionSetService.removeQuestionFromSet(
                itemRemoveRequest.getQuestionSetId(),
                itemRemoveRequest.getQuestionId(),
                loginUser);
        // 题目移出集合 → 失效该集详情缓存
        cacheInvalidator.onQuestionSetItemChanged(itemRemoveRequest.getQuestionSetId());
        return ResultUtils.success(result);
    }
}

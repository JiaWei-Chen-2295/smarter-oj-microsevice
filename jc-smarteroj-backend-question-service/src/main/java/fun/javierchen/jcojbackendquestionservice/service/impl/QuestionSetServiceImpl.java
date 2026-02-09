package fun.javierchen.jcojbackendquestionservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.javierchen.jcojbackendcommon.common.ErrorCode;
import fun.javierchen.jcojbackendcommon.constant.CommonConstant;
import fun.javierchen.jcojbackendcommon.exception.BusinessException;
import fun.javierchen.jcojbackendcommon.exception.ThrowUtils;
import fun.javierchen.jcojbackendcommon.utils.SqlUtils;
import fun.javierchen.jcojbackendmodel.dto.questionset.QuestionSetQueryRequest;
import fun.javierchen.jcojbackendmodel.entity.Question;
import fun.javierchen.jcojbackendmodel.entity.QuestionSet;
import fun.javierchen.jcojbackendmodel.entity.QuestionSetItem;
import fun.javierchen.jcojbackendmodel.entity.User;
import fun.javierchen.jcojbackendmodel.vo.QuestionSetVO;
import fun.javierchen.jcojbackendmodel.vo.QuestionVO;
import fun.javierchen.jcojbackendmodel.vo.UserVO;
import fun.javierchen.jcojbackendquestionservice.cache.UserCacheService;
import fun.javierchen.jcojbackendquestionservice.mapper.QuestionSetMapper;
import fun.javierchen.jcojbackendquestionservice.service.QuestionService;
import fun.javierchen.jcojbackendquestionservice.service.QuestionSetItemService;
import fun.javierchen.jcojbackendquestionservice.service.QuestionSetService;
import fun.javierchen.jcojbackendserverclient.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuestionSetServiceImpl extends ServiceImpl<QuestionSetMapper, QuestionSet> implements QuestionSetService {

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private UserCacheService userCacheService;

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSetItemService questionSetItemService;

    @Override
    public void validQuestionSet(QuestionSet questionSet, boolean add) {
        if (questionSet == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = questionSet.getTitle();
        String description = questionSet.getDescription();
        String tags = questionSet.getTags();

        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title), ErrorCode.PARAMS_ERROR, "题目单标题不能为空");
        }

        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "题目单标题过长");
        }

        if (StringUtils.isNotBlank(description) && description.length() > 1024) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "题目单描述过长");
        }

        if (StringUtils.isNotBlank(tags) && tags.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标签过多");
        }
    }

    @Override
    public QueryWrapper<QuestionSet> getQueryWrapper(QuestionSetQueryRequest questionSetQueryRequest) {
        QueryWrapper<QuestionSet> queryWrapper = new QueryWrapper<>();
        if (questionSetQueryRequest == null) {
            return queryWrapper;
        }

        Long id = questionSetQueryRequest.getId();
        String title = questionSetQueryRequest.getTitle();
        String description = questionSetQueryRequest.getDescription();
        Long userId = questionSetQueryRequest.getUserId();
        String sortField = questionSetQueryRequest.getSortField();
        String sortOrder = questionSetQueryRequest.getSortOrder();

        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), CommonConstant.SORT_ORDER_ASC.equals(sortOrder), sortField);
        return queryWrapper;
    }

    @Override
    public QuestionSetVO getQuestionSetVO(QuestionSet questionSet, HttpServletRequest request) {
        QuestionSetVO questionSetVO = QuestionSetVO.objToVo(questionSet);
        Long userId = questionSet.getUserId();
        if (userId != null && userId > 0) {
            questionSetVO.setUserVO(userCacheService.getUserVO(userId));
        }

        Long questionSetId = questionSet.getId();
        List<Question> questionList = this.baseMapper.getQuestionsByQuestionSetId(questionSetId);
        List<QuestionVO> questionVOList = questionList.stream().map(QuestionVO::objToVo).collect(Collectors.toList());
        questionSetVO.setQuestions(questionVOList);
        return questionSetVO;
    }

    @Override
    public Page<QuestionSetVO> getQuestionSetVOPage(Page<QuestionSet> questionSetPage, HttpServletRequest request) {
        List<QuestionSet> questionSetList = questionSetPage.getRecords();
        Page<QuestionSetVO> questionSetVOPage = new Page<>(questionSetPage.getCurrent(), questionSetPage.getSize(), questionSetPage.getTotal());
        if (CollUtil.isEmpty(questionSetList)) {
            return questionSetVOPage;
        }

        // 批量获取用户信息（带缓存）
        Set<Long> userIdSet = questionSetList.stream().map(QuestionSet::getUserId).collect(Collectors.toSet());
        Map<Long, UserVO> userVOMap = userCacheService.listUserVOByIds(userIdSet);

        Set<Long> questionSetIdSet = questionSetList.stream().map(QuestionSet::getId).collect(Collectors.toSet());
        List<Question> questionList = this.baseMapper.listQuestionMapByQuestionSetIds(questionSetIdSet);

        Map<Long, List<Question>> questionSetQuestionMap = new HashMap<>();
        for (Question question : questionList) {
            Long qSetId = question.getQuestionSetId();
            if (qSetId == null) {
                continue;
            }
            questionSetQuestionMap.computeIfAbsent(qSetId, k -> new ArrayList<>()).add(question);
        }

        List<QuestionSetVO> questionSetVOList = questionSetList.stream().map(qs -> {
            QuestionSetVO questionSetVO = QuestionSetVO.objToVo(qs);
            questionSetVO.setUserVO(userVOMap.get(qs.getUserId()));

            List<Question> questions = questionSetQuestionMap.get(qs.getId());
            if (questions == null) {
                questions = new ArrayList<>();
            }
            List<QuestionVO> questionVOList2 = questions.stream().map(QuestionVO::objToVo).collect(Collectors.toList());
            questionSetVO.setQuestions(questionVOList2);
            return questionSetVO;
        }).collect(Collectors.toList());

        questionSetVOPage.setRecords(questionSetVOList);
        return questionSetVOPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long createQuestionSet(QuestionSet questionSet, User loginUser) {
        validQuestionSet(questionSet, true);
        questionSet.setUserId(loginUser.getId());
        questionSet.setFavourNum(0);
        questionSet.setQuestionNum(0);
        boolean result = this.save(questionSet);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "创建题目单失败");
        log.info("用户 {} 创建题目单 {}", loginUser.getId(), questionSet.getId());
        return questionSet.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addQuestionToSet(Long questionSetId, Long questionId, Integer sortOrder, User loginUser) {
        ThrowUtils.throwIf(questionSetId == null || questionSetId <= 0, ErrorCode.PARAMS_ERROR, "题目单id不能为空");
        ThrowUtils.throwIf(questionId == null || questionId <= 0, ErrorCode.PARAMS_ERROR, "题目id不能为空");

        QuestionSet questionSet = this.getById(questionSetId);
        ThrowUtils.throwIf(questionSet == null, ErrorCode.NOT_FOUND_ERROR, "题目单不存在");

        if (!questionSet.getUserId().equals(loginUser.getId())) {
            boolean isAdmin = userFeignClient.isAdmin(loginUser);
            ThrowUtils.throwIf(!isAdmin, ErrorCode.NO_AUTH_ERROR, "只有题目单创建者或管理员可以添加题目");
        }

        Question question = questionService.getById(questionId);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");

        QueryWrapper<QuestionSetItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("questionSetId", questionSetId);
        queryWrapper.eq("questionId", questionId);
        long count = questionSetItemService.count(queryWrapper);
        ThrowUtils.throwIf(count > 0, ErrorCode.OPERATION_ERROR, "题目已在题目单中");

        QuestionSetItem item = new QuestionSetItem();
        item.setQuestionSetId(questionSetId);
        item.setQuestionId(questionId);
        item.setSortOrder(sortOrder != null ? sortOrder : 0);
        boolean saveResult = questionSetItemService.save(item);
        ThrowUtils.throwIf(!saveResult, ErrorCode.OPERATION_ERROR, "添加题目失败");

        questionSet.setQuestionNum(questionSet.getQuestionNum() + 1);
        this.updateById(questionSet);

        log.info("用户 {} 向题目单 {} 添加题目 {}", loginUser.getId(), questionSetId, questionId);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeQuestionFromSet(Long questionSetId, Long questionId, User loginUser) {
        ThrowUtils.throwIf(questionSetId == null || questionSetId <= 0, ErrorCode.PARAMS_ERROR, "题目单id不能为空");
        ThrowUtils.throwIf(questionId == null || questionId <= 0, ErrorCode.PARAMS_ERROR, "题目id不能为空");

        QuestionSet questionSet = this.getById(questionSetId);
        ThrowUtils.throwIf(questionSet == null, ErrorCode.NOT_FOUND_ERROR, "题目单不存在");

        if (!questionSet.getUserId().equals(loginUser.getId())) {
            boolean isAdmin = userFeignClient.isAdmin(loginUser);
            ThrowUtils.throwIf(!isAdmin, ErrorCode.NO_AUTH_ERROR, "只有题目单创建者或管理员可以移除题目");
        }

        QueryWrapper<QuestionSetItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("questionSetId", questionSetId);
        queryWrapper.eq("questionId", questionId);
        QuestionSetItem item = questionSetItemService.getOne(queryWrapper);
        ThrowUtils.throwIf(item == null, ErrorCode.NOT_FOUND_ERROR, "题目不在题目单中");

        boolean result = questionSetItemService.removeById(item.getId());
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "移除题目失败");

        questionSet.setQuestionNum(Math.max(0, questionSet.getQuestionNum() - 1));
        this.updateById(questionSet);

        log.info("用户 {} 从题目单 {} 移除题目 {}", loginUser.getId(), questionSetId, questionId);
        return true;
    }
}


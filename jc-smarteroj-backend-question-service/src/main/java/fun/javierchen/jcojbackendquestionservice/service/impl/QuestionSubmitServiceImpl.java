package fun.javierchen.jcojbackendquestionservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import fun.javierchen.jcojbackendcommon.common.ErrorCode;
import fun.javierchen.jcojbackendcommon.constant.CommonConstant;
import fun.javierchen.jcojbackendcommon.enums.QuestionSubmitLanguageEnum;
import fun.javierchen.jcojbackendcommon.exception.BusinessException;
import fun.javierchen.jcojbackendcommon.utils.SqlUtils;
import fun.javierchen.jcojbackendmodel.dto.questionsubmit.JudgeRequest;
import fun.javierchen.jcojbackendmodel.dto.questionsubmit.SubmitHeatmapRequest;
import fun.javierchen.jcojbackendmodel.enums.QuestionSubmitStatusEnum;
import fun.javierchen.jcojbackendquestionservice.mapper.QuestionSubmitMapper;
import fun.javierchen.jcojbackendmodel.dto.questionsubmit.JudgeInfo;
import fun.javierchen.jcojbackendmodel.dto.questionsubmit.QuestionSubmitAddRequest;
import fun.javierchen.jcojbackendmodel.dto.questionsubmit.QuestionSubmitQueryRequest;
import fun.javierchen.jcojbackendmodel.entity.Question;
import fun.javierchen.jcojbackendmodel.entity.QuestionSubmit;
import fun.javierchen.jcojbackendmodel.entity.User;
import fun.javierchen.jcojbackendmodel.vo.QuestionSubmitVO;
import fun.javierchen.jcojbackendmodel.vo.QuestionVO;
import fun.javierchen.jcojbackendmodel.vo.SubmitHeatmapVO;
import fun.javierchen.jcojbackendmodel.vo.UserVO;

import fun.javierchen.jcojbackendquestionservice.service.QuestionService;
import fun.javierchen.jcojbackendquestionservice.service.QuestionSubmitService;

import fun.javierchen.jcojbackendserverclient.JudgeFeignClient;
import fun.javierchen.jcojbackendserverclient.UserFeignClient;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author 16010
 * @description 针对表【question_submit(用户提交表)】的数据库操作Service实现
 * @createDate 2025-03-25 17:00:22
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private QuestionService questionService;

    /*
    Lazy 懒加载 防止循环依赖
     */
    @Resource
    @Lazy
    private JudgeFeignClient judgeService;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest 提交问题请求封装类
     * @param loginUser                登录的用户
     * @return 问题提交的 ID
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum submitLanguageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (submitLanguageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "指定的语言不存在或者不支持");
        }

        String code = questionSubmitAddRequest.getCode();
        if (StringUtils.isBlank(code)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请给出需要判题的代码");
        }

        // 判断实体是否存在，根据类别获取实体
        Long questionId = questionSubmitAddRequest.getQuestionId();
        Question question = questionService.getById(questionId);
        QuestionSubmit questionSubmit = getQuestionSubmit(loginUser, question, questionId, submitLanguageEnum, code);
        //todo: 对提交题目进行限流
        boolean save = this.save(questionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        Long id = questionSubmit.getId();
        CompletableFuture.runAsync(() -> {
            JudgeRequest judgeRequest = new JudgeRequest();
            judgeRequest.setQuestionSubmitId(id);
            judgeRequest.setLoginUser(loginUser);
            judgeService.doJudge(judgeRequest);
        });
        return id;
    }

    @NotNull
    private static QuestionSubmit getQuestionSubmit(User loginUser, Question question, Long questionId, QuestionSubmitLanguageEnum questionSubmitLanguageEnum, String code) {
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        long userId = loginUser.getId();

        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setUserId(userId);
        questionSubmit.setLanguage(questionSubmitLanguageEnum.getValue());
        questionSubmit.setCode(code);
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(new JudgeInfo()));
        return questionSubmit;
    }

    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }

        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();

        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);

        if (ObjectUtils.isNotEmpty(status)) {
            QuestionSubmitStatusEnum questionSubmitEnum = QuestionSubmitStatusEnum.getEnumByValue(status);
            queryWrapper.eq(ObjectUtils.isNotEmpty(questionSubmitEnum), "status", questionSubmitEnum.getValue());
        }

        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);

        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);

        // 如果当前用户不是管理员或者题目提交者 就不返回代码详情
        Long id = loginUser.getId();
        if (!questionSubmit.getUserId().equals(id) && !userFeignClient.isAdmin(loginUser)) {
            questionSubmit.setCode(null);
        }

        // 1. 关联查询用户信息
        Long userId = questionSubmit.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userFeignClient.getById(userId);
        }
        UserVO userVO = userFeignClient.getUserVO(user);
        questionSubmitVO.setUserVO(userVO);
        // 2.关联查询题目信息
        Long questionId = questionSubmit.getQuestionId();
        if (questionId != null && questionId > 1) {
            Question question = questionService.getById(questionId);
            QuestionVO questionVO = questionService.getQuestionVO(question);
            questionSubmitVO.setQuestionVO(questionVO);
        }

        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollUtil.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionSubmitList.stream().map(QuestionSubmit::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userFeignClient.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream().map(questionSubmit ->
            getQuestionSubmitVO(questionSubmit, loginUser)
        ).collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }

    @Override
    public SubmitHeatmapVO getSubmitHeatmap(Long userId, SubmitHeatmapRequest request) {
        // 参数校验：如果未传日期，默认查询最近365天
        Date endDate = request.getEndDate();
        Date startDate = request.getStartDate();
        if (endDate == null) {
            endDate = DateUtil.endOfDay(new Date());
        } else {
            // 确保 endDate 是当天的结束时间
            endDate = DateUtil.endOfDay(endDate);
        }
        if (startDate == null) {
            startDate = DateUtil.offsetDay(endDate, -364);
        }
        // 确保 startDate 是当天的开始时间
        startDate = DateUtil.beginOfDay(startDate);

        // 查询每日提交统计
        List<Map<String, Object>> dailyStats = baseMapper.selectDailySubmitCount(userId, startDate, endDate);

        // 构建热力图数据
        List<SubmitHeatmapVO.DailySubmitCount> heatmapData = new ArrayList<>();
        long totalSubmissions = 0;
        int maxDailySubmissions = 0;

        // 将查询结果转换为Map，方便查找
        Map<String, Integer> dateCountMap = new HashMap<>();
        for (Map<String, Object> stat : dailyStats) {
            String date = (String) stat.get("submitDate");
            Integer count = ((Number) stat.get("submitCount")).intValue();
            dateCountMap.put(date, count);
            totalSubmissions += count;
            maxDailySubmissions = Math.max(maxDailySubmissions, count);
        }

        // 生成日期范围内的所有日期（包含没有提交的日期，记为0）
        Date current = startDate;
        while (!current.after(endDate)) {
            String dateStr = DateUtil.format(current, "yyyy-MM-dd");
            SubmitHeatmapVO.DailySubmitCount dailyCount = new SubmitHeatmapVO.DailySubmitCount();
            dailyCount.setDate(dateStr);
            dailyCount.setCount(dateCountMap.getOrDefault(dateStr, 0));
            heatmapData.add(dailyCount);
            current = DateUtil.offsetDay(current, 1);
        }

        // 计算连续提交天数
        int currentStreak = 0;
        int maxStreak = 0;
        int tempStreak = 0;
        String today = DateUtil.format(new Date(), "yyyy-MM-dd");
        String yesterday = DateUtil.format(DateUtil.offsetDay(new Date(), -1), "yyyy-MM-dd");

        // 从最新日期开始计算当前连续天数
        for (int i = heatmapData.size() - 1; i >= 0; i--) {
            SubmitHeatmapVO.DailySubmitCount dc = heatmapData.get(i);
            if (dc.getCount() > 0) {
                // 当前连续：从今天或昨天开始连续
                if (currentStreak == 0 && (dc.getDate().equals(today) || dc.getDate().equals(yesterday))) {
                    currentStreak = 1;
                    // 继续往前查找
                    for (int j = i - 1; j >= 0; j--) {
                        if (heatmapData.get(j).getCount() > 0) {
                            String prevDate = DateUtil.format(DateUtil.offsetDay(DateUtil.parse(heatmapData.get(j + 1).getDate()), -1), "yyyy-MM-dd");
                            if (heatmapData.get(j).getDate().equals(prevDate)) {
                                currentStreak++;
                            } else {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
                break;
            }
        }

        // 计算最长连续天数
        for (SubmitHeatmapVO.DailySubmitCount dc : heatmapData) {
            if (dc.getCount() > 0) {
                tempStreak++;
                maxStreak = Math.max(maxStreak, tempStreak);
            } else {
                tempStreak = 0;
            }
        }

        // 构建返回结果
        SubmitHeatmapVO result = new SubmitHeatmapVO();
        result.setHeatmapData(heatmapData);
        result.setTotalSubmissions(totalSubmissions);
        result.setMaxDailySubmissions(maxDailySubmissions);
        result.setActiveDays(dateCountMap.size());
        result.setCurrentStreak(currentStreak);
        result.setMaxStreak(maxStreak);

        return result;
    }

}

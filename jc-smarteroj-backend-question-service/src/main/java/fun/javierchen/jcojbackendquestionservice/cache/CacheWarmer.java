package fun.javierchen.jcojbackendquestionservice.cache;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.javierchen.jcojbackendmodel.dto.question.QuestionQueryRequest;
import fun.javierchen.jcojbackendmodel.dto.questionset.QuestionSetQueryRequest;
import fun.javierchen.jcojbackendmodel.entity.Question;
import fun.javierchen.jcojbackendquestionservice.service.QuestionService;
import fun.javierchen.jcojbackendquestionservice.service.QuestionSetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 缓存预热
 * <p>
 * 服务启动时异步预热热点数据到 L1 + L2 缓存。
 * </p>
 *
 * @author JavierChen
 */
@Component
@Slf4j
public class CacheWarmer implements ApplicationRunner {

    @Resource
    private QuestionCacheService questionCacheService;

    @Resource
    private QuestionSetCacheService questionSetCacheService;

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSetService questionSetService;

    @Override
    public void run(ApplicationArguments args) {
        // 异步预热，不阻塞启动
        new Thread(() -> {
            try {
                log.info("[CacheWarmer] 开始预热缓存...");
                warmQuestionList();
                warmTopQuestions();
                warmQuestionSets();
                log.info("[CacheWarmer] 缓存预热完成");
            } catch (Exception e) {
                log.warn("[CacheWarmer] 缓存预热失败: {}", e.getMessage());
            }
        }, "cache-warmer").start();
    }

    /**
     * 预热题目列表前3页（默认排序）
     */
    private void warmQuestionList() {
        for (int page = 1; page <= 3; page++) {
            try {
                QuestionQueryRequest request = new QuestionQueryRequest();
                request.setCurrent(page);
                request.setPageSize(20);
                questionCacheService.listQuestionVOByPageWithCache(request, null);
                log.info("[CacheWarmer] 预热题目列表第{}页", page);
            } catch (Exception e) {
                log.warn("[CacheWarmer] 预热题目列表第{}页失败: {}", page, e.getMessage());
            }
        }
    }

    /**
     * 预热提交量Top50题目详情
     */
    private void warmTopQuestions() {
        try {
            QuestionQueryRequest request = new QuestionQueryRequest();
            request.setCurrent(1);
            request.setPageSize(50);
            request.setSortField("submitNum");
            request.setSortOrder("descend");
            Page<Question> topPage = questionService.page(
                    new Page<>(1, 50),
                    questionService.getQueryWrapper(request));
            List<Question> topQuestions = topPage.getRecords();
            for (Question q : topQuestions) {
                try {
                    questionCacheService.getQuestionVOById(q.getId());
                } catch (Exception e) {
                    log.warn("[CacheWarmer] 预热题目{}失败: {}", q.getId(), e.getMessage());
                }
            }
            log.info("[CacheWarmer] 预热Top{}题目详情完成", topQuestions.size());
        } catch (Exception e) {
            log.warn("[CacheWarmer] 预热Top题目失败: {}", e.getMessage());
        }
    }

    /**
     * 预热题目集列表前3页
     */
    private void warmQuestionSets() {
        for (int page = 1; page <= 3; page++) {
            try {
                QuestionSetQueryRequest request = new QuestionSetQueryRequest();
                request.setCurrent(page);
                request.setPageSize(20);
                questionSetCacheService.listQuestionSetVOByPageWithCache(request, null);
                log.info("[CacheWarmer] 预热题目集列表第{}页", page);
            } catch (Exception e) {
                log.warn("[CacheWarmer] 预热题目集列表第{}页失败: {}", page, e.getMessage());
            }
        }
    }
}

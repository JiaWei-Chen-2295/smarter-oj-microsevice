package fun.javierchen.jcojbackendquestionservice.controller;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import fun.javierchen.jcojbackendcommon.common.BaseResponse;
import fun.javierchen.jcojbackendcommon.common.ErrorCode;
import fun.javierchen.jcojbackendcommon.common.ResultUtils;
import fun.javierchen.jcojbackendcommon.exception.ThrowUtils;
import fun.javierchen.jcojbackendmodel.vo.QuestionSetVO;
import fun.javierchen.jcojbackendmodel.vo.QuestionVO;
import fun.javierchen.jcojbackendmodel.vo.UserVO;
import fun.javierchen.jcojbackendserverclient.utils.UserUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 缓存监控接口（仅管理员）
 *
 * @author JavierChen
 */
@RestController
@RequestMapping("/admin/cache")
@Tag(name = "缓存监控")
public class CacheStatsController {

    @Resource
    private Cache<Long, QuestionVO> questionVOCache;

    @Resource
    private Cache<String, String> questionListCache;

    @Resource
    private Cache<Long, QuestionSetVO> questionSetVOCache;

    @Resource
    private Cache<String, String> questionSetListCache;

    @Resource
    private Cache<Long, UserVO> userVOCache;

    @GetMapping("/stats")
    @Operation(summary = "获取缓存统计信息", description = "返回所有 L1 Caffeine 缓存实例的命中率、大小等统计信息")
    public BaseResponse<Map<String, Object>> getCacheStats() {
        // 仅管理员可查看
        ThrowUtils.throwIf(!UserUtils.isAdmin(), ErrorCode.NO_AUTH_ERROR);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("questionVO", buildStats("questionVO", questionVOCache));
        result.put("questionList", buildStats("questionList", questionListCache));
        result.put("questionSetVO", buildStats("questionSetVO", questionSetVOCache));
        result.put("questionSetList", buildStats("questionSetList", questionSetListCache));
        result.put("userVO", buildStats("userVO", userVOCache));
        return ResultUtils.success(result);
    }

    private Map<String, Object> buildStats(String name, Cache<?, ?> cache) {
        CacheStats stats = cache.stats();
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("size", cache.estimatedSize());
        map.put("hitCount", stats.hitCount());
        map.put("missCount", stats.missCount());
        map.put("hitRate", String.format("%.2f%%", stats.hitRate() * 100));
        map.put("evictionCount", stats.evictionCount());
        map.put("loadCount", stats.loadCount());
        return map;
    }
}

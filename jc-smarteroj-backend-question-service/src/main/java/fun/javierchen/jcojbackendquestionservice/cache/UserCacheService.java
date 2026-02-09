package fun.javierchen.jcojbackendquestionservice.cache;

import com.github.benmanes.caffeine.cache.Cache;
import fun.javierchen.jcojbackendmodel.entity.User;
import fun.javierchen.jcojbackendmodel.vo.UserVO;
import fun.javierchen.jcojbackendserverclient.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 用户信息客户端缓存
 * <p>
 * 在 question-service 本地缓存 UserVO，消除 Feign N+1 调用瓶颈。
 * 仅使用 L1 Caffeine（用户信息变更低频，10分钟自然过期可接受）。
 * </p>
 *
 * @author JavierChen
 */
@Service
@Slf4j
public class UserCacheService {

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private Cache<Long, UserVO> userVOCache;

    /**
     * 根据 userId 获取 UserVO（带缓存）
     */
    public UserVO getUserVO(Long userId) {
        if (userId == null || userId <= 0) {
            return null;
        }
        UserVO cached = userVOCache.getIfPresent(userId);
        if (cached != null) {
            return cached;
        }
        // miss → Feign 调用
        User user = userFeignClient.getById(userId);
        UserVO userVO = userFeignClient.getUserVO(user);
        if (userVO != null) {
            userVOCache.put(userId, userVO);
        }
        return userVO;
    }

    /**
     * 批量获取 UserVO（带缓存，仅对 miss 部分发起 Feign 调用）
     */
    public Map<Long, UserVO> listUserVOByIds(Collection<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<Long, UserVO> result = new HashMap<>(userIds.size());
        List<Long> missIds = new ArrayList<>();

        // 1. 从 L1 批量查
        for (Long userId : userIds) {
            UserVO cached = userVOCache.getIfPresent(userId);
            if (cached != null) {
                result.put(userId, cached);
            } else {
                missIds.add(userId);
            }
        }

        // 2. miss 部分批量 Feign 调用
        if (!missIds.isEmpty()) {
            log.debug("UserCache miss {} ids, fetching via Feign", missIds.size());
            List<User> users = userFeignClient.listByIds(missIds);
            if (users != null) {
                for (User user : users) {
                    UserVO userVO = userFeignClient.getUserVO(user);
                    if (userVO != null) {
                        userVOCache.put(user.getId(), userVO);
                        result.put(user.getId(), userVO);
                    }
                }
            }
        }

        return result;
    }
}

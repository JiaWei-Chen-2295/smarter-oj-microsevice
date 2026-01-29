package fun.javierchen.jcojbackendserverclient;

import fun.javierchen.jcojbackendmodel.entity.User;
import fun.javierchen.jcojbackendmodel.enums.UserRoleEnum;
import fun.javierchen.jcojbackendmodel.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

/**
 * 用户服务 Feign 客户端
 * 
 * <p>
 * 提供用户相关的远程调用和本地工具方法
 * </p>
 * <p>
 * 注意：getLoginUser、isAdmin 等方法是 default 实现，在本地执行，使用 Sa-Token 验证
 * </p>
 *
 * @author JavierChen
 */
@FeignClient(name = "jc-smarteroj-backend-user-service", path = "/api/user/inner")
public interface UserFeignClient {

    /**
     * 根据 id 获取用户（远程调用）
     *
     * @param userId 用户 ID
     * @return 用户信息
     */
    @GetMapping("/get/id")
    User getById(@RequestParam("userId") Long userId);

    /**
     * 通过 id 集合查询用户集合（远程调用）
     *
     * @param idList 用户 ID 列表
     * @return 用户列表
     */
    @GetMapping("/list/id")
    List<User> listByIds(@RequestParam("idList") Collection<Long> idList);

    /**
     * 判断指定用户是否为管理员
     *
     * @param user 用户对象
     * @return 是否为管理员
     */
    default boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 获取脱敏的用户信息
     *
     * @param user 用户对象
     * @return 脱敏后的用户 VO
     */
    default UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
}

package fun.javierchen.jcojbackendserverclient;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import fun.javierchen.jcojbackendcommon.common.ErrorCode;
import fun.javierchen.jcojbackendcommon.exception.BusinessException;
import fun.javierchen.jcojbackendmodel.entity.User;
import fun.javierchen.jcojbackendmodel.enums.UserRoleEnum;
import fun.javierchen.jcojbackendmodel.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

import static fun.javierchen.jcojbackendcommon.constant.UserConstant.SA_SESSION_USER_KEY;

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
     * 获取当前登录用户（本地执行，使用 Sa-Token）
     * 
     * <p>
     * 此方法不再依赖 HttpServletRequest，直接使用 Sa-Token 获取登录状态
     * </p>
     *
     * @return 当前登录用户信息
     * @throws BusinessException 未登录时抛出异常
     */
    default User getLoginUser() {
        // 1. 使用 Sa-Token 判断是否已登录
        if (!StpUtil.isLogin()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 2. 从 Sa-Token Session 获取用户信息
        SaSession session = StpUtil.getSession();
        User currentUser = session.getModel(SA_SESSION_USER_KEY, User.class);
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 获取当前登录用户（兼容旧代码，保留 request 参数）
     * 
     * <p>
     * 实际实现委托给无参版本，request 参数仅用于接口兼容
     * </p>
     *
     * @param request HttpServletRequest（不再使用，仅为兼容）
     * @return 当前登录用户信息
     */
    default User getLoginUser(HttpServletRequest request) {
        return getLoginUser();
    }

    /**
     * 判断当前用户是否为管理员（本地执行）
     *
     * @return 是否为管理员
     */
    default boolean isAdmin() {
        if (!StpUtil.isLogin()) {
            return false;
        }
        SaSession session = StpUtil.getSession();
        User user = session.getModel(SA_SESSION_USER_KEY, User.class);
        return isAdmin(user);
    }

    /**
     * 判断当前用户是否为管理员（兼容旧代码）
     *
     * @param request HttpServletRequest（不再使用，仅为兼容）
     * @return 是否为管理员
     */
    default boolean isAdmin(HttpServletRequest request) {
        return isAdmin();
    }

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

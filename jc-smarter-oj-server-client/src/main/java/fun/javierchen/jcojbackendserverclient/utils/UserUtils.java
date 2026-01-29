package fun.javierchen.jcojbackendserverclient.utils;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import fun.javierchen.jcojbackendcommon.common.ErrorCode;
import fun.javierchen.jcojbackendcommon.exception.BusinessException;
import fun.javierchen.jcojbackendmodel.entity.User;
import fun.javierchen.jcojbackendmodel.enums.UserRoleEnum;

import javax.servlet.http.HttpServletRequest;

import static fun.javierchen.jcojbackendcommon.constant.UserConstant.SA_SESSION_USER_KEY;

/**
 * 用户工具类
 *
 * @author JavierChen
 */
public class UserUtils {

    /**
     * 获取当前登录用户（使用 Sa-Token）
     *
     * @return 当前登录用户信息
     * @throws BusinessException 未登录时抛出异常
     */
    public static User getLoginUser() {
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
     * @param request HttpServletRequest（不再使用，仅为兼容）
     * @return 当前登录用户信息
     */
    public static User getLoginUser(HttpServletRequest request) {
        return getLoginUser();
    }

    /**
     * 判断当前用户是否为管理员
     *
     * @return 是否为管理员
     */
    public static boolean isAdmin() {
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
    public static boolean isAdmin(HttpServletRequest request) {
        return isAdmin();
    }

    /**
     * 判断指定用户是否为管理员
     *
     * @param user 用户对象
     * @return 是否为管理员
     */
    public static boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }
}

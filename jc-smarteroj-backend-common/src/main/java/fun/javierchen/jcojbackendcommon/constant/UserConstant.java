package fun.javierchen.jcojbackendcommon.constant;

/**
 * 用户常量
 *
 * @author JavierChen
 */
public interface UserConstant {

    /**
     * 用户登录态键（兼容旧代码）
     */
    String USER_LOGIN_STATE = "user_login";

    /**
     * Sa-Token Session 中存储用户信息的 Key
     */
    String SA_SESSION_USER_KEY = "user_info";

    // region 权限

    /**
     * 默认角色
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员角色
     */
    String ADMIN_ROLE = "admin";

    /**
     * 被封号
     */
    String BAN_ROLE = "ban";

    // endregion
}

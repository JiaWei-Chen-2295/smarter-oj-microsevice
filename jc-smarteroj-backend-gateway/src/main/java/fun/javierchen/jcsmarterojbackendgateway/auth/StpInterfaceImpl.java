package fun.javierchen.jcsmarterojbackendgateway.auth;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sa-Token 权限认证接口实现
 * 
 * <p>
 * 实现 Sa-Token 的 StpInterface 接口，用于获取用户的权限列表和角色列表。
 * 此实现从 Sa-Token Session（存储在 Redis 中）获取用户信息。
 * </p>
 * 
 * <p>
 * 当前系统支持三种用户角色：
 * <ul>
 * <li>user - 普通用户</li>
 * <li>admin - 管理员</li>
 * <li>ban - 被封禁用户</li>
 * </ul>
 * </p>
 *
 * @author JavierChen
 * @see cn.dev33.satoken.stp.StpInterface
 */
@Slf4j
@Component
public class StpInterfaceImpl implements StpInterface {

    /**
     * Sa-Token Session 中存储用户信息的 Key
     * 与 UserConstant.SA_SESSION_USER_KEY 保持一致
     */
    private static final String SA_SESSION_USER_KEY = "user_info";

    /**
     * 用户角色字段名
     */
    private static final String USER_ROLE_FIELD = "userRole";

    /**
     * 默认角色
     */
    private static final String DEFAULT_ROLE = "user";

    /**
     * 管理员角色
     */
    private static final String ADMIN_ROLE = "admin";

    /**
     * 被封禁角色
     */
    private static final String BAN_ROLE = "ban";

    /**
     * 获取用户权限列表
     * 
     * <p>
     * 当前系统基于角色进行权限控制，权限列表根据用户角色动态生成。
     * 可以根据业务需求扩展更细粒度的权限控制。
     * </p>
     * 
     * <p>
     * 权限规则：
     * <ul>
     * <li>ban 用户：无任何权限</li>
     * <li>user 用户：拥有基本操作权限（如查看、提交题目等）</li>
     * <li>admin 用户：拥有所有权限（包括用户管理、题目管理等）</li>
     * </ul>
     * </p>
     *
     * @param loginId   登录用户ID
     * @param loginType 登录类型
     * @return 用户权限列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> permissions = new ArrayList<>();

        // 获取用户角色
        String userRole = getUserRole(loginId);
        if (userRole == null) {
            log.warn("[权限获取] 无法获取用户角色, loginId: {}", loginId);
            return permissions;
        }

        // 被封禁用户无任何权限
        if (BAN_ROLE.equals(userRole)) {
            log.info("[权限获取] 用户已被封禁, loginId: {}", loginId);
            return permissions;
        }

        // 普通用户权限
        if (DEFAULT_ROLE.equals(userRole) || ADMIN_ROLE.equals(userRole)) {
            // 基础权限 - 所有登录用户都有
            permissions.add("question:view"); // 查看题目
            permissions.add("question:submit"); // 提交题目
            permissions.add("submission:view"); // 查看提交记录
            permissions.add("user:profile"); // 查看个人信息
            permissions.add("user:update"); // 更新个人信息
            permissions.add("post:view"); // 查看帖子
            permissions.add("post:create"); // 创建帖子
            permissions.add("post:comment"); // 评论帖子
        }

        // 管理员权限
        if (ADMIN_ROLE.equals(userRole)) {
            // 用户管理权限
            permissions.add("user:manage"); // 用户管理
            permissions.add("user:ban"); // 封禁用户
            permissions.add("user:unban"); // 解封用户

            // 题目管理权限
            permissions.add("question:create"); // 创建题目
            permissions.add("question:edit"); // 编辑题目
            permissions.add("question:delete"); // 删除题目
            permissions.add("question:import"); // 导入题目

            // 帖子管理权限
            permissions.add("post:edit"); // 编辑帖子
            permissions.add("post:delete"); // 删除帖子

            // 系统管理权限
            permissions.add("system:admin"); // 系统管理
        }

        log.debug("[权限获取] loginId: {}, role: {}, permissions: {}", loginId, userRole, permissions);
        return permissions;
    }

    /**
     * 获取用户角色列表
     * 
     * <p>
     * 从 Sa-Token Session 中获取用户信息并提取角色。
     * 当前系统每个用户只有一个角色，返回单元素列表。
     * </p>
     *
     * @param loginId   登录用户ID
     * @param loginType 登录类型
     * @return 用户角色列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 获取用户角色
        String userRole = getUserRole(loginId);
        if (userRole == null) {
            log.warn("[角色获取] 无法获取用户角色, loginId: {}", loginId);
            return Collections.emptyList();
        }

        log.debug("[角色获取] loginId: {}, role: {}", loginId, userRole);
        return Collections.singletonList(userRole);
    }

    /**
     * 从 Sa-Token Session 中获取用户角色
     * 
     * <p>
     * 用户信息在登录时已存储到 Sa-Token Session（Redis）中，
     * 此方法直接从 Session 读取用户对象并提取角色字段。
     * </p>
     *
     * @param loginId 登录用户ID
     * @return 用户角色，获取失败返回 null
     */
    private String getUserRole(Object loginId) {
        try {
            // 根据 loginId 获取对应的 Session
            SaSession session = StpUtil.getSessionByLoginId(loginId, false);
            if (session == null) {
                log.warn("[获取角色] Session 不存在, loginId: {}", loginId);
                return null;
            }

            // 从 Session 中获取用户信息
            Object userObj = session.get(SA_SESSION_USER_KEY);
            if (userObj == null) {
                log.warn("[获取角色] Session 中无用户信息, loginId: {}", loginId);
                return null;
            }

            // 使用 Hutool JSON 解析用户对象获取角色
            // 由于 Gateway 模块不依赖 Model 模块，这里使用 JSON 方式读取角色字段
            String userJson = JSONUtil.toJsonStr(userObj);
            JSONObject userJsonObj = JSONUtil.parseObj(userJson);
            String userRole = userJsonObj.getStr(USER_ROLE_FIELD);

            if (userRole == null || userRole.isEmpty()) {
                log.warn("[获取角色] 用户角色为空, loginId: {}, 使用默认角色", loginId);
                return DEFAULT_ROLE;
            }

            return userRole;
        } catch (Exception e) {
            log.error("[获取角色] 获取用户角色异常, loginId: {}", loginId, e);
            return null;
        }
    }
}

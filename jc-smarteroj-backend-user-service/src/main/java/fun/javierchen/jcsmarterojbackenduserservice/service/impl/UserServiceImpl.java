package fun.javierchen.jcsmarterojbackenduserservice.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.javierchen.jcojbackendcommon.common.ErrorCode;
import fun.javierchen.jcojbackendcommon.constant.CommonConstant;
import fun.javierchen.jcojbackendcommon.exception.BusinessException;
import fun.javierchen.jcojbackendcommon.utils.SqlUtils;
import fun.javierchen.jcojbackendmodel.dto.user.UserQueryRequest;
import fun.javierchen.jcojbackendmodel.entity.User;
import fun.javierchen.jcojbackendmodel.enums.UserRoleEnum;
import fun.javierchen.jcojbackendmodel.vo.LoginUserVO;
import fun.javierchen.jcojbackendmodel.vo.UserVO;
import fun.javierchen.jcsmarterojbackenduserservice.mapper.UserMapper;
import fun.javierchen.jcsmarterojbackenduserservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import fun.javierchen.jcojbackendmodel.dto.user.SmsCaptchaRequest;
import fun.javierchen.jcojbackendmodel.dto.user.UserPhoneLoginRequest;
import fun.javierchen.jcsmarterojbackenduserservice.utils.SmsUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static fun.javierchen.jcojbackendcommon.constant.UserConstant.SA_SESSION_USER_KEY;

/**
 * 用户服务实现
 *
 * @author JavierChen
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 盐值，混淆密码
     */
    public static final String SALT = "yupi";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        synchronized (userAccount.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            // 2. 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            // 3. 插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        }
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        long stepTime = startTime;

        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        log.info("[登录性能] 参数校验耗时: {}ms", System.currentTimeMillis() - stepTime);
        stepTime = System.currentTimeMillis();

        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        log.info("[登录性能] 密码加密耗时: {}ms", System.currentTimeMillis() - stepTime);
        stepTime = System.currentTimeMillis();

        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        log.info("[登录性能] 数据库查询耗时: {}ms", System.currentTimeMillis() - stepTime);
        stepTime = System.currentTimeMillis();

        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 3. 使用 Sa-Token 记录用户的登录态
        StpUtil.login(user.getId());
        log.info("[登录性能] Sa-Token登录耗时: {}ms", System.currentTimeMillis() - stepTime);
        stepTime = System.currentTimeMillis();

        // 将用户信息存入 Sa-Token Session
        SaSession session = StpUtil.getSession();
        log.info("[登录性能] 获取Session耗时: {}ms", System.currentTimeMillis() - stepTime);
        stepTime = System.currentTimeMillis();

        session.set(SA_SESSION_USER_KEY, user);
        log.info("[登录性能] 设置Session耗时: {}ms", System.currentTimeMillis() - stepTime);

        LoginUserVO result = this.getLoginUserVO(user);
        log.info("[登录性能] 总耗时: {}ms", System.currentTimeMillis() - startTime);
        return result;
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        long stepTime = startTime;

        // 1. 使用 Sa-Token 判断是否已登录
        log.info("[getLoginUser] 步骤1: 开始检查登录状态");
        if (!StpUtil.isLogin()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        log.info("[getLoginUser] 步骤1: StpUtil.isLogin() 耗时: {}ms", System.currentTimeMillis() - stepTime);
        stepTime = System.currentTimeMillis();

        // 2. 从 Sa-Token Session 获取用户信息
        log.info("[getLoginUser] 步骤2: 开始获取 Session");
        SaSession session = StpUtil.getSession();
        log.info("[getLoginUser] 步骤2: StpUtil.getSession() 耗时: {}ms", System.currentTimeMillis() - stepTime);
        stepTime = System.currentTimeMillis();

        log.info("[getLoginUser] 步骤3: 开始从 Session 获取用户数据");
        User currentUser = session.getModel(SA_SESSION_USER_KEY, User.class);
        log.info("[getLoginUser] 步骤3: session.getModel() 耗时: {}ms", System.currentTimeMillis() - stepTime);

        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        // 【性能优化】直接使用 Session 缓存的用户信息，不再查询数据库
        // 用户信息变更时（如修改资料、更新权限等），需要同步更新 Session 中的用户信息
        // 如果需要实时获取最新数据，可以调用 getLoginUserWithRefresh 方法

        long duration = System.currentTimeMillis() - startTime;
        log.info("[getLoginUser] 总耗时: {}ms", duration);
        return currentUser;
    }

    /**
     * 获取当前登录用户（强制刷新，从数据库获取最新信息）
     * 
     * <p>
     * 当需要获取用户最新信息时使用此方法，例如：
     * - 用户修改个人资料后
     * - 管理员修改用户权限后
     * - 其他需要确保用户信息最新的场景
     * </p>
     *
     * @param request
     * @return 最新的用户信息
     */
    public User getLoginUserWithRefresh(HttpServletRequest request) {
        long startTime = System.currentTimeMillis();

        // 1. 使用 Sa-Token 判断是否已登录
        if (!StpUtil.isLogin()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        // 2. 从 Sa-Token Session 获取用户 ID
        SaSession session = StpUtil.getSession();
        User cachedUser = session.getModel(SA_SESSION_USER_KEY, User.class);

        if (cachedUser == null || cachedUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        // 3. 从数据库查询最新信息
        User currentUser = this.getById(cachedUser.getId());

        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        // 4. 更新 Session 中的用户信息
        session.set(SA_SESSION_USER_KEY, currentUser);

        log.info("[获取登录用户性能] getLoginUserWithRefresh 总耗时: {}ms", System.currentTimeMillis() - startTime);
        return currentUser;
    }

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUserPermitNull(HttpServletRequest request) {
        // 1. 使用 Sa-Token 判断是否已登录
        if (!StpUtil.isLogin()) {
            return null;
        }
        // 2. 从 Sa-Token Session 获取用户信息
        SaSession session = StpUtil.getSession();
        User currentUser = session.getModel(SA_SESSION_USER_KEY, User.class);
        if (currentUser == null || currentUser.getId() == null) {
            return null;
        }
        // 3. 从数据库查询最新信息
        return this.getById(currentUser.getId());
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 使用 Sa-Token 判断是否为管理员
        if (!StpUtil.isLogin()) {
            return false;
        }
        SaSession session = StpUtil.getSession();
        User user = session.getModel(SA_SESSION_USER_KEY, User.class);
        return isAdmin(user);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        // 使用 Sa-Token 判断是否已登录
        if (!StpUtil.isLogin()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 使用 Sa-Token 登出
        StpUtil.logout();
        return true;
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String unionId = userQueryRequest.getUnionId();
        String mpOpenId = userQueryRequest.getMpOpenId();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(unionId), "unionId", unionId);
        queryWrapper.eq(StringUtils.isNotBlank(mpOpenId), "mpOpenId", mpOpenId);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StringUtils.isNotBlank(userProfile), "userProfile", userProfile);
        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Resource
    private SmsUtils smsUtils;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("${captcha.id}")
    private String captchaId;

    @Value("${captcha.key}")
    private String captchaKey;

    @Value("${captcha.domain}")
    private String captchaDomain;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public boolean sendSmsCaptcha(SmsCaptchaRequest request) {
        // 1. 校验参数
        String phone = request.getPhone();
        String captchaVerification = request.getCaptchaVerification();
        if (StringUtils.isAnyBlank(phone, captchaVerification)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }

        // 2. 行为验证码二次校验 (Alibaba Captcha)
        try {
            JSONObject json = JSONUtil.parseObj(captchaVerification);
            String lotNumber = json.getStr("lot_number");
            String captchaOutput = json.getStr("captcha_output");
            String passToken = json.getStr("pass_token");
            String genTime = json.getStr("gen_time");

            // 生成签名
            String signToken = new HMac(HmacAlgorithm.HmacSHA256, captchaKey.getBytes())
                    .digestHex(lotNumber);

            // 上传校验参数
            MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
            queryParams.add("lot_number", lotNumber);
            queryParams.add("captcha_output", captchaOutput);
            queryParams.add("pass_token", passToken);
            queryParams.add("gen_time", genTime);
            queryParams.add("sign_token", signToken);

            String url = String.format(captchaDomain + "/validate" + "?captcha_id=%s", captchaId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(queryParams, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            JSONObject validateResult = JSONUtil.parseObj(response.getBody());

            if (!"success".equals(validateResult.getStr("result"))) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "行为验证码校验失败: " + validateResult.getStr("reason"));
            }
        } catch (Exception e) {
            log.error("Captcha validation error, phone: {}", phone, e);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "行为验证码校验失败");
        }

        // 3. 限流校验 (Redis)
        // 单手机号限制：60s 内只能发送一次
        String limitKey = "sms:limit:phone:" + phone;
        if (stringRedisTemplate.hasKey(limitKey)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "请求过于频繁，请稍后再试");
        }

        // 4. 生成验证码
        String code = cn.hutool.core.util.RandomUtil.randomNumbers(6);

        // 5. 发送短信
        boolean sendResult = smsUtils.sendSms(phone, code);
        if (!sendResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "短信发送失败");
        }

        // 6. 保存验证码到 Redis (5分钟有效)
        String captchaKey = "sms:captcha:" + phone;
        stringRedisTemplate.opsForValue().set(captchaKey, code, 5, java.util.concurrent.TimeUnit.MINUTES);

        // 7. 设置限流 Key (60s)
        stringRedisTemplate.opsForValue().set(limitKey, "1", 60, java.util.concurrent.TimeUnit.SECONDS);

        return true;
    }

    @Override
    public LoginUserVO userLoginByPhone(UserPhoneLoginRequest request, HttpServletRequest httpRequest) {
        String phone = request.getPhone();
        String captcha = request.getCaptcha();
        if (StringUtils.isAnyBlank(phone, captcha)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }

        // 1. 校验验证码
        String captchaKey = "sms:captcha:" + phone;
        String validCode = stringRedisTemplate.opsForValue().get(captchaKey);
        if (validCode == null || !validCode.equals(captcha)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误或已失效");
        }

        // 验证通过，删除验证码
        stringRedisTemplate.delete(captchaKey);

        // 2. 查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        User user = this.baseMapper.selectOne(queryWrapper);

        // 3. 如果用户不存在，自动注册
        if (user == null) {
            user = new User();
            user.setUserAccount("user_" + cn.hutool.core.util.RandomUtil.randomString(8));
            user.setPhone(phone);
            user.setUserRole(UserRoleEnum.USER.getValue());
            // 默认密码或无密码，这里设一个随机密码防止被空密码登录（虽然只能手机号登）
            user.setUserPassword(DigestUtils.md5DigestAsHex((SALT + "12345678").getBytes()));
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败");
            }
        }

        // 4. 登录
        StpUtil.login(user.getId());
        SaSession session = StpUtil.getSession();
        session.set(SA_SESSION_USER_KEY, user);
        return this.getLoginUserVO(user);
    }
}

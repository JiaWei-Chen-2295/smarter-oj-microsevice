package fun.javierchen.jcsmarterojbackenduserservice.controller.inner;

import fun.javierchen.jcojbackendmodel.entity.User;
import fun.javierchen.jcojbackendserverclient.UserFeignClient;
import fun.javierchen.jcsmarterojbackenduserservice.service.UserService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

// todo: 这里没有添加注解 看看会不会报错

/**
 * 用于内部调用
 */
@RestController("/inner")
public class InnerUserController implements UserFeignClient {

    @Resource
    private UserService userService;

    @Override
    public User getById(Long userId) {
        return userService.getById(userId);
    }

    @Override
    public List<User> listByIds(Collection<Long> idList) {
        return userService.listByIds(idList);
    }
}

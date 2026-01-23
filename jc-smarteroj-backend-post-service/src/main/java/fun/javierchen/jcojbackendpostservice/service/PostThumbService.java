package fun.javierchen.jcojbackendpostservice.service;

import fun.javierchen.jcojbackendmodel.entity.PostThumb;
import com.baomidou.mybatisplus.extension.service.IService;
import fun.javierchen.jcojbackendmodel.entity.User;

/**
 * 帖子点赞服务
 *
 * @author JavierChen
 */
public interface PostThumbService extends IService<PostThumb> {

    /**
     * 点赞
     *
     * @param postId
     * @param loginUser
     * @return
     */
    int doPostThumb(long postId, User loginUser);

    /**
     * 帖子点赞（内部服务）
     *
     * @param userId
     * @param postId
     * @return
     */
    int doPostThumbInner(long userId, long postId);
}

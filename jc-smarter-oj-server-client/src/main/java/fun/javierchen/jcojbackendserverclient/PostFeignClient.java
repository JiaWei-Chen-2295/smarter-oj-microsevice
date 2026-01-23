package fun.javierchen.jcojbackendserverclient;

import fun.javierchen.jcojbackendmodel.entity.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

/**
 * 帖子服务
 *
 * @author JavierChen
 */
@FeignClient(name = "jc-smarteroj-backend-post-service", path = "/api/post/inner")
public interface PostFeignClient {

    /**
     * 根据 id 获取帖子
     *
     * @param postId
     * @return
     */
    @GetMapping("/get/id")
    Post getById(@RequestParam("postId") Long postId);

    /**
     * 通过id集合查询帖子集合
     *
     * @param idList
     * @return
     */
    @GetMapping("/list/id")
    List<Post> listByIds(@RequestParam("idList") Collection<Long> idList);

}

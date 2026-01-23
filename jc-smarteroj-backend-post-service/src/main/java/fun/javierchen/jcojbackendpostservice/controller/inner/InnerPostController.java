package fun.javierchen.jcojbackendpostservice.controller.inner;

import fun.javierchen.jcojbackendmodel.entity.Post;
import fun.javierchen.jcojbackendpostservice.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 帖子内部接口（供其他微服务调用）
 *
 * @author JavierChen
 */
@RestController
@RequestMapping("/inner")
@Tag(name = "帖子内部接口")
public class InnerPostController {

    @Resource
    private PostService postService;

    /**
     * 根据 id 获取帖子
     *
     * @param postId
     * @return
     */
    @GetMapping("/get/id")
    @Operation(summary = "根据id获取帖子")
    public Post getById(@RequestParam("postId") Long postId) {
        return postService.getById(postId);
    }

    /**
     * 通过id集合查询帖子集合
     *
     * @param idList
     * @return
     */
    @GetMapping("/list/id")
    @Operation(summary = "通过id集合查询帖子集合")
    public List<Post> listByIds(@RequestParam("idList") Collection<Long> idList) {
        return postService.listByIds(idList);
    }

}

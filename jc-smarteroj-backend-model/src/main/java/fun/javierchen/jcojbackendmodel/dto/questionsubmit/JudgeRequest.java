package fun.javierchen.jcojbackendmodel.dto.questionsubmit;

import fun.javierchen.jcojbackendmodel.entity.User;
import lombok.Data;

import java.io.Serializable;

/**
 * 判题请求包装类
 * 用于 Feign 远程调用时传递多个参数
 */
@Data
public class JudgeRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 题目提交ID
     */
    private Long questionSubmitId;
    
    /**
     * 登录用户信息
     */
    private User loginUser;
}

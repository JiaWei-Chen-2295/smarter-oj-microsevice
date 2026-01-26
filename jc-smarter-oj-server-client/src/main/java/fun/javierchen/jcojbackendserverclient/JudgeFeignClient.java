package fun.javierchen.jcojbackendserverclient;


import fun.javierchen.jcojbackendmodel.dto.questionsubmit.JudgeRequest;
import fun.javierchen.jcojbackendmodel.entity.User;
import fun.javierchen.jcojbackendmodel.vo.QuestionSubmitVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "jc-smarteroj-backend-judge-service", path = "/api/judge/inner")
public interface JudgeFeignClient {
    /**
     * 判题服务
     * @param judgeRequest 判题请求（包含题目提交ID和登录用户信息）
     * @return 判题结果VO
     */
    @PostMapping("/do")
    QuestionSubmitVO doJudge(@RequestBody JudgeRequest judgeRequest);
}

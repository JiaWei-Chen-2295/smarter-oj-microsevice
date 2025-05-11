package fun.javierchen.jcsmarterojbackendjudgeservice.controller.inner;

import fun.javierchen.jcojbackendmodel.entity.User;
import fun.javierchen.jcojbackendmodel.vo.QuestionSubmitVO;
import fun.javierchen.jcojbackendserverclient.JudgeFeignClient;
import fun.javierchen.jcsmarterojbackendjudgeservice.service.JudgeService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("/inner")
public class InnerJudgeController implements JudgeFeignClient {

    @Resource
    private JudgeService judgeService;

    @Override
    public QuestionSubmitVO doJudge(Long questionSubmitId, User loginUser) {
        return judgeService.doJudge(questionSubmitId, loginUser);
    }
}

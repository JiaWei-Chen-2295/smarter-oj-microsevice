package fun.javierchen.jcsmarterojbackendjudgeservice.controller.inner;

import fun.javierchen.jcojbackendmodel.dto.questionsubmit.JudgeRequest;
import fun.javierchen.jcojbackendmodel.entity.User;
import fun.javierchen.jcojbackendmodel.vo.QuestionSubmitVO;
import fun.javierchen.jcojbackendserverclient.JudgeFeignClient;
import fun.javierchen.jcsmarterojbackendjudgeservice.service.JudgeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class InnerJudgeController implements JudgeFeignClient {

    @Resource
    private JudgeService judgeService;

    @Override
    public QuestionSubmitVO doJudge(JudgeRequest judgeRequest) {
        return judgeService.doJudge(judgeRequest.getQuestionSubmitId(), judgeRequest.getLoginUser());
    }


}

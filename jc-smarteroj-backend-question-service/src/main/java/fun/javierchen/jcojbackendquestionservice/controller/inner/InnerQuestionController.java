package fun.javierchen.jcojbackendquestionservice.controller.inner;

import fun.javierchen.jcojbackendmodel.entity.Question;
import fun.javierchen.jcojbackendmodel.entity.QuestionSubmit;
import fun.javierchen.jcojbackendmodel.entity.User;
import fun.javierchen.jcojbackendmodel.vo.QuestionSubmitVO;
import fun.javierchen.jcojbackendquestionservice.service.QuestionService;
import fun.javierchen.jcojbackendquestionservice.service.QuestionSubmitService;
import fun.javierchen.jcojbackendserverclient.QuestionFeignClient;
import fun.javierchen.jcojbackendserverclient.UserFeignClient;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController("/inner")
public class InnerQuestionController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private QuestionSubmitService questionSubmitService;;

    @Override
    public Question getQuestionById(Long questionId) {
        return questionService.getById(questionId);
    }

    @Override
    public QuestionSubmit getQuestionSubmitById(Long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);
    }

    @Override
    public boolean updateQuestionSubmitById(QuestionSubmit questionSubmitUpdate) {
        return questionSubmitService.updateById(questionSubmitUpdate);
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmitById, User loginUser) {
        return questionSubmitService.getQuestionSubmitVO(questionSubmitById, loginUser);
    }
}

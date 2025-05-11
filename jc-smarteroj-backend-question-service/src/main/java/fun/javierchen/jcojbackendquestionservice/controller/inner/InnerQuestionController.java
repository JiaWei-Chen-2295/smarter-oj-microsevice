package fun.javierchen.jcojbackendquestionservice.controller.inner;

import fun.javierchen.jcojbackendmodel.entity.Question;
import fun.javierchen.jcojbackendmodel.entity.QuestionSubmit;
import fun.javierchen.jcojbackendquestionservice.service.QuestionService;
import fun.javierchen.jcojbackendquestionservice.service.QuestionSubmitService;
import fun.javierchen.jcojbackendserverclient.QuestionFeignClient;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("/inner")
public class InnerQuestionController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;

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
}

package fun.javierchen.jcojbackendserverclient;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import fun.javierchen.jcojbackendmodel.dto.question.QuestionQueryRequest;
import fun.javierchen.jcojbackendmodel.entity.Question;
import fun.javierchen.jcojbackendmodel.entity.QuestionSubmit;
import fun.javierchen.jcojbackendmodel.entity.User;
import fun.javierchen.jcojbackendmodel.vo.QuestionSubmitVO;
import fun.javierchen.jcojbackendmodel.vo.QuestionVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
* @author 16010
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2025-03-25 16:59:28
*/
@FeignClient(name = "jc-smarteroj-backend-question-service", path="/api/question/inner")
public interface QuestionFeignClient {

    /*
    需要向外界提供的服务
      ○ questionService.getById(questionId)
      ○ questionSubmitService.getById(questionSubmitId)
      ○ questionSubmitService.updateById(questionSubmitUpdate)
      ○ questionSubmitService.getQuestionSubmitVO()
     */

    @GetMapping("/get/id")
    Question getQuestionById(@RequestParam("questionId") Long questionId);

    @GetMapping("/submit/update/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") Long questionSubmitId);

    @PostMapping("/submit/update")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmitUpdate);

    @PostMapping("/submit/get/vo")
    QuestionSubmitVO getQuestionSubmitVO(@RequestBody QuestionSubmit questionSubmitById,@RequestParam("loginUser") User loginUser);
}

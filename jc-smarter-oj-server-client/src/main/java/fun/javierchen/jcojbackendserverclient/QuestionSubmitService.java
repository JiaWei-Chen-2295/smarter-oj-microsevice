package fun.javierchen.jcojbackendserverclient;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import fun.javierchen.jcojbackendmodel.dto.questionsubmit.QuestionSubmitAddRequest;
import fun.javierchen.jcojbackendmodel.dto.questionsubmit.QuestionSubmitQueryRequest;
import fun.javierchen.jcojbackendmodel.entity.QuestionSubmit;
import fun.javierchen.jcojbackendmodel.entity.User;
import fun.javierchen.jcojbackendmodel.vo.QuestionSubmitVO;

/**
* @author 16010
* @description 针对表【question_submit(用户提交表)】的数据库操作Service
* @createDate 2025-03-25 17:00:22
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest 提交题目请求封装类
     * @param loginUser 当前登录用户
     * @return 创建的QuestionSubmit的id
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);
    /**
     * 获取问题提交封装
     *
     * @param questionSubmit
     * @param request
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取问题提交封装
     *
     * @param questionSubmitPage
     * @param request
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);
}

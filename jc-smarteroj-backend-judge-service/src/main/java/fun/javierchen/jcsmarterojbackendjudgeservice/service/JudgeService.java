package fun.javierchen.jcsmarterojbackendjudgeservice.service;


import fun.javierchen.jcojbackendmodel.entity.User;
import fun.javierchen.jcojbackendmodel.vo.QuestionSubmitVO;

public interface JudgeService {
    /**
     * 判题服务
     * @param questionSubmitId
     * @return
     */
    QuestionSubmitVO doJudge(Long questionSubmitId, User loginUser);
}

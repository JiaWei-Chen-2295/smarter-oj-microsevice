package fun.javierchen.jcojbackendquestionservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import fun.javierchen.jcojbackendmodel.dto.questionset.QuestionSetQueryRequest;
import fun.javierchen.jcojbackendmodel.entity.QuestionSet;
import fun.javierchen.jcojbackendmodel.entity.User;
import fun.javierchen.jcojbackendmodel.vo.QuestionSetVO;

import javax.servlet.http.HttpServletRequest;

public interface QuestionSetService extends IService<QuestionSet> {

    void validQuestionSet(QuestionSet questionSet, boolean add);

    QueryWrapper<QuestionSet> getQueryWrapper(QuestionSetQueryRequest questionSetQueryRequest);

    QuestionSetVO getQuestionSetVO(QuestionSet questionSet, HttpServletRequest request);

    Page<QuestionSetVO> getQuestionSetVOPage(Page<QuestionSet> questionSetPage, HttpServletRequest request);

    long createQuestionSet(QuestionSet questionSet, User loginUser);

    boolean addQuestionToSet(Long questionSetId, Long questionId, Integer sortOrder, User loginUser);

    boolean removeQuestionFromSet(Long questionSetId, Long questionId, User loginUser);
}


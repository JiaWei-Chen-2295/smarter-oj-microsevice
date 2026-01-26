package fun.javierchen.jcojbackendquestionservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.javierchen.jcojbackendmodel.entity.Question;
import fun.javierchen.jcojbackendmodel.entity.QuestionSet;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface QuestionSetMapper extends BaseMapper<QuestionSet> {

    List<Question> getQuestionsByQuestionSetId(@Param("questionSetId") Long questionSetId);

    List<Question> listQuestionMapByQuestionSetIds(@Param("collection") Collection<Long> questionSetIdCollection);
}


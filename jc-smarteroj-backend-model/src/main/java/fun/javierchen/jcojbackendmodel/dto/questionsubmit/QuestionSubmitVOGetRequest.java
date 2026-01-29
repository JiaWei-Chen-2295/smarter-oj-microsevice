package fun.javierchen.jcojbackendmodel.dto.questionsubmit;

import fun.javierchen.jcojbackendmodel.entity.QuestionSubmit;
import fun.javierchen.jcojbackendmodel.entity.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionSubmitVOGetRequest implements Serializable {

    private QuestionSubmit questionSubmit;

    private User loginUser;

    private static final long serialVersionUID = 1L;
}

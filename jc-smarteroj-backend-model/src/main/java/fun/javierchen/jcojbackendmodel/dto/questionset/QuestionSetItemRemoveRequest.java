package fun.javierchen.jcojbackendmodel.dto.questionset;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionSetItemRemoveRequest implements Serializable {

    private Long questionSetId;

    private Long questionId;

    private static final long serialVersionUID = 1L;
}


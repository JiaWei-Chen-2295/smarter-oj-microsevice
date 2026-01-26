package fun.javierchen.jcojbackendmodel.dto.questionset;

import fun.javierchen.jcojbackendcommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionSetQueryRequest extends PageRequest implements Serializable {

    private Long id;

    private String title;

    private String description;

    private Long userId;

    private static final long serialVersionUID = 1L;
}


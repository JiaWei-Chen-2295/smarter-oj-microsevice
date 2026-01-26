package fun.javierchen.jcojbackendmodel.dto.questionset;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class QuestionSetEditRequest implements Serializable {

    private Long id;

    private String title;

    private String description;

    private List<String> tags;

    private static final long serialVersionUID = 1L;
}


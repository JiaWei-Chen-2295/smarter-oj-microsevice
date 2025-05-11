package fun.javierchen.jcojbackendmodel.codesandbox;


import fun.javierchen.jcojbackendmodel.dto.question.JudgeConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeSandBoxRequest {
    private String language;
    private String code;
    private List<String> inputCase;
    private JudgeConfig judgeConfig;
}

package fun.javierchen.jcojbackendmodel.codesandbox;

import fun.javierchen.jcojbackendmodel.dto.questionsubmit.JudgeInfo;
import lombok.Data;

import java.util.List;

@Data
public class CodeSandBoxResponse {

    private String message;
    private JudgeInfo judgeInfo;
    private List<String> outputList;
    // 来自 QuestionSubmitStatus
    private int status;
}

package fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox.model.judge0;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Judge0BatchResponse {
    private List<Judge0SubmissionResponse> submissions;
}

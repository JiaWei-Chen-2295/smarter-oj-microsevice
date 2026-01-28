package fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox.model.judge0;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Judge0SubmissionResponse {
    private String stdout;
    private String stderr;
    private String time;
    private Double memory;
    private String token;
    @JsonProperty("compile_output")
    private String compileOutput;
    private Status status;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Status {
        @JsonProperty("id")
        private Integer id;
        @JsonProperty("description")
        private String description;
    }
}

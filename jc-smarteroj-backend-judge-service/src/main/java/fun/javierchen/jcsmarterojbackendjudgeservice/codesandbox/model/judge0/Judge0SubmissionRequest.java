package fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox.model.judge0;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Judge0SubmissionRequest {
    @JsonProperty("source_code")
    private String sourceCode;

    @JsonProperty("language_id")
    private Integer languageId;

    @JsonProperty("stdin")
    private String stdin;

    @JsonProperty("expected_output")
    private String expectedOutput;

    @JsonProperty("cpu_time_limit")
    private Double cpuTimeLimit;

    @JsonProperty("memory_limit")
    private Double memoryLimit;

    @JsonProperty("stack_limit")
    private Double stackLimit;

    @JsonProperty("base64_encoded")
    private Boolean base64Encoded;
}

package fun.javierchen.jcojbackendmodel.dto.questionsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 判题信息
 *
 * @author JavierChen
 * @date 2025/03/25
 */
@Data
public class JudgeInfo implements Serializable {
    /**
     * 判题结果信息
     */
    private String message;
    /**
     * 判题时间
     */
    private Long time;
    /**
     * 判题的内存占用
     */
    private Long memory;

    private static final long serialVersionUID = 1L;
}

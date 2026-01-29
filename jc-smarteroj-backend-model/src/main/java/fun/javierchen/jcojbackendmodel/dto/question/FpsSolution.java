package fun.javierchen.jcojbackendmodel.dto.question;

import lombok.Data;

/**
 * FPS XML 中的参考答案
 *
 * @author JavierChen
 */
@Data
public class FpsSolution {
    /**
     * 编程语言 (C, C++, Java, Python 等)
     */
    private String language;

    /**
     * 代码内容
     */
    private String code;
}

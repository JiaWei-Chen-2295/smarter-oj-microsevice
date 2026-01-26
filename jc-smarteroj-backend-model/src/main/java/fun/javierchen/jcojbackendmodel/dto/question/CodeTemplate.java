package fun.javierchen.jcojbackendmodel.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 代码模板
 *
 * @author JavierChen
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeTemplate implements Serializable {

    /**
     * Java 代码模板
     */
    private String java;

    /**
     * C++ 代码模板
     */
    private String cpp;

    /**
     * Python 代码模板
     */
    private String python;

    private static final long serialVersionUID = 1L;

    /**
     * 获取默认代码模板
     */
    public static CodeTemplate getDefaultTemplate() {
        CodeTemplate template = new CodeTemplate();
        template.setJava("import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        // TODO: 在此编写你的代码\n        \n    }\n}");
        template.setCpp("#include <iostream>\nusing namespace std;\n\nint main() {\n    // TODO: 在此编写你的代码\n    \n    return 0;\n}");
        template.setPython("# TODO: 在此编写你的代码\n");
        return template;
    }
}

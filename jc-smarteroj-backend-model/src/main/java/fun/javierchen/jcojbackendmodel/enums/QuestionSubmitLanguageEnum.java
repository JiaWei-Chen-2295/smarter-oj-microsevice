package fun.javierchen.jcojbackendmodel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 程序判题语言枚举
 */
@AllArgsConstructor
@Getter
public enum QuestionSubmitLanguageEnum {

    JAVA("java", "java"),
    CPLUSPLUS("cpp", "cpp"),
    PYTHON("python", "python")
    ;

    public static QuestionSubmitLanguageEnum getEnumByValue(String value) {
        for (QuestionSubmitLanguageEnum questionSubmitLanguageEnum : QuestionSubmitLanguageEnum.values()) {
            if (questionSubmitLanguageEnum.value.equals(value)) {
                return questionSubmitLanguageEnum;
            }
        }
        return null;
    }

    private final String text;
    private final String value;

}

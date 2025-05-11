package fun.javierchen.jcojbackendmodel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum QuestionSubmitStatusEnum {

    WAITING("待判题", 0),
    RUNNING("正在判题", 1),
    SUCCEED("判题成功", 2),
    FAILED("判题失败", 3);

    private final String text;
    private final int value;

    public static QuestionSubmitStatusEnum getEnumByValue(int value) {
        for (QuestionSubmitStatusEnum questionSubmitStatusEnum : QuestionSubmitStatusEnum.values()) {
            if (questionSubmitStatusEnum.value == value) {
                return questionSubmitStatusEnum;
            }
        }
        return null;
    }

}

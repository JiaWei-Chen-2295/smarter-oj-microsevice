package fun.javierchen.jcojbackendmodel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户提交表
 * @TableName question_submit
 */
@TableName(value ="question_submit")
@Data
public class QuestionSubmit {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 提交的用户id
     */
    private Long userId;

    /**
     * 提交的语言
     */
    private String language;

    /**
     * 用户提交的代码
     */
    private String code;

    /**
     * 判题状态0-待判题 1-判题中 2-成功 3-失败
     */
    private Integer status;

    /**
     * 判题信息
     */
    private String judgeInfo;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 代码的运行结果
     */
    private String outputResult;
}
package fun.javierchen.jcojbackendmodel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName(value = "question_set_item")
@Data
public class QuestionSetItem implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("questionSetId")
    private Long questionSetId;

    @TableField("questionId")
    private Long questionId;

    private Integer sortOrder;

    @TableField("createTime")
    private Date createTime;

    @TableField("updateTime")
    private Date updateTime;

    @TableLogic(value = "isDelete", delval = "1")
    @TableField("isDelete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}


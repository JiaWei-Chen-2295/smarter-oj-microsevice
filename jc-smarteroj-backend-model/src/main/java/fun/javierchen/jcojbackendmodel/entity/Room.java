package fun.javierchen.jcojbackendmodel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName(value = "room")
@Data
public class Room implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("room_name")
    private String name;

    private String description;

    @TableField("mate_num")
    private Integer mateNum;

    @TableField("user_id")
    private Long userId;

    private Integer status;

    private String password;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    @TableLogic
    @TableField("is_delete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}


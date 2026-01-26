package fun.javierchen.jcojbackendmodel.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RoomMemberVO implements Serializable {

    private Long userId;

    private UserVO userVO;

    private Date joinTime;

    private Boolean isLeader;

    private static final long serialVersionUID = 1L;
}


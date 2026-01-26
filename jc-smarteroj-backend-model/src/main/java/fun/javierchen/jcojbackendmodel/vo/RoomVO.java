package fun.javierchen.jcojbackendmodel.vo;

import fun.javierchen.jcojbackendmodel.entity.Room;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class RoomVO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private Integer mateNum;

    private Long userId;

    private Integer status;

    private Date createTime;

    private Integer currentNum;

    private UserVO userVO;

    private List<RoomMemberVO> members;

    public static RoomVO objToVo(Room room) {
        if (room == null) {
            return null;
        }
        RoomVO roomVO = new RoomVO();
        BeanUtils.copyProperties(room, roomVO);
        return roomVO;
    }

    public static Room voToObj(RoomVO roomVO) {
        if (roomVO == null) {
            return null;
        }
        Room room = new Room();
        BeanUtils.copyProperties(roomVO, room);
        return room;
    }

    private static final long serialVersionUID = 1L;
}


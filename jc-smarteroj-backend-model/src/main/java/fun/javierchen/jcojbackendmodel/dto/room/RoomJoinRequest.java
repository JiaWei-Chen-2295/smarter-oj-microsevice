package fun.javierchen.jcojbackendmodel.dto.room;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoomJoinRequest implements Serializable {

    private Long roomId;

    private String password;

    private static final long serialVersionUID = 1L;
}


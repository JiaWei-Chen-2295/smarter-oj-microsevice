package fun.javierchen.jcojbackendmodel.dto.room;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoomAuthRequest implements Serializable {

    private Long roomId;

    private Long userId;

    private Long timestamp;

    private String token;

    private static final long serialVersionUID = 1L;
}


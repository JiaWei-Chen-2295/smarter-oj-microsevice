package fun.javierchen.jcojbackendmodel.dto.room;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoomTransferRequest implements Serializable {

    private Long roomId;

    private Long newLeaderUserId;

    private static final long serialVersionUID = 1L;
}


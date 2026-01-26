package fun.javierchen.jcojbackendmodel.dto.room;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoomQuitRequest implements Serializable {

    private Long roomId;

    private static final long serialVersionUID = 1L;
}


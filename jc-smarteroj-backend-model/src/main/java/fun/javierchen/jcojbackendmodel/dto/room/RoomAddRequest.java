package fun.javierchen.jcojbackendmodel.dto.room;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoomAddRequest implements Serializable {

    private String name;

    private String description;

    private Integer mateNum;

    private Integer status;

    private String password;

    private static final long serialVersionUID = 1L;
}


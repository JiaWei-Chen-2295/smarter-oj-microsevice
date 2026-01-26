package fun.javierchen.jcojbackendmodel.dto.room;

import fun.javierchen.jcojbackendcommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoomQueryRequest extends PageRequest implements Serializable {

    private Long id;

    private String name;

    private String description;

    private Integer status;

    private Long userId;

    private static final long serialVersionUID = 1L;
}


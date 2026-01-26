package fun.javierchen.jcojbackendroomservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import fun.javierchen.jcojbackendmodel.dto.room.RoomQueryRequest;
import fun.javierchen.jcojbackendmodel.entity.Room;
import fun.javierchen.jcojbackendmodel.entity.User;
import fun.javierchen.jcojbackendmodel.vo.RoomVO;

import javax.servlet.http.HttpServletRequest;

public interface RoomService extends IService<Room> {

    void validRoom(Room room, boolean add);

    QueryWrapper<Room> getQueryWrapper(RoomQueryRequest roomQueryRequest);

    RoomVO getRoomVO(Room room, HttpServletRequest request);

    Page<RoomVO> getRoomVOPage(Page<Room> roomPage, HttpServletRequest request);

    long createRoom(Room room, User loginUser);

    boolean joinRoom(Long roomId, String password, User loginUser);

    boolean quitRoom(Long roomId, User loginUser);

    boolean transferLeader(Long roomId, Long newLeaderUserId, User loginUser);

    RoomVO getRoomDetail(Long roomId, HttpServletRequest request);

    boolean isUserInRoom(Long roomId, Long userId);
}


package fun.javierchen.jcojbackendroomservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.javierchen.jcojbackendcommon.common.BaseResponse;
import fun.javierchen.jcojbackendcommon.common.DeleteRequest;
import fun.javierchen.jcojbackendcommon.common.ErrorCode;
import fun.javierchen.jcojbackendcommon.common.ResultUtils;
import fun.javierchen.jcojbackendcommon.exception.BusinessException;
import fun.javierchen.jcojbackendcommon.exception.ThrowUtils;
import fun.javierchen.jcojbackendmodel.dto.room.*;
import fun.javierchen.jcojbackendmodel.entity.Room;
import fun.javierchen.jcojbackendmodel.entity.User;
import fun.javierchen.jcojbackendmodel.vo.RoomVO;
import fun.javierchen.jcojbackendroomservice.service.RoomService;
import fun.javierchen.jcojbackendroomservice.utils.AuthTokenUtils;
import fun.javierchen.jcojbackendserverclient.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
@Slf4j
public class RoomController {

    @Resource
    private RoomService roomService;

    @Resource
    private UserFeignClient userFeignClient;

    @PostMapping("/add")
    public BaseResponse<Long> addRoom(@RequestBody RoomAddRequest roomAddRequest, HttpServletRequest request) {
        if (roomAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Room room = new Room();
        BeanUtils.copyProperties(roomAddRequest, room);
        roomService.validRoom(room, true);

        User loginUser = userFeignClient.getLoginUser(request);
        long newRoomId = roomService.createRoom(room, loginUser);
        return ResultUtils.success(newRoomId);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteRoom(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userFeignClient.getLoginUser(request);
        long id = deleteRequest.getId();
        Room oldRoom = roomService.getById(id);
        ThrowUtils.throwIf(oldRoom == null, ErrorCode.NOT_FOUND_ERROR);
        if (!oldRoom.getUserId().equals(user.getId()) && !userFeignClient.isAdmin()) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = roomService.removeById(id);
        return ResultUtils.success(b);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateRoom(@RequestBody RoomUpdateRequest roomUpdateRequest) {
        User loginUser = userFeignClient.getLoginUser();
        ThrowUtils.throwIf(!userFeignClient.isAdmin(loginUser), ErrorCode.NO_AUTH_ERROR);
        if (roomUpdateRequest == null || roomUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Room room = new Room();
        BeanUtils.copyProperties(roomUpdateRequest, room);
        roomService.validRoom(room, false);
        long id = roomUpdateRequest.getId();
        Room oldRoom = roomService.getById(id);
        ThrowUtils.throwIf(oldRoom == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = roomService.updateById(room);
        return ResultUtils.success(result);
    }

    @GetMapping("/get/vo")
    public BaseResponse<RoomVO> getRoomVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RoomVO roomVO = roomService.getRoomDetail(id, request);
        return ResultUtils.success(roomVO);
    }

    @GetMapping("/get")
    public BaseResponse<Room> getRoomById(long id) {
        User loginUser = userFeignClient.getLoginUser();
        ThrowUtils.throwIf(!userFeignClient.isAdmin(loginUser), ErrorCode.NO_AUTH_ERROR);
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Room room = roomService.getById(id);
        if (room == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(room);
    }

    @PostMapping("/list/page")
    public BaseResponse<Page<Room>> listRoomByPage(@RequestBody RoomQueryRequest roomQueryRequest) {
        User loginUser = userFeignClient.getLoginUser();
        ThrowUtils.throwIf(!userFeignClient.isAdmin(loginUser), ErrorCode.NO_AUTH_ERROR);
        long current = roomQueryRequest.getCurrent();
        long size = roomQueryRequest.getPageSize();
        Page<Room> roomPage = roomService.page(new Page<>(current, size), roomService.getQueryWrapper(roomQueryRequest));
        return ResultUtils.success(roomPage);
    }

    @PostMapping("/list/page/vo")
    public BaseResponse<Page<RoomVO>> listRoomVOByPage(@RequestBody RoomQueryRequest roomQueryRequest, HttpServletRequest request) {
        long current = roomQueryRequest.getCurrent();
        long size = roomQueryRequest.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Room> roomPage = roomService.page(new Page<>(current, size), roomService.getQueryWrapper(roomQueryRequest));
        return ResultUtils.success(roomService.getRoomVOPage(roomPage, request));
    }

    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<RoomVO>> listMyRoomVOByPage(@RequestBody RoomQueryRequest roomQueryRequest, HttpServletRequest request) {
        if (roomQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        roomQueryRequest.setUserId(loginUser.getId());
        long current = roomQueryRequest.getCurrent();
        long size = roomQueryRequest.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Room> roomPage = roomService.page(new Page<>(current, size), roomService.getQueryWrapper(roomQueryRequest));
        return ResultUtils.success(roomService.getRoomVOPage(roomPage, request));
    }

    @PostMapping("/edit")
    public BaseResponse<Boolean> editRoom(@RequestBody RoomEditRequest roomEditRequest, HttpServletRequest request) {
        if (roomEditRequest == null || roomEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Room room = new Room();
        BeanUtils.copyProperties(roomEditRequest, room);
        roomService.validRoom(room, false);
        User loginUser = userFeignClient.getLoginUser(request);
        long id = roomEditRequest.getId();
        Room oldRoom = roomService.getById(id);
        ThrowUtils.throwIf(oldRoom == null, ErrorCode.NOT_FOUND_ERROR);
        if (!oldRoom.getUserId().equals(loginUser.getId()) && !userFeignClient.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = roomService.updateById(room);
        return ResultUtils.success(result);
    }

    @PostMapping("/join")
    public BaseResponse<Boolean> joinRoom(@RequestBody RoomJoinRequest roomJoinRequest, HttpServletRequest request) {
        if (roomJoinRequest == null || roomJoinRequest.getRoomId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        Long roomId = roomJoinRequest.getRoomId();
        String password = roomJoinRequest.getPassword();
        boolean result = roomService.joinRoom(roomId, password, loginUser);
        return ResultUtils.success(result);
    }

    @PostMapping("/quit")
    public BaseResponse<Boolean> quitRoom(@RequestBody RoomQuitRequest roomQuitRequest, HttpServletRequest request) {
        if (roomQuitRequest == null || roomQuitRequest.getRoomId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        Long roomId = roomQuitRequest.getRoomId();
        boolean result = roomService.quitRoom(roomId, loginUser);
        return ResultUtils.success(result);
    }

    @PostMapping("/transfer")
    public BaseResponse<Boolean> transferLeader(@RequestBody RoomTransferRequest roomTransferRequest, HttpServletRequest request) {
        if (roomTransferRequest == null || roomTransferRequest.getRoomId() == null || roomTransferRequest.getNewLeaderUserId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        Long roomId = roomTransferRequest.getRoomId();
        Long newLeaderUserId = roomTransferRequest.getNewLeaderUserId();
        boolean result = roomService.transferLeader(roomId, newLeaderUserId, loginUser);
        return ResultUtils.success(result);
    }

    @GetMapping("/auth/token")
    public BaseResponse<RoomAuthRequest> generateAuthToken(@RequestParam Long roomId, HttpServletRequest request) {
        if (roomId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        Long timestamp = System.currentTimeMillis();
        String token = AuthTokenUtils.generateToken(loginUser.getId(), roomId, timestamp);
        RoomAuthRequest authRequest = new RoomAuthRequest();
        authRequest.setUserId(loginUser.getId());
        authRequest.setRoomId(roomId);
        authRequest.setTimestamp(timestamp);
        authRequest.setToken(token);
        return ResultUtils.success(authRequest);
    }

    @PostMapping("/auth")
    public BaseResponse<Boolean> auth(@RequestBody RoomAuthRequest roomAuthRequest, HttpServletRequest request) {
        if (roomAuthRequest == null || roomAuthRequest.getRoomId() == null || roomAuthRequest.getUserId() == null
                || roomAuthRequest.getToken() == null || roomAuthRequest.getTimestamp() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "缺少必要参数");
        }

        Long roomId = roomAuthRequest.getRoomId();
        Long userId = roomAuthRequest.getUserId();
        String token = roomAuthRequest.getToken();
        Long timestamp = roomAuthRequest.getTimestamp();

        boolean isValidToken = AuthTokenUtils.verifyToken(userId, roomId, token, timestamp, 5);
        if (!isValidToken) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "认证失败：token无效或已过期");
        }

        User user = userFeignClient.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }

        boolean result = roomService.isUserInRoom(roomId, userId);
        return ResultUtils.success(result);
    }
}

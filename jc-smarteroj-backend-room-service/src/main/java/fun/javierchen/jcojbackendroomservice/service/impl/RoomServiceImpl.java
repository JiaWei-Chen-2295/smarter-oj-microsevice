package fun.javierchen.jcojbackendroomservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.javierchen.jcojbackendcommon.common.ErrorCode;
import fun.javierchen.jcojbackendcommon.constant.CommonConstant;
import fun.javierchen.jcojbackendcommon.exception.BusinessException;
import fun.javierchen.jcojbackendcommon.exception.ThrowUtils;
import fun.javierchen.jcojbackendcommon.utils.SqlUtils;
import fun.javierchen.jcojbackendmodel.dto.room.RoomQueryRequest;
import fun.javierchen.jcojbackendmodel.entity.Room;
import fun.javierchen.jcojbackendmodel.entity.User;
import fun.javierchen.jcojbackendmodel.entity.UserRoom;
import fun.javierchen.jcojbackendmodel.vo.RoomMemberVO;
import fun.javierchen.jcojbackendmodel.vo.RoomVO;
import fun.javierchen.jcojbackendroomservice.mapper.RoomMapper;
import fun.javierchen.jcojbackendroomservice.service.RoomService;
import fun.javierchen.jcojbackendroomservice.service.UserRoomService;
import fun.javierchen.jcojbackendserverclient.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private UserRoomService userRoomService;

    @Override
    public void validRoom(Room room, boolean add) {
        if (room == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = room.getName();
        String description = room.getDescription();
        Integer mateNum = room.getMateNum();
        Integer status = room.getStatus();
        String password = room.getPassword();

        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name), ErrorCode.PARAMS_ERROR, "房间名称不能为空");
            ThrowUtils.throwIf(mateNum == null, ErrorCode.PARAMS_ERROR, "房间最大人数不能为空");
            ThrowUtils.throwIf(status == null, ErrorCode.PARAMS_ERROR, "房间状态不能为空");
        }

        if (StringUtils.isNotBlank(name) && name.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "房间名称过长");
        }

        if (StringUtils.isNotBlank(description) && description.length() > 1024) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "房间描述过长");
        }

        if (mateNum != null && (mateNum < 2 || mateNum > 20)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "房间人数必须在2-20之间");
        }

        if (status != null && status != 0 && status != 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "房间状态错误");
        }

        if (status != null) {
            if (status == 0) {
                if (StringUtils.isNotBlank(password)) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "公开房间不能设置密码");
                }
            } else {
                if (StringUtils.isBlank(password)) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "非公开房间必须设置密码");
                }
                if (password.length() > 256) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码过长");
                }
            }
        }
    }

    @Override
    public QueryWrapper<Room> getQueryWrapper(RoomQueryRequest roomQueryRequest) {
        QueryWrapper<Room> queryWrapper = new QueryWrapper<>();
        if (roomQueryRequest == null) {
            return queryWrapper;
        }

        Long id = roomQueryRequest.getId();
        String name = roomQueryRequest.getName();
        String description = roomQueryRequest.getDescription();
        Integer status = roomQueryRequest.getStatus();
        Long userId = roomQueryRequest.getUserId();
        String sortField = roomQueryRequest.getSortField();
        String sortOrder = roomQueryRequest.getSortOrder();

        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.like(StringUtils.isNotBlank(name), "room_name", name);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "user_id", userId);
        queryWrapper.eq("is_delete", 0);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), CommonConstant.SORT_ORDER_ASC.equals(sortOrder), sortField);
        return queryWrapper;
    }

    @Override
    public RoomVO getRoomVO(Room room, HttpServletRequest request) {
        RoomVO roomVO = RoomVO.objToVo(room);
        Long userId = room.getUserId();
        if (userId != null && userId > 0) {
            User user = userFeignClient.getById(userId);
            roomVO.setUserVO(userFeignClient.getUserVO(user));
        }

        QueryWrapper<UserRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_id", room.getId());
        queryWrapper.eq("is_delete", 0);
        long count = userRoomService.count(queryWrapper);
        roomVO.setCurrentNum((int) count);
        return roomVO;
    }

    @Override
    public Page<RoomVO> getRoomVOPage(Page<Room> roomPage, HttpServletRequest request) {
        List<Room> roomList = roomPage.getRecords();
        Page<RoomVO> roomVOPage = new Page<>(roomPage.getCurrent(), roomPage.getSize(), roomPage.getTotal());
        if (CollUtil.isEmpty(roomList)) {
            return roomVOPage;
        }

        Set<Long> userIdSet = roomList.stream().map(Room::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userFeignClient.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));

        Set<Long> roomIdSet = roomList.stream().map(Room::getId).collect(Collectors.toSet());
        QueryWrapper<UserRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("room_id", roomIdSet);
        queryWrapper.eq("is_delete", 0);
        List<UserRoom> userRoomList = userRoomService.list(queryWrapper);
        Map<Long, Long> roomIdCountMap = userRoomList.stream()
                .collect(Collectors.groupingBy(UserRoom::getRoomId, Collectors.counting()));

        List<RoomVO> roomVOList = roomList.stream().map(room -> {
            RoomVO roomVO = RoomVO.objToVo(room);
            Long userId = room.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            roomVO.setUserVO(userFeignClient.getUserVO(user));
            Long count = roomIdCountMap.getOrDefault(room.getId(), 0L);
            roomVO.setCurrentNum(count.intValue());
            return roomVO;
        }).collect(Collectors.toList());

        roomVOPage.setRecords(roomVOList);
        return roomVOPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long createRoom(Room room, User loginUser) {
        validRoom(room, true);

        QueryWrapper<Room> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", loginUser.getId());
        queryWrapper.eq("is_delete", 0);
        long count = this.count(queryWrapper);
        ThrowUtils.throwIf(count > 0, ErrorCode.OPERATION_ERROR, "您已创建过房间，一个用户只能创建一个房间");

        room.setUserId(loginUser.getId());
        boolean result = this.save(room);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "创建房间失败");

        UserRoom userRoom = new UserRoom();
        userRoom.setUserId(loginUser.getId());
        userRoom.setRoomId(room.getId());
        userRoom.setJoinTime(new Date());
        boolean saveResult = userRoomService.save(userRoom);
        ThrowUtils.throwIf(!saveResult, ErrorCode.OPERATION_ERROR, "加入房间失败");

        log.info("用户 {} 创建房间 {}", loginUser.getId(), room.getId());
        return room.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean joinRoom(Long roomId, String password, User loginUser) {
        ThrowUtils.throwIf(roomId == null || roomId <= 0, ErrorCode.PARAMS_ERROR, "房间id不能为空");

        Room room = this.getById(roomId);
        ThrowUtils.throwIf(room == null, ErrorCode.NOT_FOUND_ERROR, "房间不存在");

        QueryWrapper<UserRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", loginUser.getId());
        queryWrapper.eq("room_id", roomId);
        queryWrapper.eq("is_delete", 0);
        long count = userRoomService.count(queryWrapper);
        ThrowUtils.throwIf(count > 0, ErrorCode.OPERATION_ERROR, "您已加入该房间");

        QueryWrapper<UserRoom> countWrapper = new QueryWrapper<>();
        countWrapper.eq("room_id", roomId);
        countWrapper.eq("is_delete", 0);
        long currentCount = userRoomService.count(countWrapper);
        ThrowUtils.throwIf(currentCount >= room.getMateNum(), ErrorCode.OPERATION_ERROR, "房间已满");

        if (room.getStatus() == 1) {
            ThrowUtils.throwIf(StringUtils.isBlank(password), ErrorCode.PARAMS_ERROR, "非公开房间需要提供密码");
            ThrowUtils.throwIf(!password.equals(room.getPassword()), ErrorCode.PARAMS_ERROR, "密码错误");
        }

        UserRoom userRoom = new UserRoom();
        userRoom.setUserId(loginUser.getId());
        userRoom.setRoomId(roomId);
        userRoom.setJoinTime(new Date());
        boolean result = userRoomService.save(userRoom);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "加入房间失败");

        log.info("用户 {} 加入房间 {}", loginUser.getId(), roomId);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean quitRoom(Long roomId, User loginUser) {
        ThrowUtils.throwIf(roomId == null || roomId <= 0, ErrorCode.PARAMS_ERROR, "房间id不能为空");

        Room room = this.getById(roomId);
        ThrowUtils.throwIf(room == null, ErrorCode.NOT_FOUND_ERROR, "房间不存在");

        QueryWrapper<UserRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", loginUser.getId());
        queryWrapper.eq("room_id", roomId);
        queryWrapper.eq("is_delete", 0);
        UserRoom userRoom = userRoomService.getOne(queryWrapper);
        ThrowUtils.throwIf(userRoom == null, ErrorCode.OPERATION_ERROR, "您未加入该房间");

        if (room.getUserId().equals(loginUser.getId())) {
            QueryWrapper<UserRoom> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("room_id", roomId);
            boolean removeResult = userRoomService.remove(deleteWrapper);
            ThrowUtils.throwIf(!removeResult, ErrorCode.OPERATION_ERROR, "删除房间成员失败");

            boolean result = this.removeById(roomId);
            ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "删除房间失败");

            log.info("队长 {} 退出房间 {}，房间已删除", loginUser.getId(), roomId);
            return true;
        }

        boolean result = userRoomService.removeById(userRoom.getId());
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "退出房间失败");

        QueryWrapper<UserRoom> countWrapper = new QueryWrapper<>();
        countWrapper.eq("room_id", roomId);
        countWrapper.eq("is_delete", 0);
        long count = userRoomService.count(countWrapper);
        if (count == 0) {
            boolean removeResult = this.removeById(roomId);
            ThrowUtils.throwIf(!removeResult, ErrorCode.OPERATION_ERROR, "删除空房间失败");
            log.info("房间 {} 无成员，已自动删除", roomId);
        }

        log.info("用户 {} 退出房间 {}", loginUser.getId(), roomId);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean transferLeader(Long roomId, Long newLeaderUserId, User loginUser) {
        ThrowUtils.throwIf(roomId == null || roomId <= 0, ErrorCode.PARAMS_ERROR, "房间id不能为空");
        ThrowUtils.throwIf(newLeaderUserId == null || newLeaderUserId <= 0, ErrorCode.PARAMS_ERROR, "新队长id不能为空");
        ThrowUtils.throwIf(newLeaderUserId.equals(loginUser.getId()), ErrorCode.PARAMS_ERROR, "不能转让给自己");

        Room room = this.getById(roomId);
        ThrowUtils.throwIf(room == null, ErrorCode.NOT_FOUND_ERROR, "房间不存在");

        ThrowUtils.throwIf(!room.getUserId().equals(loginUser.getId()), ErrorCode.NO_AUTH_ERROR, "只有队长可以转让队长身份");

        QueryWrapper<UserRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", newLeaderUserId);
        queryWrapper.eq("room_id", roomId);
        queryWrapper.eq("is_delete", 0);
        long count = userRoomService.count(queryWrapper);
        ThrowUtils.throwIf(count == 0, ErrorCode.OPERATION_ERROR, "新队长不在房间内");

        room.setUserId(newLeaderUserId);
        boolean result = this.updateById(room);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "转让队长失败");

        log.info("用户 {} 将房间 {} 的队长转让给 {}", loginUser.getId(), roomId, newLeaderUserId);
        return true;
    }

    @Override
    public RoomVO getRoomDetail(Long roomId, HttpServletRequest request) {
        ThrowUtils.throwIf(roomId == null || roomId <= 0, ErrorCode.PARAMS_ERROR, "房间id不能为空");

        Room room = this.getById(roomId);
        ThrowUtils.throwIf(room == null, ErrorCode.NOT_FOUND_ERROR, "房间不存在");

        RoomVO roomVO = getRoomVO(room, request);

        QueryWrapper<UserRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_id", roomId);
        queryWrapper.eq("is_delete", 0);
        queryWrapper.orderByAsc("join_time");
        List<UserRoom> userRoomList = userRoomService.list(queryWrapper);

        if (CollUtil.isNotEmpty(userRoomList)) {
            Set<Long> userIdSet = userRoomList.stream().map(UserRoom::getUserId).collect(Collectors.toSet());
            Map<Long, List<User>> userIdUserListMap = userFeignClient.listByIds(userIdSet).stream()
                    .collect(Collectors.groupingBy(User::getId));

            List<RoomMemberVO> memberList = userRoomList.stream().map(ur -> {
                RoomMemberVO memberVO = new RoomMemberVO();
                memberVO.setUserId(ur.getUserId());
                memberVO.setJoinTime(ur.getJoinTime());
                memberVO.setIsLeader(ur.getUserId().equals(room.getUserId()));
                if (userIdUserListMap.containsKey(ur.getUserId())) {
                    User user = userIdUserListMap.get(ur.getUserId()).get(0);
                    memberVO.setUserVO(userFeignClient.getUserVO(user));
                }
                return memberVO;
            }).collect(Collectors.toList());
            roomVO.setMembers(memberList);
        } else {
            roomVO.setMembers(new ArrayList<>());
        }

        return roomVO;
    }

    @Override
    public boolean isUserInRoom(Long roomId, Long userId) {
        QueryWrapper<UserRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_id", roomId);
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_delete", 0);
        UserRoom one = userRoomService.getOne(queryWrapper);
        return one != null;
    }
}


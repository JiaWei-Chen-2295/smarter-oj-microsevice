package fun.javierchen.jcojbackendroomservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.javierchen.jcojbackendmodel.entity.UserRoom;
import fun.javierchen.jcojbackendroomservice.mapper.UserRoomMapper;
import fun.javierchen.jcojbackendroomservice.service.UserRoomService;
import org.springframework.stereotype.Service;

@Service
public class UserRoomServiceImpl extends ServiceImpl<UserRoomMapper, UserRoom> implements UserRoomService {
}


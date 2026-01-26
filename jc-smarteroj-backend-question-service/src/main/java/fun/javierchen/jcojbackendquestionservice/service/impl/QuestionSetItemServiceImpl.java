package fun.javierchen.jcojbackendquestionservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.javierchen.jcojbackendmodel.entity.QuestionSetItem;
import fun.javierchen.jcojbackendquestionservice.mapper.QuestionSetItemMapper;
import fun.javierchen.jcojbackendquestionservice.service.QuestionSetItemService;
import org.springframework.stereotype.Service;

@Service
public class QuestionSetItemServiceImpl extends ServiceImpl<QuestionSetItemMapper, QuestionSetItem> implements QuestionSetItemService {
}


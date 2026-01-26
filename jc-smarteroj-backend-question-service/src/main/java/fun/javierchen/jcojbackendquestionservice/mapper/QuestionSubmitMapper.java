package fun.javierchen.jcojbackendquestionservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.javierchen.jcojbackendmodel.entity.QuestionSubmit;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author 16010
* @description 针对表【question_submit(用户提交表)】的数据库操作Mapper
* @createDate 2025-03-25 17:00:22
* @Entity fun.javierchen.smarteroj.model.entity.QuestionSubmit
*/
public interface QuestionSubmitMapper extends BaseMapper<QuestionSubmit> {

    /**
     * 统计用户每日提交次数（仅成功的提交）
     *
     * @param userId    用户ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 每日提交统计列表
     */
    List<Map<String, Object>> selectDailySubmitCount(@Param("userId") Long userId,
                                                      @Param("startDate") Date startDate,
                                                      @Param("endDate") Date endDate);
}





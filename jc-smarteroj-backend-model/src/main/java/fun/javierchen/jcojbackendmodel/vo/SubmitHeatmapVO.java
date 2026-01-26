package fun.javierchen.jcojbackendmodel.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户提交热力图响应VO
 *
 * @author JavierChen
 */
@Data
public class SubmitHeatmapVO implements Serializable {

    /**
     * 热力图数据列表
     */
    private List<DailySubmitCount> heatmapData;

    /**
     * 时间范围内总提交次数
     */
    private Long totalSubmissions;

    /**
     * 单日最大提交次数（用于前端渲染颜色深度）
     */
    private Integer maxDailySubmissions;

    /**
     * 活跃天数（有提交记录的天数）
     */
    private Integer activeDays;

    /**
     * 当前连续提交天数
     */
    private Integer currentStreak;

    /**
     * 最长连续提交天数
     */
    private Integer maxStreak;

    private static final long serialVersionUID = 1L;

    /**
     * 每日提交统计
     */
    @Data
    public static class DailySubmitCount implements Serializable {
        /**
         * 日期（格式：yyyy-MM-dd）
         */
        private String date;

        /**
         * 该日期的提交次数
         */
        private Integer count;

        private static final long serialVersionUID = 1L;
    }
}

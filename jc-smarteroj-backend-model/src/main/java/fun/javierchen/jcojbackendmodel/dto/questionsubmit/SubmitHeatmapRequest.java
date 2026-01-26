package fun.javierchen.jcojbackendmodel.dto.questionsubmit;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户提交热力图请求
 *
 * @author JavierChen
 */
@Data
public class SubmitHeatmapRequest implements Serializable {

    /**
     * 开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    /**
     * 结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    private static final long serialVersionUID = 1L;
}

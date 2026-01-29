package fun.javierchen.jcojbackendquestionservice.service;

import fun.javierchen.jcojbackendmodel.dto.question.QuestionBatchImportResponse;
import fun.javierchen.jcojbackendmodel.entity.User;
import org.springframework.web.multipart.MultipartFile;

/**
 * 题目批量导入服务接口
 *
 * @author JavierChen
 */
public interface QuestionImportService {

    /**
     * 从 FPS XML 文件批量导入题目
     *
     * @param file      上传的 XML 文件
     * @param loginUser 当前登录用户(必须是管理员)
     * @return 导入结果
     */
    QuestionBatchImportResponse importFromFpsXml(MultipartFile file, User loginUser);
}

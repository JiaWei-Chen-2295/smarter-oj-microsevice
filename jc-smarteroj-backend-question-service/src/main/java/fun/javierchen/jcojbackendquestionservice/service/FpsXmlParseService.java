package fun.javierchen.jcojbackendquestionservice.service;

import fun.javierchen.jcojbackendmodel.dto.question.FpsItem;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * FPS XML 解析服务接口
 *
 * @author JavierChen
 */
public interface FpsXmlParseService {

    /**
     * 解析 FPS XML 文件
     *
     * @param file 上传的 XML 文件
     * @return 解析出的题目列表
     * @throws Exception 解析异常
     */
    List<FpsItem> parseFpsXml(MultipartFile file) throws Exception;

    /**
     * 解析 FPS XML 内容
     *
     * @param xmlContent XML 内容字符串
     * @return 解析出的题目列表
     * @throws Exception 解析异常
     */
    List<FpsItem> parseFpsXmlContent(String xmlContent) throws Exception;
}

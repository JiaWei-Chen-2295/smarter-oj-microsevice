package fun.javierchen.jcojbackendquestionservice.service.impl;

import fun.javierchen.jcojbackendcommon.common.ErrorCode;
import fun.javierchen.jcojbackendcommon.exception.BusinessException;
import fun.javierchen.jcojbackendmodel.dto.question.FpsItem;
import fun.javierchen.jcojbackendmodel.dto.question.FpsSolution;
import fun.javierchen.jcojbackendquestionservice.service.FpsXmlParseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * FPS XML 解析服务实现
 * 支持 HUSTOJ FPS (Free Problem Set) 格式
 *
 * @author JavierChen
 */
@Service
@Slf4j
public class FpsXmlParseServiceImpl implements FpsXmlParseService {

    @Override
    public List<FpsItem> parseFpsXml(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "上传文件为空");
        }

        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename) || !filename.toLowerCase().endsWith(".xml")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请上传 XML 格式的文件");
        }

        try (InputStream inputStream = file.getInputStream()) {
            return parseXmlFromInputStream(inputStream);
        }
    }

    @Override
    public List<FpsItem> parseFpsXmlContent(String xmlContent) throws Exception {
        if (StringUtils.isBlank(xmlContent)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "XML 内容为空");
        }

        try (InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8))) {
            return parseXmlFromInputStream(inputStream);
        }
    }

    /**
     * 从 InputStream 解析 XML
     */
    private List<FpsItem> parseXmlFromInputStream(InputStream inputStream) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // 禁用 DTD 验证
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputStream);
        document.getDocumentElement().normalize();

        // 获取根元素 fps
        Element root = document.getDocumentElement();
        if (!"fps".equals(root.getNodeName())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的 FPS XML 格式：根元素应为 <fps>");
        }

        // 解析所有 item 元素
        NodeList itemNodes = root.getElementsByTagName("item");
        List<FpsItem> items = new ArrayList<>();

        for (int i = 0; i < itemNodes.getLength(); i++) {
            Node itemNode = itemNodes.item(i);
            if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) itemNode;
                FpsItem fpsItem = parseItemElement(itemElement);
                items.add(fpsItem);
            }
        }

        log.info("成功解析 FPS XML，共 {} 道题目", items.size());
        return items;
    }

    /**
     * 解析单个 item 元素
     */
    private FpsItem parseItemElement(Element itemElement) {
        FpsItem item = new FpsItem();

        // 标题
        item.setTitle(getElementTextContent(itemElement, "title"));

        // URL
        item.setUrl(getElementTextContent(itemElement, "url"));

        // 时间限制
        Element timeLimitElement = getFirstChildElement(itemElement, "time_limit");
        if (timeLimitElement != null) {
            String timeLimitStr = timeLimitElement.getTextContent().trim();
            try {
                item.setTimeLimit(Long.parseLong(timeLimitStr));
            } catch (NumberFormatException e) {
                log.warn("时间限制解析失败: {}", timeLimitStr);
                item.setTimeLimit(1000L); // 默认 1000ms
            }
            item.setTimeLimitUnit(timeLimitElement.getAttribute("unit"));
        }

        // 内存限制
        Element memoryLimitElement = getFirstChildElement(itemElement, "memory_limit");
        if (memoryLimitElement != null) {
            String memoryLimitStr = memoryLimitElement.getTextContent().trim();
            try {
                item.setMemoryLimit(Long.parseLong(memoryLimitStr));
            } catch (NumberFormatException e) {
                log.warn("内存限制解析失败: {}", memoryLimitStr);
                item.setMemoryLimit(128L); // 默认 128MB
            }
            item.setMemoryLimitUnit(memoryLimitElement.getAttribute("unit"));
        }

        // 题目描述
        item.setDescription(getElementTextContent(itemElement, "description"));

        // 输入说明
        item.setInput(getElementTextContent(itemElement, "input"));

        // 输出说明
        item.setOutput(getElementTextContent(itemElement, "output"));

        // 样例输入
        item.setSampleInputs(getAllElementTextContents(itemElement, "sample_input"));

        // 样例输出
        item.setSampleOutputs(getAllElementTextContents(itemElement, "sample_output"));

        // 测试输入
        item.setTestInputs(getAllElementTextContents(itemElement, "test_input"));

        // 测试输出
        item.setTestOutputs(getAllElementTextContents(itemElement, "test_output"));

        // 提示
        item.setHint(getElementTextContent(itemElement, "hint"));

        // 来源
        item.setSource(getElementTextContent(itemElement, "source"));

        // 参考答案
        item.setSolutions(parseSolutions(itemElement));

        return item;
    }

    /**
     * 解析参考答案
     */
    private List<FpsSolution> parseSolutions(Element itemElement) {
        List<FpsSolution> solutions = new ArrayList<>();
        NodeList solutionNodes = itemElement.getElementsByTagName("solution");

        for (int i = 0; i < solutionNodes.getLength(); i++) {
            Node node = solutionNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element solutionElement = (Element) node;
                FpsSolution solution = new FpsSolution();
                solution.setLanguage(solutionElement.getAttribute("language"));
                solution.setCode(solutionElement.getTextContent());
                solutions.add(solution);
            }
        }

        return solutions;
    }

    /**
     * 获取指定子元素的文本内容
     */
    private String getElementTextContent(Element parent, String tagName) {
        Element element = getFirstChildElement(parent, tagName);
        if (element != null) {
            return element.getTextContent().trim();
        }
        return null;
    }

    /**
     * 获取第一个指定名称的子元素
     */
    private Element getFirstChildElement(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                return (Element) node;
            }
        }
        return null;
    }

    /**
     * 获取所有指定名称的子元素的文本内容
     */
    private List<String> getAllElementTextContents(Element parent, String tagName) {
        List<String> contents = new ArrayList<>();
        NodeList nodeList = parent.getElementsByTagName(tagName);

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String text = node.getTextContent().trim();
                if (StringUtils.isNotBlank(text)) {
                    contents.add(text);
                }
            }
        }

        return contents;
    }
}

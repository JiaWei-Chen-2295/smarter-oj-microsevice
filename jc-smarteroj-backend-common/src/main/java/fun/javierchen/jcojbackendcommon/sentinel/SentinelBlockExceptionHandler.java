package fun.javierchen.jcojbackendcommon.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Sentinel 限流异常处理器
 * 
 * 统一处理各类 Sentinel 异常，返回标准的 JSON 响应格式
 * 
 * 异常类型说明：
 * - FlowException: 流控规则触发
 * - DegradeException: 熔断降级规则触发
 * - ParamFlowException: 热点参数限流规则触发
 * - AuthorityException: 授权规则触发
 * - SystemBlockException: 系统保护规则触发
 * 
 * @author JavierChen
 * @since 2026-01-30
 */
@Slf4j
public class SentinelBlockExceptionHandler implements BlockExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        String requestUri = request.getRequestURI();
        String message = buildMessage(e);
        int code = buildCode(e);

        log.warn("[Sentinel] 请求被拦截: URI={}, 类型={}, 规则={}, 消息={}",
                requestUri,
                e.getClass().getSimpleName(),
                e.getRule() != null ? e.getRule().getResource() : "无",
                message);

        // 设置响应状态和类型
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // 构建标准响应体
        Map<String, Object> result = new HashMap<>();
        result.put("code", code);
        result.put("message", message);
        result.put("data", null);

        // 写入响应
        PrintWriter out = response.getWriter();
        out.print(objectMapper.writeValueAsString(result));
        out.flush();
        out.close();
    }

    /**
     * 根据异常类型构建错误消息
     */
    private String buildMessage(BlockException e) {
        if (e instanceof FlowException) {
            return "请求过于频繁，请稍后重试";
        } else if (e instanceof DegradeException) {
            return "服务暂时不可用，请稍后重试";
        } else if (e instanceof ParamFlowException) {
            return "请求频率过高，请稍后重试";
        } else if (e instanceof AuthorityException) {
            return "无权访问该资源";
        } else if (e instanceof SystemBlockException) {
            return "系统繁忙，请稍后重试";
        }
        return "服务繁忙，请稍后重试";
    }

    /**
     * 根据异常类型构建错误码
     */
    private int buildCode(BlockException e) {
        if (e instanceof FlowException) {
            return 42901; // 流控限流
        } else if (e instanceof DegradeException) {
            return 42902; // 熔断降级
        } else if (e instanceof ParamFlowException) {
            return 42903; // 热点参数限流
        } else if (e instanceof AuthorityException) {
            return 40301; // 授权拦截
        } else if (e instanceof SystemBlockException) {
            return 50301; // 系统保护
        }
        return 429; // 默认 Too Many Requests
    }
}

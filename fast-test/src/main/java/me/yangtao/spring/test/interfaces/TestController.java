package me.yangtao.spring.test.interfaces;

import lombok.extern.slf4j.Slf4j;
import me.yangtao.spring.demo.api.request.OperatorRequest;
import me.yangtao.spring.demo.api.response.OperatorResponse;
import me.yangtao.spring.demo.common.Result;
import me.yangtao.spring.demo.common.utils.JsonUtil;
import me.yangtao.spring.test.infra.config.RedisPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private HttpServletRequest request;


    @RequestMapping("/testHeader")
    public String testHeader(@RequestHeader Map<String, String> headers) {
        log.info("testHeader>" + headers);
        return JsonUtil.GetJsonResult(headers);
    }

    @PostMapping("/testPost")
    public Result<Map<String, String>> testPost(@RequestHeader Map<String, String> headers) {
        return Result.success("testPost", headers);
    }

    @GetMapping("/testGet")
    public Result<Map<String, String>> testGet(@RequestParam Long userId, @RequestHeader Map<String, String> headers) {
        return Result.success("testGet", headers);
    }

    @DeleteMapping("/testDel")
    public Result<Map<String, String>> testDel(@RequestHeader Map<String, String> headers) {
        return Result.success("testDel", headers);
    }

    @PutMapping("/testPut")
    public Result<Map<String, String>> testPut(@RequestHeader Map<String, String> headers) {
        return Result.success("testPut", headers);
    }

    @PatchMapping("/testPatch")
    public Result<Map<String, String>> testPatch(@RequestHeader Map<String, String> headers) {
        return Result.success("testPatch", headers);
    }

}

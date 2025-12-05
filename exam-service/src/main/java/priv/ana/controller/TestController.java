package priv.ana.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import priv.ana.core.enums.ResponseStatus;
import priv.ana.core.web.domain.Response;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/test")
public class TestController {

    private final StringRedisTemplate stringRedisTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final RocketMQTemplate rocketMQTemplate;

    public TestController(StringRedisTemplate stringRedisTemplate, JdbcTemplate jdbcTemplate, RocketMQTemplate rocketMQTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.jdbcTemplate = jdbcTemplate;
        this.rocketMQTemplate = rocketMQTemplate;
    }

    @Value("${exam.test.redis-key:exam:test:ping}")
    private String redisKeyPrefix;

    @GetMapping("/list")
    public Response<Map<String, Object>> list() {
        Map<String, Object> details = new HashMap<>();
        boolean allPassed = true;

        try {
            details.put("redis", checkRedis());
        } catch (Exception ex) {
            allPassed = false;
            log.error("Redis check failed", ex);
            details.put("redis", buildError(ex));
        }

        try {
            details.put("mysql", checkMySql());
        } catch (Exception ex) {
            allPassed = false;
            log.error("MySQL check failed", ex);
            details.put("mysql", buildError(ex));
        }

        try {
            details.put("rocketmq", checkRocketMq());
        } catch (Exception ex) {
            allPassed = false;
            log.error("RocketMQ check failed", ex);
            details.put("rocketmq", buildError(ex));
        }

        if (allPassed) {
            return Response.success(details);
        }
        return Response.fail(ResponseStatus.INTERNAL_SERVER_ERROR, "依赖检查失败", details);
    }

    @GetMapping("/test")
    public Response<Void> test(@RequestHeader("User-Id") String userId, @RequestHeader("User-Role") String userRole) {
        log.info("userId:{},userRole:{}", userId, userRole);
        return Response.success();
    }

    private Map<String, Object> checkRedis() {
        String key = redisKeyPrefix + ":" + UUID.randomUUID();
        String value = "pong-" + UUID.randomUUID();
        stringRedisTemplate.opsForValue().set(key, value, Duration.ofMinutes(1));
        String readValue = stringRedisTemplate.opsForValue().get(key);

        Map<String, Object> info = new HashMap<>();
        info.put("key", key);
        info.put("written", value);
        info.put("read", readValue);
        info.put("ok", Objects.equals(value, readValue));
        return info;
    }

    private Map<String, Object> checkMySql() {
        Integer queryResult = jdbcTemplate.queryForObject("SELECT 1", Integer.class);

        Map<String, Object> info = new HashMap<>();
        info.put("result", queryResult);
        info.put("ok", Objects.equals(queryResult, 1));
        return info;
    }

    private Map<String, Object> checkRocketMq() throws MQClientException {
        String payload = "health-check-" + UUID.randomUUID();
        String rocketMqTopic = "exam_test_topic";
        SendResult sendResult = rocketMQTemplate.syncSend(rocketMqTopic, payload);

        Map<String, Object> info = new HashMap<>();
        info.put("topic", rocketMqTopic);
        info.put("msgId", sendResult.getMsgId());
        info.put("status", sendResult.getSendStatus());
        info.put("ok", sendResult.getSendStatus() != null);
        return info;
    }

    private Map<String, Object> buildError(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("ok", false);
        error.put("error", ex.getMessage());
        return error;
    }
}

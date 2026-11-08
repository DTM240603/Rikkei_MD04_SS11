package ra.edu.hw.md04.ss10.alert;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Service
public class AlertPublisher {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public AlertPublisher(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(String channel, AlertMessage alert) {
        try {
            redisTemplate.convertAndSend(channel, objectMapper.writeValueAsString(alert));
        } catch (JacksonException exception) {
            throw new IllegalStateException("Could not serialize alert message", exception);
        }
    }
}

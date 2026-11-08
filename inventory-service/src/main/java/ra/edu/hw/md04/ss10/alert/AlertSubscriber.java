package ra.edu.hw.md04.ss10.alert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
public class AlertSubscriber implements MessageListener {
    private static final Logger log = LoggerFactory.getLogger(AlertSubscriber.class);

    private final ObjectMapper objectMapper;

    public AlertSubscriber(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            JsonNode alert = objectMapper.readTree(message.getBody());
            log.info("=== THÔNG BÁO DASHBOARD QUẢN LÝ ===\nType : {}\nNội dung: {}\n====================================",
                alert.path("type").asText(), alert.path("message").asText());
        } catch (JacksonException exception) {
            log.error("Could not read alert message from Redis channel", exception);
        }
    }
}

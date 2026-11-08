package ra.edu.hw.md04.ss10.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ra.edu.hw.md04.ss10.event.OrderEvent;

import java.util.concurrent.CompletableFuture;

@Service
public class OrderEventProducer {
    private static final Logger log = LoggerFactory.getLogger(OrderEventProducer.class);
    private static final String TOPIC = "medicine-stock-events";

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CompletableFuture<SendResult<String, OrderEvent>> publish(OrderEvent event) {
        return kafkaTemplate.send(TOPIC, event.medicineId(), event)
            .whenComplete((result, exception) -> {
                if (exception == null) {
                    log.info("Published order {} for medicine {} to topic {} partition {}",
                        event.orderId(),
                        event.medicineId(),
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition());
                } else {
                    log.error("Failed to publish order {} for medicine {}",
                        event.orderId(),
                        event.medicineId(),
                        exception);
                }
            });
    }
}

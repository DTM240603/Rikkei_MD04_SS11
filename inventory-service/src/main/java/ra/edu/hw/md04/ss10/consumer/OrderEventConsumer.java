package ra.edu.hw.md04.ss10.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ra.edu.hw.md04.ss10.event.OrderEvent;
import ra.edu.hw.md04.ss10.service.InventoryService;

@Component
public class OrderEventConsumer {
    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);

    private final InventoryService inventoryService;

    public OrderEventConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(topics = "medicine-stock-events")
    public void consume(OrderEvent event) {
        inventoryService.process(event);
        log.info("Processed order {} for medicine {} (quantity {})",
            event.orderId(), event.medicineId(), event.quantity());
    }
}

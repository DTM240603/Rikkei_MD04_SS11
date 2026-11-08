package ra.edu.hw.md04.ss10.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ra.edu.hw.md04.ss10.dto.OrderRequest;
import ra.edu.hw.md04.ss10.dto.OrderResponse;
import ra.edu.hw.md04.ss10.event.OrderEvent;
import ra.edu.hw.md04.ss10.service.OrderEventProducer;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderEventProducer orderEventProducer;

    public OrderController(OrderEventProducer orderEventProducer) {
        this.orderEventProducer = orderEventProducer;
    }

    @PostMapping("/checkout")
    public CompletableFuture<ResponseEntity<OrderResponse>> checkout(
        @Valid @RequestBody OrderRequest request
    ) {
        OrderEvent event = new OrderEvent(
            request.orderId(),
            request.medicineId(),
            request.quantity(),
            Instant.now().toString()
        );

        return orderEventProducer.publish(event)
            .thenApply(result -> ResponseEntity.ok(
                new OrderResponse("Thanh toán thành công", event.orderId())
            ));
    }
}

package ra.edu.hw.md04.ss10.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "processed_orders")
public class ProcessedOrder {
    @Id
    @Column(name = "order_id", nullable = false, updatable = false)
    private String orderId;

    @Column(name = "medicine_id", nullable = false)
    private String medicineId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String timestamp;

    protected ProcessedOrder() {
    }

    public ProcessedOrder(String orderId, String medicineId, int quantity, String timestamp) {
        this.orderId = orderId;
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    public String getOrderId() {
        return orderId;
    }
}

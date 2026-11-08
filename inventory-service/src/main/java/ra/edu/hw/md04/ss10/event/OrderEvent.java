package ra.edu.hw.md04.ss10.event;

public record OrderEvent(
    String orderId,
    String medicineId,
    int quantity,
    String timestamp
) {
}

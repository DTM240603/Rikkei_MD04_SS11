package ra.edu.hw.md04.ss10.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ra.edu.hw.md04.ss10.entity.Medicine;
import ra.edu.hw.md04.ss10.entity.ProcessedOrder;
import ra.edu.hw.md04.ss10.event.OrderEvent;
import ra.edu.hw.md04.ss10.repository.MedicineRepository;
import ra.edu.hw.md04.ss10.repository.ProcessedOrderRepository;

@Service
public class InventoryService {
    private final MedicineRepository medicineRepository;
    private final ProcessedOrderRepository processedOrderRepository;

    public InventoryService(
        MedicineRepository medicineRepository,
        ProcessedOrderRepository processedOrderRepository
    ) {
        this.medicineRepository = medicineRepository;
        this.processedOrderRepository = processedOrderRepository;
    }

    @Cacheable(value = "medicines", key = "#id")
    public Medicine getMedicine(String id) {
        return medicineRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicine not found"));
    }

    @Transactional
    @CacheEvict(value = "medicines", key = "#event.medicineId()")
    public void process(OrderEvent event) {
        if (event.orderId() == null || event.orderId().isBlank()
            || event.medicineId() == null || event.medicineId().isBlank()
            || event.quantity() <= 0) {
            throw new IllegalArgumentException("Order event must have an order, medicine, and positive quantity");
        }

        if (processedOrderRepository.existsById(event.orderId())) {
            return;
        }

        int updatedRows = medicineRepository.deductStock(event.medicineId(), event.quantity());
        if (updatedRows == 0) {
            if (medicineRepository.existsById(event.medicineId())) {
                throw new IllegalStateException("Insufficient stock for medicine " + event.medicineId());
            }
            throw new IllegalArgumentException("Unknown medicine " + event.medicineId());
        }

        processedOrderRepository.save(new ProcessedOrder(
            event.orderId(),
            event.medicineId(),
            event.quantity(),
            event.timestamp()
        ));
    }
}

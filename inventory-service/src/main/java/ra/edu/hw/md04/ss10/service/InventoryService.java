package ra.edu.hw.md04.ss10.service;

import java.math.BigDecimal;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ra.edu.hw.md04.ss10.entity.Medicine;
import ra.edu.hw.md04.ss10.entity.ProcessedOrder;
import ra.edu.hw.md04.ss10.event.MedicineImportedEvent;
import ra.edu.hw.md04.ss10.event.OrderEvent;
import ra.edu.hw.md04.ss10.repository.MedicineRepository;
import ra.edu.hw.md04.ss10.repository.ProcessedOrderRepository;

@Service
public class InventoryService {
    private final MedicineRepository medicineRepository;
    private final ProcessedOrderRepository processedOrderRepository;
    private final ApplicationEventPublisher eventPublisher;

    public InventoryService(
        MedicineRepository medicineRepository,
        ProcessedOrderRepository processedOrderRepository,
        ApplicationEventPublisher eventPublisher
    ) {
        this.medicineRepository = medicineRepository;
        this.processedOrderRepository = processedOrderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Cacheable(value = "medicines", key = "#id")
    public Medicine getMedicine(String id) {
        return medicineRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicine not found"));
    }

    @Transactional
    @CacheEvict(value = "medicines", key = "#id")
    public Medicine updatePrice(String id, BigDecimal price) {
        if (price == null || price.signum() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Price must be zero or greater");
        }

        Medicine medicine = medicineRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicine not found"));
        medicine.setPrice(price);
        return medicineRepository.save(medicine);
    }

    @Transactional
    @CacheEvict(value = "medicines", key = "#id")
    public Medicine importStock(String id, int quantity) {
        if (quantity <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Import quantity must be greater than zero");
        }

        int updatedRows = medicineRepository.addStock(id, quantity);
        if (updatedRows == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicine not found");
        }

        Medicine medicine = medicineRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicine not found"));
        eventPublisher.publishEvent(new MedicineImportedEvent(medicine.getMedicineName(), quantity));
        return medicine;
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

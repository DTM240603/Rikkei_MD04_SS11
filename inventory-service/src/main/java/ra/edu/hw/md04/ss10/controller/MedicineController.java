package ra.edu.hw.md04.ss10.controller;

import java.math.BigDecimal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ra.edu.hw.md04.ss10.entity.Medicine;
import ra.edu.hw.md04.ss10.service.InventoryService;

@RestController
@RequestMapping("/api/v1/inventory/medicines")
public class MedicineController {
    private final InventoryService inventoryService;

    public MedicineController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{id}")
    public Medicine getMedicine(@PathVariable String id) {
        return inventoryService.getMedicine(id);
    }

    @PutMapping("/{id}/price")
    public Medicine updatePrice(@PathVariable String id, @RequestBody PriceUpdateRequest request) {
        return inventoryService.updatePrice(id, request.price());
    }

    public record PriceUpdateRequest(BigDecimal price) {
    }
}

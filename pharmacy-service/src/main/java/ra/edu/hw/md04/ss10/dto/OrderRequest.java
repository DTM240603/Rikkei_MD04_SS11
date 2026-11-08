package ra.edu.hw.md04.ss10.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record OrderRequest(
    @NotBlank String orderId,
    @NotBlank String medicineId,
    @Positive int quantity
) {
}

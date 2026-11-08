package ra.edu.hw.md04.ss10.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.edu.hw.md04.ss10.entity.ProcessedOrder;

public interface ProcessedOrderRepository extends JpaRepository<ProcessedOrder, String> {
}

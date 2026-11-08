package ra.edu.hw.md04.ss10.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ra.edu.hw.md04.ss10.entity.Medicine;

public interface MedicineRepository extends JpaRepository<Medicine, String> {
    @Modifying
    @Query("""
        update Medicine medicine
        set medicine.stock = medicine.stock - :quantity
        where medicine.medicineId = :medicineId
          and medicine.stock >= :quantity
        """)
    int deductStock(@Param("medicineId") String medicineId, @Param("quantity") int quantity);
}

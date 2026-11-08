package ra.edu.hw.md04.ss10.entity;

import java.math.BigDecimal;
import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "medicines")
public class Medicine implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "medicine_id", nullable = false, updatable = false)
    private String medicineId;

    @Column(name = "medicine_name", nullable = false)
    private String medicineName;

    @Column(nullable = false)
    private int stock;

    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    protected Medicine() {
    }

    public Medicine(String medicineId, String medicineName, int stock) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.stock = stock;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public int getStock() {
        return stock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}

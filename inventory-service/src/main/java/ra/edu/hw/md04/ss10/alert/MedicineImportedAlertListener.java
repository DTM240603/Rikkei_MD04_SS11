package ra.edu.hw.md04.ss10.alert;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ra.edu.hw.md04.ss10.event.MedicineImportedEvent;

@Component
public class MedicineImportedAlertListener {
    private final AlertPublisher alertPublisher;

    public MedicineImportedAlertListener(AlertPublisher alertPublisher) {
        this.alertPublisher = alertPublisher;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onMedicineImported(MedicineImportedEvent event) {
        alertPublisher.publish(
            AlertChannel.PHARMACY_ALERTS,
            new AlertMessage("IMPORT", "Đã nhập " + event.quantity() + " hộp " + event.medicineName())
        );
    }
}

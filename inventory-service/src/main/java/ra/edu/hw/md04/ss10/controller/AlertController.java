package ra.edu.hw.md04.ss10.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ra.edu.hw.md04.ss10.alert.AlertChannel;
import ra.edu.hw.md04.ss10.alert.AlertMessage;
import ra.edu.hw.md04.ss10.alert.AlertPublisher;

@RestController
@RequestMapping("/api/v1/alerts")
public class AlertController {
    private final AlertPublisher alertPublisher;

    public AlertController(AlertPublisher alertPublisher) {
        this.alertPublisher = alertPublisher;
    }

    @PostMapping
    public AlertResponse publish(@RequestBody AlertRequest request) {
        if (request == null || request.topicListen() == null
            || !AlertChannel.PHARMACY_ALERTS.equals(request.topicListen())
            || request.type() == null || request.type().isBlank()
            || request.message() == null || request.message().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A valid channel, type, and message are required");
        }

        alertPublisher.publish(request.topicListen(), new AlertMessage(request.type(), request.message()));
        return new AlertResponse("Đã gửi thông báo đến kênh: " + request.topicListen());
    }

    public record AlertRequest(String topicListen, String type, String message) {
    }

    public record AlertResponse(String message) {
    }
}

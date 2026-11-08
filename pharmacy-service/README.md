# Pharmacy Service - Kafka Producer

## Run

Start Kafka from the repository root, then start this service:

```powershell
docker compose up -d
cd pharmacy-service
.\gradlew.bat bootRun
```

Kafka connects at `localhost:9092`, configured in `src/main/resources/application.yaml`.

## Submit an order

Send a `POST` request to `http://localhost:8080/api/orders/checkout`:

```json
{
  "orderId": "ORD-1001",
  "medicineId": "MED-001",
  "quantity": 12
}
```

The service creates an `OrderEvent` with a server-generated ISO-8601 timestamp and publishes it as JSON to `medicine-stock-events`. The Kafka message key is `medicineId`, so events for the same medicine are routed to the same partition. The API responds with payment success only after Kafka acknowledges the event.

Check the event in Kafka:

```bash
docker exec kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic medicine-stock-events --from-beginning --property print.key=true
```

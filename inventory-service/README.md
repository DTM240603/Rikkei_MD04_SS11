# Inventory Service - Kafka Consumer

Inventory service consumes JSON `OrderEvent` messages from `medicine-stock-events` and deducts the ordered amount from PostgreSQL stock. The Kafka group is `inventory-service-stock-consumer`; all instances in that group share the partitions. Processing and recording the order happen in one database transaction, and the order ID prevents a redelivered event from deducting stock twice.

## Run

From the repository root, start Kafka and PostgreSQL:

```powershell
docker compose up -d
```

Then start the service:

```powershell
cd inventory-service
.\gradlew.bat bootRun
```

The default PostgreSQL connection is `localhost:5433`, database `inventory_db`, username `inventory`. Set `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` to override these values. `data.sql` inserts sample medicines without resetting existing stock.

## Verify

Submit an order through `pharmacy-service`, then inspect the database:

```powershell
docker exec inventory-postgres psql -U inventory -d inventory_db -c "SELECT medicine_id, medicine_name, stock FROM medicines ORDER BY medicine_id;"
docker exec inventory-postgres psql -U inventory -d inventory_db -c "SELECT order_id, medicine_id, quantity, timestamp FROM processed_orders ORDER BY order_id;"
```

Kafka consumer settings, including the JSON deserializer and group ID, are in `src/main/resources/application.yaml`.

Run tests with:

```powershell
.\gradlew.bat test
```

# Kafka Topic Setup Exercise

This project sets up a minimal Kafka cluster with ZooKeeper and creates the required topics for the pharmacy domain.

## Requirements

- Docker Engine
- Docker Compose

## Start the infrastructure

```bash
docker compose up -d
```

The inventory service uses Redis to cache medicine details. Start the service
with Redis available on `localhost:6379` and PostgreSQL on `localhost:5433`:

```powershell
cd inventory-service
.\gradlew.bat bootRun
```

In another terminal, request a medicine:

```powershell
Invoke-RestMethod -Uri http://localhost:8080/api/v1/inventory/medicines/MED-001
```

The first request reads the medicine from PostgreSQL; subsequent requests use
the `medicines` Redis cache, which expires after 60 minutes. The cache entry is
evicted when an order changes that medicine's stock or when its price is updated:

```powershell
Invoke-RestMethod -Method Put `
  -Uri http://localhost:8080/api/v1/inventory/medicines/MED-001/price `
  -ContentType "application/json" -Body '{"price":15000.00}'
```

Override the Redis host and port with `REDIS_HOST` and `REDIS_PORT`.

## Redis Pub/Sub alerts

The inventory service subscribes to the Redis channel `pharmacy-alerts`. Publish
an alert with:

```powershell
Invoke-RestMethod -Method Post `
  -Uri http://localhost:8080/api/v1/alerts `
  -ContentType "application/json" `
  -Body '{"topicListen":"pharmacy-alerts","type":"IMPORT","message":"Đã nhập 100 hộp Panadol"}'
```

The API publishes the JSON message immediately and the service prints received
alerts to its console. Redis Pub/Sub is fire-and-forget; messages are not retained
for subscribers that are offline.

Import stock to update the database and automatically publish an `IMPORT` alert
after the transaction commits:

```powershell
Invoke-RestMethod -Method Put `
  -Uri http://localhost:8080/api/v1/inventory/medicines/MED-001/stock/import `
  -ContentType "application/json" -Body '{"quantity":100}'
```

## Create topics

```bash
bash scripts/create-topics.sh
```

## Topics created

- `medicine-stock-events` — 3 partitions, replication factor 1
- `medicine-price-updates` — 1 partition, replication factor 1
- `pharmacy-notifications` — 2 partitions, replication factor 1

## Check from the CLI

```bash
docker exec kafka /opt/bitnami/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
docker exec kafka /opt/bitnami/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic medicine-stock-events
```

## Stop the stack

```bash
docker compose down -v
```

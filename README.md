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
the `medicines` Redis cache. The cache entry is evicted when an order changes
that medicine's stock. Override the Redis host and port with `REDIS_HOST` and
`REDIS_PORT`.

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

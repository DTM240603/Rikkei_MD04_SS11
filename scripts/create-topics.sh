#!/usr/bin/env bash
set -euo pipefail

BOOTSTRAP_SERVER="localhost:9092"
TOPICS=(
  "medicine-stock-events:3:1"
  "medicine-price-updates:1:1"
  "pharmacy-notifications:2:1"
)

for topic in "${TOPICS[@]}"; do
  IFS=':' read -r name partitions replication <<< "$topic"
  docker exec kafka kafka-topics \
    --bootstrap-server "$BOOTSTRAP_SERVER" \
    --create \
    --topic "$name" \
    --partitions "$partitions" \
    --replication-factor "$replication" \
    --if-not-exists

done

echo "Listing topics..."
docker exec kafka kafka-topics --bootstrap-server "$BOOTSTRAP_SERVER" --list

echo "Describing topics..."
docker exec kafka kafka-topics --bootstrap-server "$BOOTSTRAP_SERVER" --describe --topic medicine-stock-events
docker exec kafka kafka-topics --bootstrap-server "$BOOTSTRAP_SERVER" --describe --topic medicine-price-updates
docker exec kafka kafka-topics --bootstrap-server "$BOOTSTRAP_SERVER" --describe --topic pharmacy-notifications
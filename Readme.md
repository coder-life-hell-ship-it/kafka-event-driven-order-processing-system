# Event-Driven Order Processing System

A Spring Boot + Apache Kafka event-driven microservice project demonstrating asynchronous service communication, Kafka producers/consumers, partitions, consumer groups, offsets, lag, manual acknowledgment, retries, Dead Letter Topic (DLT), and idempotent message processing.

---

## 1. Project Overview

This project simulates an order-processing workflow using three independent Spring Boot services.

### Services

1. **Order Service**

   * Exposes REST API to create an order.
   * Publishes `OrderCreatedEvent` to Kafka.

2. **Inventory Service**

   * Consumes `OrderCreatedEvent`.
   * Checks inventory availability.
   * Publishes `InventoryProcessedEvent`.

3. **Notification Service**

   * Consumes `InventoryProcessedEvent`.
   * Simulates notification processing.
   * Uses manual acknowledgment.
   * Implements retry and Dead Letter Topic handling.
   * Uses persistent idempotency to avoid duplicate processing.

---

## 2. Architecture


                        POST /orders
                             |
                             v
                    +-------------------+
                    |   Order Service   |
                    |    Port: 8081     |
                    +---------+---------+
                              |
                              | OrderCreatedEvent
                              v
                    +-------------------+
                    |   Kafka Topic     |
                    |   order-events    |
                    +---------+---------+
                              |
                              v
                  +-----------------------+
                  |   Inventory Service   |
                  |      Port: 8082       |
                  +----------+------------+
                             |
                             | InventoryProcessedEvent
                             v
                  +-----------------------+
                  |   Kafka Topic         |
                  |   inventory-events    |
                  +----------+------------+
                             |
                             v
                +---------------------------+
                |    Notification Service   |
                |         Port: 8083        |
                +-------------+-------------+
                              |
                    +---------+---------+
                    |                   |
                  Success             Failure
                    |                   |
                    v                   v
                H2 Database          Retry
                                        |
                                        v
                              inventory-events.DLT


---

## 3. Technology Stack

* Java 11
* Spring Boot 2.7.18
* Spring Kafka
* Apache Kafka 2.8.2
* Apache ZooKeeper
* Maven
* H2 Database
* Spring Data JPA
* Postman

---

## 4. Kafka Topics

### `order-events`

Produced by:


Order Service


Consumed by:


Inventory Service


Configuration:


Partitions: 3
Replication Factor: 1


---

### `inventory-events`

Produced by:


Inventory Service


Consumed by:


Notification Service


Configuration:


Partitions: 3
Replication Factor: 1


---

### `inventory-events.DLT`

Used for records that continue to fail after the configured retry attempts.

Configuration:


Partitions: 3
Replication Factor: 1


---

## 5. Event Flow

### Order Created


POST /orders
     |
     v
OrderController
     |
     v
OrderService
     |
     v
OrderCreatedEvent
     |
     v
OrderProducer
     |
     v
order-events


### Inventory Processing


order-events
     |
     v
OrderConsumer
     |
     v
InventoryService
     |
     +--> IN_STOCK
     |
     +--> OUT_OF_STOCK
     |
     v
InventoryProcessedEvent
     |
     v
InventoryProducer
     |
     v
inventory-events


### Notification Processing


inventory-events
     |
     v
NotificationConsumer
     |
     v
NotificationService
     |
     v
H2 processed_events table


---

## 6. Kafka Consumer Groups

### Inventory Service


Consumer Group: inventory-group
Topic: order-events


### Notification Service


Consumer Group: notification-group
Topic: inventory-events


### Audit demonstration

A separate:


audit-group


was used to demonstrate that different consumer groups independently consume the same topic.

---

## 7. Kafka Concepts Demonstrated

This project practically demonstrates:

* Kafka Producer
* Kafka Consumer
* Topics
* Partitions
* Message keys
* Ordering within a partition
* Consumer groups
* Partition assignment
* Consumer offsets
* Consumer lag
* Consumer restart and catch-up
* Manual acknowledgment
* Retry handling
* Dead Letter Topic
* Idempotent consumer
* Persistent duplicate detection
* Event-driven microservice communication

---

## 8. Message Key

The `orderId` is used as the Kafka message key.

Example:


Key = ORD-1001


This allows events for the same order to be routed consistently to a partition while different orders can be processed in parallel across partitions.

Ordering is guaranteed within a partition, not globally across the topic.

---

## 9. Inventory Business Rule

For this learning project, inventory is simulated with the following rule:


quantity <= 10
    -> IN_STOCK

quantity > 10
    -> OUT_OF_STOCK


This can later be replaced by a real inventory database or external inventory system.

---

## 10. Manual Acknowledgment

Notification Service uses:

properties
spring.kafka.listener.ack-mode=manual


The consumer acknowledges a message only after successful processing:

java
acknowledgment.acknowledge();


This demonstrates explicit control over message acknowledgment and offset progress.

---

## 11. Retry Handling

Notification Service uses a fixed retry policy:


Initial attempt
     |
Retry #1
     |
Retry #2


The configured backoff is:


1 second


between attempts.

---

## 12. Dead Letter Topic

When processing continues to fail after the configured retry attempts, the failed record is published to:


inventory-events.DLT


Flow:


inventory-events
       |
     failure
       |
     retry
       |
     retry
       |
retry exhausted
       |
       v
inventory-events.DLT


The DLT allows failed records to be inspected and potentially reprocessed later.

---

## 13. Idempotency

Notification Service stores processed event IDs in the H2 table:


processed_events


Example:


event_id
-------------------
INV-ORD-IDEMP-001


Before processing a message:


Check eventId
     |
     +-- Exists -> Skip
     |
     +-- Not exists -> Process -> Save eventId


This protects the notification process from duplicate Kafka deliveries.

The database is file-based:

properties
spring.datasource.url=jdbc:h2:file:./data/notificationdb


Therefore processed event information remains available after application restart.

---

## 14. Important Error Scenarios Tested

### Consumer Downtime

Notification Service was stopped while new events were produced.

Result:


Messages remained in Kafka
        |
Consumer restarted
        |
Messages consumed
        |
Lag reduced toward 0


### Retry

A controlled processing failure was introduced.

Result:


Failure
  -> Retry
  -> Retry
  -> Retry exhausted


### DLT

The permanently failing record was successfully published to:


inventory-events.DLT


### Duplicate Event

The same logical event was published again.

Result:


First event  -> processed
Second event -> detected as duplicate and skipped


The duplicate protection was also verified after Notification Service restart.

---

## 15. Useful Kafka Commands

### List topics

cmd
bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092


### Describe a topic

cmd
bin\windows\kafka-topics.bat --describe --bootstrap-server localhost:9092 --topic order-events


### Describe a consumer group

cmd
bin\windows\kafka-consumer-groups.bat --bootstrap-server localhost:9092 --describe --group notification-group


### Consume from a topic

cmd
bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic order-events --from-beginning


### Consume DLT messages

cmd
bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic inventory-events.DLT --from-beginning


---

## 16. Example API

### Create Order

http
POST http://localhost:8081/orders
Content-Type: application/json


Request:

json
{
  "orderId": "ORD-1001",
  "customerId": "CUS-101",
  "productId": "PROD-501",
  "quantity": 2
}


Response:

json
{
  "orderId": "ORD-1001",
  "customerId": "CUS-101",
  "productId": "PROD-501",
  "quantity": 2,
  "status": "CREATED"
}


---

## 17. How to Run

### 1. Start ZooKeeper

cmd
bin\windows\zookeeper-server-start.bat config\zookeeper.properties


### 2. Start Kafka

cmd
bin\windows\kafka-server-start.bat config\server.properties


### 3. Start Order Service

cmd
mvn spring-boot:run


### 4. Start Inventory Service

cmd
mvn spring-boot:run


### 5. Start Notification Service

cmd
mvn spring-boot:run


Run each Spring Boot service from its own terminal.

---

## 18. Project Learning Outcomes

This project demonstrates how multiple Spring Boot services can communicate asynchronously using Kafka without directly calling each other through REST.

It also demonstrates how Kafka supports:

* scalable partitioned processing
* consumer-group based parallelism
* offset-based recovery
* retry handling
* dead-letter processing
* duplicate protection
* resilient asynchronous workflows

---

## 19. Future Improvements

Possible production-level extensions include:

* PostgreSQL instead of H2
* Schema Registry
* Avro or Protobuf
* Kafka security with SSL/SASL
* Transactional event processing
* Outbox pattern
* External notification provider
* Dockerized Kafka and services
* Monitoring with Prometheus/Grafana
* CI/CD with Jenkins

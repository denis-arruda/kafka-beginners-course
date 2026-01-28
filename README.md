
# Kafka Beginners Course



This project is a multi-module Maven Java application for learning Apache Kafka basics and integrating with OpenSearch. It includes simple producer and consumer demos, callback examples, key-based message sending, and a pipeline to stream Wikimedia changes into OpenSearch.

## Modules
- **kafka-basics**: Basic Kafka producer and consumer examples, including:
	- ProducerDemo: Simple producer
	- ProducerDemoWithCallback: Producer with callback
	- ProducerDemoKeys: Producer with keys
	- ConsumerDemo: Simple consumer
	- ConsumerDemoWithShutdown: Consumer with graceful shutdown
	- ConsumerDemoCooperative: Consumer with cooperative rebalancing
- **kafka-producer-wikimedia**: Streams Wikimedia recent changes to a Kafka topic (`wikimedia.recentchange`).
- **kafka-consumer-opensearch**: Consumes from Kafka and indexes messages into OpenSearch. Includes Docker Compose for local OpenSearch setup.

## Requirements
- Java 25+
- Maven 3.9+
- Apache Kafka (local or remote broker)
- Docker (for OpenSearch)

## Usage
### 1. Start Kafka Broker
Start your Kafka broker (default: `127.0.0.1:9092`).

### 2. Build the Project
```bash
./mvnw clean install
```

### 3. Start OpenSearch (Optional, for opensearch consumer)
```bash
docker compose -f kafka-consumer-opensearch/docker-compose.yml up -d --build
```
- OpenSearch: http://localhost:9200
- OpenSearch Dashboards: http://localhost:5601

### 4. Run Producer and Consumer Demos
#### Kafka Basics
```bash
# Producer examples
java -cp kafka-basics/target/kafka-basics-1.0-SNAPSHOT.jar com.denisarruda.kafkabasic.ProducerDemo
java -cp kafka-basics/target/kafka-basics-1.0-SNAPSHOT.jar com.denisarruda.kafkabasic.ProducerDemoWithCallback
java -cp kafka-basics/target/kafka-basics-1.0-SNAPSHOT.jar com.denisarruda.kafkabasic.ProducerDemoKeys

# Consumer examples
java -cp kafka-basics/target/kafka-basics-1.0-SNAPSHOT.jar com.denisarruda.kafkabasic.ConsumerDemo
java -cp kafka-basics/target/kafka-basics-1.0-SNAPSHOT.jar com.denisarruda.kafkabasic.ConsumerDemoWithShutdown
java -cp kafka-basics/target/kafka-basics-1.0-SNAPSHOT.jar com.denisarruda.kafkabasic.ConsumerDemoCooperative
```
- For consumers with shutdown support, use Ctrl+C to test graceful shutdown.
- Topics used: `first_topic`, `second_topic`, `third_topic` (ensure they exist in your broker).

#### Wikimedia Producer
```bash
java -cp kafka-producer-wikimedia/target/kafka-producer-wikimedia-1.0-SNAPSHOT.jar com.denisarruda.kafka.wikimedia.WikimediaChangesProducer
```
- Streams Wikimedia changes to topic `wikimedia.recentchange`.

#### OpenSearch Consumer
```bash
java -cp kafka-consumer-opensearch/target/kafka-consumer-opensearch-1.0-SNAPSHOT.jar com.denisarruda.kafka.opensearch.OpenSearchConsumer
```
- Consumes from `wikimedia.recentchange` and indexes into OpenSearch index `wikimedia`.

## Features
- Java Kafka producer with callback
- Sending messages with keys
- Multiple consumer patterns (simple, shutdown, cooperative)
- Wikimedia streaming integration
- OpenSearch consumer with bulk indexing
- Multi-module Maven structure
- JUnit 5 tests

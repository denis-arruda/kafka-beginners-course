
# Kafka Beginners Course


This project is a multi-module Maven Java application for learning Apache Kafka basics. It includes simple producer and consumer demos, callback examples, and key-based message sending.

## Modules
- **kafka-basics**: Contains basic Kafka producer and consumer examples, including:
	- ProducerDemo: Simple producer
	- ProducerDemoWithCallback: Producer with callback
	- ProducerDemoKeys: Producer with keys
	- ConsumerDemo: Simple consumer
	- ConsumerDemoWithShutdown: Consumer with graceful shutdown
	- ConsumerDemoCooperative: Consumer with cooperative rebalancing

## Requirements
- Java 25+
- Maven 3.9+
- Apache Kafka (local or remote broker)

## Usage
1. Start your Kafka broker (default: `127.0.0.1:9092`).
2. Build the project:
	```bash
	./mvnw clean install
	```
3. Run producer or consumer demos (replace `ClassName` as needed):
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

## Features
- Java Kafka producer with callback
- Sending messages with keys
- Multiple consumer patterns (simple, shutdown, cooperative)
- Multi-module Maven structure
- JUnit 5 tests
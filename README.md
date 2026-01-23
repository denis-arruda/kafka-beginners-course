
# Kafka Beginners Course

This project is a multi-module Maven Java application for learning Apache Kafka basics. It includes simple producer demos and callback examples.

## Modules
- **kafka-basics**: Contains basic Kafka producer examples, including sending messages with callbacks and keys.

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
3. Run producer demos:
	```bash
	# Example: Run ProducerDemoWithCallback
	java -cp kafka-basics/target/kafka-basics-1.0-SNAPSHOT.jar com.denisarruda.kafkabasic.ProducerDemoWithCallback
	# Example: Run ProducerDemoKeys
	java -cp kafka-basics/target/kafka-basics-1.0-SNAPSHOT.jar com.denisarruda.kafkabasic.ProducerDemoKeys
	```

## Features
- Java Kafka producer with callback
- Sending messages with keys
- Multi-module Maven structure
- JUnit 5 tests

## Contributing
Feel free to fork and submit pull requests for improvements or new Kafka examples.

## License
MIT

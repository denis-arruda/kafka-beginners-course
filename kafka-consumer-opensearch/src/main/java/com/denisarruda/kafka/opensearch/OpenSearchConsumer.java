package com.denisarruda.kafka.opensearch;

import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.opensearch.action.admin.indices.create.CreateIndexRequest;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.indices.GetIndexRequest;
import org.opensearch.common.xcontent.XContentType;
import org.slf4j.Logger;

import com.google.gson.JsonParser;

public class OpenSearchConsumer {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(OpenSearchConsumer.class);

    public static RestHighLevelClient createOpenSearchClient() {
        String connString = "http://localhost:9200";
//        String connString = "https://c9p5mwld41:45zeygn9hy@kafka-course-2322630105.eu-west-1.bonsaisearch.net:443";

        // we build a URI from the connection string
        RestHighLevelClient restHighLevelClient;
        URI connUri = URI.create(connString);
        // extract login information if it exists
        String userInfo = connUri.getUserInfo();

        if (userInfo == null) {
            // REST client without security
            restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost(connUri.getHost(), connUri.getPort(), "http")));

        } else {
            // REST client with security
            String[] auth = userInfo.split(":");

            CredentialsProvider cp = new BasicCredentialsProvider();
            cp.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(auth[0], auth[1]));

            restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(new HttpHost(connUri.getHost(), connUri.getPort(), connUri.getScheme()))
                            .setHttpClientConfigCallback(
                                    httpAsyncClientBuilder -> httpAsyncClientBuilder.setDefaultCredentialsProvider(cp)
                                            .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())));


        }

        return restHighLevelClient;
    }
    public static void main(String[] args) throws IOException {
        RestHighLevelClient openSearchClient = createOpenSearchClient();

        KafkaConsumer<String, String> consumer = createKafkaConsumer();
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("wikimedia");
        try (openSearchClient; consumer){

            boolean indexExists = openSearchClient.indices().exists(new GetIndexRequest("wikimedia"), RequestOptions.DEFAULT);

            if (!indexExists) {
                openSearchClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
                log.info("Index created");
            } else {
                log.info("Index already exists");
            }

            consumer.subscribe(java.util.Collections.singletonList("wikimedia.recentchange"));
            while (true) {
                try {
                    ConsumerRecords<String, String> records = consumer.poll(java.time.Duration.ofMillis(3000));
                    int recordCount = records.count();
                    log.info("Received {} records", recordCount);

                    for (var record : records) {
                        String id = extractIdFromRecord(record.value());
                        IndexRequest indexRequest = new IndexRequest("wikimedia")
                                .source(record.value(), XContentType.JSON)
                                .id(id); // this is to make our consumer idempotent
                        IndexResponse response = openSearchClient.index(indexRequest, RequestOptions.DEFAULT);
                        log.info("Inserted 1 document into OpenSearch with id: {}", response.getId());
                    };
                } catch (IOException e) {
                }
            }
        }
    }
    private static String extractIdFromRecord(String value) {
        return JsonParser.parseString(value)
                .getAsJsonObject()
                .get("meta")
                .getAsJsonObject()
                .get("id")
                .getAsString();
    }
    private static KafkaConsumer<String, String> createKafkaConsumer() {
        log.info("Starting Kafka Consumer");

        String groupId = "consumer-opensearch-group";

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("group.id", groupId);
        properties.setProperty("auto.offset.reset", "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        

        return consumer;
    }
}

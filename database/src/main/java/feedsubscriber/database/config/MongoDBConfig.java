package feedsubscriber.database.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@SuppressWarnings("FieldCanBeLocal")
@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class MongoDBConfig {
    private final String host = "localhost";
    private final int port = 27017;
    private final String database = "admin";
    private final String username = "test";
    private final String password = "test";

    @Bean
    public MongoClient mongoClient() {
        String uri = "mongodb://" + username + ":" + password + "@" + host + ":" + port + "/" + database;
        return MongoClients.create(uri);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), database);
    }
}
package feedsubscriber.restful;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SuppressWarnings("SpellCheckingInspection")
@EnableMongoRepositories(basePackages = {"feedsubscriber.database"})
@SpringBootApplication(scanBasePackages = {"feedsubscriber.common", "feedsubscriber.database", "feedsubscriber.restful"})
public class RestfulApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestfulApplication.class, args);
    }
}

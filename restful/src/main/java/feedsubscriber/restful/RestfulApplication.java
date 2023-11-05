package feedsubscriber.restful;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SuppressWarnings("SpellCheckingInspection")
@EnableMongoRepositories(basePackages = {"feedsubscriber.common", "feedsubscriber.restful"})
@SpringBootApplication(scanBasePackages = {"feedsubscriber.common", "feedsubscriber.restful"})
public class RestfulApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestfulApplication.class, args);
    }
}

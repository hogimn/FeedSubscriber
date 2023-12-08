package feedsubscriber.gateway;

import jakarta.annotation.PostConstruct;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.config.GatewayProperties;

/**
 * Spring Boot application for the gateway service.
 */
@SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection"})
@Slf4j
@SpringBootApplication
public class GatewayApplication {

  @Autowired
  private GatewayProperties props;

  public static void main(String[] args) {
    SpringApplication.run(GatewayApplication.class,
            args);
  }

  @PostConstruct
  public void init() {
    log.info(Objects.toString(props));
  }
}

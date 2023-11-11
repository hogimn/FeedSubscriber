package feedsubscriber.collector.client;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for creating beans related to client functionality.
 */
@Component
public class ClientBeans {
  @Bean
  public RestTemplate getRestTemplate() {
    return new RestTemplate();
  }
}

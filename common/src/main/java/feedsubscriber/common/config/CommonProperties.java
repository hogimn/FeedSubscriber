package feedsubscriber.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for the common settings.
 */
@Component
@ConfigurationProperties(prefix = "common")
@Data
public class CommonProperties {
  private FrontendProperties frontend = new FrontendProperties();
  private EurekaProperties eureka = new EurekaProperties();
  private GatewayProperties gateway = new GatewayProperties();
  private RestfulProperties restful = new RestfulProperties();
  private AuthProperties auth = new AuthProperties();

  /**
   * Configuration properties for the frontend settings.
   */
  @Data
  public static class FrontendProperties {
    private int port;
    private String baseUrl;
    private String authorizedUrl;
  }

  /**
   * Configuration properties for the Eureka settings.
   */
  @Data
  public static class EurekaProperties {
    private int port;
    private String contextUrl;
  }

  /**
   * Configuration properties for the Gateway settings.
   */
  @Data
  public static class GatewayProperties {
    private int port;
    private String baseUrl;
  }

  /**
   * Configuration properties for the Restful settings.
   */
  @Data
  public static class RestfulProperties {
    private int port;
    private String baseUrl;
  }

  /**
   * Configuration properties for the Auth settings.
   */
  @Data
  public static class AuthProperties {
    private int port;
    private String baseUrl;
  }
}

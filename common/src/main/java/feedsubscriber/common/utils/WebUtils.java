package feedsubscriber.common.utils;

import java.util.Objects;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Utility class for making HTTP requests.
 */
public final class WebUtils {
  /**
   * Makes an HTTP GET request to the specified URL and returns the result as an object of
   * the given class.
   *
   * @param restTemplate The RestTemplate used to make the HTTP request.
   * @param url          The URL to which the GET request is sent.
   * @param clazz        The class of the expected response type.
   * @param <T>          The type of the response body.
   * @return The response body as an object of the specified class.
   * @throws NullPointerException if the response body is null.
   */
  public static <T> T makeGetRequestList(RestTemplate restTemplate,
                                         String url,
                                         Class<T> clazz) {
    ResponseEntity<T> responseEntity = restTemplate
            .exchange(url,
                    HttpMethod.GET,
                    null,
                    clazz);

    var result = responseEntity.getBody();
    return Objects.requireNonNull(result);
  }
}

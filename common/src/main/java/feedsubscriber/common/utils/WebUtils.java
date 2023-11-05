package feedsubscriber.common.utils;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

public final class WebUtils {
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

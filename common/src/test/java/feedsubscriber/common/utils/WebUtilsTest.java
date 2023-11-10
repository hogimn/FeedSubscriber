package feedsubscriber.common.utils;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WebUtilsTest {
    @Test
    void testMakeGetRequestList() {
        String url = "https://example.com/api";
        Class<String> responseType = String.class;

        RestTemplate restTemplateMock = mock(RestTemplate.class);

        String responseBody = "Mocked response";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(responseBody);

        when(restTemplateMock.exchange(eq(url), eq(HttpMethod.GET), any(), eq(responseType)))
                .thenReturn(responseEntity);

        String result = WebUtils.makeGetRequestList(restTemplateMock, url, responseType);

        assertEquals(responseBody, result);

        verify(restTemplateMock, times(1))
                .exchange(eq(url), eq(HttpMethod.GET), any(), eq(responseType));
    }
}

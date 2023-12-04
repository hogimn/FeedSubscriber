package feedsubscriber.restful;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import feedsubscriber.common.dto.EndpointDto;
import feedsubscriber.common.dto.RssItemDto;
import feedsubscriber.database.endpoint.Endpoint;
import feedsubscriber.database.endpoint.EndpointService;
import feedsubscriber.database.rss.RssService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("resource")
class RestfulServiceTest {
  @Mock
  private RssService rssService;

  @Mock
  private EndpointService endpointService;

  @InjectMocks
  private RestfulService restfulService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetRssItems() {
    RssItemDto rssItem1 = new RssItemDto(
        "Title 1",
        "Description 1",
        "01 Jan 2022",
        "https://example.com/1",
        "Author 1",
        "user1");
    RssItemDto rssItem2 = new RssItemDto(
        "Title 2",
        "Description 2",
        "02 Jan 2022",
        "https://example.com/2",
        "Author 2",
        "user1");

    when(rssService.findByUsernameAndSortByPubDateDesc("user1"))
        .thenReturn(Arrays.asList(rssItem1, rssItem2));

    List<RssItemDto> result = restfulService.getRssItems("user1");

    assertEquals(2, result.size());
    assertEquals("Title 1", result.get(0).getTitle());
    assertEquals("Title 2", result.get(1).getTitle());
  }

  @Test
  void testGetEndpoints() {
    EndpointDto endpoint1 = new EndpointDto("https://example.com/api1");
    EndpointDto endpoint2 = new EndpointDto("https://example.com/api2");

    when(endpointService.findByUsername("user1")).thenReturn(Arrays.asList(endpoint1, endpoint2));

    List<EndpointDto> result = restfulService.getEndpoints("user1");

    assertEquals(2, result.size());
    assertEquals("https://example.com/api1", result.get(0).getUrl());
    assertEquals("https://example.com/api2", result.get(1).getUrl());
  }

  @Test
  void testSaveEndpoint() {
    Endpoint endpoint = new Endpoint("https://example.com/api", "user1");

    restfulService.saveEndpoint(endpoint);

    verify(endpointService, times(1)).saveEndpoint(endpoint);
  }

  @Test
  void testDeleteEndpoint() {
    Endpoint endpoint = new Endpoint("https://example.com/api", "user1");

    restfulService.deleteEndpoint(endpoint);

    verify(endpointService, times(1)).delete(endpoint);
  }
}

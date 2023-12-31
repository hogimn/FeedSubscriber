package feedsubscriber.collector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import feedsubscriber.collector.jobs.CollectorJob;
import feedsubscriber.common.serialization.Channel;
import feedsubscriber.common.serialization.Item;
import feedsubscriber.common.serialization.Rss;
import feedsubscriber.database.endpoint.Endpoint;
import feedsubscriber.database.endpoint.EndpointService;
import feedsubscriber.database.rss.RssItem;
import feedsubscriber.database.rss.RssService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SuppressWarnings("resource")
class CollectorJobTest {
  private static final String FEED_URL = "https://example.com/feed";
  private static final String EXISTING_ITEM_LINK = "https://example.com/item1";
  private static final String NEW_ITEM_LINK = "https://example.com/item2";
  private static final String USERNAME = "user1";

  @Mock
  private EndpointService endpointService;

  @Mock
  private RssService rssService;

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private JobExecutionContext jobExecutionContext;

  @InjectMocks
  private CollectorJob collectorJob;

  @Captor
  private ArgumentCaptor<List<RssItem>> rssItemListCaptor;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testExecute() {
    setupMocksForExecute();

    collectorJob.execute(jobExecutionContext);

    verify(endpointService, times(1)).findAll();
    verify(restTemplate, times(1))
            .exchange(eq(FEED_URL), eq(HttpMethod.GET), eq(null), eq(Rss.class));

    verify(rssService, times(1)).findByLinkAndUsername(NEW_ITEM_LINK, USERNAME);
    verify(rssService, times(1)).saveAll(rssItemListCaptor.capture());

    List<RssItem> capturedItemList = rssItemListCaptor.getValue();
    assertEquals(1, capturedItemList.size());
    assertEquals(NEW_ITEM_LINK, capturedItemList.get(0).getLink());
  }

  private void setupMocksForExecute() {
    List<Endpoint> urls = List.of(new Endpoint(FEED_URL, USERNAME));
    when(endpointService.findAll()).thenReturn(urls);

    RssItem existingRssItem = new RssItem();
    existingRssItem.setLink(EXISTING_ITEM_LINK);
    when(rssService.findByLinkAndUsername(existingRssItem.getLink(), USERNAME))
        .thenReturn(existingRssItem);

    Item newItem = new Item();
    newItem.setLink(NEW_ITEM_LINK);

    Rss rss = new Rss();
    rss.setChannel(new Channel());
    rss.getChannel().setItem(List.of(newItem));

    ResponseEntity<Rss> mockResponseEntity = ResponseEntity.ok(rss);
    when(restTemplate.exchange(eq(FEED_URL), eq(HttpMethod.GET), eq(null), eq(Rss.class)))
            .thenReturn(mockResponseEntity);

    when(endpointService.findAllSubscribedUsersByUrl(FEED_URL)).thenReturn(List.of("user1"));
  }
}

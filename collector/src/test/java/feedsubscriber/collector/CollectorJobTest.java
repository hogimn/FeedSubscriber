package feedsubscriber.collector;

import feedsubscriber.collector.jobs.CollectorJob;
import feedsubscriber.common.serialization.Channel;
import feedsubscriber.common.serialization.Item;
import feedsubscriber.common.serialization.RSS;
import feedsubscriber.database.endpoint.EndpointService;
import feedsubscriber.database.rss.RSSItem;
import feedsubscriber.database.rss.RSSService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.quartz.JobExecutionContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SuppressWarnings("resource")
class CollectorJobTest {

    private static final String FEED_URL = "https://example.com/feed";
    private static final String EXISTING_ITEM_LINK = "https://example.com/item1";
    private static final String NEW_ITEM_LINK = "https://example.com/item2";

    @Mock
    private EndpointService endpointService;

    @Mock
    private RSSService rssService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @InjectMocks
    private CollectorJob collectorJob;

    @Captor
    private ArgumentCaptor<List<RSSItem>> rssItemListCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecute() {
        setupMocksForExecute();

        collectorJob.execute(jobExecutionContext);

        verify(endpointService, times(1)).findAllUrls();
        verify(restTemplate, times(1))
                .exchange(eq(FEED_URL), eq(HttpMethod.GET), eq(null), eq(RSS.class));

        verify(rssService, times(1)).findByLink(NEW_ITEM_LINK);
        verify(rssService, times(1)).saveAll(rssItemListCaptor.capture());

        List<RSSItem> capturedItemList = rssItemListCaptor.getValue();
        assertEquals(1, capturedItemList.size());
        assertEquals(NEW_ITEM_LINK, capturedItemList.get(0).getLink());
    }

    private void setupMocksForExecute() {
        List<String> urls = List.of(FEED_URL);
        when(endpointService.findAllUrls()).thenReturn(urls);

        RSSItem existingRssItem = new RSSItem();
        existingRssItem.setLink(EXISTING_ITEM_LINK);
        when(rssService.findByLink(existingRssItem.getLink())).thenReturn(existingRssItem);

        Item newItem = new Item();
        newItem.setLink(NEW_ITEM_LINK);

        RSS rss = new RSS();
        rss.setChannel(new Channel());
        rss.getChannel().setItem(List.of(newItem));

        ResponseEntity<RSS> mockResponseEntity = ResponseEntity.ok(rss);
        when(restTemplate.exchange(eq(FEED_URL), eq(HttpMethod.GET), eq(null), eq(RSS.class)))
                .thenReturn(mockResponseEntity);
    }
}

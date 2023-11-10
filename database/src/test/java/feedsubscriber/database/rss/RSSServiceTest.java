package feedsubscriber.database.rss;

import feedsubscriber.common.dto.RSSItemDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SuppressWarnings("resource")
class RSSServiceTest {
    @Mock
    private RSSRepository rssRepository;

    @InjectMocks
    private RSSService rssService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllAndSortByPubDateDesc() {
        RSSItem rssItem1 = new RSSItem(
                "Title 1",
                "Description 1",
                "Thu, 01 Jan 2022 12:00:00 GMT",
                "https://example.com/1",
                "Author 1");
        RSSItem rssItem2 = new RSSItem(
                "Title 2",
                "Description 2",
                "Thu, 02 Jan 2022 12:00:00 GMT",
                "https://example.com/2",
                "Author 2");

        List<RSSItem> rssItems = Arrays.asList(rssItem1, rssItem2);
        when(rssRepository.findAll()).thenReturn(rssItems);

        List<RSSItemDTO> result = rssService.findAllAndSortByPubDateDesc();

        assertEquals(2, result.size());
        assertEquals("Title 2", result.get(0).getTitle());
        assertEquals("Title 1", result.get(1).getTitle());
    }

    @Test
    void testFindByLink() {
        String link = "https://example.com/1";
        RSSItem rssItem = new RSSItem("Title 1",
                "Description 1",
                "Thu, 01 Jan 2022 12:00:00 GMT",
                link,
                "Author 1");

        when(rssRepository.findByLink(link)).thenReturn(rssItem);

        RSSItem result = rssService.findByLink(link);

        assertEquals(rssItem, result);
    }

    @Test
    void testSaveAll() {
        RSSItem rssItem1 = new RSSItem("Title 1",
                "Description 1",
                "Thu, 01 Jan 2022 12:00:00 GMT",
                "https://example.com/1",
                "Author 1");
        RSSItem rssItem2 = new RSSItem(
                "Title 2",
                "Description 2",
                "Thu, 02 Jan 2022 12:00:00 GMT",
                "https://example.com/2",
                "Author 2");

        List<RSSItem> rssItems = Arrays.asList(rssItem1, rssItem2);

        rssService.saveAll(rssItems);

        verify(rssRepository, times(1)).saveAll(rssItems);
    }
}

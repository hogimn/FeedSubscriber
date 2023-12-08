package feedsubscriber.database.rss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import feedsubscriber.common.dto.RssItemDto;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("resource")
class RssServiceTest {
  @Mock
  private RssRepository rssRepository;

  @InjectMocks
  private RssService rssService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testFindByUsernameAndSortByPubDateDesc() {
    RssItem rssItem1 = new RssItem(
        "Title 1",
        "Description 1",
        "Thu, 01 Jan 2022 12:00:00 GMT",
        "https://example.com/1",
        "Author 1",
        "user1");
    RssItem rssItem2 = new RssItem(
        "Title 2",
        "Description 2",
        "Thu, 02 Jan 2022 12:00:00 GMT",
        "https://example.com/2",
        "Author 2",
        "user1");

    List<RssItem> rssItems = Arrays.asList(rssItem1, rssItem2);
    when(rssRepository.findByUsername("user1")).thenReturn(rssItems);

    List<RssItemDto> result = rssService.findByUsernameAndSortByPubDateDesc("user1");

    assertEquals(2, result.size());
    assertEquals("Title 2", result.get(0).getTitle());
    assertEquals("Title 1", result.get(1).getTitle());
  }

  @Test
  void testFindByLink() {
    String link = "https://example.com/1";
    RssItem rssItem = new RssItem(
        "Title 1",
        "Description 1",
        "Thu, 01 Jan 2022 12:00:00 GMT",
        link,
        "Author 1",
        "user1");

    when(rssRepository.findByLink(link)).thenReturn(rssItem);

    RssItem result = rssService.findByLink(link);

    assertEquals(rssItem, result);
  }

  @Test
  void testSaveAll() {
    RssItem rssItem1 = new RssItem(
        "Title 1",
        "Description 1",
        "Thu, 01 Jan 2022 12:00:00 GMT",
        "https://example.com/1",
        "Author 1",
        "user1");
    RssItem rssItem2 = new RssItem(
        "Title 2",
        "Description 2",
        "Thu, 02 Jan 2022 12:00:00 GMT",
        "https://example.com/2",
        "Author 2",
        "user1");

    List<RssItem> rssItems = Arrays.asList(rssItem1, rssItem2);

    rssService.saveAll(rssItems);

    verify(rssRepository, times(1)).saveAll(rssItems);
  }
}

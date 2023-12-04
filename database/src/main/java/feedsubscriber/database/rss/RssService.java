package feedsubscriber.database.rss;

import feedsubscriber.common.dto.RssItemDto;
import feedsubscriber.database.endpoint.Endpoint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing RSS feed items.
 */
@SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection", "UnusedReturnValue"})
@Service
public class RssService {
  @Autowired
  RssRepository rssRepository;

  /**
   * Retrieves all RSS feed items and sorts them by publication date in descending order.
   *
   * @return List of RssItemDto objects representing RSS feed items sorted by publication date.
   */
  public List<RssItemDto> findByUsernameAndSortByPubDateDesc(String username) {
    SimpleDateFormat inputDateFormat = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);

    List<RssItemDto> rssList = new ArrayList<>(rssRepository.findByUsername(username)
            .stream()
            .map(r -> new RssItemDto(
                r.getTitle(),
                r.getDescription(),
                r.getPubDate(),
                r.getLink(),
                r.getAuthor(),
                r.getUsername()))
            .toList());

    rssList.sort((r1, r2) -> {
      Date d1;
      Date d2;
      try {
        d1 = inputDateFormat.parse(r1.getPubDate());
        d2 = inputDateFormat.parse(r2.getPubDate());
      } catch (ParseException e) {
        throw new RuntimeException(e);
      }
      return d2.compareTo(d1);
    });

    return rssList;
  }

  public RssItem findByLink(String link) {
    return rssRepository.findByLink(link);
  }

  public void saveAll(List<RssItem> rss) {
    rssRepository.saveAll(rss);
  }

  public void deleteAllByEndpoint(Endpoint endpoint) {
    rssRepository.deleteAllByEndpoint(endpoint);
  }
}

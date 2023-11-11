package feedsubscriber.database.rss;

import feedsubscriber.common.serialization.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents an RSS feed item stored in the MongoDB database.
 */
@Document(collection = "rss")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RssItem {
  String title;
  String description;
  String pubDate;
  String link;
  String author;

  /**
   * Constructs an RssItem using information from an external Item object.
   *
   * @param item The external Item object to extract information from.
   */
  public RssItem(Item item) {
    title = item.getTitle();
    description = item.getDescription();
    pubDate = item.getPubDate();
    link = item.getLink();
    author = item.getAuthor();
  }
}

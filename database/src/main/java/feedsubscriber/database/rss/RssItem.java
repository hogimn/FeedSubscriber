package feedsubscriber.database.rss;

import feedsubscriber.common.serialization.Item;
import feedsubscriber.database.endpoint.Endpoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Represents an RSS feed item stored in the MongoDB database.
 */
@Document(collection = "rss")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class RssItem {
  @NonNull
  String title;
  @NonNull
  String description;
  @NonNull
  String pubDate;
  @NonNull
  String link;
  @NonNull
  String author;

  @DBRef
  @Field("endpoint")
  private Endpoint endpoint;

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

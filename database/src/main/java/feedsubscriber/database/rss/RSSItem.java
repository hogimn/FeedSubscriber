package feedsubscriber.database.rss;

import feedsubscriber.common.serialization.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rss")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RSSItem {
    String title;
    String description;
    String pubDate;
    String link;
    String author;

    public RSSItem(Item item) {
        title = item.getTitle();
        description = item.getDescription();
        pubDate = item.getPubDate();
        link = item.getLink();
        author = item.getAuthor();
    }
}

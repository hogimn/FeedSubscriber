package feedsubscriber.common.db.rss;

import feedsubscriber.common.serialization.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rss")
@Getter
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

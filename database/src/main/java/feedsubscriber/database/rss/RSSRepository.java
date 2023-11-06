package feedsubscriber.database.rss;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RSSRepository extends MongoRepository<RSSItem, String> {
    RSSItem findByLink(String link);
}

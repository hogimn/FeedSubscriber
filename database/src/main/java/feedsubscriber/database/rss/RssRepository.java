package feedsubscriber.database.rss;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository interface for managing RSS feed items in the MongoDB database.
 */
public interface RssRepository extends MongoRepository<RssItem, String> {
  RssItem findByLink(String link);
}

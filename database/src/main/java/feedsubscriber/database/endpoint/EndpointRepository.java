package feedsubscriber.database.endpoint;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository interface for managing Endpoint entities in the MongoDB database.
 */
public interface EndpointRepository extends MongoRepository<Endpoint, String> {
  Endpoint findByUrl(String url);

  void deleteByUrl(String url);
}

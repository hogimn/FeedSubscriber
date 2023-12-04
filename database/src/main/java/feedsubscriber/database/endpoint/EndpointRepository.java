package feedsubscriber.database.endpoint;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository interface for managing Endpoint entities in the MongoDB database.
 */
public interface EndpointRepository extends MongoRepository<Endpoint, String> {
  Endpoint findByUrl(String url);

  List<Endpoint> findByUsername(String username);

  void deleteByUrl(String url);
}

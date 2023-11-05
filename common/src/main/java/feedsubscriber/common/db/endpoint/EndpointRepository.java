package feedsubscriber.common.db.endpoint;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EndpointRepository extends MongoRepository<Endpoint, String> {
    void deleteByUrl(String url);
}

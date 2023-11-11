package feedsubscriber.database.endpoint;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents an endpoint entity stored in the MongoDB database.
 */
@Document(collection = "endpoint")
@Getter
@AllArgsConstructor
public class Endpoint {
  @Id
  String url;
}

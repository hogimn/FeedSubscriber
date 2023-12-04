package feedsubscriber.database.endpoint;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents an endpoint entity stored in the MongoDB database.
 */
@SuppressWarnings("unused")
@Document(collection = "endpoint")
@Getter
@RequiredArgsConstructor
public class Endpoint {
  @Id
  private ObjectId id;
  @NonNull
  String url;
  @NonNull
  String username;
}

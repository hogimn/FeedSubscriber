package feedsubscriber.database.endpoint;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "endpoint")
@Getter
@AllArgsConstructor
public class Endpoint {
    @Id
    String url;
}

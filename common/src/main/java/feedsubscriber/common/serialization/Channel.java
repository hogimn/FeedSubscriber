package feedsubscriber.common.serialization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a channel containing a list of items.
 * This class is used for deserialization of XML data using Jackson.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Channel {
  @JacksonXmlElementWrapper(useWrapping = false)
  List<Item> item;
}

package feedsubscriber.common.serialization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents an RSS feed containing a channel.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rss {
  String id;

  @JacksonXmlProperty(isAttribute = true)
  Channel channel;
}

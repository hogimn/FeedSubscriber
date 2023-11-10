package feedsubscriber.common.serialization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    @JacksonXmlProperty(isAttribute = true, localName = "title")
    String title;

    @JacksonXmlProperty(isAttribute = true, localName = "description")
    String description;

    @JacksonXmlProperty(isAttribute = true, localName = "pubDate")
    String pubDate;

    @JacksonXmlProperty(isAttribute = true, localName = "link")
    String link;

    @JacksonXmlProperty(isAttribute = true, localName = "author")
    String author;
}

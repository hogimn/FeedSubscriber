package feedsubscriber.common.serialization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Channel {
    @JacksonXmlElementWrapper(useWrapping = false)
    List<Item> item;
}

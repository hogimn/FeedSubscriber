package feedsubscriber.common.serialization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RSS {
    String id;

    @JacksonXmlProperty(isAttribute = true)
    Channel channel;
}

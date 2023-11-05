package feedsubscriber.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RSSItemDTO {
    String title;
    String description;
    String pubDate;
    String link;
    String author;
}
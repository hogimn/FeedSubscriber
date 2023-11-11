package feedsubscriber.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing an RSS feed item.
 */
@Getter
@Setter
@AllArgsConstructor
public class RssItemDto {
  String title;
  String description;
  String pubDate;
  String link;
  String author;
}
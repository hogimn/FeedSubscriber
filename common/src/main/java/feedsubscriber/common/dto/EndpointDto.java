package feedsubscriber.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing an endpoint URL.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EndpointDto {
  String url;
}
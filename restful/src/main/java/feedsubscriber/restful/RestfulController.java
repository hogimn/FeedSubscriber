package feedsubscriber.restful;

import feedsubscriber.common.dto.EndpointDto;
import feedsubscriber.common.dto.RssItemDto;
import feedsubscriber.database.endpoint.Endpoint;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for handling RESTful service endpoints.
 */
@SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection", "SpellCheckingInspection"})
@RestController
public class RestfulController {
  private static final Logger logger = LoggerFactory
          .getLogger(RestfulController.class);

  @Autowired
  ApplicationContext applicationContext;

  @Autowired
  RestfulService restService;

  @GetMapping({"/", "/actuator/info"})
  ResponseEntity<String> info() {
    return ResponseEntity
            .ok(applicationContext.getId()
                    + " is alive and running on"
                    + Thread.currentThread()
                    + "\n");
  }

  /**
   * Retrieves a list of RSS feed items.
   *
   * @return List of RssItemDto representing RSS feed items.
   */
  @GetMapping("/rss_items")
  public List<RssItemDto> findRssItems(Authentication authentication) throws Exception {
    logger.info("Fetching RSS items");
    String username = "";

    if (authentication != null && authentication.isAuthenticated()) {
      username = authentication.getName();
      logger.info("Authenticated user: {}", username);
    } else {
      throw new Exception("User not authenticated");
    }

    List<RssItemDto> rssItems = restService.getRssItems(username);
    logger.info("Fetched {} RSS items", rssItems.size());
    return rssItems;
  }

  /**
   * Retrieves a list of endpoints.
   *
   * @return List of EndpointDto representing endpoints.
   */
  @GetMapping("/endpoints")
  public List<EndpointDto> findEndpoints(Authentication authentication) throws Exception {
    logger.info("Fetching endpoints");
    String username = "";

    if (authentication != null && authentication.isAuthenticated()) {
      username = authentication.getName();
      logger.info("Authenticated user: {}", username);
    } else {
      throw new Exception("User not authenticated");
    }

    List<EndpointDto> endpoints = restService.getEndpoints(username);
    logger.info("Fetched {} endpoints", endpoints.size());
    return endpoints;
  }

  /**
   * Saves a new endpoint.
   *
   * @param endpointDto The DTO representing the new endpoint.
   */
  @PostMapping("/endpoint")
  public void saveEndpoint(
      @RequestBody EndpointDto endpointDto,
      Authentication authentication
  ) throws Exception {
    logger.info("Saving endpoint: {}", endpointDto.getUrl());
    String username = "";

    if (authentication != null && authentication.isAuthenticated()) {
      username = authentication.getName();
      logger.info("Authenticated user: {}", username);
    } else {
      throw new Exception("User not authenticated");
    }

    if (endpointDto.getUrl().isEmpty()) {
      logger.info("Url is empty");
      return;
    }
    restService.saveEndpoint(new Endpoint(endpointDto.getUrl(), username));
    logger.info("Endpoint saved: {}", endpointDto.getUrl());
  }

  /**
   * Deletes an existing endpoint.
   *
   * @param endpointDto The DTO representing the endpoint to be deleted.
   */
  @DeleteMapping("/endpoint")
  public void deleteEndpoint(
      @RequestBody EndpointDto endpointDto,
      Authentication authentication
  ) throws Exception {
    String username = "";

    if (authentication != null && authentication.isAuthenticated()) {
      username = authentication.getName();
      logger.info("Authenticated user: {}", username);
    } else {
      throw new Exception("User not authenticated");
    }

    logger.info("Deleting endpoint: {}", endpointDto.getUrl());
    restService.deleteEndpoint(new Endpoint(endpointDto.getUrl(), username));
    logger.info("Endpoint deleted: {}", endpointDto.getUrl());
  }

  /**
   * Deletes all RSS items associated with a specific endpoint.
   *
   * @param endpointDto The DTO representing the endpoint.
   */
  @DeleteMapping("/rss_items")
  public void deleteAllRssByEndpoint(@RequestBody EndpointDto endpointDto) {
    logger.info("Deleting all RSS items for endpoint: {}", endpointDto.getUrl());
    restService.deleteAllRssByEndpoint(endpointDto.getUrl());
    logger.info("All RSS items deleted for endpoint: {}", endpointDto.getUrl());
  }
}

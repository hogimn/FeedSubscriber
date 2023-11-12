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
  public List<RssItemDto> findRssItems() {
    logger.info("Fetching RSS items");
    List<RssItemDto> rssItems = restService.getRssItems();
    logger.info("Fetched {} RSS items", rssItems.size());
    return rssItems;
  }

  /**
   * Retrieves a list of endpoints.
   *
   * @return List of EndpointDto representing endpoints.
   */
  @GetMapping("/endpoints")
  public List<EndpointDto> findEndpoints() {
    logger.info("Fetching endpoints");
    List<EndpointDto> endpoints = restService.getEndpoints();
    logger.info("Fetched {} endpoints", endpoints.size());
    return endpoints;
  }

  /**
   * Saves a new endpoint.
   *
   * @param endpointDto The DTO representing the new endpoint.
   */
  @PostMapping("/endpoint")
  public void saveEndpoint(@RequestBody EndpointDto endpointDto) {
    logger.info("Saving endpoint: {}", endpointDto.getUrl());
    if (endpointDto.getUrl().isEmpty()) {
      logger.info("Url is empty");
      return;
    }
    restService.saveEndpoint(new Endpoint(endpointDto.getUrl()));
    logger.info("Endpoint saved: {}", endpointDto.getUrl());
  }

  /**
   * Deletes an existing endpoint.
   *
   * @param endpointDto The DTO representing the endpoint to be deleted.
   */
  @DeleteMapping("/endpoint")
  public void deleteEndpoint(@RequestBody EndpointDto endpointDto) {
    logger.info("Deleting endpoint: {}", endpointDto.getUrl());
    restService.deleteEndpoint(new Endpoint(endpointDto.getUrl()));
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

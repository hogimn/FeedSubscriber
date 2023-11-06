package feedsubscriber.restful;

import feedsubscriber.common.dto.EndpointDTO;
import feedsubscriber.common.dto.RSSItemDTO;
import feedsubscriber.database.endpoint.Endpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
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

    @GetMapping("/rss_items")
    public List<RSSItemDTO> findRSSItems() {
        logger.info("Fetching RSS items");
        List<RSSItemDTO> rssItems = restService.getRSSItems();
        logger.info("Fetched {} RSS items", rssItems.size());
        return rssItems;
    }

    @GetMapping("/endpoints")
    public List<EndpointDTO> findEndpoints() {
        logger.info("Fetching endpoints");
        List<EndpointDTO> endpoints = restService.getEndpoints();
        logger.info("Fetched {} endpoints", endpoints.size());
        return endpoints;
    }

    @PostMapping("/endpoint")
    public void saveEndpoint(@RequestBody EndpointDTO endpointDTO) {
        logger.info("Saving endpoint: {}", endpointDTO.getUrl());
        if (endpointDTO.getUrl().isEmpty()) {
            logger.info("Url is empty");
            return;
        }
        restService.saveEndpoint(new Endpoint(endpointDTO.getUrl()));
        logger.info("Endpoint saved: {}", endpointDTO.getUrl());
    }

    @DeleteMapping("/endpoint/{url}")
    public void deleteEndpoint(@PathVariable String url) {
        logger.info("Deleting endpoint: {}", url);
        restService.deleteEndpoint(new Endpoint(url));
        logger.info("Endpoint deleted: {}", url);
    }
}

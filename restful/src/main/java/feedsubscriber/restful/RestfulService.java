package feedsubscriber.restful;

import feedsubscriber.common.dto.EndpointDto;
import feedsubscriber.common.dto.RssItemDto;
import feedsubscriber.database.endpoint.Endpoint;
import feedsubscriber.database.endpoint.EndpointService;
import feedsubscriber.database.rss.RssService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for handling RESTful service operations.
 */
@SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection", "SpellCheckingInspection"})
@Service
public class RestfulService {
  @Autowired
  RssService rssService;

  @Autowired
  EndpointService endpointService;

  public List<RssItemDto> getRssItems(String username) {
    return rssService.findByUsernameAndSortByPubDateDesc(username);
  }

  public List<EndpointDto> getEndpoints(String username) {
    return endpointService.findByUsername(username);
  }

  public void saveEndpoint(Endpoint endpoint) {
    endpointService.saveEndpoint(endpoint);
  }

  public void deleteEndpoint(Endpoint endpoint) {
    endpointService.delete(endpoint);
  }

  public void deleteAllRssByEndpoint(String url) {
    Endpoint endpoint = endpointService.findByUrl(url);
    rssService.deleteAllByEndpoint(endpoint);
  }
}

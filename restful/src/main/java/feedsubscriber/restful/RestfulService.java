package feedsubscriber.restful;

import feedsubscriber.common.db.endpoint.Endpoint;
import feedsubscriber.common.db.endpoint.EndpointService;
import feedsubscriber.common.db.rss.RSSService;
import feedsubscriber.common.dto.EndpointDTO;
import feedsubscriber.common.dto.RSSItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class RestfulService {
    @Autowired
    RSSService rssService;

    @Autowired
    EndpointService endpointService;

    public List<RSSItemDTO> getRSSItems() {
        return rssService.findAllAndSortByPubDateDesc();
    }

    public List<EndpointDTO> getEndpoints() {
        return endpointService.findAll();
    }

    public void saveEndpoint(Endpoint endpoint) {
        endpointService.saveEndpoint(endpoint);
    }

    public void deleteEndpoint(Endpoint endpoint) {
        endpointService.delete(endpoint);
    }
}

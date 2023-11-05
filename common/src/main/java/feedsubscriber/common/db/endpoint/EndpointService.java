package feedsubscriber.common.db.endpoint;

import feedsubscriber.common.dto.EndpointDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection", "UnusedReturnValue"})
@Service
public class EndpointService {
    @Autowired
    private EndpointRepository endpointRepository;

    public List<EndpointDTO> findAll() {
        return endpointRepository.findAll()
                .stream()
                .map(endpoint -> new EndpointDTO(endpoint.getUrl()))
                .toList();
    }

    public List<String> findAllUrls() {
        return endpointRepository.findAll()
                .stream()
                .map(Endpoint::getUrl)
                .toList();
    }

    public Endpoint saveEndpoint(Endpoint endpoint) {
        return endpointRepository.save(endpoint);
    }

    public void delete(Endpoint endpoint) {
        endpointRepository.deleteByUrl(endpoint.getUrl());
    }

    public void deleteAll() {
        endpointRepository.deleteAll();
    }

    public void deleteAll(List<Endpoint> endpoint) {
        endpointRepository.deleteAll(endpoint);
    }
}

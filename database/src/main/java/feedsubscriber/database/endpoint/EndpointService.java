package feedsubscriber.database.endpoint;

import feedsubscriber.common.dto.EndpointDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing Endpoint entities.
 */
@SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection", "UnusedReturnValue"})
@Service
public class EndpointService {
  @Autowired
  private EndpointRepository endpointRepository;

  /**
   * Retrieves the {@link Endpoint}s associated with the specified URL.
   *
   * @param url The URL of the endpoint to retrieve.
   * @return {@link Endpoint}s representing the endpoint with the given URL,
   *         or null if no endpoint is found.
   */
  public List<Endpoint> findByUrl(String url) {
    return endpointRepository
            .findByUrl(url);
  }

  /**
   * Retrieves all endpoints and maps them to EndpointDto objects.
   *
   * @return List of EndpointDto objects representing all endpoints.
   */
  public List<EndpointDto> findByUsername(String username) {
    return endpointRepository.findByUsername(username)
            .stream()
            .map(endpoint -> new EndpointDto(endpoint.getUrl()))
            .toList();
  }

  /**
   * Retrieves all URLs of existing endpoints.
   *
   * @return List of URLs.
   */
  public List<Endpoint> findAll() {
    return endpointRepository.findAll()
            .stream()
            .toList();
  }

  public Endpoint saveEndpoint(Endpoint endpoint) {
    return endpointRepository.save(endpoint);
  }

  public void delete(Endpoint endpoint) {
    endpointRepository.deleteByUrlAndUsername(endpoint.getUrl(), endpoint.getUsername());
  }

  public void deleteAll() {
    endpointRepository.deleteAll();
  }

  public void deleteAll(List<Endpoint> endpoint) {
    endpointRepository.deleteAll(endpoint);
  }

  /**
   * Retrieves the usernames of all users subscribed to the endpoint with the specified URL.
   *
   * @param url The URL of the endpoint for which subscribed users are to be retrieved.
   * @return List of usernames representing users subscribed to the specified endpoint.
   */
  public List<String> findAllSubscribedUsersByUrl(String url) {
    return findByUrl(url)
        .stream()
        .map(Endpoint::getUsername)
        .toList();
  }

  public Endpoint findByUrlAndUsername(String url, String username) {
    return endpointRepository.findByUrlAndUsername(url, username);
  }
}

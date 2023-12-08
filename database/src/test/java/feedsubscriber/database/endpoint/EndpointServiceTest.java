package feedsubscriber.database.endpoint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import feedsubscriber.common.dto.EndpointDto;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("resource")
class EndpointServiceTest {
  @Mock
  private EndpointRepository endpointRepository;

  @InjectMocks
  private EndpointService endpointService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testFindAllUrls() {
    when(endpointRepository.findAll())
            .thenReturn(Arrays.asList(
                    new Endpoint("https://example.com/api1", "user1"),
                    new Endpoint("https://example.com/api2", "user1")));

    List<Endpoint> endpoints = endpointService.findAll();

    assertEquals(2, endpoints.size());
    assertEquals(
        Arrays.asList("https://example.com/api1", "https://example.com/api2"),
        Arrays.asList(endpoints.get(0).getUrl(), endpoints.get(1).getUrl()));
    verify(endpointRepository, times(1)).findAll();
  }

  @Test
  void testSaveEndpoint() {
    Endpoint endpointToSave = new Endpoint("https://example.com/api", "user1");

    endpointService.saveEndpoint(endpointToSave);

    verify(endpointRepository, times(1)).save(endpointToSave);
  }

  @Test
  void testDelete() {
    Endpoint endpointToDelete = new Endpoint("https://example.com/api", "user1");

    endpointService.delete(endpointToDelete);

    verify(endpointRepository, times(1)).deleteByUrl(endpointToDelete.getUrl());
  }

  @Test
  void testDeleteAll() {
    endpointService.deleteAll();
    verify(endpointRepository, times(1)).deleteAll();
  }

  @Test
  void testDeleteAllWithList() {
    List<Endpoint> endpointsToDelete = Arrays.asList(
            new Endpoint("https://example.com/api1", "user1"),
            new Endpoint("https://example.com/api2", "user1"));

    endpointService.deleteAll(endpointsToDelete);

    verify(endpointRepository, times(1)).deleteAll(endpointsToDelete);
  }

  @Test
  void testFindByUsername() {
    when(endpointRepository.findByUsername("user1"))
            .thenReturn(Arrays.asList(
                    new Endpoint("https://example.com/api1", "user1"),
                    new Endpoint("https://example.com/api2", "user1")));

    List<EndpointDto> endpointDtoList = endpointService.findByUsername("user1");

    assertEquals(2, endpointDtoList.size());
    verify(endpointRepository, times(1)).findByUsername("user1");
  }
}

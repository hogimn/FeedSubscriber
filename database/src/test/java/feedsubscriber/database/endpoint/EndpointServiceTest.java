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
                    new Endpoint("https://example.com/api1"),
                    new Endpoint("https://example.com/api2")));

    List<String> urls = endpointService.findAllUrls();

    assertEquals(2, urls.size());
    assertEquals(Arrays.asList("https://example.com/api1", "https://example.com/api2"), urls);
    verify(endpointRepository, times(1)).findAll();
  }

  @Test
  void testSaveEndpoint() {
    Endpoint endpointToSave = new Endpoint("https://example.com/api");

    endpointService.saveEndpoint(endpointToSave);

    verify(endpointRepository, times(1)).save(endpointToSave);
  }

  @Test
  void testDelete() {
    Endpoint endpointToDelete = new Endpoint("https://example.com/api");

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
            new Endpoint("https://example.com/api1"),
            new Endpoint("https://example.com/api2"));

    endpointService.deleteAll(endpointsToDelete);

    verify(endpointRepository, times(1)).deleteAll(endpointsToDelete);
  }

  @Test
  void testFindAll() {
    when(endpointRepository.findAll())
            .thenReturn(Arrays.asList(
                    new Endpoint("https://example.com/api1"),
                    new Endpoint("https://example.com/api2")));

    List<EndpointDto> endpointDtoList = endpointService.findAll();

    assertEquals(2, endpointDtoList.size());
    verify(endpointRepository, times(1)).findAll();
  }
}

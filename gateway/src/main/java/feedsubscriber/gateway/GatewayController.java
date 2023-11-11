package feedsubscriber.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for handling gateway-related endpoints.
 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@RestController
public class GatewayController {
  @Autowired
  ApplicationContext applicationContext;

  @GetMapping({"/", "/actuator/info"})
  ResponseEntity<String> info() {
    return ResponseEntity
            .ok(applicationContext.getId()
                    + " is alive and running on"
                    + Thread.currentThread()
                    + "\n");
  }
}

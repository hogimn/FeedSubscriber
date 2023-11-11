package feedsubscriber.collector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for handling collector-related endpoints.
 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Controller
public class CollectorController {
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

package org.naruto.framework.hello;

import lombok.extern.slf4j.Slf4j;
import org.naruto.framework.core.exception.EmServiceError;
import org.naruto.framework.core.exception.ServiceException;
import org.naruto.framework.core.web.ResultEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@Slf4j
public class GreetingController {

    private static final String template = "Hello, %s!!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/v1/greeting")
    public ResponseEntity<ResultEntity>  greeting(@RequestParam(value="name") String name) {
        if(name==null || "".equals(name)) {
            throw new ServiceException(EmServiceError.PARAMETER_VALIDATION_ERROR);
        }else {
            log.info(name + " vist the site");
            return ResponseEntity.ok(ResultEntity.ok(new Greeting(counter.incrementAndGet(),
                    String.format(template, name))));
        }
    }

}

package org.naruto.framework.hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@Slf4j
public class GreetingController {

    private static final String template = "Hello, %s!!";
    private final AtomicLong counter = new AtomicLong();


    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        log.info(name + " vist the site");
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }
}

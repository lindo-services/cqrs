package services.lindo.cqrs.eventfeed.feed.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by vanbauwe on 19/01/2017.
 */
@RestController
public class TestControllerImpl implements TestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestControllerImpl.class);

    public String test(@PathVariable String name) {
        LOGGER.info("Test {}", name);
        return "Test " + name;
    }

}

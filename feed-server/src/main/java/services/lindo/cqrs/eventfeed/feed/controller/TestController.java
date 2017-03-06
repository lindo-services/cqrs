package services.lindo.cqrs.eventfeed.feed.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by vanbauwe on 19/01/2017.
 */
public interface TestController {
    @RequestMapping("/test/{name}")
    String test(@PathVariable(name = "name") String name);
}

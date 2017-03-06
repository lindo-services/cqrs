package services.lindo.cqrs.eventfeed.feed.controller;

import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * Created by vanbauwe on 19/01/2017.
 */
@FeignClient("feed-server")
public interface TestClient extends TestController {
}

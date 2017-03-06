package services.lindo.cqrs.eventfeed;

import services.lindo.cqrs.eventfeed.feed.repository.EventRepository;
import services.lindo.cqrs.eventfeed.feed.repository.EventRepositoryJdbcImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by vanbauwe on 17/01/2017.
 */
@Configuration
public class TestConfiguration {
    @Bean
    EventRepository<TestEvent> getTestEventRepository() {
        return new EventRepositoryJdbcImpl<>(TestEvent.class);
    }
}

package services.lindo.cqrs.eventfeed;

import services.lindo.cqrs.events.Event;
import services.lindo.cqrs.eventfeed.feed.repository.EventRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by vanbauwe on 13/01/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FeedTest {
    @Autowired
    private EventRepository<TestEvent> eventRepository;


    @Test
    public void testDb() {
        long sequenceNumber = 175L;
        UUID aggregateId = UUID.randomUUID();
        List<Event<TestEvent>> events = new ArrayList<>();
        for( int i = 0; i < 175; i++) {
            events.add(
                    Event.<TestEvent>builder()
                            .id(UUID.randomUUID())
                            .occured(OffsetDateTime.now())
                            .feedSequenceNumber(sequenceNumber)
                            .aggregateSequenceNumber(sequenceNumber++)
                            .aggregateId(aggregateId)
                            .eventType(TestEvent.class.getSimpleName())
                            .data(
                                    TestEvent.builder()
                                            .voorNaam("Philip")
                                            .familieNaam("Van Bauwel")
                                            .build()
                            )
                            .build()
            );
        }
        eventRepository.addEvents("TestEvent", events);
    }
}

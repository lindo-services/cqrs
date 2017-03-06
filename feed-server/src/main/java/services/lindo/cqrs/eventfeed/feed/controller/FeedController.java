package services.lindo.cqrs.eventfeed.feed.controller;

import com.fasterxml.jackson.databind.util.TokenBuffer;
import services.lindo.cqrs.eventfeed.feed.repository.EventRepository;
import services.lindo.cqrs.events.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import services.lindo.cqrs.feed.*;

import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by vanbauwe on 13/01/2017.
 */
@RestController
public class FeedController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeedController.class);

    private static final CacheControl CACHE_CONTROL_ONE_MONTH = CacheControl.maxAge(30, TimeUnit.DAYS);

    @Autowired
    TestClient testClient;

    @Autowired
    private EventRepository<TokenBuffer> eventRepository;

    @RequestMapping("/feed/{name}")
    public ResponseEntity<Feed<TokenBuffer>> feed(@PathVariable String name,
                                                  HttpServletRequest request) {
        return feed(name, 0L, request);
    }

    @RequestMapping("/feed/{name}/{start}")
    public ResponseEntity<Feed<TokenBuffer>> feed(@PathVariable String name, @PathVariable long start,
                                                  HttpServletRequest request) {
        return feed(name, start, FeedDirection.forward, request);
    }

    @RequestMapping("/feed/{name}/{start}/{direction}")
    public ResponseEntity<Feed<TokenBuffer>> feed(@PathVariable String name, @PathVariable long start,
                                                  @PathVariable FeedDirection direction,
                                                  HttpServletRequest request) {
        return feed(name, start, direction, 100L, request);
    }

    @RequestMapping("/feed/{name}/{start}/{direction}/{size}")
    public ResponseEntity<Feed<TokenBuffer>> feed(@PathVariable String name, @PathVariable long start,
                                                  @PathVariable FeedDirection direction, @PathVariable long size,
                                                  HttpServletRequest request) {
        LOGGER.info("feed {}", name);
        String tagOfComputedLastEntry = "W/\"" + Long.toString(start + size - 1L) + "\"";
        if (Objects.equals(tagOfComputedLastEntry, request.getHeader("If-None-Match"))) {
            return ResponseEntity
                    .status(304)
                    .eTag(tagOfComputedLastEntry)
                    .cacheControl(CACHE_CONTROL_ONE_MONTH)
                    .build();
        } else {
            // Retrieve events, partialy parse into Event<TokenBuffer>
            long startFeedSequenceNumber = start;
            if (FeedDirection.backward == direction) {
                startFeedSequenceNumber = start - size - 1;
            }

            List<Event<TokenBuffer>> events = eventRepository.getEventsPage(name, startFeedSequenceNumber, size);
            Event<TokenBuffer> lastEvent = null;
            OffsetDateTime updatedTime = OffsetDateTime.now();
            if (!events.isEmpty()) {
                lastEvent = events.get(events.size() - 1);
                updatedTime = lastEvent.getOccured();
            } else if ((startFeedSequenceNumber > 0) &&
                    (!eventRepository.doesEventExist(name, startFeedSequenceNumber - 1))) {
                // See if the last entry on the older page exists. If not, return 404.
                return ResponseEntity.notFound().build();
            }

            boolean isMostRecentPage = (events.size() < size);

            boolean isOldestPage = (startFeedSequenceNumber == 0);

            Feed.FeedBuilder<TokenBuffer> feedBuilder = Feed.<TokenBuffer>builder()
                    .title(name + "Feed")
                    .id(testClient.test(name))
                    .base(linkTo(methodOn(FeedController.class).feed(name, null)).toUri())
                    .updated(updatedTime)
                    .link(new FeedLink(new FeedPosition(0, FeedDirection.forward, size), "last"))
                    .link(new FeedLink(new FeedPosition(start, direction, size), "self"));

            // Links
            if (!isMostRecentPage) {
                feedBuilder.link(new FeedLink(new FeedPosition(startFeedSequenceNumber + size, FeedDirection.forward, size), "previous"));
            }
            if (!isOldestPage) {
                feedBuilder.link(new FeedLink(new FeedPosition(startFeedSequenceNumber + 1, FeedDirection.backward, size), "next"));
            }

            feedBuilder.entries(events.stream()
                    .map(this::mapEventToEntry)
                    .collect(Collectors.toList())
            );

            ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
            if (lastEvent != null) {
                responseBuilder.eTag("W/\"" + lastEvent.getFeedSequenceNumber() + "\"");
            }
            return responseBuilder
                    .cacheControl(isMostRecentPage ? CacheControl.noCache() : CACHE_CONTROL_ONE_MONTH) // Don't cache if the page is not full.
                    .body(feedBuilder.build());
        }
    }

    private Entry<TokenBuffer> mapEventToEntry(Event<TokenBuffer> event) {
        return Entry.<TokenBuffer>builder()
                .id(event.getId().toString())
                .links(Collections.emptyList())
                .updated(event.getOccured())
                .content(new Content<>(event.getEventType(), event.getData()))
                .build();
    }
}

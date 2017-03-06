package services.lindo.cqrs.eventfeed.feed.repository;

import services.lindo.cqrs.events.Event;

import java.util.Collection;
import java.util.List;

/**
 * Created by vanbauwe on 17/01/2017.
 */
public interface EventRepository<E> {
    List<Event<E>> getEventsPage(String aggregateType, long startSequence, long size);
    void addEvents(String aggregateType, Collection<Event<E>> events);
    boolean doesEventExist(String aggregateType, long sequenceNr);
}

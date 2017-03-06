package services.lindo.cqrs.eventfeed.feed.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import services.lindo.cqrs.events.Event;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by vanbauwe on 17/01/2017.
 */
public class EventRepositoryJdbcImpl<E> implements EventRepository<E> {
    private static final String EVENT_QUERY = "SELECT eventType, aggregateId, id, aggregateSequenceNumber, " +
            "feedSequenceNumber, occured, data FROM %s_events " +
            "WHERE feedSequenceNumber >= ? and feedSequenceNumber < ? ORDER BY feedSequenceNumber";

    private static final String EVENT_EXISTS = "SELECT exists(SELECT 1 FROM %s_events where feedSequenceNumber=?)";

    private static final String EVENT_UPDATE = "INSERT into %s_events (aggregateType, eventType, aggregateId, id, aggregateSequenceNumber, " +
            "feedSequenceNumber, occured, data) " +
            "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    private final Class<E> eventClass;

    public EventRepositoryJdbcImpl(Class<E> eventClass) {
        this.eventClass = eventClass;
    }

    @Override
    @HystrixCommand
    public List<Event<E>> getEventsPage(String aggregateType, long startSequence, long size) {
        return jdbcTemplate.query(
                String.format(EVENT_QUERY, aggregateType),
                new Object[]{startSequence, startSequence + size},
                (rs, rowNum) -> mapResultSetToEvent(aggregateType, rs)
        );
    }

    @Override
    @HystrixCommand
    public void addEvents(String aggregateType, Collection<Event<E>> events) {
        jdbcTemplate.batchUpdate(
                String.format(EVENT_UPDATE, aggregateType),
                events.stream().map((event) -> mapEventToParameters(aggregateType, event)).collect(Collectors.toList())
        );
    }

    @Override
    @HystrixCommand
    public boolean doesEventExist(String aggregateType, long sequenceNr) {
        return jdbcTemplate.queryForObject(
                String.format(EVENT_EXISTS, aggregateType),
                Boolean.TYPE,
                sequenceNr
        );
    }

    private Event<E> mapResultSetToEvent(String aggregateType, ResultSet rs) {
        try {
            return Event.<E>builder()
                    .aggregateId(UUID.fromString(rs.getString("aggregateId")))
                    .aggregateSequenceNumber(rs.getLong("aggregateSequenceNumber"))
                    .aggregateType(aggregateType)
                    .data(objectMapper.readValue(rs.getString("data"), objectMapper.constructType(this.eventClass)))
                    .eventType(rs.getString("eventType"))
                    .feedSequenceNumber(rs.getLong("feedSequenceNumber"))
                    .id(UUID.fromString(rs.getString("id")))
                    .occured(OffsetDateTime.ofInstant(rs.getTimestamp("occured").toInstant(), ZoneId.systemDefault()))
                    .build();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Object[] mapEventToParameters(String aggregateType, Event<E> event) {
        try {
            Object[] parameters = new Object[8];
            parameters[0] = aggregateType;
            parameters[1] = event.getData().getClass().getCanonicalName();
            parameters[2] = event.getAggregateId();
            parameters[3] = event.getId();
            parameters[4] = event.getAggregateSequenceNumber();
            parameters[5] = event.getFeedSequenceNumber();
            parameters[6] = Timestamp.from(event.getOccured().toInstant());
            PGobject jsonObject = new PGobject();
            jsonObject.setType("jsonb");
            jsonObject.setValue(objectMapper.writeValueAsString(event.getData()));
            parameters[7] = jsonObject;
            return parameters;
        } catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

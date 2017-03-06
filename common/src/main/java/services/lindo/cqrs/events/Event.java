package services.lindo.cqrs.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Created by vanbauwe on 16/01/2017.
 */
@Value
@AllArgsConstructor
@Wither
@Builder
public class Event<D> {
    String aggregateType;
    String eventType;
    UUID aggregateId;
    UUID id;
    long aggregateSequenceNumber;
    long feedSequenceNumber;
    OffsetDateTime occured;
    D data;
}

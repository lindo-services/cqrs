package services.lindo.cqrs.feed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Created by vanbauwe on 16/01/2017.
 */
@Value
@AllArgsConstructor
@Wither
@Builder
public class Entry<E> {
    Content<E> content;
    String id;
    List<Link> links;
    OffsetDateTime updated;
}

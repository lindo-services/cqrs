package services.lindo.cqrs.feed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.Wither;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * Created by vanbauwe on 16/01/2017.
 */
@Value
@AllArgsConstructor
@Wither
@Builder
public class Feed<E> {
    URI base;
    @Singular
    List<Entry<E>> entries;
    String id;
    @Singular
    List<FeedLink> links;
    String title;
    OffsetDateTime updated;
}

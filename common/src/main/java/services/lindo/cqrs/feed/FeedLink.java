package services.lindo.cqrs.feed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

/**
 * Created by vanbauwe on 16/01/2017.
 */
@Value
@AllArgsConstructor
@Wither
@Builder
public class FeedLink {
    FeedPosition href;
    String rel;
}

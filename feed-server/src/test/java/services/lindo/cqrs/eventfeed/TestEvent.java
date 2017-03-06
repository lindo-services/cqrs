package services.lindo.cqrs.eventfeed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

/**
 * Created by vanbauwe on 17/01/2017.
 */
@Value
@AllArgsConstructor
@Wither
@Builder
public class TestEvent {
    String familieNaam;
    String voorNaam;
}

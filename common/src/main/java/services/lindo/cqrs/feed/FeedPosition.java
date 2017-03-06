package services.lindo.cqrs.feed;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
public class FeedPosition {
    long start;
    FeedDirection direction;
    long size;

    @JsonCreator
    public FeedPosition(String str) {
        String[] parts = str.split("/");
        if( parts.length == 3) {
            size = Long.parseLong(parts[2]);
        } else {
            size = 100L;
        }
        if( parts.length >= 2) {
            direction = FeedDirection.valueOf(parts[1]);
        } else {
            direction = FeedDirection.forward;
        }
        if( parts.length >= 1) {
            start = Long.parseLong(parts[0]);
        } else {
            start = 0L;
        }
    }

    @Override
    @JsonValue
    public String toString() {
        return start + "/" + direction + "/" + size;
    }
}

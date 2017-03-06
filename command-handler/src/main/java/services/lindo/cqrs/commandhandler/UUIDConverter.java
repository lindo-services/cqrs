package services.lindo.cqrs.commandhandler;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.UUID;

/**
 * Created by vanbauwe on 14/01/2017.
 */
@Converter(autoApply = true)
public class UUIDConverter implements AttributeConverter<UUID, UUID> {
    @Override
    public UUID convertToDatabaseColumn(UUID uuid) {
        return uuid;
    }

    @Override
    public UUID convertToEntityAttribute(UUID uuid) {
        return uuid;
    }
}

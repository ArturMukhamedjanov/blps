package delivery.models.dto;

import delivery.models.auth.Role;
import lombok.Builder;

import java.time.Instant;

@Builder(toBuilder = true)
public record MessageDto(
        Long id,
        Long chatId,
        String text,
        Role role,
        Instant sendTime
) {
}

package delivery.models.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record CustomerDto(
        Long id,
        String email,
        String password,
        Long userId,
        Double x,
        Double y
) {

}

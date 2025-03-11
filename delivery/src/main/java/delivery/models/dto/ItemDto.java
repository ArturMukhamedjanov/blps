package delivery.models.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record ItemDto(
        Long id,
        String name,
        Integer count,
        Double price
) {
}

package delivery.models.dto;

import delivery.models.OrderStatus;
import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record OrderDto(
    Long id,
    Long customerId,
    Long sellerId,
    Long delivererId,
    OrderStatus status,
    List<ItemDto> items
) {
}

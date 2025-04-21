package delivery.models.mapper;

import delivery.models.orders.Deliverer;
import delivery.models.dto.DelivererDto;
import org.springframework.stereotype.Component;

@Component
public class DelivererMapper {

    // Преобразование из DTO в модель Deliverer
    public Deliverer mapFromDto(DelivererDto dto) {
        var builder = Deliverer.builder();
        builder.distance(dto.distance());
        return builder.build();
    }

    // Преобразование из модели Deliverer в DTO
    public DelivererDto mapToDto(Deliverer deliverer) {
        return DelivererDto.builder()
                .id(deliverer.getId())
                .userId(deliverer.getUser().getId())
                .email(deliverer.getUser().getEmail())
                .distance(deliverer.getDistance())
                .isFree(deliverer.isFree())
                .build();
    }
}

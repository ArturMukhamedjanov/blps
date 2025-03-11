package delivery.models.mapper;

import delivery.models.Deliverer;
import delivery.models.dto.DelivererDto;
import org.springframework.stereotype.Component;

@Component
public class DelivererMapper {

    // Преобразование из DTO в модель Deliverer
    public Deliverer mapFromDto(DelivererDto dto) {
        var builder = Deliverer.builder();
        return builder.build();
    }

    // Преобразование из модели Deliverer в DTO
    public DelivererDto mapToDto(Deliverer deliverer) {
        return DelivererDto.builder()
                .id(deliverer.getId())
                .userId(deliverer.getUser().getId())
                .email(deliverer.getUser().getEmail())
                .build();
    }
}

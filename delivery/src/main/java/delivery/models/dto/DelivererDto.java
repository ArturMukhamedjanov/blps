package delivery.models.dto;

import delivery.models.Distance;
import lombok.Builder;

@Builder(toBuilder = true)
public record DelivererDto(
        Long id,
        Long userId,
        String email,
        String password,
        Distance distance,
        boolean isFree
){

}

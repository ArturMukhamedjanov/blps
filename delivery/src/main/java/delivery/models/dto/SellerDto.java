package delivery.models.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record SellerDto(
        Long id,
        String email,
        String password,
        Long userId,
        String name,
        Double x,
        Double y
){
}

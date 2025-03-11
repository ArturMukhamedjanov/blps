package delivery.models.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record DelivererDto(
        Long id,
        Long clinicId,
        Long userId,
        String email,
        String password,
        String firstName,
        String lastName,
        String speciality
){

}

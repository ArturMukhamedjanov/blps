package delivery.models.mapper;

import delivery.models.Clinic;
import delivery.models.dto.ClinicDto;
import org.springframework.stereotype.Component;

@Component
public class ClinicMapper {

    public Clinic mapFromDto(ClinicDto dto){
        var builder =  Clinic.builder();
        if(dto.name() != null){
            builder.name(dto.name());
        }
        if(dto.description() != null){
            builder.description(dto.description());
        }
        return builder.build();
    }

    public ClinicDto mapToDto(Clinic clinic){
        return ClinicDto.builder()
                .id(clinic.getId())
                .userId(clinic.getUser().getId())
                .email(clinic.getUser().getEmail())
                .name(clinic.getName())
                .description(clinic.getDescription())
                .build();
    }
}

package delivery.models.mapper;

import delivery.models.Analysis;
import delivery.models.dto.AnalysisDto;
import org.springframework.stereotype.Component;

@Component
public class AnalysisMapper {
    public Analysis mapFromDto(AnalysisDto dto) {
        var builder = Analysis.builder();
        if(dto.name() != null){
            builder.name(dto.name());
        }
        if(dto.value() != null){
            builder.value(dto.value());
        }
        if(dto.date() != null){
            builder.date(dto.date());
        }
        if(dto.unit() != null){
            builder.unit(dto.unit());
        }
        return builder.build();
    }

    public AnalysisDto mapToDto(Analysis analysis) {
        return AnalysisDto.builder()
                .id(analysis.getId())
                .customerId(analysis.getCustomer().getId())
                .name(analysis.getName())
                .value(analysis.getValue())
                .unit(analysis.getUnit())
                .date(analysis.getDate())
                .build();
    }
}

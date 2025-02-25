package delivery.models.dto;

import lombok.Builder;

import java.time.Instant;

@Builder(toBuilder = true)
public record AnalysisDto(
        Long id,
        Long customerId,
        String name,
        String value,
        String unit,
        Instant date
) {
}

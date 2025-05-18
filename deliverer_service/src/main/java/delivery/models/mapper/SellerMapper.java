package delivery.models.mapper;

import delivery.models.orders.Seller;
import delivery.models.dto.SellerDto;
import org.springframework.stereotype.Component;

@Component
public class SellerMapper {

    public Seller mapFromDto(SellerDto dto){
        var builder =  Seller.builder();
        if(dto.name() != null){
            builder.name(dto.name());
        }
        if(dto.x() != null){
            builder.x(dto.x());
        }
        if(dto.y() != null){
            builder.y(dto.y());
        }
        return builder.build();
    }

    public SellerDto mapToDto(Seller seller){
        return SellerDto.builder()
                .id(seller.getId())
                .userId(seller.getUser().getId())
                .email(seller.getUser().getEmail())
                .name(seller.getName())
                .x(seller.getX())
                .y(seller.getY())
                .build();
    }
}

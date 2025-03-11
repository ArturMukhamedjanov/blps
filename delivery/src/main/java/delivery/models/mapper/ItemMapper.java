package delivery.models.mapper;

import delivery.models.Item;
import delivery.models.ItemOrderPool;
import delivery.models.ItemSellerPool;
import delivery.models.dto.ItemDto;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    // Преобразование из DTO в модель Item
    public Item mapFromDto(ItemDto dto) {
        var builder = Item.builder();
        builder.name(dto.name());
        if(dto.id() != null){
            builder.id(dto.id());
        }
        if(dto.count() != null){
            builder.id(dto.count());
        }
        return builder.build();
    }

    // Преобразование из модели Item в DTO
    public ItemDto mapToDto(Item deliverer) {
        return ItemDto.builder()
                .id(deliverer.getId())
                .name(deliverer.getName())
                .build();
    }

    public ItemDto mapToDto(ItemSellerPool itemSellerPool){
        return ItemDto.builder()
                .id(itemSellerPool.getItem().getId())
                .name(itemSellerPool.getItem().getName())
                .count(itemSellerPool.getCount())
                .price(itemSellerPool.getPrice())
                .build();
    }

    public ItemDto mapToDto(ItemOrderPool itemOrderPool){
        return ItemDto.builder()
                .id(itemOrderPool.getItem().getId())
                .name(itemOrderPool.getItem().getName())
                .count(itemOrderPool.getCount())
                .build();
    }
}

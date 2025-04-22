package delivery.models.mapper;

import delivery.models.items.Item;
import delivery.models.orders.ItemOrderPool;
import delivery.repositories.items.ItemRepo;
import lombok.RequiredArgsConstructor;
import delivery.models.items.ItemSellerPool;
import delivery.models.dto.ItemDto;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    private final ItemRepo itemRepo;

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
        var item = itemRepo.findById(itemOrderPool.getItemId());
        return ItemDto.builder()
                .id(itemOrderPool.getId())
                .name(item.map(Item::getName).orElse(""))
                .count(itemOrderPool.getCount())
                .build();
    }
}

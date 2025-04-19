package delivery.models.mapper;


import delivery.models.Order;
import delivery.models.dto.OrderDto;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderDto mapToDto(Order order) {
        var builder = OrderDto.builder()
                .id(order.getId())
                .customerId(order.getCustomer().getId())
                .sellerId(order.getSeller().getId())
                .status(order.getStatus())
                .description(order.getDescription())
                .sellerDescription(order.getSellerDescription());
        if(order.getDeliverer() != null){
            builder.delivererId(order.getDeliverer().getId());
        }
        return builder.build();
    }

    public Order mapToEntity(OrderDto orderDto) {
        return Order.builder()
                .id(orderDto.id())
                .status(orderDto.status())
                .description(orderDto.description())
                .sellerDescription(orderDto.sellerDescription())
                .build();
    }

}

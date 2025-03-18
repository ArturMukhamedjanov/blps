package delivery.models.mapper;


import delivery.models.Order;
import delivery.models.dto.OrderDto;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderDto mapToDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .customerId(order.getCustomer().getId())
                .sellerId(order.getSeller().getId())
                .delivererId(order.getDeliverer().getId())
                .status(order.getStatus())
                .description(order.getDescription())
                .sellerDescription(order.getSellerDescription())
                .build();
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

package delivery.models.mapper;

import delivery.models.Customer;
import delivery.models.dto.CustomerDto;
import org.springframework.stereotype.Component;


@Component
public class CustomerMapper {

    public Customer mapFromDto(CustomerDto customerDto) {
        var builder = Customer.builder();
        if(customerDto.x() != null){
            builder.x(customerDto.x());
        }
        if(customerDto.y() != null){
            builder.y(customerDto.y());
        }
        return builder.build();
    }

    public CustomerDto mapToDto(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .userId(customer.getUser().getId())
                .email(customer.getUser().getEmail())
                .x(customer.getX())
                .y(customer.getY())
                .build();
    }

}

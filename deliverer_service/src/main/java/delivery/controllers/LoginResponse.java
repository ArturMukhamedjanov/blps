package delivery.controllers;

import delivery.models.orders.auth.Role;
import lombok.Builder;

@Builder(toBuilder = true)
public record LoginResponse(
        Role role
) {
}

package delivery.controllers;

import delivery.models.auth.Role;
import lombok.Builder;

@Builder(toBuilder = true)
public record LoginResponse(
        Role role
) {
}

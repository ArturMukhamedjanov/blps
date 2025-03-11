package delivery.controllers;

import delivery.auth.services.AuthenticationService;
import delivery.models.Deliverer;
import delivery.models.dto.*;
import delivery.models.mapper.*;
import delivery.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
@RequestMapping("/deliverer")
public class DelivererController {

    private final AuthenticationService authenticationService;
    private final DelivererService delivererService;
    private final DelivererMapper delivererMapper;

    // Получение информации о докторе для текущего пользователя
    @GetMapping()
    public ResponseEntity<DelivererDto> getDeliverer() {
        var currentUser = authenticationService.getCurrentUser();
        var deliverer = delivererService.getDelivererByUser(currentUser);
        if (deliverer.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var delivererDto = delivererMapper.mapToDto(deliverer.get());
        delivererDto.toBuilder().email(currentUser.getEmail());
        return ResponseEntity.ok(delivererDto);
    }

    // Обновление информации о докторе
    @PostMapping
    public ResponseEntity<DelivererDto> updateDelivererInfo(@Valid @RequestBody DelivererDto delivererDto) {
        var currentUser = authenticationService.getCurrentUser();
        var currentDeliverer = delivererService.getDelivererByUser(currentUser);
        if (currentDeliverer.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var updatedDeliverer = mergeDeliverers(currentDeliverer.get(), delivererDto);
        updatedDeliverer = delivererService.updateDeliverer(updatedDeliverer);
        var res = delivererMapper.mapToDto(updatedDeliverer);
        res.toBuilder().email(currentUser.getEmail());
        return ResponseEntity.ok(res);
    }

    public Deliverer mergeDeliverers(Deliverer oldDeliverer, DelivererDto newDeliverer) {
        return oldDeliverer;
    }
}

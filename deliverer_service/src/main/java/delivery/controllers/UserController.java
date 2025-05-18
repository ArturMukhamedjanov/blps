package delivery.controllers;

import delivery.auth.AuthenticationRequest;
import delivery.auth.RegisterRequest;
import delivery.auth.services.AuthenticationService;
import delivery.models.orders.Deliverer;
import delivery.models.orders.Seller;
import delivery.models.orders.Customer;
import delivery.models.orders.auth.Role;
import delivery.models.dto.DelivererDto;
import delivery.models.dto.SellerDto;
import delivery.models.dto.CustomerDto;
import delivery.models.mapper.DelivererMapper;
import delivery.models.mapper.SellerMapper;
import delivery.models.mapper.CustomerMapper;
import delivery.services.UserService;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final CustomerMapper customerMapper;
    private final SellerMapper sellerMapper;
    private final DelivererMapper delivererMapper;
    private final UserService userService;
    private final AuthenticationService authenticationService;
    
    @PostMapping("/register/deliverer")
    public ResponseEntity<LoginResponse> registerDeliverer(
            @Valid @RequestBody DelivererDto delivererDto,
            HttpServletResponse response
    ){
        if(!validDeliverer(delivererDto)){
            return ResponseEntity.badRequest().body(null);
        }
        if(userService.getUserByEmail(delivererDto.email()).isPresent()){
            return ResponseEntity.badRequest().body(null);
        }
        Deliverer deliverer = delivererMapper.mapFromDto(delivererDto);
        deliverer.setFree(true);
        var registerRequest = RegisterRequest.builder()
                .email(delivererDto.email())
                .password(delivererDto.password())
                .build();
        var authResponse = authenticationService.registerDeliverer(registerRequest, deliverer);
        response.addCookie(createCookie(authResponse.getToken()));
        return ResponseEntity.ok(LoginResponse.builder().role(Role.DELIVERER).build());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(
            @RequestBody AuthenticationRequest authenticationRequest,
            HttpServletResponse response
    ){
        var authResponse = authenticationService.authenticate(authenticationRequest);
        response.addCookie(createCookie(authResponse.getToken()));
        return ResponseEntity.ok(LoginResponse.builder().role(authResponse.getRole()).build());
    }

    private boolean validateCustomer(CustomerDto customerDto){
        return customerDto.email()!= null
                && customerDto.password() != null
                && customerDto.x() != null
                && customerDto.y() != null;
    }

    private boolean validateSeller(SellerDto sellerDto){
        return sellerDto.email() != null
                && sellerDto.password() != null
                && sellerDto.name() != null
                && sellerDto.x() != null
                && sellerDto.y() != null;
    }

    private boolean validDeliverer(DelivererDto delivererDto){
        return delivererDto.email() != null
                && delivererDto.password() != null
                && delivererDto.distance() != null;
    }

    private Cookie createCookie(String token){
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        return cookie;
    }
}

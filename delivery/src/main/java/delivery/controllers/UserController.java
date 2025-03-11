package delivery.controllers;

import delivery.auth.AuthenticationRequest;
import delivery.auth.RegisterRequest;
import delivery.auth.services.AuthenticationService;
import delivery.models.Seller;
import delivery.models.Customer;
import delivery.models.auth.Role;
import delivery.models.dto.SellerDto;
import delivery.models.dto.CustomerDto;
import delivery.models.mapper.SellerMapper;
import delivery.models.mapper.CustomerMapper;
import delivery.services.UserService;
import lombok.RequiredArgsConstructor;
import javax.servlet.http.Cookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final CustomerMapper customerMapper;
    private final SellerMapper sellerMapper;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register/customer")
    public ResponseEntity<LoginResponse> registerCustomer(
            @Valid @RequestBody CustomerDto customerDto,
            HttpServletResponse response
    ){
        if(!validateCustomer(customerDto)){
            return ResponseEntity.badRequest().body(null);
        }
        if(userService.getUserByEmail(customerDto.email()).isPresent()){
            return ResponseEntity.badRequest().body(null);
        }
        Customer customer = customerMapper.mapFromDto(customerDto);
        var registerRequest = RegisterRequest.builder()
                .email(customerDto.email())
                .password(customerDto.password())
                .build();
        var authResponse = authenticationService.registerCustomer(registerRequest, customer);
        response.addCookie(createCookie(authResponse.getToken()));
        return ResponseEntity.ok(LoginResponse.builder().role(Role.CUSTOMER).build());
    }

    @PostMapping("/register/seller")
    public ResponseEntity<LoginResponse> registerClinic(
            @Valid @RequestBody SellerDto sellerDto,
            HttpServletResponse response
    ){
        if(!validateSeller(sellerDto)){
            return ResponseEntity.badRequest().body(null);
        }
        if(userService.getUserByEmail(sellerDto.email()).isPresent()){
            return ResponseEntity.badRequest().body(null);
        }
        Seller seller = sellerMapper.mapFromDto(sellerDto);
        var registerRequest = RegisterRequest.builder()
                .email(sellerDto.email())
                .password(sellerDto.password())
                .build();
        var authResponse = authenticationService.registerSeller(registerRequest, seller);
        response.addCookie(createCookie(authResponse.getToken()));
        return ResponseEntity.ok(LoginResponse.builder().role(Role.SELLER).build());
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

    private Cookie createCookie(String token){
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        return cookie;
    }
}

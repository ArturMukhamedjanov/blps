package delivery.auth.services;

import delivery.models.Seller;
import delivery.models.Customer;
import delivery.models.Deliverer;
import delivery.repos.SellerRepo;
import delivery.repos.CustomerRepo;
import delivery.repos.DelivererRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import delivery.auth.AuthenticationRequest;
import delivery.auth.AuthenticationResponse;
import delivery.auth.RegisterRequest;
import delivery.models.auth.Role;
import delivery.models.auth.User;
import delivery.models.auth.User.UserBuilder;
import delivery.repos.UserRepo;
import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserRepo userRepo;
    private final CustomerRepo customerRepo;
    private final SellerRepo sellerRepo;
    private final DelivererRepo delivererRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse registerCustomer(RegisterRequest request, Customer customer) {
        User user =  registerUser(request, Role.CUSTOMER);
        String jwtToken = jwtService.generateToken(user);
        customer.setUser(user);
        customerRepo.save(customer);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(Role.CUSTOMER)
                .build();
    }

   public AuthenticationResponse registerSeller(RegisterRequest request, Seller seller) {
       User user = registerUser(request, Role.SELLER);
       String jwtToken = jwtService.generateToken(user);
       seller.setUser(user);
       sellerRepo.save(seller);
       return AuthenticationResponse.builder()
               .token(jwtToken)
               .role(Role.SELLER)
               .build();
    }

    public AuthenticationResponse registerDeliverer(RegisterRequest request, Deliverer deliverer) {
        User user =  registerUser(request, Role.DELIVERER);
        String jwtToken = jwtService.generateToken(user);
        deliverer.setUser(user);
        delivererRepo.save(deliverer);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(Role.DELIVERER)
                .build();
    }

    public User registerUser(RegisterRequest request, Role role){
        UserBuilder userBuilder = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role);
        User user = userBuilder.build();
        user = userRepo.save(user);
        return user;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(null);
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(user.getRole())
                .build();
    }

    public User getCurrentUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepo.getUserByEmail(userDetails.getUsername());
    }
}

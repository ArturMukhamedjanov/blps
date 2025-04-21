package delivery.auth;

 import delivery.models.orders.auth.Role;
 import lombok.AllArgsConstructor;
 import lombok.Builder;
 import lombok.Data;
 import lombok.NoArgsConstructor;

 @Data
 @Builder
 @NoArgsConstructor
 @AllArgsConstructor
 public class AuthenticationResponse {
     private String token;
     private Role role;
 }

package delivery.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import delivery.auth.AuthenticationRequest;
import delivery.auth.services.AuthenticationService;
import delivery.models.orders.auth.Role;
import delivery.services.UserService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthTask implements JavaDelegate {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var username = (String) delegateExecution.getVariable("username");
        var password = (String) delegateExecution.getVariable("password");
        AuthenticationRequest request = AuthenticationRequest.builder().email(username).password(password).build();
        try{
            var response = authenticationService.authenticate(request);
            var user = userService.getUserByEmail(username);
            if(response != null && response.getRole() != null && user.isPresent()){
                
                delegateExecution.setVariable("auth", true);
                delegateExecution.setVariable("user", user.get());
                if(response.getRole() == Role.CUSTOMER){
                    delegateExecution.setVariable("customer", true);
                }
                if(response.getRole() == Role.SELLER){
                    delegateExecution.setVariable("seller", true);
                }
                if(response.getRole() == Role.DELIVERER){
                    delegateExecution.setVariable("deliverer", true);
                }
            }
        } catch (Exception e){
            delegateExecution.setVariable("auth", false);
        }

    }

    


}
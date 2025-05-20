package delivery.services;

import jakarta.annotation.Resource;
import jakarta.resource.cci.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import me.dynomake.yookassa.Yookassa;
import me.dynomake.yookassa.model.Amount;
import me.dynomake.yookassa.model.Confirmation;
import me.dynomake.yookassa.model.Payment;
import me.dynomake.yookassa.model.request.PaymentRequest;

import org.springframework.stereotype.Service;

import delivery.models.orders.Order;

@Service
@RequiredArgsConstructor
public class YookassaService {
    @Resource(name = "eis/YookassaConnectionFactory")
    private ConnectionFactory yookassaConnectionFactory;

    public void createNewPaymentFor(Order order) throws Exception{
        System.out.println("StartPayment");
        Yookassa yookassa = (Yookassa) yookassaConnectionFactory.getConnection();
        
        double price = 100;
        Amount amount = new Amount(Double.toString(price), "RUB");
        Payment payment = yookassa.createPayment(PaymentRequest.builder()
                .description("Payment for order #" + order.getId())
                .amount(amount)
                .confirmation(Confirmation.builder()
                        .type("redirect")
                        .returnUrl("")
                        .build())
                .build());
        System.out.println("EndedPayment");
        return;
    }
}
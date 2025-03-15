package com.project.service;

import com.project.dto.StudentOrderDTO;
import com.project.enums.PaymentStatus;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class StripePaymentServiceImpl implements PaymentService {

    @Value("${stripe.key}")
    private String key;

    @Value("${stripe.secret}")
    private String secret;

    @Override
    public StudentOrderDTO createOrder(StudentOrderDTO studentOrderDTO) {
        // Set your secret key. Remember to switch to your live secret key in production!
        Stripe.apiKey = secret;

        // Create a PaymentIntent with the order amount and currency
        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(studentOrderDTO.getName())
                        .build();

        // Create new line item with the above product data and associated price
        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("INR")
                        .setUnitAmount((long) (studentOrderDTO.getAmount()*100))
                        .setProductData(productData)
                        .build();

        // Create new line item with the above price data
        SessionCreateParams.LineItem lineItem =
                SessionCreateParams
                        .LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(priceData)
                        .build();

        // Create new session with the line items
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:8080/success?true&session_id={CHECKOUT_SESSION_ID}&isStripe=true")
                        .setCancelUrl("http://localhost:8080/cancel?true&session_id={CHECKOUT_SESSION_ID}")
                        .addLineItem(lineItem)
                        .build();

        // Create new session
        Session session = null;
        try {
            session = Session.create(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        studentOrderDTO.setOrderId(session.getId());
        studentOrderDTO.setStatus(PaymentStatus.CREATED.toString());
        return studentOrderDTO;
    }

    @Override
    public String extractOrderId(Map<String, String> requestParams) {
        return requestParams.get("session_id");
    }
}

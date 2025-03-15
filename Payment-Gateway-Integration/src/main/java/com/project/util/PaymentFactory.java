package com.project.util;

import com.project.enums.PaymentMethod;
import com.project.service.PaymentService;
import com.project.service.RazorpayPaymentServiceImpl;
import com.project.service.StripePaymentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentFactory {

    @Autowired
    private  RazorpayPaymentServiceImpl razorpayPaymentService;

    @Autowired
    private StripePaymentServiceImpl stripePaymentService;


    public PaymentService getPaymentMethod(PaymentMethod paymentMethod) {
        switch (paymentMethod) {
            case RAZORPAY:
                return razorpayPaymentService;
            case STRIPE:
                return stripePaymentService;
            default:
                throw new IllegalArgumentException("Invalid Payment Method");
        }
    }
}

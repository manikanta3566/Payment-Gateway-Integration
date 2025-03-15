package com.project.service;

import com.project.dto.StudentOrderDTO;
import com.project.enums.PaymentStatus;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RazorpayPaymentServiceImpl implements PaymentService{

    @Value("${razorpay.key-id}")
    private String key;

    @Value("${razorpay.secret}")
    private String secret;
    @Override
    public StudentOrderDTO createOrder(StudentOrderDTO studentOrderDTO)  {
        try {
            RazorpayClient razorpay = new RazorpayClient(key, secret);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", studentOrderDTO.getAmount() * 100);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", studentOrderDTO.getEmail());
            Order order = razorpay.orders.create(orderRequest);
            studentOrderDTO.setOrderId(order.get("id"));
            studentOrderDTO.setStatus(PaymentStatus.CREATED.toString());
            return studentOrderDTO;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public String extractOrderId(Map<String, String> requestParams) {
        return requestParams.get("razorpay_order_id");
    }
}

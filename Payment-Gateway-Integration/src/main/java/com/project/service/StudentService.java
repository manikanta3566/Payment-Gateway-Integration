package com.project.service;

import com.project.dto.StudentOrderDTO;
import com.project.entity.StudentOrder;
import com.project.enums.PaymentMethod;
import com.project.enums.PaymentStatus;
import com.project.repository.StudentOrderRepository;
import com.project.util.PaymentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Map;

@Service
public class StudentService {

    @Autowired
    private StudentOrderRepository studentOrderRepository;

    @Autowired
    private PaymentFactory paymentFactory;

    @Autowired
    private EmailService emailService;

    public StudentOrderDTO createOrder(StudentOrderDTO studentOrderDTO) {
        try {
            PaymentService paymentService = paymentFactory.getPaymentMethod(PaymentMethod.valueOf(studentOrderDTO.getPaymentMedium()));
            studentOrderDTO = paymentService.createOrder(studentOrderDTO);
            StudentOrder studentOrder = new StudentOrder();
            studentOrder.setOrderId(studentOrderDTO.getOrderId());
            studentOrder.setStatus(studentOrderDTO.getStatus());
            studentOrder.setAmount(studentOrderDTO.getAmount());
            studentOrder.setCourse(studentOrderDTO.getCourse());
            studentOrder.setEmail(studentOrderDTO.getEmail());
            studentOrder.setName(studentOrderDTO.getName());
            studentOrder.setPaymentMedium(studentOrderDTO.getPaymentMedium());
            studentOrderRepository.save(studentOrder);
            return studentOrderDTO;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void callback(Map<String, String> payload) {
        String orderId;
        if(payload.containsKey("isStripe")){
            orderId= paymentFactory.getPaymentMethod(PaymentMethod.STRIPE).extractOrderId(payload);
        }else{
            orderId=paymentFactory.getPaymentMethod(PaymentMethod.RAZORPAY).extractOrderId(payload);
        }
        StudentOrder studentOrder = studentOrderRepository.findByOrderId(orderId);
        studentOrder.setStatus(PaymentStatus.COMPLETED.toString());
        studentOrderRepository.save(studentOrder);
        // After successful payment, send an email
        String subject = "Payment Successful";
        String body = String.format(
                "Dear %s,\n\nThank you for purchasing the course '%s'.\n\n" +
                        "Your payment of %s has been successfully completed.\n\n" +
                        "We wish you all the best in your learning journey!\n\n" +
                        "Best regards,\n" +
                        "The Learning Platform Team",
                studentOrder.getName(),studentOrder.getCourse(), studentOrder.getAmount());
        emailService.sendEmail(studentOrder.getEmail(), subject, body);
    }

    // Method to generate HMAC-SHA256 hash
    private static String generateHMACSHA256(String data, String secret) throws Exception {
        // Create a secret key from the provided 'secret'
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");

        // Create a Mac instance and initialize with the secret key
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKey);

        // Compute the HMAC hash
        byte[] hashBytes = mac.doFinal(data.getBytes());

        // Convert the byte array to a hexadecimal string
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }

        // Return the hex string
        return hexString.toString();
    }

}

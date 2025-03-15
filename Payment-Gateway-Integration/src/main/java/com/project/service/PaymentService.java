package com.project.service;

import com.project.dto.StudentOrderDTO;

import java.util.Map;

public interface PaymentService {
    StudentOrderDTO createOrder(StudentOrderDTO studentOrderDTO);

    String extractOrderId(Map<String,String> requestParams);
}

package com.project.controller;

import com.project.dto.StudentOrderDTO;
import com.project.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class StudentOrderController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/")
    public String indexPage(){
        return "index";
    }

    @PostMapping("/order")
    public ResponseEntity<StudentOrderDTO> createOrder(@RequestBody StudentOrderDTO studentOrderDTO){
        return new ResponseEntity<>(studentService.createOrder(studentOrderDTO), HttpStatus.CREATED);
    }

    @PostMapping("/callback")
    public String callBack(@RequestParam Map<String,String> payload){
        System.out.println(payload);
        studentService.callback(payload);
        return "success";
    }

    @GetMapping("/success")
    public String successCallBackForStripe(@RequestParam Map<String,String> params){
        System.out.println(params);
        studentService.callback(params);
        return "success";
    }
}

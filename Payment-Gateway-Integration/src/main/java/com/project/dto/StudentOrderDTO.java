package com.project.dto;

public class StudentOrderDTO {

    private String name;

    private String email;

    private String course;

    private Integer amount;

    private String status;

    private String orderId;

    private String paymentMedium;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMedium() {
        return paymentMedium;
    }

    public void setPaymentMedium(String paymentMedium) {
        this.paymentMedium = paymentMedium;
    }
}

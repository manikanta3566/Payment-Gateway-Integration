package com.project.repository;

import com.project.entity.StudentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentOrderRepository extends JpaRepository<StudentOrder,String> {

    StudentOrder findByOrderId(String orderId);
}

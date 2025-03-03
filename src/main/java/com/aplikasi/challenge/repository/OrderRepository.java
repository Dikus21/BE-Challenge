package com.aplikasi.challenge.repository;

import com.aplikasi.challenge.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
//    @Query("select o from Order o WHERE o.id = :idOrder")
//    public Order getById(@Param("idOrder") UUID idOrder);
}

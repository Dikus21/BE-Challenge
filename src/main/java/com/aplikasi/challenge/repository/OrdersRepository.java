package com.aplikasi.challenge.repository;

import com.aplikasi.challenge.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, UUID>, JpaSpecificationExecutor<Orders> {
    @Query("select o from Orders o WHERE o.id = :idOrder")
    public Orders getById(@Param("idOrder") UUID idOrder);
}

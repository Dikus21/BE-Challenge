package com.aplikasi.challenge.repository;

import com.aplikasi.challenge.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, UUID>, JpaSpecificationExecutor<Merchant> {
    @Query("select m from Merchant m WHERE m.id = :idMerchant")
    public Merchant getById(@Param("idMerchant") UUID idMerchant);
}

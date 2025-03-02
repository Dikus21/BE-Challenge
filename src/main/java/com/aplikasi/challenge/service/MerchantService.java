package com.aplikasi.challenge.service;

import com.aplikasi.challenge.dto.PeriodReportDTO;
import com.aplikasi.challenge.entity.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public interface MerchantService {
    Map<Object, Object> save(Merchant request);

    Map<Object, Object> update(Merchant request);

    Map<Object, Object> delete(Merchant request);

    Map<Object, Object> getById(Long id);
    Page<PeriodReportDTO> generateWeeklyReport(Merchant request, Date startDate, String period, Pageable pageable);
}

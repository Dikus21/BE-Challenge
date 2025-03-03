package com.aplikasi.challenge.service.impl;

import com.aplikasi.challenge.dto.PeriodReportDTO;
import com.aplikasi.challenge.entity.Merchant;
import com.aplikasi.challenge.repository.MerchantRepository;
import com.aplikasi.challenge.service.MerchantService;
import com.aplikasi.challenge.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class MerchantImpl implements MerchantService {

    @Autowired
    public MerchantRepository merchantRepository;
    @Autowired
    public Response response;

    @Override
    public Map<Object, Object> save(Merchant request) {
        try {
            log.info("Save New Merchant");
            if (request.getName().isEmpty()) return response.error("Name is required");
            if (merchantRepository.getByName(request.getName()) > 0) return response.error("Name Used");

            if (request.getLocation().isEmpty()) return response.error("Address is required");

            log.info("Merchant Save Success");
            return response.success(merchantRepository.save(request));
        } catch (Exception e) {
            log.error("Save Merchant Error: " + e.getMessage());
            return response.error("Merchant Merchant: " + e.getMessage());
        }
    }

    @Override
    public Map<Object, Object> update(Merchant request) {
        try {
            log.info("Update Merchant");
            if (request.getId() == null) return response.error("Id is required");
            Optional<Merchant> checkDataDBMerchant = merchantRepository.findById(request.getId());
            if (!checkDataDBMerchant.isPresent()) return response.error("Id is not Registered");
            if (!request.getName().isEmpty()) {
                if (merchantRepository.getByName(request.getName()) > 0) return response.error("Merchantname Exist, try other name");
                checkDataDBMerchant.get().setName(request.getName());
            }
            if (!request.getLocation().isEmpty()) {
                checkDataDBMerchant.get().setLocation(request.getLocation());
            }
            checkDataDBMerchant.get().setOpen(request.isOpen());

            log.info("Update Merchant Success");
            return response.success(merchantRepository.save(checkDataDBMerchant.get()));
        } catch (Exception e) {
            log.error("Update Merchant Error: " + e.getMessage());
            return response.error("Update Merchant: " + e.getMessage());
        }
    }

    @Override
    public Map<Object, Object> delete(Merchant request) {
        try {
            log.info("Delete Merchant");
            if (request.getId() == null) return response.error("Id is required");
            Optional<Merchant> checkDataDBMerchant = merchantRepository.findById(request.getId());
            if (!checkDataDBMerchant.isPresent()) return response.error("Merchant not Found");

            log.info("Merchant Deleted");
            checkDataDBMerchant.get().setDeletedDate(new Date());
            return response.success(merchantRepository.save(checkDataDBMerchant.get()));
        } catch (Exception e) {
            log.error("Delete Merchant Error: " + e.getMessage());
            return response.error("Delete Merchant : " + e.getMessage());
        }
    }

    @Override
    public Map<Object, Object> getById(UUID uuid) {
        try {
            log.info("Get Merchant");
            if (uuid == null) return response.error("Id is required");
            Optional<Merchant> checkDataDBMerchant = merchantRepository.findById(uuid);
            if (!checkDataDBMerchant.isPresent()) return response.error("Merchant not Found");

            log.info("Merchant Found");
            return response.success(checkDataDBMerchant.get());
        } catch (Exception e) {
            log.error("Get Merchant Error: " + e.getMessage());
            return response.error("Get Merchant: " + e.getMessage());
        }
    }

    @Override
    public Page<PeriodReportDTO> generateWeeklyReport(Merchant request, Date startDate, String period, Pageable pageable) {
        int dayInc = 7;
        if (period.equalsIgnoreCase("Monthly") || period.equalsIgnoreCase("Month")){
            dayInc = 30;
            period = "Month";
        } else {
            period = "Week";
        }
        log.info("Generate " + period + " Report");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DAY_OF_MONTH, dayInc);
        Date endDate = calendar.getTime();
        Date currentDate = new Date();
        PeriodReportDTO periodReportDTO;
        List<PeriodReportDTO> periodReportDTOList = new ArrayList<>();
        int periodCount = 1;
        while (startDate.before(currentDate)) {
            log.info("start Date: " + startDate );
            log.info("end Date: " + endDate );
            periodReportDTO = new PeriodReportDTO();
            periodReportDTO.setStartDate(startDate);
            if (endDate.after(currentDate)) {
                long daysDiff = (currentDate.getTime() - startDate.getTime())/(24*60*60*1000);
                period = daysDiff + " days";
                calendar.add(Calendar.DAY_OF_MONTH, (int) (daysDiff-dayInc));
                endDate = calendar.getTime();
            }
            periodReportDTO.setEndDate(endDate);
            BigDecimal income = merchantRepository.sumIncomeMerchant(request.getId(), startDate, endDate);
            if (income == null) income = BigDecimal.valueOf(0);
            log.info("Income: " + income);
            periodReportDTO.setPeriod(period + " " + periodCount);
            periodCount++;
            periodReportDTO.setIncome(income);
            periodReportDTOList.add(periodReportDTO);
            calendar.setTime(endDate);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            startDate = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, dayInc);
            endDate = calendar.getTime();
        }
//        return periodReportDTOList;
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), periodReportDTOList.size());
        List<PeriodReportDTO> subList = periodReportDTOList.subList(start, end);

        return new PageImpl<>(subList, pageable, periodReportDTOList.size());
    }

//    public WeeklyReportDTO generateMonthlyReport(UUID request) {
//        // Logic to generate monthly report
//        // ...
//    }
}

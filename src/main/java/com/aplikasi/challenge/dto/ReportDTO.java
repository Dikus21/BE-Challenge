package com.aplikasi.challenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.UUID;

@Data
public class ReportDTO {
    @Schema(description = "Merchant ID", example = "1")
    private String merchantId;
    @Schema(description = "Merchant Location", example = "Jl. Raya Bogor")
    private String merchantLocation;
    @Schema(description = "Period", example = "2021-01")
    private String period;
    private Page<PeriodReportDTO> reports;
}

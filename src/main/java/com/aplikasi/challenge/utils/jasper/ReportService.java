package com.aplikasi.challenge.utils.jasper;

import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;

@Service
public class ReportService {
    @Autowired
    ApplicationContext context;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;
    public byte[] generate_pdf(Map<String, Object> parameters, String inputStream, JRDataSource dataSource) {
        try {
            JasperReport report = JasperCompileManager.compileReport(inputStream);

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataSource);

            byte[] result = JasperExportManager.exportReportToPdf(jasperPrint) ;
            OutputStream out = new FileOutputStream("./cdn/User_Invoice.pdf");
            out.write(result);
            out.close();
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

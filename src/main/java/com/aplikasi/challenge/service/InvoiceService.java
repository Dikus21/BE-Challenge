package com.aplikasi.challenge.service;

import com.aplikasi.challenge.entity.Order;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;

public interface InvoiceService {
    public byte[] generateInvoice(Order request) throws IOException, JRException;
}

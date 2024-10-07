package com.emlakjet.purchasing.service;

import com.emlakjet.purchasing.event.InvoiceRejectedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {

    @KafkaListener(topics = "invoice_rejected_topic", groupId = "saleman_invoice_reject_group")
    public void handleInvoiceRejected(InvoiceRejectedEvent event) {

        for (String email : event.getTo()) {
            // EMAILING
            log.info("EMAIL SENT TO {}, BILL NO = {} , PRODUCT = {} , TIMESTAMP = {}", email, event.getBillNo(), event.getProductName(), event.getTimestamp());
        }

    }
}

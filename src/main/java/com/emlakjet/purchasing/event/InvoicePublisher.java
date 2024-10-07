package com.emlakjet.purchasing.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class InvoicePublisher {

    private final KafkaTemplate<String, InvoiceRejectedEvent> kafkaTemplate;

    public InvoicePublisher(KafkaTemplate<String, InvoiceRejectedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishInvoiceRejectedEvent(InvoiceRejectedEvent event) {
        kafkaTemplate.send("invoice_rejected_topic", event);
    }
}
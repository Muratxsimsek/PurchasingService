package com.emlakjet.purchasing.service;

import com.emlakjet.purchasing.event.InvoicePublisher;
import com.emlakjet.purchasing.event.InvoiceRejectedEvent;
import com.emlakjet.purchasing.persistence.entity.InvoiceEntity;
import com.emlakjet.purchasing.persistence.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    @Value("${purchase.limit}")
    private double limit;

    private final InvoicePublisher invoicePublisher;

    private final InvoiceRepository invoiceRepository;

    private final UserService userService;

    public InvoiceService(InvoicePublisher invoicePublisher, InvoiceRepository invoiceRepository, UserService userService) {
        this.invoicePublisher = invoicePublisher;
        this.invoiceRepository = invoiceRepository;
        this.userService = userService;
    }

    public InvoiceEntity submitInvoice(InvoiceEntity invoice) {


            Optional<Double> approvedTotal = invoiceRepository.findApprovedTotalAmountPerUser(
                    invoice.getFirstName(), invoice.getLastName(), invoice.getEmail());

            if (approvedTotal.orElse(0D) + invoice.getAmount() <= limit) {
                invoice.setApproved(true);

                return invoiceRepository.save(invoice);
            } else {
                invoice.setApproved(false);
                InvoiceEntity newInvoiceEntity = invoiceRepository.save(invoice);

                //PUBLISH
                InvoiceRejectedEvent invoiceRejectedEvent = InvoiceRejectedEvent.builder()
                        .billNo(invoice.getBillNo())
                        .productName(invoice.getProductName())
                        .amount(invoice.getAmount())
                        .firstName(invoice.getFirstName())
                        .lastName(invoice.getLastName())
                        .email(invoice.getEmail())
                        .reason("Invoice rejected by LIMIT exceeding")
                        .timestamp(LocalDateTime.now())
                        .to(userService.getAllUserEmails())
                        .build();

                invoicePublisher.publishInvoiceRejectedEvent(invoiceRejectedEvent);

                return newInvoiceEntity;
            }

    }

    public List<InvoiceEntity> findByApproved(String firstName, String lastName, String email, boolean approved) {
        return invoiceRepository.findByApproved(firstName, lastName, email, approved);
    }

    public List<InvoiceEntity> findAllInvoices() {
        return invoiceRepository.findAll();
    }
}

package com.emlakjet.purchasing.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class InvoiceRejectedEvent implements Serializable {

    private List<String> to;

    private String firstName;
    private String lastName;
    private String email;
    private Double amount;
    private String productName;
    private String billNo;

    private String reason;
    private LocalDateTime timestamp;

}
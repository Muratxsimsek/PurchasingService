package com.emlakjet.purchasing.service;

import com.emlakjet.purchasing.event.InvoicePublisher;
import com.emlakjet.purchasing.event.InvoiceRejectedEvent;
import com.emlakjet.purchasing.persistence.entity.InvoiceEntity;
import com.emlakjet.purchasing.persistence.repository.InvoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:truncate_tables.sql")
//@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@EmbeddedKafka(partitions = 1, topics = {"invoice_rejected_topic"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private UserService userService;

    @Mock
    private InvoicePublisher invoicePublisher;

    @InjectMocks
    private InvoiceService invoiceService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(invoiceService, "limit", 2000.0);
    }

    @Test
    public void should_submitInvoice_Add1Invoice_GetLimitFailureExceededAndVerifyPublishInvoiceRejectedEvent() {
        //LIMIT = 2000
        InvoiceEntity invoiceEntity = InvoiceEntity.builder().firstName("John").lastName("Doe").email("john@doe.com").amount(2001.0).productName("Laptop").billNo("TR000123").build();

        when(invoiceRepository.findApprovedTotalAmountPerUser(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(2001.0));

        when(invoiceRepository.save(any(InvoiceEntity.class))).thenReturn(invoiceEntity);
        when(userService.getAllUserEmails()).thenReturn(List.of("john@doe.com"));

        invoiceService.submitInvoice(invoiceEntity);

        verify(invoicePublisher, times(1)).publishInvoiceRejectedEvent(any(InvoiceRejectedEvent.class));
    }
}

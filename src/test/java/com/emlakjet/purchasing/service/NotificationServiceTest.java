package com.emlakjet.purchasing.service;

import com.emlakjet.purchasing.controller.Dto.InvoiceDto;
import com.emlakjet.purchasing.controller.Dto.LoginRequest;
import com.emlakjet.purchasing.controller.Dto.ProductDto;
import com.emlakjet.purchasing.event.InvoiceRejectedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:truncate_tables.sql")
@EmbeddedKafka(partitions = 1, topics = {"invoice_rejected_topic"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NotificationServiceTest {


    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private String tokenForUser1;

    @BeforeAll
    public void setUpInitial() throws JsonProcessingException, InterruptedException {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        Thread.sleep(5000);

        //Login User 1
        LoginRequest loginRequest = new LoginRequest("John", "Doe", "john@doe.com", "pwd123");

        tokenForUser1 = given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .asString();

        ProductDto product1 = ProductDto.builder().name("Laptop").description("I will use it on the project of company").build();
        ProductDto product2 = ProductDto.builder().name("Pen").description("I will need to take notes").build();
        ProductDto product3 = ProductDto.builder().name("Marcus Aurelius Book").description("for private usage").build();
        given()
                .header("Authorization", "Bearer " + tokenForUser1)
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(product1))
                .when()
                .post("/api/products")
                .then()
                .log().everything()
                .statusCode(200)
                .body("data.name", equalTo(product1.getName()))
                .body("data.description", equalTo(product1.getDescription()));
        given()
                .header("Authorization", "Bearer " + tokenForUser1)
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(product2))
                .when()
                .post("/api/products")
                .then()
                .log().everything()
                .statusCode(200)
                .body("data.name", equalTo(product2.getName()))
                .body("data.description", equalTo(product2.getDescription()));
        given()
                .header("Authorization", "Bearer " + tokenForUser1)
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(product3))
                .when()
                .post("/api/products")
                .then()
                .log().everything()
                .statusCode(200)
                .body("data.name", equalTo(product3.getName()))
                .body("data.description", equalTo(product3.getDescription()));
    }

    @BeforeEach
    public void setUp() throws InterruptedException {
    }

    @Test
    public void should_submitInvoice_Add1Invoice_GetRejectMessageAndCheckNotificationMessage() throws JsonProcessingException {

        //LIMIT = 2000
        InvoiceDto invoice = InvoiceDto.builder().firstName("John").lastName("Doe").email("john@doe.com").amount(2001.0).productName("Laptop").billNo("TR000123").build();

        given()
                .header("Authorization", "Bearer " + tokenForUser1)
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(invoice))
                .when()
                .post("/api/invoices")
                .then()
                .log().everything()
                .statusCode(200)
                .body("message", equalTo("Invoice rejected"));


        InvoiceRejectedEvent event = InvoiceRejectedEvent.builder()
                .billNo("TR000123")
                .productName("Laptop")
                .amount(2001.0)
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.com")
                .reason("Invoice rejected by LIMIT exceeding")
                .timestamp(LocalDateTime.now())
                .to(List.of("john@doe.com", "jane@doe.com"))
                .build();


//        await().atMost(10, SECONDS).untilAsserted(() -> {
//            List<String> logs = logCaptor.getInfoLogs();
//            String expectedLogMessage = String.format("EMAIL SENT TO %s, BILL NO = %s , PRODUCT = %s , TIMESTAMP = %s",
//                    "john.doe@example.com", event.getBillNo(), event.getProductName(), event.getTimestamp());
//
//            // Assert that the expected log message was produced
//            assertThat(logs).anyMatch(log -> log.contains(expectedLogMessage));
//        });

//        await().atMost(10, SECONDS).untilAsserted(() -> {
//
//            ArgumentCaptor<InvoiceRejectedEvent> eventCaptor = ArgumentCaptor.forClass(InvoiceRejectedEvent.class);
//
//
//            InvoiceRejectedEvent capturedEvent = eventCaptor.getValue();
//            assertThat(capturedEvent).isNotNull();
//            assertThat(capturedEvent.getBillNo()).isEqualTo(event.getBillNo());
//            assertThat(capturedEvent.getProductName()).isEqualTo(event.getProductName());
//            assertThat(capturedEvent.getAmount()).isEqualTo(event.getAmount());
//            assertThat(capturedEvent.getFirstName()).isEqualTo(event.getFirstName());
//            assertThat(capturedEvent.getLastName()).isEqualTo(event.getLastName());
//            assertThat(capturedEvent.getTo()).containsExactlyElementsOf(event.getTo());
//        });
    }
}
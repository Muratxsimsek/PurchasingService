package com.emlakjet.purchasing.controller;


import com.emlakjet.purchasing.controller.Dto.InvoiceDto;
import com.emlakjet.purchasing.controller.Dto.LoginRequest;
import com.emlakjet.purchasing.controller.Dto.ProductDto;
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

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:truncate_tables.sql")
//@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@EmbeddedKafka(partitions = 1, topics = {"invoice_rejected_topic"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InvoiceControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private String tokenForUser1;
    private String tokenForUser2;

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
    public void setUp() throws InterruptedException, JsonProcessingException {
    }


    @Test
    public void should_submitInvoice_Add1Invoice_GetSuccessMessage() throws Exception {
        //LIMIT = 2000
        InvoiceDto invoice = InvoiceDto.builder().firstName("John").lastName("John").email("john@doe.com").amount(500.0).productName("Laptop").billNo("TR000123").build();

        given()
                .header("Authorization", "Bearer " + tokenForUser1)
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(invoice))
                .when()
                .post("/api/invoices")
                .then()
                .log().everything()
                .statusCode(200)
                .body("message", equalTo("Invoice accepted"));
    }

    @Test
    public void should_submitInvoice_Add2Invoices_GetSuccessMessage() throws Exception {
        //LIMIT = 2000
        InvoiceDto invoice = InvoiceDto.builder().firstName("John").lastName("John").email("john@doe.com").amount(700.0).productName("Laptop").billNo("TR000123").build();

        given()
                .header("Authorization", "Bearer " + tokenForUser1)
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(invoice))
                .when()
                .post("/api/invoices")
                .then()
                .log().everything()
                .statusCode(200)
                .body("message", equalTo("Invoice accepted"));

        given()
                .header("Authorization", "Bearer " + tokenForUser1)
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(invoice))
                .when()
                .post("/api/invoices")
                .then()
                .log().everything()
                .statusCode(200)
                .body("message", equalTo("Invoice accepted"));

    }

    @Test
    public void should_submitInvoice_Add3Invoices_GetLimitFailureExceeded() throws Exception {
        //LIMIT = 2000
        InvoiceDto invoice = InvoiceDto.builder().firstName("John").lastName("John").email("john@doe.com").amount(700.0).productName("Laptop").billNo("TR000123").build();

        given()
                .header("Authorization", "Bearer " + tokenForUser1)
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(invoice))
                .when()
                .post("/api/invoices")
                .then()
                .log().everything()
                .statusCode(200)
                .body("message", equalTo("Invoice accepted"));

        given()
                .header("Authorization", "Bearer " + tokenForUser1)
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(invoice))
                .when()
                .post("/api/invoices")
                .then()
                .log().everything()
                .statusCode(200)
                .body("message", equalTo("Invoice accepted"));


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
    }

    @Test
    public void should_submitInvoice_Add1Invoice_GetLimitFailureExceeded() throws Exception {
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

    }

    @Test
    public void should_submitInvoice_Add1Invoice_GetProductNotFoundException() throws Exception {
        //LIMIT = 2000
        InvoiceDto invoice = InvoiceDto.builder().firstName("John").lastName("John").email("john@doe.com").amount(500.0).productName("Monitor").billNo("TR000123").build();

        given()
                .header("Authorization", "Bearer " + tokenForUser1)
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(invoice))
                .when()
                .post("/api/invoices")
                .then()
                .log().everything()
                .statusCode(500)
                .body("message", equalTo("Product not found"));
    }


}

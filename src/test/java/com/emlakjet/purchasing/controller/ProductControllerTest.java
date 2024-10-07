package com.emlakjet.purchasing.controller;

import com.emlakjet.purchasing.controller.Dto.LoginRequest;
import com.emlakjet.purchasing.controller.Dto.ProductDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:truncate_tables.sql")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EmbeddedKafka(partitions = 1, topics = {"invoice_rejected_topic"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductControllerTest {

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

    @Order(1)
    @Test
    public void should_getAllProducts_FindAllProducts_Get3Products() throws Exception {
        //LIMIT = 2000

        given()
                .header("Authorization", "Bearer " + tokenForUser1)
                .contentType("application/json")
                .when()
                .get("/api/products")
                .then()
                .log().everything()
                .statusCode(200)
                .body("data", hasItems(
                        allOf(
                                hasEntry("name", "Laptop"),
                                hasEntry("description", "I will use it on the project of company")
                        ),
                        allOf(
                                hasEntry("name", "Pen"),
                                hasEntry("description", "I will need to take notes")
                        ),
                        allOf(
                                hasEntry("name", "Marcus Aurelius Book"),
                                hasEntry("description", "for private usage")
                        )
                ))
                .body("message", equalTo(null));
    }

    @Order(2)
    @Test
    public void should_getProductByName_FindSpecialProduct_GetProductNotFoundException() throws Exception {
        //LIMIT = 2000
        ProductDto productDto = ProductDto.builder().name("Rose").description("Red Rose").build();

        given()
                .header("Authorization", "Bearer " + tokenForUser1)
                .contentType("application/json")
                .when()
                .get("/api/products/"+productDto.getName())
                .then()
                .log().everything()
                .statusCode(500)
                .body("message", equalTo("Product not found"));
    }

    @Order(3)
    @Test
    public void should_updateProduct_UpdateSpecialProduct_GetUpdatedProduct() throws Exception {
        //LIMIT = 2000
        ProductDto productDto = ProductDto.builder().name("Rose").description("Red Rose").build();

        given()
                .header("Authorization", "Bearer " + tokenForUser1)
                .contentType("application/json")
                .body(objectMapper.writeValueAsString(productDto))
                .when()
                .put("/api/products/Laptop")
                .then()
                .log().everything()
                .statusCode(200)
                .body("data.name", equalTo("Laptop"))
                .body("data.description", equalTo("Red Rose"))
                .body("message", equalTo(null));
    }

    @Order(4)
    @Test
    public void should_deleteProduct_DeleteSpecialProduct_VerifyDeletedProduct() throws Exception {
        //LIMIT = 2000

        given()
                .header("Authorization", "Bearer " + tokenForUser1)
                .contentType("application/json")
                .when()
                .delete("/api/products/Laptop")
                .then()
                .log().everything()
                .statusCode(200)
                .body("data", equalTo(null))
                .body("message", equalTo("Product Deleted"));
    }

    @Order(5)
    @Test
    public void should_deleteProduct_DeleteSpecialProduct_GetProductCantRemovedException() throws Exception {
        //LIMIT = 2000

        given()
                .header("Authorization", "Bearer " + tokenForUser1)
                .contentType("application/json")
                .when()
                .delete("/api/products/Magazine")
                .then()
                .log().everything()
                .statusCode(500)
                .body("data", equalTo(null))
                .body("message", equalTo("Product can not be removed"));
    }

}
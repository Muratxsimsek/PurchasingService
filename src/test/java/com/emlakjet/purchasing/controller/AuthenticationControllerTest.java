package com.emlakjet.purchasing.controller;


import com.emlakjet.purchasing.controller.Dto.LoginRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;


@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:truncate_tables.sql")
//@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@EmbeddedKafka(partitions = 1, topics = {"invoice_rejected_topic"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationControllerTest {

    @LocalServerPort
    private int port;

    private String tokenForUser1;

    @BeforeEach
    public void setUp() throws InterruptedException, JsonProcessingException {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        Thread.sleep(5000);
    }


    @Test
    public void should_login_TryLogin_GetToken() throws Exception {

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

        assertThat(tokenForUser1).isNotNull();
    }

    @Test
    public void should_login_TryLogin_GetNotAuthorizedException() throws Exception {

        LoginRequest loginRequest = new LoginRequest("John", "Doe", "john@doe.com", "xxxx23999");
        given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/api/auth/login")
                .then()
                .log().everything()
                .statusCode(500)
                .body("message", equalTo("User not authorized"));
    }


}

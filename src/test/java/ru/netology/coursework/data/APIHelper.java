package ru.netology.coursework.data;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class APIHelper {

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(8080)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private APIHelper() {
    }

    public static void sendRequestNoBody(CardInfo card, String path, int status) {
        given() // "дано"
                .spec(requestSpec)
                .body(card)
                .when()
                .post(path)
                .then()
                .statusCode(status);
    }

    public static void sendRequestWithBody(CardInfo card, String path, int status, String statusTransaction) {
        given() // "дано"
                .spec(requestSpec)
                .body(card)
                .when()
                .post(path)
                .then()
                .statusCode(status)
                .contentType(ContentType.JSON)
                .body("status", equalTo(statusTransaction))
                ;
    }

    @Value
    public static class CardInfo {
        String number;
        String year;
        String month;
        String holder;
        String cvc;
    }

}

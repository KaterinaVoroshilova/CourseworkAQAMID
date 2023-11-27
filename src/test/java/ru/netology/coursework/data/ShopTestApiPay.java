package ru.netology.coursework.data;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static ru.netology.coursework.data.SQLHelper.cleanDatabase;

public class ShopTestApiPay {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        cleanDatabase();
        SelenideLogger.removeListener("allure");
    }

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(8080)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @Test
    @DisplayName("Should approved card API pay")
    void shouldApprovedCardAPIPay() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/pay")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("status", equalTo("APPROVED"))
        ;
        Assertions.assertEquals("APPROVED", SQLHelper.getStatusPay());
    }

    @Test
    @DisplayName("Should declined card API pay")
    void shouldDeclinedCardAPIPay() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getDeclinedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/pay")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("status", equalTo("DECLINED"))
        ;
        Assertions.assertEquals("DECLINED", SQLHelper.getStatusPay());
    }

    @Test
    @DisplayName("Should not pay unValid card API")
    void shouldNotPayUnValidCardAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getUnValidNotNullCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/pay")
                .then()
                .statusCode(500)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay empty card API")
    void shouldNotPayEmptyCardAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo("", DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/pay")
                .then()
                .statusCode(500)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay null card API")
    void shouldNotPayNullCardAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getNullCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/pay")
                .then()
                .statusCode(500)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay empty request")
    void shouldNotPayEmptyRequest() {
        DataHelper.CardInfo card = new DataHelper.CardInfo("", "", "", "", "");
        String expectedId = SQLHelper.getIDPay();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/pay")
                .then()
                .statusCode(500)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay expired card in last year API")
    void shouldNotPayExpiredCardInLastYearAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateLastYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/pay")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with empty year API")
    void shouldNotPayWithEmptyYearAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), "", DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/pay")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with year with letters API")
    void shouldNotPayWithYearWithLettersAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), "year", DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/pay")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay expired card in last month API")
    void shouldNotPayExpiredCardInLastMonthAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateLastMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/pay")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with empty month API")
    void shouldNotPayWithEmptyMonthAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), "", DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/pay")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with unValid holder cyrillic API")
    void shouldNotPayWithMonthWithLettersAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getUnValidHolderCyr(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/pay")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with space in the start API")
    void shouldNotPayWithHolderWithSpaceInStartAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), " " + DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/pay")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with dot in the start API")
    void shouldNotPayWithHolderWithDotInStartAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), "." + DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/pay")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with hyphen in the start API")
    void shouldNotPayWithHolderWithHyphenInStartAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), "-" + DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/pay")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with space in the end API")
    void shouldNotPayWithHolderWithSpaceInTheEndAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder() + " ", DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/pay")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with dot in the end API")
    void shouldNotPayWithHolderWithDotInTheEndAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder() + ".", DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/pay")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with hyphen in the end API")
    void shouldNotPayWithHolderWithHyphenInTheEndAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder() + "-", DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/pay")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with numbers API")
    void shouldNotPayWithHolderWithNumbersAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder() + "123", DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/pay")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with special characters API")
    void shouldNotPayWithHolderWithSpecialCharactersAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder() + "@", DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/pay")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with empty holder API")
    void shouldNotPayWithEmptyHolderAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), "", DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/pay")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with CVC/CVV as 2 numbers and letter API")
    void shouldNotPayWithCVCAs2NumbersAndLetterAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateUnValidCVV2() + "f");
        String expectedId = SQLHelper.getIDPay();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/pay")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with CVC/CVV as 4 numbers API")
    void shouldNotPayWithCVCAs4NumbersAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateUnValidCVV4());
        String expectedId = SQLHelper.getIDPay();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/pay")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with CVC/CVV as 2 numbers API")
    void shouldNotPayWithCVCAs2NumbersAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateUnValidCVV2());
        String expectedId = SQLHelper.getIDPay();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/pay")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with empty CVC/CVV API")
    void shouldNotPayWithEmptyCVCAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), "");
        String expectedId = SQLHelper.getIDPay();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/pay")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

}

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

public class ShopTestApiCredit {
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
    @DisplayName("Should approved card API credit")
    void shouldApprovedCardAPICredit() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/credit")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("status", equalTo("APPROVED"))
        ;
        Assertions.assertEquals("APPROVED", SQLHelper.getStatusCredit());
    }

    @Test
    @DisplayName("Should declined card API")
    void shouldDeclinedCardAPICredit() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getDeclinedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/credit")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("status", equalTo("DECLINED"))
        ;
        Assertions.assertEquals("DECLINED", SQLHelper.getStatusCredit());
    }

    @Test
    @DisplayName("Should not credit unValid card API")
    void shouldNotCreditUnValidCardAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getUnValidNotNullCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/credit")
                .then()
                .statusCode(500)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit empty card API")
    void shouldNotCreditEmptyCardAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo("", DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/credit")
                .then()
                .statusCode(500)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit null card API")
    void shouldNotCreditNullCardAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getNullCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/credit")
                .then()
                .statusCode(500)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit empty request")
    void shouldNotCreditEmptyRequest() {
        DataHelper.CardInfo card = new DataHelper.CardInfo("", "", "", "", "");
        String expectedId = SQLHelper.getIDCredit();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/credit")
                .then()
                .statusCode(500)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit expired card in last year API")
    void shouldNotCreditExpiredCardInLastYearAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateLastYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/credit")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with empty year API")
    void shouldNotCreditWithEmptyYearAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), "", DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/credit")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with year with letters API")
    void shouldNotCreditWithYearWithLettersAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), "year", DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/credit")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit expired card in last month API")
    void shouldNotCreditExpiredCardInLastMonthAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateLastMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/credit")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with empty month API")
    void shouldNotCreditWithEmptyMonthAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), "", DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/credit")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with unValid holder cyrillic API")
    void shouldNotCreditWithMonthWithLettersAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getUnValidHolderCyr(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/credit")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with space in the start API")
    void shouldNotCreditWithHolderWithSpaceInStartAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), " " + DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/credit")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with dot in the start API")
    void shouldNotCreditWithHolderWithDotInStartAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), "." + DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/credit")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with hyphen in the start API")
    void shouldNotCreditWithHolderWithHyphenInStartAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), "-" + DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/credit")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with space in the end API")
    void shouldNotCreditWithHolderWithSpaceInTheEndAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder() + " ", DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/credit")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with dot in the end API")
    void shouldNotCreditWithHolderWithDotInTheEndAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder() + ".", DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/credit")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with hyphen in the end API")
    void shouldNotCreditWithHolderWithHyphenInTheEndAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder() + "-", DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/credit")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with numbers API")
    void shouldNotCreditWithHolderWithNumbersAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder() + "123", DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/credit")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with special characters API")
    void shouldNotCreditWithHolderWithSpecialCharactersAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder() + "@", DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/credit")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with empty holder API")
    void shouldNotCreditWithEmptyHolderAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), "", DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/credit")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with CVC/CVV as 2 numbers and letter API")
    void shouldNotCreditWithCVCAs2NumbersAndLetterAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateUnValidCVV2() + "f");
        String expectedId = SQLHelper.getIDCredit();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/credit")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with CVC/CVV as 4 numbers API")
    void shouldNotCreditWithCVCAs4NumbersAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateUnValidCVV4());
        String expectedId = SQLHelper.getIDCredit();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/credit")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with CVC/CVV as 2 numbers API")
    void shouldNotCreditWithCVCAs2NumbersAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateUnValidCVV2());
        String expectedId = SQLHelper.getIDCredit();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/credit")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with empty CVC/CVV API")
    void shouldNotCreditWithEmptyCVCAPI() {
        DataHelper.CardInfo card = new DataHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), "");
        String expectedId = SQLHelper.getIDCredit();
        given()
                .spec(requestSpec)
                .when()
                .body(card)
                .post("/api/v1/credit")
                .then()
                .statusCode(400)
        ;
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

}


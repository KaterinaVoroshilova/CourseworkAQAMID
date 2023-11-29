package ru.netology.coursework.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import ru.netology.coursework.data.APIHelper;
import ru.netology.coursework.data.DataHelper;
import ru.netology.coursework.data.SQLHelper;

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

    @Test
    @DisplayName("Should approved card API pay")
    void shouldApprovedCardAPIPay() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        APIHelper.sendRequestWithBody(card, "/api/v1/pay", 200, "APPROVED");
        Assertions.assertEquals("APPROVED", SQLHelper.getStatusPay());
    }

    @Test
    @DisplayName("Should declined card API pay")
    void shouldDeclinedCardAPIPay() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getDeclinedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        APIHelper.sendRequestWithBody(card, "/api/v1/pay", 200, "DECLINED");
        Assertions.assertEquals("DECLINED", SQLHelper.getStatusPay());
    }

    @Test
    @DisplayName("Should not pay unValid card API")
    void shouldNotPayUnValidCardAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getUnValidNotNullCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        APIHelper.sendRequestNoBody(card, "/api/v1/pay", 500);
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay empty card API")
    void shouldNotPayEmptyCardAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo("", DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        APIHelper.sendRequestNoBody(card, "/api/v1/pay", 500);
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay null card API")
    void shouldNotPayNullCardAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getNullCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        APIHelper.sendRequestNoBody(card, "/api/v1/pay", 500);
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay empty request")
    void shouldNotPayEmptyRequest() {
        APIHelper.CardInfo card = new APIHelper.CardInfo("", "", "", "", "");
        String expectedId = SQLHelper.getIDPay();
        APIHelper.sendRequestNoBody(card, "/api/v1/pay", 500);
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay expired card in last year API")
    void shouldNotPayExpiredCardInLastYearAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateLastYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        APIHelper.sendRequestNoBody(card, "/api/v1/pay", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with empty year API")
    void shouldNotPayWithEmptyYearAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), "", DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        APIHelper.sendRequestNoBody(card, "/api/v1/pay", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with year with letters API")
    void shouldNotPayWithYearWithLettersAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), "year", DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        APIHelper.sendRequestNoBody(card, "/api/v1/pay", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay expired card in last month API")
    void shouldNotPayExpiredCardInLastMonthAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateLastMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        APIHelper.sendRequestNoBody(card, "/api/v1/pay", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with empty month API")
    void shouldNotPayWithEmptyMonthAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), "", DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        APIHelper.sendRequestNoBody(card, "/api/v1/pay", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with unValid holder cyrillic API")
    void shouldNotPayWithMonthWithLettersAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getUnValidHolderCyr(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        APIHelper.sendRequestNoBody(card, "/api/v1/pay", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with space in the start API")
    void shouldNotPayWithHolderWithSpaceInStartAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), " " + DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        APIHelper.sendRequestNoBody(card, "/api/v1/pay", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with dot in the start API")
    void shouldNotPayWithHolderWithDotInStartAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), "." + DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        APIHelper.sendRequestNoBody(card, "/api/v1/pay", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with hyphen in the start API")
    void shouldNotPayWithHolderWithHyphenInStartAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), "-" + DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        APIHelper.sendRequestNoBody(card, "/api/v1/pay", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with space in the end API")
    void shouldNotPayWithHolderWithSpaceInTheEndAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder() + " ", DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        APIHelper.sendRequestNoBody(card, "/api/v1/pay", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with dot in the end API")
    void shouldNotPayWithHolderWithDotInTheEndAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder() + ".", DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        APIHelper.sendRequestNoBody(card, "/api/v1/pay", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with hyphen in the end API")
    void shouldNotPayWithHolderWithHyphenInTheEndAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder() + "-", DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        APIHelper.sendRequestNoBody(card, "/api/v1/pay", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with numbers API")
    void shouldNotPayWithHolderWithNumbersAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder() + "123", DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        APIHelper.sendRequestNoBody(card, "/api/v1/pay", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with special characters API")
    void shouldNotPayWithHolderWithSpecialCharactersAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder() + "@", DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        APIHelper.sendRequestNoBody(card, "/api/v1/pay", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with empty holder API")
    void shouldNotPayWithEmptyHolderAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), "", DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDPay();
        APIHelper.sendRequestNoBody(card, "/api/v1/pay", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with CVC/CVV as 2 numbers and letter API")
    void shouldNotPayWithCVCAs2NumbersAndLetterAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateUnValidCVV2() + "f");
        String expectedId = SQLHelper.getIDPay();
        APIHelper.sendRequestNoBody(card, "/api/v1/pay", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with CVC/CVV as 4 numbers API")
    void shouldNotPayWithCVCAs4NumbersAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateUnValidCVV4());
        String expectedId = SQLHelper.getIDPay();
        APIHelper.sendRequestNoBody(card, "/api/v1/pay", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with CVC/CVV as 2 numbers API")
    void shouldNotPayWithCVCAs2NumbersAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateUnValidCVV2());
        String expectedId = SQLHelper.getIDPay();
        APIHelper.sendRequestNoBody(card, "/api/v1/pay", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with empty CVC/CVV API")
    void shouldNotPayWithEmptyCVCAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), "");
        String expectedId = SQLHelper.getIDPay();
        APIHelper.sendRequestNoBody(card, "/api/v1/pay", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

}

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


    @Test
    @DisplayName("Should approved card API credit")
    void shouldApprovedCardAPICredit() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        APIHelper.sendRequestWithBody(card, "/api/v1/credit", 200, "APPROVED");
        Assertions.assertEquals("APPROVED", SQLHelper.getStatusCredit());
    }

    @Test
    @DisplayName("Should declined card API")
    void shouldDeclinedCardAPICredit() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getDeclinedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        APIHelper.sendRequestWithBody(card, "/api/v1/credit", 200, "DECLINED");
        Assertions.assertEquals("DECLINED", SQLHelper.getStatusCredit());
    }

    @Test
    @DisplayName("Should not credit unValid card API")
    void shouldNotCreditUnValidCardAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getUnValidNotNullCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        APIHelper.sendRequestNoBody(card, "/api/v1/credit", 500);
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit empty card API")
    void shouldNotCreditEmptyCardAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo("", DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        APIHelper.sendRequestNoBody(card, "/api/v1/credit", 500);
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit null card API")
    void shouldNotCreditNullCardAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getNullCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        APIHelper.sendRequestNoBody(card, "/api/v1/credit", 500);
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit empty request")
    void shouldNotCreditEmptyRequest() {
        APIHelper.CardInfo card = new APIHelper.CardInfo("", "", "", "", "");
        String expectedId = SQLHelper.getIDCredit();
        APIHelper.sendRequestNoBody(card, "/api/v1/credit", 500);
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit expired card in last year API")
    void shouldNotCreditExpiredCardInLastYearAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateLastYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        APIHelper.sendRequestNoBody(card, "/api/v1/credit", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with empty year API")
    void shouldNotCreditWithEmptyYearAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), "", DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        APIHelper.sendRequestNoBody(card, "/api/v1/credit", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with year with letters API")
    void shouldNotCreditWithYearWithLettersAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), "year", DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        APIHelper.sendRequestNoBody(card, "/api/v1/credit", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit expired card in last month API")
    void shouldNotCreditExpiredCardInLastMonthAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateLastMonth(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        APIHelper.sendRequestNoBody(card, "/api/v1/credit", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with empty month API")
    void shouldNotCreditWithEmptyMonthAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), "", DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        APIHelper.sendRequestNoBody(card, "/api/v1/credit", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with unValid holder cyrillic API")
    void shouldNotCreditWithMonthWithLettersAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getUnValidHolderCyr(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        APIHelper.sendRequestNoBody(card, "/api/v1/credit", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with space in the start API")
    void shouldNotCreditWithHolderWithSpaceInStartAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), " " + DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        APIHelper.sendRequestNoBody(card, "/api/v1/credit", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with dot in the start API")
    void shouldNotCreditWithHolderWithDotInStartAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), "." + DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        APIHelper.sendRequestNoBody(card, "/api/v1/credit", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with hyphen in the start API")
    void shouldNotCreditWithHolderWithHyphenInStartAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), "-" + DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        APIHelper.sendRequestNoBody(card, "/api/v1/credit", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with space in the end API")
    void shouldNotCreditWithHolderWithSpaceInTheEndAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder() + " ", DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        APIHelper.sendRequestNoBody(card, "/api/v1/credit", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with dot in the end API")
    void shouldNotCreditWithHolderWithDotInTheEndAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder() + ".", DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        APIHelper.sendRequestNoBody(card, "/api/v1/credit", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with hyphen in the end API")
    void shouldNotCreditWithHolderWithHyphenInTheEndAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder() + "-", DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        APIHelper.sendRequestNoBody(card, "/api/v1/credit", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with numbers API")
    void shouldNotCreditWithHolderWithNumbersAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder() + "123", DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        APIHelper.sendRequestNoBody(card, "/api/v1/credit", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with special characters API")
    void shouldNotCreditWithHolderWithSpecialCharactersAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder() + "@", DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        APIHelper.sendRequestNoBody(card, "/api/v1/credit", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with empty holder API")
    void shouldNotCreditWithEmptyHolderAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), "", DataHelper.generateValidCVV());
        String expectedId = SQLHelper.getIDCredit();
        APIHelper.sendRequestNoBody(card, "/api/v1/credit", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with CVC/CVV as 2 numbers and letter API")
    void shouldNotCreditWithCVCAs2NumbersAndLetterAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateUnValidCVV2() + "f");
        String expectedId = SQLHelper.getIDCredit();
        APIHelper.sendRequestNoBody(card, "/api/v1/credit", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with CVC/CVV as 4 numbers API")
    void shouldNotCreditWithCVCAs4NumbersAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateUnValidCVV4());
        String expectedId = SQLHelper.getIDCredit();
        APIHelper.sendRequestNoBody(card, "/api/v1/credit", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with CVC/CVV as 2 numbers API")
    void shouldNotCreditWithCVCAs2NumbersAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), DataHelper.generateUnValidCVV2());
        String expectedId = SQLHelper.getIDCredit();
        APIHelper.sendRequestNoBody(card, "/api/v1/credit", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with empty CVC/CVV API")
    void shouldNotCreditWithEmptyCVCAPI() {
        APIHelper.CardInfo card = new APIHelper.CardInfo(DataHelper.getApprovedCardNumber(), DataHelper.generateYear(), DataHelper.generateMonth(), DataHelper.getValidHolder(), "");
        String expectedId = SQLHelper.getIDCredit();
        APIHelper.sendRequestNoBody(card, "/api/v1/credit", 400);
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

}


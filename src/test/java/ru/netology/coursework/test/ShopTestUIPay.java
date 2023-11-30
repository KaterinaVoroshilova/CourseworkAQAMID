package ru.netology.coursework.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.coursework.data.DataHelper;
import ru.netology.coursework.data.SQLHelper;
import ru.netology.coursework.page.PaymentPage;
import ru.netology.coursework.page.StartPage;

import static com.codeborne.selenide.Selenide.*;
import static ru.netology.coursework.data.SQLHelper.cleanDatabase;

public class ShopTestUIPay {
    StartPage startPage;
    PaymentPage payment;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        cleanDatabase();
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:8080");
        startPage = new StartPage();
        payment = startPage.goToPaymentPage();
    }

    @Test
    @DisplayName("Should approved pay")
    void shouldApprovedPay() {
        payment.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        payment.waitNotificationSuccessVisible();
        Assertions.assertEquals("APPROVED", SQLHelper.getStatusPay());
    }

    @Test
    @DisplayName("Should declined pay")
    void shouldDeclinedPay() {
        payment.putData(DataHelper.getDeclinedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        payment.waitNotificationFailedVisible();
        Assertions.assertEquals("DECLINED", SQLHelper.getStatusPay());
    }

    @Test
    @DisplayName("Should not pay nullCard")
    void shouldNotPayNullCard() {
        String expectedId = SQLHelper.getIDPay();
        payment.putData(DataHelper.getNullCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        payment.waitNotificationFailedVisible();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay unValid Card")
    void shouldNotPayUnValidCard() {
        String expectedId = SQLHelper.getIDPay();
        payment.putData(DataHelper.getUnValidNotNullCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        payment.waitNotificationFailedVisible();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay empty form")
    void shouldNotPayEmptyForm() {
        String expectedId = SQLHelper.getIDPay();
        payment.putData("", "", "", "", "");
        payment.waitNotificationWrongFormatCard();
        payment.waitNotificationWrongFormatMonth();
        payment.waitNotificationWrongFormatYear();
        payment.waitNotificationEmptyHolderError();
        payment.waitNotificationWrongFormatCVV();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay empty CardNumber")
    void shouldNotPayEmptyCardNumber() {
        String expectedId = SQLHelper.getIDPay();
        payment.putData("", DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        payment.waitNotificationWrongFormatCard();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay expired card in last month")
    void shouldNotPayExpiredCardInLastMonth() {
        String expectedId = SQLHelper.getIDPay();
        payment.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateLastMonth(), DataHelper.generateYear(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        payment.waitNotificationValidityError();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with month equal 13")
    void shouldNotPayWithMonthEqual13() {
        String expectedId = SQLHelper.getIDPay();
        payment.putData(DataHelper.getApprovedCardNumber(), "13", DataHelper.generateYear(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        payment.waitNotificationValidityError();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with month equal 00")
    void shouldNotPayWithMonthEqual00() {
        String expectedId = SQLHelper.getIDPay();
        payment.putData(DataHelper.getApprovedCardNumber(), "00", DataHelper.generateYear(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        payment.waitNotificationValidityError();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with empty month")
    void shouldNotPayWithEmptyMoth() {
        String expectedId = SQLHelper.getIDPay();
        payment.putData(DataHelper.getApprovedCardNumber(), "", DataHelper.generateYear(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        payment.waitNotificationWrongFormatMonth();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with unValid month")
    void shouldNotPayWithUnValidMoth() {
        String expectedId = SQLHelper.getIDPay();
        payment.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateUnValidMonth(), DataHelper.generateYear(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        payment.waitNotificationWrongFormatMonth();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay expired card in last year")
    void shouldNotPayExpiredCardInLastYear() {
        String expectedId = SQLHelper.getIDPay();
        payment.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateLastYear(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        payment.waitNotificationCardExpiredError();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with empty year")
    void shouldNotPayWithEmptyYear() {
        String expectedId = SQLHelper.getIDPay();
        payment.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), "", DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        payment.waitNotificationWrongFormatYear();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with unValid year")
    void shouldNotPayWithUnValidYear() {
        String expectedId = SQLHelper.getIDPay();
        payment.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateUnValidYear(), DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        payment.waitNotificationWrongFormatYear();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with empty holder")
    void shouldNotPayWithEmptyHolder() {
        String expectedId = SQLHelper.getIDPay();
        payment.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), "", DataHelper.generateValidCVV());
        payment.waitNotificationEmptyHolderError();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder cyrillic")
    void shouldNotPayWithHolderCyrillic() {
        String expectedId = SQLHelper.getIDPay();
        payment.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getUnValidHolderCyr(), DataHelper.generateValidCVV());
        payment.waitNotificationWrongFormatHolder();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with space in the start")
    void shouldNotPayWithHolderWithSpaceInStart() {
        String expectedId = SQLHelper.getIDPay();
        payment.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), " " + DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        payment.waitNotificationWrongFormatHolder();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with dot in the start")
    void shouldNotPayWithHolderWithDotInStart() {
        String expectedId = SQLHelper.getIDPay();
        payment.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), "." + DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        payment.waitNotificationWrongFormatHolder();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with hyphen in the start")
    void shouldNotPayWithHolderWithHyphenInStart() {
        String expectedId = SQLHelper.getIDPay();
        payment.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), "-" + DataHelper.getValidHolder(), DataHelper.generateValidCVV());
        payment.waitNotificationWrongFormatHolder();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with space in the end")
    void shouldNotPayWithHolderWithSpaceInTheEnd() {
        String expectedId = SQLHelper.getIDPay();
        payment.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder() + " ", DataHelper.generateValidCVV());
        payment.waitNotificationWrongFormatHolder();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with dot in the end")
    void shouldNotPayWithHolderWithDotInTheEnd() {
        String expectedId = SQLHelper.getIDPay();
        payment.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder() + ".", DataHelper.generateValidCVV());
        payment.waitNotificationWrongFormatHolder();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with hyphen in the end")
    void shouldNotPayWithHolderWithHyphenInTheEnd() {
        String expectedId = SQLHelper.getIDPay();
        payment.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder() + "-", DataHelper.generateValidCVV());
        payment.waitNotificationWrongFormatHolder();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with number")
    void shouldNotPayWithHolderWithNumber() {
        String expectedId = SQLHelper.getIDPay();
        payment.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder() + "1", DataHelper.generateValidCVV());
        payment.waitNotificationWrongFormatHolder();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with special characters")
    void shouldNotPayWithHolderWithSpecialCharacters() {
        String expectedId = SQLHelper.getIDPay();
        payment.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder() + "@", DataHelper.generateValidCVV());
        payment.waitNotificationWrongFormatHolder();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder equals one symbol")
    void shouldNotPayWithHolderEqualsOneSymbol() {
        String expectedId = SQLHelper.getIDPay();
        payment.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), "f", DataHelper.generateValidCVV());
        payment.waitNotificationWrongFormatHolder();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with empty CVC/CVV")
    void shouldNotPayWithEmptyCVC() {
        String expectedId = SQLHelper.getIDPay();
        payment.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder(), "");
        payment.waitNotificationWrongFormatCVV();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with CVC/CVV as 2 numbers")
    void shouldNotPayWithCVCAs2Numbers() {
        String expectedId = SQLHelper.getIDPay();
        payment.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder(), DataHelper.generateUnValidCVV2());
        payment.waitNotificationWrongFormatCVV();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with CVC/CVV as 4 numbers")
    void shouldNotPayWithCVCAs4Numbers() {
        String cvv4 = DataHelper.generateUnValidCVV4();
        String expectedCVV = cvv4.substring(0, 3);
        payment.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder(), cvv4);
        payment.getCVVAssert(expectedCVV);
    }

    @Test
    @DisplayName("Should not pay with CVC/CVV as 2 numbers and letter")
    void shouldNotPayWithCVCAs2NumbersAndLetter() {
        String expectedId = SQLHelper.getIDPay();
        String cvv2 = DataHelper.generateUnValidCVV2();
        payment.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder(), cvv2 + "f");
        payment.waitNotificationWrongFormatCVV();
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

}

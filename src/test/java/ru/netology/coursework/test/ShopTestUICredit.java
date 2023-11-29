package ru.netology.coursework.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.coursework.data.DataHelper;
import ru.netology.coursework.data.SQLHelper;
import ru.netology.coursework.page.CreditPage;
import ru.netology.coursework.page.PaymentPage;
import ru.netology.coursework.page.StartPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.coursework.data.SQLHelper.cleanDatabase;

public class ShopTestUICredit {
    StartPage startPage;
    CreditPage credit;

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
        credit = startPage.goToCreditPage();
    }

    @Test
    @DisplayName("Should approved credit")
    void shouldApprovedCredit() {
        credit.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder(),DataHelper.generateValidCVV());
        credit.waitNotificationSuccessVisible();
        Assertions.assertEquals("APPROVED", SQLHelper.getStatusCredit());
    }

    @Test
    @DisplayName("Should declined credit")
    void shouldDeclinedCredit() {
        credit.putData(DataHelper.getDeclinedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder(),DataHelper.generateValidCVV());
        credit.waitNotificationFailedVisible();
        Assertions.assertEquals("DECLINED", SQLHelper.getStatusCredit());
    }

    @Test
    @DisplayName("Should not credit nullCard")
    void shouldNotPayNullCard() {
        String expectedId = SQLHelper.getIDCredit();
        credit.putData(DataHelper.getNullCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder(),DataHelper.generateValidCVV());
        credit.waitNotificationFailedVisible();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit unValid Card")
    void shouldNotCreditUnValidCard() {
        String expectedId = SQLHelper.getIDCredit();
        credit.putData(DataHelper.getUnValidNotNullCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder(),DataHelper.generateValidCVV());
        credit.waitNotificationFailedVisible();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit empty form")
    void shouldNotCreditEmptyForm() {
        String expectedId = SQLHelper.getIDCredit();
        credit.putData("","","","","");
        credit.waitNotificationWrongFormatCard();
        credit.waitNotificationWrongFormatMonth();
        credit.waitNotificationWrongFormatYear();
        credit.waitNotificationEmptyHolderError();
        credit.waitNotificationWrongFormatCVV();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit empty CardNumber")
    void shouldNotCreditEmptyCardNumber() {
        String expectedId = SQLHelper.getIDCredit();
        credit.putData("", DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder(),DataHelper.generateValidCVV());
        credit.waitNotificationWrongFormatCard();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit expired card in last month")
    void shouldNotCreditExpiredCardInLastMonth() {
        String expectedId = SQLHelper.getIDCredit();
        credit.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateLastMonth(), DataHelper.generateYear(), DataHelper.getValidHolder(),DataHelper.generateValidCVV());
        credit.waitNotificationValidityError();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with month equal 13")
    void shouldNotCreditWithMonthEqual13() {
        String expectedId = SQLHelper.getIDCredit();
        credit.putData(DataHelper.getApprovedCardNumber(), "13", DataHelper.generateYear(), DataHelper.getValidHolder(),DataHelper.generateValidCVV());
        credit.waitNotificationValidityError();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with month equal 00")
    void shouldNotCreditWithMonthEqual00() {
        String expectedId = SQLHelper.getIDCredit();
        credit.putData(DataHelper.getApprovedCardNumber(), "00", DataHelper.generateYear(), DataHelper.getValidHolder(),DataHelper.generateValidCVV());
        credit.waitNotificationValidityError();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with empty month")
    void shouldNotPayWithEmptyMoth() {
        String expectedId = SQLHelper.getIDCredit();
        credit.putData(DataHelper.getApprovedCardNumber(), "", DataHelper.generateYear(), DataHelper.getValidHolder(),DataHelper.generateValidCVV());
        credit.waitNotificationWrongFormatMonth();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with unValid month")
    void shouldNotCreditWithUnValidMoth() {
        String expectedId = SQLHelper.getIDCredit();
        credit.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateUnValidMonth(), DataHelper.generateYear(), DataHelper.getValidHolder(),DataHelper.generateValidCVV());
        credit.waitNotificationWrongFormatMonth();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit expired card in last year")
    void shouldNotCreditExpiredCardInLastYear() {
        String expectedId = SQLHelper.getIDCredit();
        credit.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateLastYear(), DataHelper.getValidHolder(),DataHelper.generateValidCVV());
        credit.waitNotificationCardExpiredError();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with empty year")
    void shouldNotCreditWithEmptyYear() {
        String expectedId = SQLHelper.getIDCredit();
        credit.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), "", DataHelper.getValidHolder(),DataHelper.generateValidCVV());
        credit.waitNotificationWrongFormatYear();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with unValid year")
    void shouldNotCreditWithUnValidYear() {
        String expectedId = SQLHelper.getIDCredit();
        credit.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateUnValidYear(), DataHelper.getValidHolder(),DataHelper.generateValidCVV());
        credit.waitNotificationWrongFormatYear();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with empty holder")
    void shouldNotCreditWithEmptyHolder() {
        String expectedId = SQLHelper.getIDCredit();
        credit.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), "",DataHelper.generateValidCVV());
        credit.waitNotificationEmptyHolderError();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder cyrillic")
    void shouldNotCreditWithHolderCyrillic() {
        String expectedId = SQLHelper.getIDCredit();
        credit.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getUnValidHolderCyr(),DataHelper.generateValidCVV());
        credit.waitNotificationWrongFormatHolder();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with space in the start")
    void shouldNotCreditWithHolderWithSpaceInStart() {
        String expectedId = SQLHelper.getIDCredit();
        credit.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), " " + DataHelper.getValidHolder(),DataHelper.generateValidCVV());
        credit.waitNotificationWrongFormatHolder();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with dot in the start")
    void shouldNotCreditWithHolderWithDotInStart() {
        String expectedId = SQLHelper.getIDCredit();
        credit.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), "." + DataHelper.getValidHolder(),DataHelper.generateValidCVV());
        credit.waitNotificationWrongFormatHolder();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with hyphen in the start")
    void shouldNotCreditWithHolderWithHyphenInStart() {
        String expectedId = SQLHelper.getIDCredit();
        credit.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), "-" + DataHelper.getValidHolder(),DataHelper.generateValidCVV());
        credit.waitNotificationWrongFormatHolder();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with space in the end")
    void shouldNotCreditWithHolderWithSpaceInTheEnd() {
        String expectedId = SQLHelper.getIDCredit();
        credit.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder() + " ",DataHelper.generateValidCVV());
        credit.waitNotificationWrongFormatHolder();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with dot in the end")
    void shouldNotCreditWithHolderWithDotInTheEnd() {
        String expectedId = SQLHelper.getIDCredit();
        credit.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder() + ".",DataHelper.generateValidCVV());
        credit.waitNotificationWrongFormatHolder();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }
    @Test
    @DisplayName("Should not credit with holder with hyphen in the end")
    void shouldNotCreditWithHolderWithHyphenInTheEnd() {
        String expectedId = SQLHelper.getIDCredit();
        credit.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder() + "-",DataHelper.generateValidCVV());
        credit.waitNotificationWrongFormatHolder();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with number")
    void shouldNotCreditWithHolderWithNumber() {
        String expectedId = SQLHelper.getIDCredit();
        credit.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder() + "1",DataHelper.generateValidCVV());
        credit.waitNotificationWrongFormatHolder();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with special characters")
    void shouldNotCreditWithHolderWithSpecialCharacters() {
        String expectedId = SQLHelper.getIDCredit();
        credit.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder() + "@",DataHelper.generateValidCVV());
        credit.waitNotificationWrongFormatHolder();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder equals one symbol")
    void shouldNotCreditWithHolderEqualsOneSymbol() {
        String expectedId = SQLHelper.getIDCredit();
        credit.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), "f",DataHelper.generateValidCVV());
        credit.waitNotificationWrongFormatHolder();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with empty CVC/CVV")
    void shouldNotCreditWithEmptyCVC() {
        String expectedId = SQLHelper.getIDCredit();
        credit.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder(),"");
        credit.waitNotificationWrongFormatCVV();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with CVC/CVV as 2 numbers")
    void shouldNotCreditWithCVCAs2Numbers() {
        String expectedId = SQLHelper.getIDCredit();
        credit.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder(), DataHelper.generateUnValidCVV2());
        credit.waitNotificationWrongFormatCVV();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with CVC/CVV as 4 numbers")
    void shouldNotPayWithCVCAs4Numbers() {
        String cvv4 = DataHelper.generateUnValidCVV4();
        String expectedCVV = cvv4.substring(0, 3);
        String actualCVV = credit.putDataWithoutButtonClick(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder(), cvv4);
        Assertions.assertEquals(expectedCVV,actualCVV);
    }

    @Test
    @DisplayName("Should not credit with CVC/CVV as 2 numbers and letter")
    void shouldNotCreditWithCVCAs2NumbersAndLetter() {
        String cvv2 = DataHelper.generateUnValidCVV2();
        String expectedId = SQLHelper.getIDCredit();
        credit.putData(DataHelper.getApprovedCardNumber(), DataHelper.generateMonth(), DataHelper.generateYear(), DataHelper.getValidHolder(),cvv2 + "f");
        credit.waitNotificationWrongFormatCVV();
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

}

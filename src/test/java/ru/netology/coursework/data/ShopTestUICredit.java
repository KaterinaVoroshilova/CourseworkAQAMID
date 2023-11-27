package ru.netology.coursework.data;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.coursework.data.SQLHelper.cleanDatabase;

public class ShopTestUICredit {

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
        $$("button span.button__text").find(exactText("Купить в кредит")).click();
        $$("h3").get(1)
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Кредит по данным карты"));
    }


    @Test
    @DisplayName("Should approved credit")
    void shouldApprovedCredit() {
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".notification_status_ok .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Операция одобрена Банком."));
        Assertions.assertEquals("APPROVED",SQLHelper.getStatusCredit());
    }

    @Test
    @DisplayName("Should declined credit")
    void shouldDeclinedCredit() {
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getDeclinedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(" .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Ошибка! Банк отказал в проведении операции."));
        Assertions.assertEquals("DECLINED",SQLHelper.getStatusCredit());
    }

    @Test
    @DisplayName("Should not credit nullCard")
    void shouldNotCreditNullCard() {
        String expectedId = SQLHelper.getIDCredit();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getNullCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".notification_status_error .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Ошибка! Банк отказал в проведении операции."));
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit unValid Card")
    void shouldNotCreditUnValidCard() {
        String expectedId = SQLHelper.getIDCredit();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getUnValidNotNullCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".notification_status_error .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Ошибка! Банк отказал в проведении операции."));
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit empty form")
    void shouldNotCreditEmptyForm() {
        String expectedId = SQLHelper.getIDCredit();
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $$(".input__sub").get(0)
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        $$(".input__sub").get(1)
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        $$(".input__sub").get(2)
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        $$(".input__sub").get(3)
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Поле обязательно для заполнения"));
        $$(".input__sub").get(4)
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit empty CardNumber")
    void shouldNotCreditEmptyCardNumber() {
        String expectedId = SQLHelper.getIDCredit();
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $$(".input__sub").get(0)
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit expired card in last month")
    void shouldNotCreditExpiredCardInLastMonth() {
        String expectedId = SQLHelper.getIDCredit();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateLastMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверно указан срок действия карты"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with month equal 13")
    void shouldNotCreditWithMonthEqual13() {
        String expectedId = SQLHelper.getIDCredit();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue("13");
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверно указан срок действия карты"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with month equal 00")
    void shouldNotCreditWithMonthEqual00() {
        String expectedId = SQLHelper.getIDCredit();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue("00");
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверно указан срок действия карты"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with empty month")
    void shouldNotCreditWithEmptyMoth() {
        String expectedId = SQLHelper.getIDCredit();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with unValid month")
    void shouldNotCreditWithUnValidMoth() {
        String expectedId = SQLHelper.getIDCredit();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateUnValidMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit expired card in last year")
    void shouldNotCreditExpiredCardInLastYear() {
        String expectedId = SQLHelper.getIDCredit();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateLastYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Истёк срок действия карты"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with empty year")
    void shouldNotCreditWithEmptyYear() {
        String expectedId = SQLHelper.getIDCredit();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with unValid year")
    void shouldNotCreditWithUnValidYear() {
        String expectedId = SQLHelper.getIDCredit();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateUnValidYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with empty holder")
    void shouldNotCreditWithEmptyHolder() {
        String expectedId = SQLHelper.getIDCredit();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Поле обязательно для заполнения"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder cyrillic")
    void shouldNotCreditWithHolderCyrillic() {
        String expectedId = SQLHelper.getIDCredit();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getUnValidHolderCyr());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not credit with holder with space in the start")
    void shouldNotCreditWithHolderWithSpaceInStart() {
        String expectedId = SQLHelper.getIDCredit();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(" " + DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with dot in the start")
    void shouldNotCreditWithHolderWithDotInStart() {
        String expectedId = SQLHelper.getIDCredit();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue("." + DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with hyphen in the start")
    void shouldNotCreditWithHolderWithHyphenInStart() {
        String expectedId = SQLHelper.getIDCredit();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue("-" + DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with space in the end")
    void shouldNotCreditWithHolderWithSpaceInTheEnd() {
        String expectedId = SQLHelper.getIDCredit();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder() + " ");
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with dot in the end")
    void shouldNotCreditWithHolderWithDotInTheEnd() {
        String expectedId = SQLHelper.getIDCredit();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder() + ".");
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with number")
    void shouldNotCreditWithHolderWithNumber() {
        String expectedId = SQLHelper.getIDCredit();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder() + "1");
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder with special characters")
    void shouldNotCreditWithHolderWithSpecialCharacters() {
        String expectedId = SQLHelper.getIDCredit();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder() + "@");
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with holder equals one symbol")
    void shouldNotCreditWithHolderEqualsOneSymbol() {
        String expectedId = SQLHelper.getIDCredit();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue("f");
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with empty CVC/CVV")
    void shouldNotCreditWithEmptyCVC() {
        String expectedId = SQLHelper.getIDCredit();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with CVC/CVV as 2 numbers")
    void shouldNotCreditWithCVCAs2Numbers() {
        String expectedId = SQLHelper.getIDCredit();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateUnValidCVV2());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
    }

    @Test
    @DisplayName("Should not credit with CVC/CVV as 4 numbers")
    void shouldNotCreditWithCVCAs4Numbers() {
        String cvv4 = DataHelper.generateUnValidCVV4();
        String expectedCVV = cvv4.substring(0,3);
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(cvv4);
        Assertions.assertEquals(expectedCVV, $(".input [placeholder = '999']").getValue());
    }

    @Test
    @DisplayName("Should not credit with CVC/CVV as 2 numbers and letter")
    void shouldNotCreditWithCVCAs2NumbersAndLetter() {
        String expectedId = SQLHelper.getIDCredit();
        String cvv2 = DataHelper.generateUnValidCVV2();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(cvv2+"f");
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDCredit());
        Assertions.assertEquals(cvv2, $(".input [placeholder = '999']").getValue());
    }

}

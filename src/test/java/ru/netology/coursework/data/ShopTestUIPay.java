package ru.netology.coursework.data;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.coursework.data.SQLHelper.cleanDatabase;

public class ShopTestUIPay {

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
        $$("button span.button__text").find(exactText("Купить")).click();
        $$("h3").get(1)
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Оплата по карте"));
    }

    @Test
    @DisplayName("Should approved pay")
    void shouldApprovedPay() {
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".notification_status_ok .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Операция одобрена Банком."));
        Assertions.assertEquals("APPROVED", SQLHelper.getStatusPay());
    }

    @Test
    @DisplayName("Should declined pay")
    void shouldDeclinedPay() {
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getDeclinedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(" .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Ошибка! Банк отказал в проведении операции."));
        Assertions.assertEquals("DECLINED", SQLHelper.getStatusPay());
    }

    @Test
    @DisplayName("Should not pay nullCard")
    void shouldNotPayNullCard() {
        String expectedId = SQLHelper.getIDPay();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getNullCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".notification_status_error .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Ошибка! Банк отказал в проведении операции."));
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay unValid Card")
    void shouldNotPayUnValidCard() {
        String expectedId = SQLHelper.getIDPay();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getUnValidNotNullCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".notification_status_error .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Ошибка! Банк отказал в проведении операции."));
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay empty form")
    void shouldNotPayEmptyForm() {
        String expectedId = SQLHelper.getIDPay();
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
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay empty CardNumber")
    void shouldNotPayEmptyCardNumber() {
        String expectedId = SQLHelper.getIDPay();
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $$(".input__sub").get(0)
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay expired card in last month")
    void shouldNotPayExpiredCardInLastMonth() {
        String expectedId = SQLHelper.getIDPay();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateLastMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверно указан срок действия карты"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with month equal 13")
    void shouldNotPayWithMonthEqual13() {
        String expectedId = SQLHelper.getIDPay();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue("13");
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверно указан срок действия карты"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with month equal 00")
    void shouldNotPayWithMonthEqual00() {
        String expectedId = SQLHelper.getIDPay();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue("00");
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверно указан срок действия карты"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with empty month")
    void shouldNotPayWithEmptyMoth() {
        String expectedId = SQLHelper.getIDPay();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with unValid month")
    void shouldNotPayWithUnValidMoth() {
        String expectedId = SQLHelper.getIDPay();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateUnValidMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay expired card in last year")
    void shouldNotPayExpiredCardInLastYear() {
        String expectedId = SQLHelper.getIDPay();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateLastYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Истёк срок действия карты"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with empty year")
    void shouldNotPayWithEmptyYear() {
        String expectedId = SQLHelper.getIDPay();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with unValid year")
    void shouldNotPayWithUnValidYear() {
        String expectedId = SQLHelper.getIDPay();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateUnValidYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with empty holder")
    void shouldNotPayWithEmptyHolder() {
        String expectedId = SQLHelper.getIDPay();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Поле обязательно для заполнения"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder cyrillic")
    void shouldNotPayWithHolderCyrillic() {
        String expectedId = SQLHelper.getIDPay();
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
    @DisplayName("Should not pay with holder with space in the start")
    void shouldNotPayWithHolderWithSpaceInStart() {
        String expectedId = SQLHelper.getIDPay();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(" " + DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with dot in the start")
    void shouldNotPayWithHolderWithDotInStart() {
        String expectedId = SQLHelper.getIDPay();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue("." + DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with hyphen in the start")
    void shouldNotPayWithHolderWithHyphenInStart() {
        String expectedId = SQLHelper.getIDPay();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue("-" + DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with space in the end")
    void shouldNotPayWithHolderWithSpaceInTheEnd() {
        String expectedId = SQLHelper.getIDPay();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder() + " ");
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with dot in the end")
    void shouldNotPayWithHolderWithDotInTheEnd() {
        String expectedId = SQLHelper.getIDPay();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder() + ".");
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with number")
    void shouldNotPayWithHolderWithNumber() {
        String expectedId = SQLHelper.getIDPay();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder() + "1");
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder with special characters")
    void shouldNotPayWithHolderWithSpecialCharacters() {
        String expectedId = SQLHelper.getIDPay();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder() + "@");
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with holder equals one symbol")
    void shouldNotPayWithHolderEqualsOneSymbol() {
        String expectedId = SQLHelper.getIDPay();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue("f");
        $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with empty CVC/CVV")
    void shouldNotPayWithEmptyCVC() {
        String expectedId = SQLHelper.getIDPay();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with CVC/CVV as 2 numbers")
    void shouldNotPayWithCVCAs2Numbers() {
        String expectedId = SQLHelper.getIDPay();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(DataHelper.generateUnValidCVV2());
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
    }

    @Test
    @DisplayName("Should not pay with CVC/CVV as 4 numbers")
    void shouldNotPayWithCVCAs4Numbers() {
        String cvv4 = DataHelper.generateUnValidCVV4();
        String expectedCVV = cvv4.substring(0, 3);
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(cvv4);
        Assertions.assertEquals(expectedCVV, $(".input [placeholder = '999']").getValue());
    }

    @Test
    @DisplayName("Should not pay with CVC/CVV as 2 numbers and letter")
    void shouldNotPayWithCVCAs2NumbersAndLetter() {
        String expectedId = SQLHelper.getIDPay();
        String cvv2 = DataHelper.generateUnValidCVV2();
        $(".input [placeholder = '0000 0000 0000 0000']").setValue(DataHelper.getApprovedCardNumber());
        $(".input [placeholder = '08']").setValue(DataHelper.generateMonth());
        $(".input [placeholder = '22']").setValue(DataHelper.generateYear());
        $$(".input__control").get(3).setValue(DataHelper.getValidHolder());
        $(".input [placeholder = '999']").setValue(cvv2 + "f");
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(expectedId, SQLHelper.getIDPay());
        Assertions.assertEquals(cvv2, $(".input [placeholder = '999']").getValue());
    }

}

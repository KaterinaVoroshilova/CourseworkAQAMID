package ru.netology.coursework.page;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.coursework.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.by;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PaymentPage {
    private SelenideElement heading = $$("h3").get(1).shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldHave(exactText("Оплата по карте"));
    private SelenideElement cardNumber = $(".input [placeholder = '0000 0000 0000 0000']");
    private SelenideElement monthField = $(".input [placeholder = '08']");
    private SelenideElement yearField =  $(".input [placeholder = '22']");
    private SelenideElement holderField = $$(".input__control").get(3);
    private SelenideElement CVCField = $(".input [placeholder = '999']").setValue(DataHelper.generateValidCVV());
    private String getCVV = $(".input [placeholder = '999']").getValue();


    private SelenideElement approvedMessage =  $$(".notification__content").find(text("Операция одобрена Банком."));;
    private SelenideElement errorMessage =  $$(" .notification__content").find(text("Ошибка! Банк отказал в проведении операции."));
    private SelenideElement wrongFormatCard = $(byText("Неверный формат"));
    private SelenideElement wrongFormatMonth = $(byText("Неверный формат"));
    private SelenideElement wrongFormatYear = $(byText("Неверный формат"));
    private SelenideElement validityError =$(byText("Неверно указан срок действия карты"));
    private SelenideElement cardExpiredError = $(byText("Истёк срок действия карты"));
    private SelenideElement emptyHolderError = $(byText("Поле обязательно для заполнения"));
    private SelenideElement wrongFormatHolder = $(byText("Неверный формат"));
    private SelenideElement wrongFormatCVV = $(byText("Неверный формат"));
    private SelenideElement continueButton = $$("button span.button__text").find(exactText("Продолжить"));


    public PaymentPage() {
        heading.shouldBe(visible);
    }


    public void putData(String number, String month, String year, String holder, String CVC) {
        cardNumber.setValue(number);
        monthField.setValue(month);
        yearField.setValue(year);
        holderField.setValue(holder);
        CVCField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        CVCField.setValue(CVC);
        continueButton.click();
    }

    public String putDataWithoutButtonClick(String number, String month, String year, String holder, String CVC) {
        cardNumber.setValue(number);
        monthField.setValue(month);
        yearField.setValue(year);
        holderField.setValue(holder);
        CVCField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        CVCField.setValue(CVC);
        String CVV = getCVV;
        return CVV;
    }

    public void waitNotificationSuccessVisible() {
        approvedMessage.shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldBe(visible);
    }

    public void waitNotificationFailedVisible() {
        errorMessage.shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldBe(visible);
    }

    public void waitNotificationWrongFormatCard() {
        wrongFormatCard.shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldBe(visible);
    }

    public void waitNotificationWrongFormatMonth() {
        wrongFormatMonth.shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldBe(visible);
    }

    public void waitNotificationWrongFormatYear() {
        wrongFormatYear.shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldBe(visible);
    }

    public void waitNotificationValidityError() {
        validityError.shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldBe(visible);
    }

    public void waitNotificationCardExpiredError() {
        cardExpiredError.shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldBe(visible);
    }

    public void waitNotificationEmptyHolderError() {
        emptyHolderError.shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldBe(visible);
    }

    public void waitNotificationWrongFormatHolder() {
        wrongFormatHolder.shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldBe(visible);
    }
    public void waitNotificationWrongFormatCVV() {
        wrongFormatCVV.shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldBe(visible);
    }
}

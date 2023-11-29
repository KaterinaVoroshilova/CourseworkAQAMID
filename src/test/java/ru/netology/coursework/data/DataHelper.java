package ru.netology.coursework.data;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Random;
import java.util.Locale;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;

public class DataHelper {


    private DataHelper() {
    }

    public static String getApprovedCardNumber() {
        return ("1111 2222 3333 4444");
    }

    public static String getDeclinedCardNumber() {
        return ("5555 6666 7777 8888");
    }

    public static String getNullCardNumber() {
        return ("0000 0000 0000 0000");
    }

    public static String getUnValidNotNullCardNumber() {
        return ("1111 1111 1111 1111");
    }

    public static String generateYear() {
        var date = LocalDate.now();
        int yearInt = date.getYear();
        String yearAll = Integer.toString(yearInt);
        String year = yearAll.substring(2);
        return year;
    }

    public static String generateLastYear() {
        var date = LocalDate.now();
        int lastYearInt = date.getYear() - 1;
        String lastYearAll = Integer.toString(lastYearInt);
        String lastYear = lastYearAll.substring(2);
        return lastYear;
    }

    public static String generateMonth() {
        var date = LocalDate.now();
        int monthInt = date.getMonthValue();
        String monthAll = Integer.toString(monthInt);
        if (monthAll.length() == 1) {
            String month = "0" + monthAll;
        }
        String month = monthAll;
        return month;
    }

    public static String generateLastMonth() {
        var date = LocalDate.now();
        int lastMonthInt;
        if (date.getMonthValue() - 1 == 0) {
            lastMonthInt = 12;
        } else {
            lastMonthInt = date.getMonthValue() - 1;
        }
        String lastMonthAll = Integer.toString(lastMonthInt);
        if (lastMonthAll.length() == 1) {
            String month = "0" + lastMonthAll;
        }
        String lastMonth = lastMonthAll;
        return lastMonth;
    }

    public static String generateUnValidMonth() {
        final Faker faker = new Faker(new Locale("en"));
        return faker.numerify("#");
    }

    public static String generateUnValidYear() {
        final Faker faker = new Faker(new Locale("en"));
        return faker.numerify("#");
    }

    public static String getValidHolder() {
        final Faker faker = new Faker(new Locale("en"));
        return faker.name().fullName();
    }

    public static String getUnValidHolderCyr() {
        final Faker faker = new Faker(new Locale("ru"));
        return faker.name().fullName();
    }

    public static String generateValidCVV() {
        final Faker faker = new Faker(new Locale("en"));
        return faker.numerify("###");
    }

    public static String generateUnValidCVV4() {
        final Faker faker = new Faker(new Locale("en"));
        return faker.numerify("####");
    }

    public static String generateUnValidCVV2() {
        final Faker faker = new Faker(new Locale("en"));
        return faker.numerify("##");
    }


}

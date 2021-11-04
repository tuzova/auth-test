package ru.netology.testmode.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static ru.netology.testmode.data.DataGenerator.Registration.getActiveUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getBlockedUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getNotRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Condition.visible;

class AuthTest {

    @BeforeEach
    void setup() {
        Configuration.browser = "chrome";
        open("http://localhost:7777");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var activeUser = getActiveUser("active");
        $("[data-test-id='login'] input").setValue(activeUser.getLogin());
        $("[data-test-id='password'] input").setValue(activeUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $(withText("Личный кабинет")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getNotRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $(withText("Ошибка")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='error-notification'] div.notification__content").shouldBe(visible);
        $(withText("Неверно указан логин или пароль")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getBlockedUser("blocked");
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $(withText("Ошибка")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='error-notification'] div.notification__content").shouldBe(visible);
        $(withText("Пользователь заблокирован")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var activeUser = getActiveUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id='login'] input").setValue(wrongLogin);
        $("[data-test-id='password'] input").setValue(activeUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $(withText("Ошибка")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='error-notification'] div.notification__content").shouldBe(visible);
        $(withText("Неверно указан логин или пароль")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var activeUser = getActiveUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id='login'] input").setValue(activeUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $$("button").find(exactText("Продолжить")).click();
        $(withText("Ошибка")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='error-notification'] div.notification__content").shouldBe(visible);
        $(withText("Неверно указан логин или пароль")).shouldBe(visible);
    }
}


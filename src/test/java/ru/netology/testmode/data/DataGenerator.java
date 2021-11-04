package ru.netology.testmode.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

// cпецификация запроса
public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(7777)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static final Faker faker = new Faker(new Locale("ru"));


    private DataGenerator() {
    }

    // шаблон запроса
    private static void sendRequest(User user) {
        given()
                .spec(requestSpec) // cпецификация запроса
                .body(user) // передаём объект user
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    // шаблон объекта User
    @Value
    public static class User {
        String login;
        String password;
        String status;
    }

    // рандомный логин юзера
    public static String getRandomLogin() {
        String login = faker.name().username();
        return login;
    }

    // рандомный пароль юзера
    public static String getRandomPassword() {
        String password = faker.internet().password();
        return password;
    }

    public static class Registration {
        private Registration() {
        }

        // создаем активного юзера на основе шаблона
        public static User getActiveUser(String status) {
            User activeUser = new User("vasya", "password", "active");
            sendRequest(activeUser);
            return activeUser;
        }

        // создаем заблокированного юзера на основе шаблона
        public static User getBlockedUser(String status) {
            User blockedUser = new User("vasya", "password", "blocked");
            sendRequest(blockedUser);
            return blockedUser;
        }

        // создаем незарегистрированного юзера на основе шаблона
        public static User getNotRegisteredUser(String status) {
            User notRegisteredUser = new User(getRandomLogin(), getRandomPassword(), "active");
            return notRegisteredUser;
        }
    }
}


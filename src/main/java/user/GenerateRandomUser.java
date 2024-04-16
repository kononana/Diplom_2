package user;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;

public class GenerateRandomUser {
    public static Faker faker = new Faker();

    public static String getRandomEmail(){
        return faker.internet().emailAddress();
    }

    public static String getRandomPassword(){
        return faker.internet().password();
    }

    public static String getRandomName(){
        return faker.name().firstName();
    }

    @Step("Создание нового пользователя с рандомными данными")
    public static User createValidRandomUser() {
        return new User(
                getRandomName(),
               getRandomEmail(),
                getRandomPassword());
    }
    @Step("Создание пользователя с рандомными данными без логина")
    public static User createNoNameUser() {
        return new User(
                "",
                getRandomEmail(),
                getRandomPassword());
    }
    @Step("Создание пользователя с рандомными данными без почты")
    public static User createNoEmailUser() {
        return new User(
                getRandomName(),
                "",
                getRandomPassword());
    }
    @Step("Создание пользователя с рандомными данными без пароля")
    public static User createNoPasswordUser() {
        return new User(
                getRandomName(),
                getRandomEmail(),
                "");
    }
}

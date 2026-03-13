package login;

import domain.User;
import fixtures.PlaywrightTestCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LoginWithRegisteredUserTest extends PlaywrightTestCase {

    @Test
    @DisplayName("Should be able to login with registered user")
    void should_login_with_registered_user() {
        //Register a user via the API
        User user = User.randomUser();
        UserAPIClient userAPIClient = new UserAPIClient(page);
        userAPIClient.registerUser(user);
        LoginPage loginPage = new LoginPage(page);
        loginPage.open();
        loginPage.loginAs(user);
        Assertions.assertThat(loginPage.title()).isEqualTo("My account");
    }

    @Test
    @DisplayName("should reject a user if they provide wrong password")
    void should_reject_user_with_invalid_password() {
        User user = User.randomUser();
        UserAPIClient userAPIClient = new UserAPIClient(page);
        userAPIClient.registerUser(user);

        LoginPage loginPage = new LoginPage(page);
        loginPage.open();
        loginPage.loginAs(user.withPassword("wrong-password"));

        assertThat(loginPage.loginErrorMessage()).isEqualTo("Invalid email or password");
    }


}

/*
@date 30.11.2023
@author Sergey Bugaienko
*/

import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import repository.AccountRepository;
import repository.CurrencyRepository;
import repository.OperationRepository;
import repository.UserRepository;
import service.CurrencyService;
import service.ExchangeService;
import service.UserService;
import validators.exceptions.EmailValidateException;
import validators.exceptions.PasswordValidateException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CurrencyTests {

    private User activeUser;

    private final CurrencyService currencyService;
    private final UserService userService;
    private final ExchangeService exchangeService;

    public CurrencyTests() {
        this.currencyService = new CurrencyService(new CurrencyRepository(), new AccountRepository(), new OperationRepository());
        this.userService = new UserService(new UserRepository());
        this.exchangeService = new ExchangeService(userService, currencyService);
    }

    @BeforeEach
    void init() {

    }

    @ParameterizedTest
    @MethodSource("dataTestCreateUserValidData")
    public void testCreateUserValidData(String email, String password) throws EmailValidateException, PasswordValidateException {
        int size = userService.getAllUsers().size();
        Optional<User> userOptional = userService.createUser(email, password);
        List<User> allUsers = userService.getAllUsers();
        int newSize = allUsers.size();
        assertTrue(userOptional.isPresent());
        assertEquals(size + 1, newSize);
        assertTrue(allUsers.contains(userOptional.get()));

    }
    static Stream<Arguments> dataTestCreateUserValidData() {
        return Stream.of(
                Arguments.of("valid@email.com", "qwerty!1Q"),
                Arguments.of("valid2@test.net", "qwerty%2R"),
                Arguments.of("valid3@test.net", "ASDFGH1@a"),
                Arguments.of("valid2@test.net", "qwerty&2H")
        );
    }
    @ParameterizedTest
    @MethodSource("dataTestCreateUserErrorEmailData")
    public void testCreateUserErrorEmailData(String email, String password) throws EmailValidateException, PasswordValidateException {
        assertThrows(EmailValidateException.class, () -> userService.createUser(email, password));
    }

    static Stream<Arguments> dataTestCreateUserErrorEmailData(){
        return Stream.of(
                Arguments.of("invalid.email.com", "QWERTY!1q"),
                Arguments.of("invalid@ema@il.com", "QWERTY!1q"),
                Arguments.of("invalid@email.co.m", "QWERTY!1q"),
                Arguments.of("invalid@email.com.", "QWERTY!1q"),
                Arguments.of("i%nvalid.email.com", "QWERTY!1q"),
                Arguments.of("invalid@emailcom", "QWERTY!1q")
        );
    }
    @ParameterizedTest
    @MethodSource("dataTestCreateUserErrorPasswordData")
    public void testCreateUserErrorPasswordData(String email, String password) throws EmailValidateException, PasswordValidateException {
        assertThrows(PasswordValidateException.class, () -> userService.createUser(email, password));
    }

    static Stream<Arguments> dataTestCreateUserErrorPasswordData(){
        return Stream.of(
                Arguments.of("valid1@email.com", "QWERTY!1"),
                Arguments.of("valid1@email.com", "qwRT!@1"),
                Arguments.of("valid1@email.com", "qwerty2%"),
                Arguments.of("valid1@email.com", "qwerty1A"),
                Arguments.of("valid1@email.com", "qwerty$Q")
        );
    }

    @Test
    public void testGetAllUsers(){
        List<User> users = userService.getAllUsers();
        assertNotEquals(0, users.size());
    }

    @ParameterizedTest
    @MethodSource("dataTestAuthorisation")
    void testAuthorisation(String email, String password) {
        User authUser = userService.authorisation(email, password);
        assertEquals(email, authUser.getEmail());
        assertEquals(password, authUser.getPassword());
    }

    static Stream<Arguments> dataTestAuthorisation() {
        return Stream.of(
                Arguments.of("test@email.net", "qwerty!Q1"),
                Arguments.of("user2@email.net", "qwerty!Q1")
        );
    }

    @ParameterizedTest
    @MethodSource("dataTestAuthorisationInvalid")
    void testAuthorisationInvalid(String email, String password) {
        User authUser = userService.authorisation(email, password);
        assertNull(authUser);
    }

    static Stream<Arguments> dataTestAuthorisationInvalid() {
        return Stream.of(
                Arguments.of("test@email.ne", "qwerty!Q1"),
                Arguments.of("user2@email.net", "qwerty!Q")
        );
    }

    @Test
    void testLogout() {
        User authUser = userService.authorisation("test@email.net", "qwerty!Q1");
        User active = userService.getActiveUser();
        assertEquals(authUser, active);
        userService.logout();
        active = userService.getActiveUser();
        assertNull(active);
    }

    @ParameterizedTest
    @MethodSource("dataTestGetUserById")
    void testGetUserById(int userId, String email, String password) {
        User user = userService.authorisation(email, password);
        assertEquals(userId, user.getId());
    }

    static Stream<Arguments> dataTestGetUserById() {
        return Stream.of(
                Arguments.of(1, "test@email.net", "qwerty!Q1"),
                Arguments.of(4, "user3@email.net", "qwerty!Q1")
        );
    }
}

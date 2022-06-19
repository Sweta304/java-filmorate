package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserControllerTest {

    private User user = User.builder().login("Sveta")
            .name("Svetlana")
            .birthday(LocalDate.of(1990, 06, 03))
            .email("box@ya.ru")
            .build();

    @Test
    void contextLoads() {
    }

    @Test
    void blankEmail() {
        user.setEmail(" ");
        UserController userController = new UserController();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException, UserAlreadyExistsException {
                        userController.addUser(user);
                    }
                });
        assertEquals("данные о пользователе указаны некорректно", exception.getMessage());
    }

    @Test
    void emptyEmail() {
        user.setEmail("");
        UserController userController = new UserController();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException, UserAlreadyExistsException {
                        userController.addUser(user);
                    }
                });
        assertEquals("данные о пользователе указаны некорректно", exception.getMessage());
    }

    @Test
    void emailWithoutAt() {
        user.setEmail("box");
        UserController userController = new UserController();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException, UserAlreadyExistsException {
                        userController.addUser(user);
                    }
                });
        assertEquals("данные о пользователе указаны некорректно", exception.getMessage());
    }

    @Test
    void nullEmail() {
        user.setEmail(null);
        UserController userController = new UserController();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException, UserAlreadyExistsException {
                        userController.addUser(user);
                    }
                });
        assertEquals("данные о пользователе указаны некорректно", exception.getMessage());
    }

    @Test
    void loginIsBlank() {
        user.setLogin(" ");
        UserController userController = new UserController();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException, UserAlreadyExistsException {
                        userController.addUser(user);
                    }
                });
        assertEquals("данные о пользователе указаны некорректно", exception.getMessage());
    }

    @Test
    void loginIsEmpty() {
        user.setLogin("");
        UserController userController = new UserController();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException, UserAlreadyExistsException {
                        userController.addUser(user);
                    }
                });
        assertEquals("данные о пользователе указаны некорректно", exception.getMessage());
    }

    @Test
    void loginIsNull() {
        user.setLogin(null);
        UserController userController = new UserController();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException, UserAlreadyExistsException {
                        userController.addUser(user);
                    }
                });
        assertEquals("данные о пользователе указаны некорректно", exception.getMessage());
    }

    @Test
    void incorrectBirthDay() {
        user.setBirthday(LocalDate.now().plusDays(1));
        UserController userController = new UserController();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException, UserAlreadyExistsException {
                        userController.addUser(user);
                    }
                });
        assertEquals("данные о пользователе указаны некорректно", exception.getMessage());
    }

}

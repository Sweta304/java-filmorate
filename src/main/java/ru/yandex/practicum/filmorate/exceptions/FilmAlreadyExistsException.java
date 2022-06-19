package ru.yandex.practicum.filmorate.exceptions;

public class FilmAlreadyExistsException extends Exception {

    public FilmAlreadyExistsException(String message) {
        super(message);
    }
}

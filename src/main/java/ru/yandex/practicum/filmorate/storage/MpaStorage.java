package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

@Component
public interface MpaStorage {
    public MpaRating getMpaById(int id) throws MpaNotFoundException;

    public List<MpaRating> getAllMpa();
}

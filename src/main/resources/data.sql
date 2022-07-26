insert into MPA (MPA_NAME)
values ('G'),
       ('PG'),
       ('PG-13'),
       ('R'),
       ('NC-17');

insert into GENRE (GENRE_NAME)
values ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');

insert into FILMORATE_USER (USER_LOGIN, USER_NAME, BIRTHDAY, EMAIL)
values ('dolore', 'Nick Name', '1946-08-20', 'mail@mail.ru');

insert into FILM (FILM_NAME, DESCRIPTION, RELEASEDATE, DURATION, RATE, MPA)
values ('labore nulla', 'Duis in consequat esse', '1979-04-17', 100, 4, 1),
       ('pshec', 'vaclav', '1985-05-27', 200, 4, 1);

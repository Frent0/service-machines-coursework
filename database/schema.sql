CREATE DATABASE IF NOT EXISTS service_machines
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE service_machines;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS Пользователи_Роли;
DROP TABLE IF EXISTS Ремонты;
DROP TABLE IF EXISTS Станки;
DROP TABLE IF EXISTS Пользователи_системы;
DROP TABLE IF EXISTS Роли;
DROP TABLE IF EXISTS Виды_ремонта;
DROP TABLE IF EXISTS Типы_станков;
DROP TABLE IF EXISTS Клиенты;

SET FOREIGN_KEY_CHECKS = 1;


CREATE TABLE Клиенты (
                         id_клиента INT AUTO_INCREMENT PRIMARY KEY,
                         название VARCHAR(150) NOT NULL UNIQUE,
                         адрес VARCHAR(255) NOT NULL,
                         номер_телефона VARCHAR(30) NOT NULL UNIQUE,
                         контактное_лицо VARCHAR(100) NOT NULL
);


CREATE TABLE Типы_станков (
                              id_типа INT AUTO_INCREMENT PRIMARY KEY,
                              страна_производитель VARCHAR(100) NOT NULL,
                              год_выпуска SMALLINT NOT NULL,
                              бренд VARCHAR(100) NOT NULL,

                              CONSTRAINT uq_тип_станка
                                  UNIQUE (страна_производитель, год_выпуска, бренд),

                              CONSTRAINT chk_год_выпуска
                                  CHECK (год_выпуска >= 1900 AND год_выпуска <= 2100)
);


CREATE TABLE Станки (
                        id_станка INT AUTO_INCREMENT PRIMARY KEY,
                        id_клиента INT NOT NULL,
                        id_типа INT NOT NULL,
                        инвентарный_номер VARCHAR(50) NOT NULL UNIQUE,

                        CONSTRAINT fk_станок_клиент
                            FOREIGN KEY (id_клиента)
                                REFERENCES Клиенты(id_клиента)
                                ON UPDATE CASCADE
                                ON DELETE RESTRICT,

                        CONSTRAINT fk_станок_тип
                            FOREIGN KEY (id_типа)
                                REFERENCES Типы_станков(id_типа)
                                ON UPDATE CASCADE
                                ON DELETE RESTRICT
);


CREATE TABLE Виды_ремонта (
                              id_вида_ремонта INT AUTO_INCREMENT PRIMARY KEY,
                              название_ремонта VARCHAR(150) NOT NULL UNIQUE,
                              длительность INT NOT NULL,
                              стоимость DECIMAL(12,2) NOT NULL,

                              CONSTRAINT chk_длительность
                                  CHECK (длительность > 0),

                              CONSTRAINT chk_стоимость
                                  CHECK (стоимость >= 0)
);


CREATE TABLE Ремонты (
                         id_ремонта INT AUTO_INCREMENT PRIMARY KEY,
                         id_станка INT NOT NULL,
                         id_вида_ремонта INT NOT NULL,
                         дата_начала DATE NOT NULL,
                         дата_окончания DATE NULL,
                         статус VARCHAR(30) NOT NULL,

                         CONSTRAINT fk_ремонт_станок
                             FOREIGN KEY (id_станка)
                                 REFERENCES Станки(id_станка)
                                 ON UPDATE CASCADE
                                 ON DELETE RESTRICT,

                         CONSTRAINT fk_ремонт_вид
                             FOREIGN KEY (id_вида_ремонта)
                                 REFERENCES Виды_ремонта(id_вида_ремонта)
                                 ON UPDATE CASCADE
                                 ON DELETE RESTRICT,

                         CONSTRAINT chk_даты_ремонта
                             CHECK (
                                 дата_окончания IS NULL
                                     OR дата_окончания >= дата_начала
                                 ),

                         CONSTRAINT chk_статус_ремонта
                             CHECK (
                                 статус IN (
                                            'Запланирован',
                                            'В процессе',
                                            'Завершён',
                                            'Отменён'
                                     )
                                 )
);


CREATE TABLE Пользователи_системы (
                                      id_пользователя INT AUTO_INCREMENT PRIMARY KEY,
                                      id_клиента INT NULL,
                                      логин VARCHAR(50) NOT NULL UNIQUE,
                                      хэш_пароля VARCHAR(255) NOT NULL,

                                      CONSTRAINT fk_пользователь_клиент
                                          FOREIGN KEY (id_клиента)
                                              REFERENCES Клиенты(id_клиента)
                                              ON UPDATE CASCADE
                                              ON DELETE SET NULL
);


CREATE TABLE Роли (
                      id_роли INT AUTO_INCREMENT PRIMARY KEY,
                      название_роли VARCHAR(50) NOT NULL UNIQUE
);


CREATE TABLE Пользователи_Роли (
                                   id_пользователя INT NOT NULL,
                                   id_роли INT NOT NULL,

                                   PRIMARY KEY (id_пользователя, id_роли),

                                   CONSTRAINT fk_пользователь_роль_пользователь
                                       FOREIGN KEY (id_пользователя)
                                           REFERENCES Пользователи_системы(id_пользователя)
                                           ON UPDATE CASCADE
                                           ON DELETE CASCADE,

                                   CONSTRAINT fk_пользователь_роль_роль
                                       FOREIGN KEY (id_роли)
                                           REFERENCES Роли(id_роли)
                                           ON UPDATE CASCADE
                                           ON DELETE CASCADE
);


INSERT INTO Роли (название_роли)
VALUES
    ('ADMIN'),
    ('CLIENT'),
    ('GUEST');
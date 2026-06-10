CREATE TABLE arma (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(40) NOT NULL,
    descripcion VARCHAR(100) NOT NULL,
    dañoArma INT NOT NULL
)

CREATE TABLE aventurero (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL,
    partyId INT
);

CREATE TABLE bolsoPociones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cantidad INT NOT NULL
)

CREATE TABLE equipamiento (
    id INT AUTO_INCREMENT PRIMARY KEY
)

CREATE TABLE pocion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL,
    descripcion VARCHAR(100) NOT NULL
)

CREATE TABLE profesion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre INT NOT NULL,
    descripcion NOT NULL
)





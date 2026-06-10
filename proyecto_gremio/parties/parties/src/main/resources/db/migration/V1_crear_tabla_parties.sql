CREATE TABLE parties (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    nivel INT NOT NULL
);

CREATE TABLE rangos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    nivel INT NOT NULL
);

CREATE TABLE reputaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    nivel INT NOT NULL,
    faccion_id INT NOT NULL
);

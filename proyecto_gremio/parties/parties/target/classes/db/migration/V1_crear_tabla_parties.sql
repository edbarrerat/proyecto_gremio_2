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

INSERT INTO parties (nombre, nivel) VALUES ('Los Guardianes del Fuego', 10);
INSERT INTO parties (nombre, nivel) VALUES ('Cazadores de Sombras', 15);

INSERT INTO rangos (nombre, nivel) VALUES ('Novato', 1);
INSERT INTO rangos (nombre, nivel) VALUES ('Veterano', 5);
INSERT INTO rangos (nombre, nivel) VALUES ('Maestro', 10);

INSERT INTO reputaciones (nombre, nivel, faccion_id) VALUES ('Honorable', 5, 1);
INSERT INTO reputaciones (nombre, nivel, faccion_id) VALUES ('Exaltado', 10, 2);
INSERT INTO reputaciones (nombre, nivel, faccion_id) VALUES ('Infame', 1, 1);
CREATE TABLE gremios {
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    oro INT NOT NULL
}

CREATE TABLE misiones {
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    nivel INT NOT NULL,
    expRecompensa INT NOT NULL,
    oroRecompensa INT NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT FALSE                                                                                                                                                                                
}

CREATE TABLE facciones {
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    hostilidad BOOLEAN NOT NULL DEFAULT FALSE
}
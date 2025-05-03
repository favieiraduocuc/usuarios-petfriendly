CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    email VARCHAR(100),
    password VARCHAR(100),
    rol VARCHAR(50)
);

INSERT INTO usuarios (nombre, email, password, rol)
VALUES ('Fabián Vieira', 'fabian@mail.com', '123456', 'dueño de mascota');

INSERT INTO usuarios (nombre, email, password, rol)
VALUES ('Laura Soto', 'laura@mail.com', 'conductor123', 'conductor');

INSERT INTO usuarios (nombre, email, password, rol)
VALUES ('Carlos Méndez', 'carlos@mail.com', 'abc123', 'dueño de mascota');

COMMIT;
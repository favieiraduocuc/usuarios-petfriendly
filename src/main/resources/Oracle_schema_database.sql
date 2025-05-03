-- 🔁 Elimina primero la tabla y la secuencia si existen (ejecuta solo si ya las tenías)
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE usuarios CASCADE CONSTRAINTS';
EXCEPTION
    WHEN OTHERS THEN
        NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_usuarios';
EXCEPTION
    WHEN OTHERS THEN
        NULL;
END;
/

-- 🧱 Crear tabla usuarios
CREATE TABLE usuarios (
    id_usuario NUMBER PRIMARY KEY,
    nombre VARCHAR2(100),
    email VARCHAR2(100),
    password VARCHAR2(100),
    rol VARCHAR2(50)
);

-- 🔢 Crear secuencia para IDs
CREATE SEQUENCE seq_usuarios START WITH 1 INCREMENT BY 1;

-- ⚙️ Crear trigger para autogenerar ID
CREATE OR REPLACE TRIGGER trg_usuarios_bi
BEFORE INSERT ON usuarios
FOR EACH ROW
BEGIN
    IF :NEW.id_usuario IS NULL THEN
        SELECT seq_usuarios.NEXTVAL INTO :NEW.id_usuario FROM dual;
    END IF;
END;
/

-- 📝 Insertar datos
INSERT INTO usuarios (nombre, email, password, rol)
VALUES ('Fabián Vieira', 'fabian@mail.com', '123456', 'dueño de mascota');

INSERT INTO usuarios (nombre, email, password, rol)
VALUES ('Laura Soto', 'laura@mail.com', 'conductor123', 'conductor');

INSERT INTO usuarios (nombre, email, password, rol)
VALUES ('Carlos Méndez', 'carlos@mail.com', 'abc123', 'dueño de mascota');

COMMIT;

CREATE DATABASE IF NOT EXISTS javamed;
USE javamed;

CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    last_login TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    position VARCHAR(50) NOT NULL
);

INSERT INTO users (id, username, last_login, position) VALUES
(1, 'MaríaTB', '2025-06-16 22:50:53', 'reception'),
(2, 'MaríaBT', '2025-06-17 10:50:09', 'reception');

CREATE TABLE IF NOT EXISTS passwords (
    id_password INT PRIMARY KEY AUTO_INCREMENT,
    id_user INT NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    FOREIGN KEY (id_user) REFERENCES users(id) ON DELETE CASCADE
);

INSERT INTO passwords (id_password, id_user, password_hash) VALUES
(1, 1, '$2a$10$LVsg6uxHXmgokuvLn9BWBeCyDfJZ9LO53QFZMkpfLrBXL2or4lMcS'),
(2, 2, '$2a$10$coPslfIKVlGBF/c5.QPSceSevghwMLDD2MKfAllyyZzzXuxlUDy0y');

CREATE TABLE IF NOT EXISTS appointments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    paciente VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    usuario_asignado VARCHAR(100) NOT NULL
);

INSERT INTO appointments (id, fecha, hora, paciente, descripcion, usuario_asignado) VALUES
(1, '2025-07-27', '09:00:00', 'Laura Gómez', 'Control de presión arterial', 'dr.jimenez'),
(2, '2025-03-02', '11:30:00', 'Carlos Ruiz', 'Consulta por cefalea recurrente', 'dra.morales'),
(3, '2025-07-05', '10:00:00', 'Ana Torres', 'Control general', 'dr.jimenez');

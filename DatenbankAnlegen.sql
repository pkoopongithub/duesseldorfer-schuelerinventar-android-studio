CREATE DATABASE personality_test CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    is_admin BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE profiles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    item1 TINYINT DEFAULT 2,
    item2 TINYINT DEFAULT 2,
    item3 TINYINT DEFAULT 2,
    item4 TINYINT DEFAULT 2,
    item5 TINYINT DEFAULT 2,
    item6 TINYINT DEFAULT 2,
    item7 TINYINT DEFAULT 2,
    item8 TINYINT DEFAULT 2,
    item9 TINYINT DEFAULT 2,
    item10 TINYINT DEFAULT 2,
    item11 TINYINT DEFAULT 2,
    item12 TINYINT DEFAULT 2,
    item13 TINYINT DEFAULT 2,
    item14 TINYINT DEFAULT 2,
    item15 TINYINT DEFAULT 2,
    item16 TINYINT DEFAULT 2,
    item17 TINYINT DEFAULT 2,
    item18 TINYINT DEFAULT 2,
    item19 TINYINT DEFAULT 2,
    item20 TINYINT DEFAULT 2,
    item21 TINYINT DEFAULT 2,
    item22 TINYINT DEFAULT 2,
    item23 TINYINT DEFAULT 2,
    item24 TINYINT DEFAULT 2,
    item25 TINYINT DEFAULT 2,
    item26 TINYINT DEFAULT 2,
    item27 TINYINT DEFAULT 2,
    item28 TINYINT DEFAULT 2,
    item29 TINYINT DEFAULT 2,
    item30 TINYINT DEFAULT 2,
    item31 TINYINT DEFAULT 2,
    item32 TINYINT DEFAULT 2,
    item33 TINYINT DEFAULT 2,
    item34 TINYINT DEFAULT 2,
    item35 TINYINT DEFAULT 2,
    item36 TINYINT DEFAULT 2,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE normSEhs (
    kompetenzID TINYINT PRIMARY KEY,
    kompetenzName VARCHAR(50) NOT NULL,
    p1 DECIMAL(5,2) NOT NULL,
    p2 DECIMAL(5,2) NOT NULL,
    p3 DECIMAL(5,2) NOT NULL,
    p4 DECIMAL(5,2) NOT NULL,
    p5 DECIMAL(5,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO normSEhs (kompetenzID, kompetenzName, p1, p2, p3, p4, p5) VALUES
(1, 'Arbeitsverhalten', 21.33, 25.33, 29.33, 33.32, 37.32),
(2, 'Lernverhalten', 20.87, 24.95, 29.03, 33.10, 37.18),
(3, 'Sozialverhalten', 17.93, 21.37, 24.80, 28.23, 31.67),
(4, 'Fachkompetenz', 13.98, 17.71, 21.44, 25.17, 28.90),
(5, 'Personale Kompetenz', 24.06, 28.55, 33.04, 37.53, 42.01),
(6, 'Methodenkompetenz', 15.53, 18.97, 22.40, 25.83, 29.27);

-- Test-User (Passwort: "test1234")
INSERT INTO users (username, email, password_hash, is_admin) VALUES
('testuser', 'test@example.com', '$2$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 1);

-- Test-Profil
INSERT INTO profiles (user_id, name) VALUES
(1, 'Mein erstes Profil');


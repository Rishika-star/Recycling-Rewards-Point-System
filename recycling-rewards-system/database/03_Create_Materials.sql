USE recycling_rewards;

CREATE TABLE materials (
    material_id INT AUTO_INCREMENT PRIMARY KEY,
    material_name VARCHAR(60) NOT NULL UNIQUE,
    points_per_kg DECIMAL(8,2) NOT NULL
);

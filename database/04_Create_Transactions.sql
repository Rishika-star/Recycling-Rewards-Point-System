USE recycling_rewards;

CREATE TABLE transactions (
    transaction_id VARCHAR(30) PRIMARY KEY,
    resident_id INT NOT NULL,
    material_id INT,
    transaction_type VARCHAR(15) NOT NULL,
    weight_kg DECIMAL(10,2),
    points INT NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (resident_id) REFERENCES residents(resident_id),
    FOREIGN KEY (material_id) REFERENCES materials(material_id)
);
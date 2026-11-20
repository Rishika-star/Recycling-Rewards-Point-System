USE recycling_rewards;



CREATE TABLE residents (
    resident_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    membership_id VARCHAR(30) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    reward_balance INT NOT NULL DEFAULT 0
);
Select * from residents;
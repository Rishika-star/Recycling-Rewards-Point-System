USE recycling_rewards;
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    CONSTRAINT_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE REFERENCED_TABLE_NAME = 'residents'
  AND REFERENCED_TABLE_SCHEMA = 'recycling_rewards';


SELECT * FROM transactions;
USE recycling_rewards;

START TRANSACTION;

-- Remove only the dummy resident
DELETE FROM residents
WHERE membership_id = 'RR999';

-- Temporarily move the real residents to IDs 101–104
UPDATE residents SET resident_id = 101 WHERE membership_id = 'RR101';
UPDATE residents SET resident_id = 102 WHERE membership_id = 'RR102';
UPDATE residents SET resident_id = 103 WHERE membership_id = 'RR103';
UPDATE residents SET resident_id = 104 WHERE membership_id = 'RR104';

-- Assign the final IDs
UPDATE residents SET resident_id = 1 WHERE membership_id = 'RR101';
UPDATE residents SET resident_id = 2 WHERE membership_id = 'RR102';
UPDATE residents SET resident_id = 3 WHERE membership_id = 'RR103';
UPDATE residents SET resident_id = 4 WHERE membership_id = 'RR104';

COMMIT;

ALTER TABLE residents AUTO_INCREMENT = 5;
SELECT resident_id, name, membership_id, phone, reward_balance
FROM residents
ORDER BY resident_id;
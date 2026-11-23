USE recycling_rewards;


INSERT INTO residents (name, membership_id, phone)
VALUES ('Rishika', 'RR101', 'YOUR_PHONE_NUMBER');
-- =========================================
-- 7. VIEW RESIDENT REWARD BALANCE
-- =========================================

SELECT resident_id, name, membership_id, reward_balance
FROM residents;

-- TO CHECK REWARD BALANCE OF A SPECIFIC RESIDENT
SELECT name, membership_id, reward_balance
FROM residents
WHERE membership_id = 'RR101';

-- =========================================
-- 8. DELETE RESIDENT
-- =========================================

DELETE FROM residents
WHERE membership_id = 'RR101'; 

-- =========================================
-- 9. UPDATE RESIDENT DETAILS
-- =========================================

INSERT INTO residents (name, membership_id, phone)
VALUES ('Rahul Sharma', 'RR103', '9876543210');

UPDATE residents
SET name = 'Rahul Sharma',
    phone = '9999999999'
WHERE membership_id = 'RR103';

-- =========================================
-- 10. REGISTER RESIDENT
-- =========================================

INSERT INTO residents (name, membership_id, phone)
VALUES ('Anupama', 'RR104', '911234788');


-- =========================================
-- 11. SEARCH RESIDENT
-- =========================================

SELECT *
FROM residents
WHERE membership_id = 'RR103';


-- =========================================
-- 12. VIEW ALL RESIDENTS
-- =========================================

SELECT resident_id, name, membership_id, reward_balance
FROM residents;



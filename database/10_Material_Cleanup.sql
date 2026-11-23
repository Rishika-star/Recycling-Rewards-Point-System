
USE recycling_rewards;

-- RESTORE ORIGINAL PLASTIC POINTS
UPDATE materials
SET points_per_kg = 10.00
WHERE material_name = 'Plastic';

-- DELETE TEMPORARY TEST MATERIAL
DELETE FROM materials
WHERE material_name = 'Test Material';

-- VERIFY FINAL MATERIAL DATA
SELECT *
FROM materials
ORDER BY material_id;

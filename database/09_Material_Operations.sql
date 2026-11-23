USE recycling_rewards;

-- VIEW ALL MATERIALS
SELECT *
FROM materials
ORDER BY material_id;

-- DELETE TEMPORARY TEST MATERIAL
DELETE FROM materials
WHERE material_name = 'Test Material';

-- VERIFY MATERIALS AFTER CLEANUP
SELECT *
FROM materials
ORDER BY material_id;
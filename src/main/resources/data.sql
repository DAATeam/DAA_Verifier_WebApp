INSERT INTO signature (app_id, data)
SELECT * FROM (SELECT '0', 'data test db') AS tmp
WHERE NOT EXISTS (
    SELECT app_id FROM signature WHERE app_id = 0
) LIMIT 1;

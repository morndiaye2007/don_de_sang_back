-- Direct fix for SMALLINT column issue
-- The column is still SMALLINT, so we can't insert string values

-- Step 1: Drop the SMALLINT column completely
ALTER TABLE campagne DROP COLUMN status_campagne CASCADE;

-- Step 2: Add the column back as VARCHAR
ALTER TABLE campagne ADD COLUMN status_campagne VARCHAR(50);

-- Step 3: Set default values (this will work now since it's VARCHAR)
UPDATE campagne SET status_campagne = 'PLANIFIEE';

-- Step 4: Verify the fix
SELECT column_name, data_type FROM information_schema.columns 
WHERE table_name = 'campagne' AND column_name = 'status_campagne';

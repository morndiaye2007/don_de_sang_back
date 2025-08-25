-- Fix SMALLINT column issue - the column still exists as SMALLINT
-- We need to drop it completely first before recreating

-- Step 1: Check current column type
SELECT column_name, data_type, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'campagne' AND column_name = 'status_campagne';

-- Step 2: Drop the SMALLINT column completely with CASCADE to remove all constraints
ALTER TABLE campagne DROP COLUMN status_campagne CASCADE;

-- Step 3: Add the column back as VARCHAR
ALTER TABLE campagne ADD COLUMN status_campagne VARCHAR(50);

-- Step 4: Set default values for existing records (now it will work since it's VARCHAR)
UPDATE campagne SET status_campagne = 'PLANIFIEE' WHERE status_campagne IS NULL;

-- Step 5: Verify the column is now VARCHAR
SELECT column_name, data_type, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'campagne' AND column_name = 'status_campagne';

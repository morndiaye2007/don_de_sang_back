-- Fix PostgreSQL constraint issue for status_campagne column
-- The issue is that there's a constraint preventing the ALTER TABLE operation

-- Step 1: Check current constraints on the table
SELECT conname, contype, pg_get_constraintdef(oid) as definition
FROM pg_constraint 
WHERE conrelid = 'campagne'::regclass 
AND conname LIKE '%status_campagne%';

-- Step 2: Drop all constraints related to status_campagne
DO $$ 
DECLARE 
    constraint_name text;
BEGIN
    FOR constraint_name IN 
        SELECT conname 
        FROM pg_constraint 
        WHERE conrelid = 'campagne'::regclass 
        AND pg_get_constraintdef(oid) LIKE '%status_campagne%'
    LOOP
        EXECUTE 'ALTER TABLE campagne DROP CONSTRAINT IF EXISTS ' || constraint_name || ' CASCADE';
    END LOOP;
END $$;

-- Step 3: Drop the problematic column completely
ALTER TABLE campagne DROP COLUMN IF EXISTS status_campagne CASCADE;

-- Step 4: Add the column back as VARCHAR
ALTER TABLE campagne ADD COLUMN status_campagne VARCHAR(50);

-- Step 5: Set default values for existing records
UPDATE campagne SET status_campagne = 'PLANIFIEE' WHERE status_campagne IS NULL;

-- Step 6: Verify the fix
SELECT column_name, data_type, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'campagne' AND column_name = 'status_campagne';

-- Step 7: Check that no problematic constraints remain
SELECT conname, contype, pg_get_constraintdef(oid) as definition
FROM pg_constraint 
WHERE conrelid = 'campagne'::regclass;

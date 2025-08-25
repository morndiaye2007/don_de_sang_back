-- Fix status_campagne column type issue
-- The column is currently SMALLINT but needs to be VARCHAR for enum strings

-- Connect to your database first, then run:

-- Check current column type
SELECT column_name, data_type, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'campagne' AND column_name IN ('status_campagne', 'objectif');

-- Drop the problematic status_campagne column (it's SMALLINT but we need VARCHAR)
ALTER TABLE campagne DROP COLUMN IF EXISTS status_campagne CASCADE;

-- Add status_campagne as VARCHAR to store enum string values
ALTER TABLE campagne ADD COLUMN status_campagne VARCHAR(50);

-- Set default value for existing records
UPDATE campagne SET status_campagne = 'PLANIFIEE' WHERE status_campagne IS NULL;

-- Verify the fix
SELECT column_name, data_type, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'campagne' AND column_name IN ('status_campagne', 'objectif');

-- Test insert to verify it works
-- INSERT INTO campagne (nom_campagne, description, date_debut, date_fin, nbre_de_poche, objectif, status_campagne, centre_id) 
-- VALUES ('Test Campaign', 'Test Description', CURRENT_DATE, CURRENT_DATE + INTERVAL '7 days', 100, 100, 'PLANIFIEE', 1);

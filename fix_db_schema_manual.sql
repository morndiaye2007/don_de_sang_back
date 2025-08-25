-- Manual database fix for campaign table
-- Execute this directly in your PostgreSQL database

-- First, check current table structure
\d campagne;

-- Drop problematic columns completely (CASCADE removes constraints)
ALTER TABLE campagne DROP COLUMN IF EXISTS objectif CASCADE;
ALTER TABLE campagne DROP COLUMN IF EXISTS status_campagne CASCADE;

-- Add objectif column as nullable DOUBLE PRECISION
ALTER TABLE campagne ADD COLUMN objectif DOUBLE PRECISION;

-- Add status_campagne column as nullable VARCHAR (not SMALLINT)
ALTER TABLE campagne ADD COLUMN status_campagne VARCHAR(50);

-- Set default values for existing records
UPDATE campagne SET objectif = nbre_de_poche WHERE objectif IS NULL;
UPDATE campagne SET status_campagne = 'PLANIFIEE' WHERE status_campagne IS NULL;

-- Verify the fix
SELECT id, nom_campagne, objectif, status_campagne FROM campagne LIMIT 5;

-- Check final table structure
\d campagne;

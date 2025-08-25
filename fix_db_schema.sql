-- Fix database schema for campaign table
-- Add missing columns with default values to avoid NOT NULL constraint errors

-- Drop existing objectif column if it exists with wrong type
ALTER TABLE campagne DROP COLUMN IF EXISTS objectif;

-- Add objectif column as nullable DOUBLE PRECISION
ALTER TABLE campagne ADD COLUMN objectif DOUBLE PRECISION;

-- Update existing NULL values in objectif column with default values
UPDATE campagne SET objectif = COALESCE(nbre_de_poche, 0) WHERE objectif IS NULL;

-- Drop existing status_campagne column if it exists with wrong type
ALTER TABLE campagne DROP COLUMN IF EXISTS status_campagne;

-- Add status_campagne column as nullable VARCHAR
ALTER TABLE campagne ADD COLUMN status_campagne VARCHAR(50);

-- Update existing NULL values in status_campagne column
UPDATE campagne SET status_campagne = 'PLANIFIEE' WHERE status_campagne IS NULL;

-- Verify the changes
SELECT id, nom_campagne, objectif, status_campagne FROM campagne LIMIT 5;

-- Script pour vérifier la relation entre RDV et Donneur
-- Vérifier la structure de la table rendez_vous
SELECT column_name, data_type, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'rendez_vous' 
ORDER BY ordinal_position;

-- Vérifier si les RDV ont des donneur_id associés
SELECT 
    id,
    date_rdv,
    donneur_id,
    CASE 
        WHEN donneur_id IS NULL THEN 'PAS DE DONNEUR'
        ELSE 'DONNEUR ASSOCIÉ'
    END as status_donneur
FROM rendez_vous 
ORDER BY id;

-- Compter les RDV avec et sans donneur
SELECT 
    COUNT(*) as total_rdv,
    COUNT(donneur_id) as rdv_avec_donneur,
    COUNT(*) - COUNT(donneur_id) as rdv_sans_donneur
FROM rendez_vous;

-- Vérifier les donneurs existants
SELECT id, nom_donneur, prenom_donneur 
FROM donneur 
ORDER BY id 
LIMIT 10;

-- Si besoin, créer des associations test (à exécuter seulement si nécessaire)
-- UPDATE rendez_vous SET donneur_id = 1 WHERE id = 1 AND donneur_id IS NULL;
-- UPDATE rendez_vous SET donneur_id = 2 WHERE id = 2 AND donneur_id IS NULL;

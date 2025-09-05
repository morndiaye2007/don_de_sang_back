-- Migration pour ajouter la colonne fcm_token à la table donneur
-- Cette colonne stockera les tokens Firebase Cloud Messaging pour les notifications push

ALTER TABLE donneur ADD COLUMN fcm_token VARCHAR(255) NULL;

-- Ajouter un commentaire pour documenter la colonne
COMMENT ON COLUMN donneur.fcm_token IS 'Token Firebase Cloud Messaging pour les notifications push';

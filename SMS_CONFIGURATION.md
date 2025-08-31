# Configuration SMS pour Don de Sang

## Fonctionnalités SMS Implémentées

✅ **SMS de confirmation** : Envoyé automatiquement quand un donneur prend un rendez-vous
✅ **SMS de mise à jour** : Envoyé quand le statut du RDV change (validé/refusé)

## Configuration Twilio

### 1. Créer un compte Twilio
1. Aller sur [twilio.com](https://www.twilio.com)
2. Créer un compte et obtenir :
   - Account SID
   - Auth Token  
   - Numéro de téléphone Twilio

### 2. Configuration dans application-sms.properties

```properties
# Activer les SMS
sms.enabled=true

# Paramètres Twilio (remplacer par vos vraies valeurs)
twilio.account.sid=ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
twilio.auth.token=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
twilio.phone.number=+1234567890
```

### 3. Format des numéros sénégalais
Le service gère automatiquement :
- `774445566` → `+221774445566`
- `0774445566` → `+221774445566` 
- `+221774445566` → `+221774445566` (inchangé)

## Messages SMS

### Confirmation de RDV
```
Bonjour [Nom Prénom],

Votre rendez-vous de don de sang est confirmé :
📅 Date : 15/09/2024
🕐 Heure : 14:30
🏥 Centre : Centre de Dakar

Merci pour votre générosité !
Don de Sang Sénégal
```

### Mise à jour de statut
```
Bonjour [Nom Prénom],

✅ Votre rendez-vous a été validé pour votre rendez-vous du 15/09/2024.

Don de Sang Sénégal
```

## Mode Test (SMS désactivé)
Par défaut, `sms.enabled=false` - les messages apparaissent dans les logs sans être envoyés.

## Redémarrage requis
Après configuration, redémarrer le backend Spring Boot pour activer les SMS.

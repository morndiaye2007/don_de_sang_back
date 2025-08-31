# Instructions de Déploiement SMS

## Étapes de Configuration

### 1. Configuration Twilio (Production)
```bash
# Modifier application-sms.properties
sms.enabled=true
twilio.account.sid=YOUR_REAL_ACCOUNT_SID
twilio.auth.token=YOUR_REAL_AUTH_TOKEN  
twilio.phone.number=YOUR_TWILIO_NUMBER
```

### 2. Redémarrer le Backend
```bash
cd DonDeSang_sn
mvn clean install
mvn spring-boot:run
```

### 3. Test du Flux Complet

**Via Mobile App :**
1. Donneur se connecte
2. Prend un RDV → SMS automatique envoyé
3. Admin valide/refuse → SMS de mise à jour envoyé

**Via Admin Panel :**
1. Voir l'icône 📧 pour les RDV avec téléphone
2. Valider/Refuser → Message "SMS envoyé au donneur"

## Messages SMS Automatiques

✅ **Création RDV** : Confirmation avec date/heure/centre
✅ **Validation** : "Votre rendez-vous a été validé"  
✅ **Refus** : "Votre rendez-vous a été refusé"

## Sécurité
- Numéros sénégalais formatés automatiquement (+221)
- Échec SMS n'interrompt pas le processus RDV
- Logs détaillés pour debugging

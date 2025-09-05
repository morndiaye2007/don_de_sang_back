package com.groupeisi.com.dondesang_sn.entity.enums;

public enum StatutLivraison {
    EN_PREPARATION("En préparation"),
    EXPEDIEE("Expédiée"),
    EN_TRANSIT("En transit"),
    LIVREE("Livrée"),
    ANNULEE("Annulée"),
    RETOURNEE("Retournée");

    private final String description;

    StatutLivraison(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

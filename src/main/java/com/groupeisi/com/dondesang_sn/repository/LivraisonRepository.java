package com.groupeisi.com.dondesang_sn.repository;

import com.groupeisi.com.dondesang_sn.entity.LivraisonEntity;
import com.groupeisi.com.dondesang_sn.entity.enums.StatutLivraison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LivraisonRepository extends JpaRepository<LivraisonEntity, Long>, QuerydslPredicateExecutor<LivraisonEntity> {
    
    @Query("SELECT l FROM LivraisonEntity l WHERE l.demande.id = :demandeId")
    List<LivraisonEntity> findByDemandeId(@Param("demandeId") Long demandeId);
    
    @Query("SELECT l FROM LivraisonEntity l WHERE l.demande.hopital.id = :hopitalId ORDER BY l.dateCreation DESC")
    List<LivraisonEntity> findByHopitalId(@Param("hopitalId") Long hopitalId);
    
    Long countByStatutLivraison(StatutLivraison statutLivraison);
    
    @Query("SELECT COUNT(l) FROM LivraisonEntity l WHERE l.statutLivraison = 'LIVREE' AND MONTH(l.dateLivraison) = MONTH(CURRENT_DATE) AND YEAR(l.dateLivraison) = YEAR(CURRENT_DATE)")
    Long countLivraisonsThisMonth();
}

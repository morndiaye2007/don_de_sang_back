package com.groupeisi.com.dondesang_sn.repository;

import com.groupeisi.com.dondesang_sn.entity.DemandeEntity;
import com.groupeisi.com.dondesang_sn.entity.enums.StatutDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DemandeRepository extends JpaRepository<DemandeEntity, Long>, QuerydslPredicateExecutor<DemandeEntity> {
    
    @Query("SELECT d FROM DemandeEntity d WHERE d.medecin.id = :medecinId ORDER BY d.dateDemande DESC")
    List<DemandeEntity> findByMedecinId(@Param("medecinId") Long medecinId);
    
    @Query("SELECT d FROM DemandeEntity d WHERE d.hopital.id = :hopitalId ORDER BY d.dateDemande DESC")
    List<DemandeEntity> findByHopitalId(@Param("hopitalId") Long hopitalId);
    
    Long countByStatutDemande(StatutDemande statutDemande);
    
    @Query("SELECT SUM(d.nombreDepoche) FROM DemandeEntity d WHERE d.statutDemande = :statut")
    Double sumNombreDepocheByStatut(@Param("statut") StatutDemande statut);
}

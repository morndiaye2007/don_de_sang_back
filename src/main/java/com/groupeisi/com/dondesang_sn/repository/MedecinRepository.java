package com.groupeisi.com.dondesang_sn.repository;

import com.groupeisi.com.dondesang_sn.entity.MedecinEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedecinRepository extends JpaRepository<MedecinEntity, Long> {
    
    Optional<MedecinEntity> findByEmail(String email);
    
    Optional<MedecinEntity> findByTelephone(String telephone);
    
    @Query("SELECT m FROM MedecinEntity m WHERE m.hopital.id = :hopitalId")
    java.util.List<MedecinEntity> findByHopitalId(@Param("hopitalId") Long hopitalId);
    
    boolean existsByEmail(String email);
    
    boolean existsByTelephone(String telephone);
}

package com.groupeisi.com.dondesang_sn.repository;

import com.groupeisi.com.dondesang_sn.entity.UtilisateurEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<UtilisateurEntity, Long> , QuerydslPredicateExecutor<UtilisateurEntity> {
    Optional<UtilisateurEntity> findByNom(String nom);
    Optional<UtilisateurEntity> findByEmail(String email);
}

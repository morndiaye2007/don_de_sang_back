package com.groupeisi.com.dondesang_sn.repository;

import com.groupeisi.com.dondesang_sn.entity.DemandeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface DemandeRepository extends JpaRepository<DemandeEntity, Long>, QuerydslPredicateExecutor<DemandeEntity> {
}

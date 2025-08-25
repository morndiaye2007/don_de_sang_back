package com.groupeisi.com.dondesang_sn.repository;

import com.groupeisi.com.dondesang_sn.entity.DonneurEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DonneurRepository extends JpaRepository<DonneurEntity, Long>, QuerydslPredicateExecutor<DonneurEntity> {
    Optional<DonneurEntity> findByTelephone(String telephone);
}

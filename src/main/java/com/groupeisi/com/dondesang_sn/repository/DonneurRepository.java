package com.groupeisi.com.dondesang_sn.repository;

import com.groupeisi.com.dondesang_sn.controller.DonneurController;
import com.groupeisi.com.dondesang_sn.entity.DonneurEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DonneurRepository extends JpaRepository<DonneurEntity, Long>, QuerydslPredicateExecutor<DonneurEntity> {
}

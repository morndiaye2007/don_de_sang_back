package com.groupeisi.com.dondesang_sn.repository;

import com.groupeisi.com.dondesang_sn.entity.RdvEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RdvRepository extends JpaRepository<RdvEntity, Long>, QuerydslPredicateExecutor<RdvEntity> {

}

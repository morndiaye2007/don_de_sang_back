package com.groupeisi.com.dondesang_sn.repository;

import com.groupeisi.com.dondesang_sn.entity.RdvEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RdvRepository extends JpaRepository<RdvEntity, Long>, QuerydslPredicateExecutor<RdvEntity> {

    List<RdvEntity> findByDonneurId(Long donneurId);

}

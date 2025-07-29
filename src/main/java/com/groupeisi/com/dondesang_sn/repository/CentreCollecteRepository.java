package com.groupeisi.com.dondesang_sn.repository;

import com.groupeisi.com.dondesang_sn.entity.CentreCollecteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CentreCollecteRepository extends JpaRepository<CentreCollecteEntity, Long> , QuerydslPredicateExecutor<CentreCollecteEntity> {

}

package com.groupeisi.com.dondesang_sn.repository;

import com.groupeisi.com.dondesang_sn.entity.StockSangEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StockSangRepository extends JpaRepository<StockSangEntity, Long> , QuerydslPredicateExecutor<StockSangEntity> {

}

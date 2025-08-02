package com.groupeisi.com.dondesang_sn.repository;

import com.groupeisi.com.dondesang_sn.entity.CNTSEntity;
import com.groupeisi.com.dondesang_sn.entity.CampagneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CNTSRepository extends JpaRepository<CNTSEntity, Long>, QuerydslPredicateExecutor<CNTSEntity> {
}

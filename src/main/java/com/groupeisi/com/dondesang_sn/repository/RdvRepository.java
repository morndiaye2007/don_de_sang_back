package com.groupeisi.com.dondesang_sn.repository;

import com.groupeisi.com.dondesang_sn.entity.RdvEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RdvRepository extends JpaRepository<RdvEntity, Long>, QuerydslPredicateExecutor<RdvEntity> {

    List<RdvEntity> findByDonneurId(Long donneurId);
    
    @Query("SELECT r FROM RdvEntity r LEFT JOIN FETCH r.donneur LEFT JOIN FETCH r.centreCollecte LEFT JOIN FETCH r.campagne")
    Page<RdvEntity> findAllWithDonneur(Pageable pageable);

}

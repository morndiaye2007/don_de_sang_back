package com.groupeisi.com.dondesang_sn.repository;

import com.groupeisi.com.dondesang_sn.entity.StockSangEntity;
import com.groupeisi.com.dondesang_sn.entity.enums.TypeGroupeSanguin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockSangRepository extends JpaRepository<StockSangEntity, Long> , QuerydslPredicateExecutor<StockSangEntity> {
    
    @Query("SELECT s FROM StockSangEntity s WHERE s.groupeSanguin = :groupeSanguin AND s.centreCollecte.id = :centreCollecteId")
    Optional<StockSangEntity> findByGroupeSanguinAndCentreCollecteId(@Param("groupeSanguin") TypeGroupeSanguin groupeSanguin, @Param("centreCollecteId") Long centreCollecteId);

}

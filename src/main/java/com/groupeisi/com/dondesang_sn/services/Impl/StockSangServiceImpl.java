package com.groupeisi.com.dondesang_sn.services.Impl;

import com.groupeisi.com.dondesang_sn.entity.QStockSangEntity;
import com.groupeisi.com.dondesang_sn.entity.StockSangEntity;
import com.groupeisi.com.dondesang_sn.entity.enums.TypeGroupeSanguin;
import com.groupeisi.com.dondesang_sn.mapper.StockSangMapper;
import com.groupeisi.com.dondesang_sn.models.StockSangDTO;
import com.groupeisi.com.dondesang_sn.repository.CentreCollecteRepository;
import com.groupeisi.com.dondesang_sn.repository.StockSangRepository;
import com.groupeisi.com.dondesang_sn.services.StockSangService;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StockSangServiceImpl implements StockSangService {

    private final StockSangRepository stockSangRepository;
    private final StockSangMapper stockSangMapper;
    private final CentreCollecteRepository centreCollecteRepository;

    @Override
    public StockSangDTO createStockSang(StockSangDTO stockSangDTO) {
        var entity = stockSangMapper.asEntity(stockSangDTO);
        var entitySave = stockSangRepository.save(entity);
        return stockSangMapper.asDto(entitySave);
    }

    @Override
    public StockSangDTO updateStockSang(StockSangDTO stockSangDTO) {
        var entityUpdate = stockSangMapper.asEntity(stockSangDTO);
        var updatedEntity = stockSangRepository.save(entityUpdate);
        return stockSangMapper.asDto(updatedEntity);
    }

    @Override
    public void deleteStockSang(Long id) {
        if (!stockSangRepository.existsById(id)) {
            throw new RuntimeException("Agent not found");
        }
        stockSangRepository.deleteById(id);
    }

    @Override
    public StockSangDTO getStockSang(Long id) {
        var entity = stockSangRepository.findById(id);
        return stockSangMapper.asDto(entity.get());
    }


    @Override
    public Page<StockSangDTO> getAllStockSangs(Map<String, String> searchParams, Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return stockSangRepository.findAll(booleanBuilder, pageable)
                .map(stockSangMapper::asDto);
    }

    private void buildSearch(Map<String, String> searchParams, BooleanBuilder booleanBuilder) {
        if (Objects.nonNull(searchParams)) {
            var qEntity = QStockSangEntity.stockSangEntity;
            if (searchParams.containsKey("dateEntree")) {
                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(searchParams.get("dateEntree"));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                booleanBuilder.and(qEntity.dateEntree.eq(date));
            }
            if (searchParams.containsKey("datePeremption")) {
                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(searchParams.get("datePeremption"));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                booleanBuilder.and(qEntity.datePeremption.eq(date));
            }
            String groupeSanguin = searchParams.get("groupeSanguin");
            if (groupeSanguin != null && !groupeSanguin.isEmpty()) {
                booleanBuilder.and(qEntity.groupeSanguin.stringValue().lower().containsIgnoreCase(groupeSanguin.toLowerCase()));
            }

        }
    }

    @Override
    public void incrementerStock(TypeGroupeSanguin groupeSanguin, Double nombrePoches, Long centreCollecteId) {
        // Rechercher un stock existant pour ce groupe sanguin et centre
        var stockExistant = stockSangRepository.findByGroupeSanguinAndCentreCollecteId(groupeSanguin, centreCollecteId);
        
        if (stockExistant.isPresent()) {
            // Incrémenter le stock existant
            var stock = stockExistant.get();
            stock.setNombreDepoche(stock.getNombreDepoche() + nombrePoches);
            stockSangRepository.save(stock);
            log.info("Stock incrémenté pour {} au centre {}: +{} poches", groupeSanguin, centreCollecteId, nombrePoches);
        } else {
            // Créer un nouveau stock
            var nouveauStock = new StockSangEntity();
            nouveauStock.setGroupeSanguin(groupeSanguin);
            nouveauStock.setNombreDepoche(nombrePoches);
            nouveauStock.setDateEntree(new Date());
            
            // Calculer date de péremption (42 jours pour le sang)
            long quaranteDeuxJours = 42L * 24 * 60 * 60 * 1000;
            nouveauStock.setDatePeremption(new Date(System.currentTimeMillis() + quaranteDeuxJours));
            
            if (centreCollecteId != null) {
                var centre = centreCollecteRepository.findById(centreCollecteId);
                centre.ifPresent(nouveauStock::setCentreCollecte);
            }
            
            stockSangRepository.save(nouveauStock);
            log.info("Nouveau stock créé pour {} au centre {}: {} poches", groupeSanguin, centreCollecteId, nombrePoches);
        }
    }

    @Override
    public Map<String, Object> getStockStatistiquesByGroupeSanguin() {
        try {
            // Récupérer tous les stocks
            var stocks = stockSangRepository.findAll();
            
            // Initialiser les statistiques pour tous les groupes sanguins
            Map<String, Double> stockParGroupe = new HashMap<>();
            stockParGroupe.put("A+", 0.0);
            stockParGroupe.put("A-", 0.0);
            stockParGroupe.put("B+", 0.0);
            stockParGroupe.put("B-", 0.0);
            stockParGroupe.put("AB+", 0.0);
            stockParGroupe.put("AB-", 0.0);
            stockParGroupe.put("O+", 0.0);
            stockParGroupe.put("O-", 0.0);
            
            // Calculer le total par groupe sanguin
            for (var stock : stocks) {
                String groupeFormate = formatGroupeSanguin(stock.getGroupeSanguin());
                stockParGroupe.put(groupeFormate, 
                    stockParGroupe.get(groupeFormate) + stock.getNombreDepoche());
            }
            
            // Calculer le total général
            double totalStock = stockParGroupe.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();
            
            // Préparer la réponse
            Map<String, Object> response = new HashMap<>();
            response.put("stockParGroupe", stockParGroupe);
            response.put("totalStock", totalStock);
            response.put("nombreGroupes", stockParGroupe.size());
            
            log.info("Statistiques de stock calculées: {} poches au total", totalStock);
            return response;
            
        } catch (Exception e) {
            log.error("Erreur lors du calcul des statistiques de stock", e);
            throw new RuntimeException("Erreur lors du calcul des statistiques de stock", e);
        }
    }
    
    private String formatGroupeSanguin(TypeGroupeSanguin groupeSanguin) {
        if (groupeSanguin == null) return "O+";
        
        switch (groupeSanguin) {
            case A_POS: return "A+";
            case A_NEG: return "A-";
            case B_POS: return "B+";
            case B_NEG: return "B-";
            case AB_POS: return "AB+";
            case AB_NEG: return "AB-";
            case O_POS: return "O+";
            case O_NEG: return "O-";
            default: return "O+";
        }
    }
}
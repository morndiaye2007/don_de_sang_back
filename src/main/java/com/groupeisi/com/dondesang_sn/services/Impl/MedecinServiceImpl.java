package com.groupeisi.com.dondesang_sn.services.Impl;

import com.groupeisi.com.dondesang_sn.entity.MedecinEntity;
import com.groupeisi.com.dondesang_sn.mapper.MedecinMapper;
import com.groupeisi.com.dondesang_sn.models.MedecinDTO;
import com.groupeisi.com.dondesang_sn.models.Response;
import com.groupeisi.com.dondesang_sn.repository.MedecinRepository;
import com.groupeisi.com.dondesang_sn.repository.HopitalRepository;
import com.groupeisi.com.dondesang_sn.services.MedecinService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class MedecinServiceImpl implements MedecinService {

    private final MedecinRepository medecinRepository;
    private final HopitalRepository hopitalRepository;
    private final MedecinMapper medecinMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MedecinDTO createMedecin(MedecinDTO medecinDTO) {
        if (medecinRepository.existsByEmail(medecinDTO.getEmail())) {
            throw new RuntimeException("Un médecin avec cet email existe déjà");
        }
        
        if (medecinRepository.existsByTelephone(medecinDTO.getTelephone())) {
            throw new RuntimeException("Un médecin avec ce téléphone existe déjà");
        }

        var hopital = hopitalRepository.findById(medecinDTO.getHopitalId())
                .orElseThrow(() -> new RuntimeException("Hôpital non trouvé"));

        var medecinEntity = medecinMapper.toEntity(medecinDTO);
        medecinEntity.setHopital(hopital);
        medecinEntity.setMotDePasse(passwordEncoder.encode(medecinDTO.getMotDePasse()));
        
        var savedEntity = medecinRepository.save(medecinEntity);
        return medecinMapper.toDto(savedEntity);
    }

    @Override
    public MedecinDTO updateMedecin(MedecinDTO medecinDTO) {
        var existingMedecin = medecinRepository.findById(medecinDTO.getId())
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé"));

        if (medecinDTO.getMotDePasse() != null && !medecinDTO.getMotDePasse().isEmpty()) {
            existingMedecin.setMotDePasse(passwordEncoder.encode(medecinDTO.getMotDePasse()));
        }
        
        existingMedecin.setNom(medecinDTO.getNom());
        existingMedecin.setPrenom(medecinDTO.getPrenom());
        existingMedecin.setEmail(medecinDTO.getEmail());
        existingMedecin.setTelephone(medecinDTO.getTelephone());
        existingMedecin.setSpecialite(medecinDTO.getSpecialite());

        var savedEntity = medecinRepository.save(existingMedecin);
        return medecinMapper.toDto(savedEntity);
    }

    @Override
    public void deleteMedecin(Long id) {
        if (!medecinRepository.existsById(id)) {
            throw new RuntimeException("Médecin non trouvé");
        }
        medecinRepository.deleteById(id);
    }

    @Override
    public MedecinDTO getMedecin(Long id) {
        var medecin = medecinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé"));
        return medecinMapper.toDto(medecin);
    }

    @Override
    public Page<MedecinDTO> getAllMedecins(Map<String, String> searchParams, Pageable pageable) {
        var medecins = medecinRepository.findAll(pageable);
        return medecins.map(medecinMapper::toDto);
    }

    @Override
    public Response<Object> authenticateMedecin(String email, String motDePasse) {
        var medecin = medecinRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email ou mot de passe incorrect"));

        if (!passwordEncoder.matches(motDePasse, medecin.getMotDePasse())) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }

        var medecinDto = medecinMapper.toDto(medecin);
        String token = "medecin_" + medecin.getId();

        return Response.ok()
                .setPayload(Map.of(
                        "medecin", medecinDto,
                        "token", token
                ))
                .setMessage("Connexion réussie");
    }
}

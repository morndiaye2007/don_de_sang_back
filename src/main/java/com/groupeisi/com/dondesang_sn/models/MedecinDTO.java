package com.groupeisi.com.dondesang_sn.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MedecinDTO implements Serializable {
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    
    @NotEmpty(message = "Le nom est requis")
    private String nom;
    
    @NotEmpty(message = "Le prénom est requis")
    private String prenom;
    
    @Email(message = "Email invalide")
    @NotEmpty(message = "L'email est requis")
    private String email;
    
    @NotEmpty(message = "Le téléphone est requis")
    private String telephone;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String motDePasse;
    
    private String specialite;
    
    private Long hopitalId;
    
    private HopitalDTO hopital;
}

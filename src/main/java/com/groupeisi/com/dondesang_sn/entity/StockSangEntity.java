package com.groupeisi.com.dondesang_sn.entity;

import com.groupeisi.com.dondesang_sn.entity.enums.TypeGroupeSanguin;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stock_sang")
@Entity
public class StockSangEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_de_poche")
    private double nombreDepoche;
    @Enumerated(EnumType.STRING)
    @Column(name = "groupe_sanguin")
    private TypeGroupeSanguin groupeSanguin;
    @Temporal(TemporalType.DATE)
    @Column(name = "date_entree")
    private Date dateEntree;
    @Temporal(TemporalType.DATE)
    @Column(name = "date_peremption")
    private Date datePeremption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hopital_id", nullable = true)
    private HopitalEntity hopital;

    @ManyToOne
    @JoinColumn(name = "centre_id")
    private CentreCollecteEntity centreCollecte;


    @ManyToOne
    private CNTSEntity cnts;


}

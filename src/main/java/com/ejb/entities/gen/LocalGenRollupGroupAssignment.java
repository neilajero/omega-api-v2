package com.ejb.entities.gen;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "GenRollupGroupAssignment")
@Table(name = "GEN_RLLP_GRP_ASSGNMNT")
public class LocalGenRollupGroupAssignment extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RLGA_CODE", nullable = false)
    private Integer rlgaCode;

    @Column(name = "RLGA_AD_CMPNY", columnDefinition = "INT")
    private Integer rlgaAdCompany;

    @JoinColumn(name = "GEN_ROLLUP_GROUP", referencedColumnName = "RLG_CODE")
    @ManyToOne
    private LocalGenRollupGroup genRollupGroup;

    @JoinColumn(name = "GEN_VALUE_SET_VALUE", referencedColumnName = "VSV_CODE")
    @ManyToOne
    private LocalGenValueSetValue genValueSetValue;

    public Integer getRlgaCode() {

        return rlgaCode;
    }

    public void setRlgaCode(Integer RLGA_CODE) {

        this.rlgaCode = RLGA_CODE;
    }

    public Integer getRlgaAdCompany() {

        return rlgaAdCompany;
    }

    public void setRlgaAdCompany(Integer RLGA_AD_CMPNY) {

        this.rlgaAdCompany = RLGA_AD_CMPNY;
    }

    public LocalGenRollupGroup getGenRollupGroup() {

        return genRollupGroup;
    }

    public void setGenRollupGroup(LocalGenRollupGroup genRollupGroup) {

        this.genRollupGroup = genRollupGroup;
    }

    public LocalGenValueSetValue getGenValueSetValue() {

        return genValueSetValue;
    }

    public void setGenValueSetValue(LocalGenValueSetValue genValueSetValue) {

        this.genValueSetValue = genValueSetValue;
    }

}
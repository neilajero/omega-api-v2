package com.ejb.entities.gen;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "GenQualifier")
@Table(name = "GEN_QLFR")
public class LocalGenQualifier extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QL_CODE", nullable = false)
    private Integer qlCode;

    @Column(name = "QL_ACCNT_TYP", columnDefinition = "VARCHAR")
    private String qlAccountType;

    @Column(name = "QL_BDGTNG_ALLWD", columnDefinition = "TINYINT")
    private byte qlBudgetingAllowed;

    @Column(name = "QL_PSTNG_ALLWD", columnDefinition = "TINYINT")
    private byte qlPostingAllowed;

    @Column(name = "QL_AD_CMPNY", columnDefinition = "INT")
    private Integer qlAdCompany;

    @OneToMany(mappedBy = "genQualifier", cascade = CascadeType.ALL)
    private List<LocalGenValueSetValue> genValueSetValues;

    public Integer getQlCode() {

        return qlCode;
    }

    public void setQlCode(Integer QL_CODE) {

        this.qlCode = QL_CODE;
    }

    public String getQlAccountType() {

        return qlAccountType;
    }

    public void setQlAccountType(String QL_ACCNT_TYP) {

        this.qlAccountType = QL_ACCNT_TYP;
    }

    public byte getQlBudgetingAllowed() {

        return qlBudgetingAllowed;
    }

    public void setQlBudgetingAllowed(byte QL_BDGTNG_ALLWD) {

        this.qlBudgetingAllowed = QL_BDGTNG_ALLWD;
    }

    public byte getQlPostingAllowed() {

        return qlPostingAllowed;
    }

    public void setQlPostingAllowed(byte QL_PSTNG_ALLWD) {

        this.qlPostingAllowed = QL_PSTNG_ALLWD;
    }

    public Integer getQlAdCompany() {

        return qlAdCompany;
    }

    public void setQlAdCompany(Integer QL_AD_CMPNY) {

        this.qlAdCompany = QL_AD_CMPNY;
    }

    @XmlTransient
    public List getGenValueSetValues() {

        return genValueSetValues;
    }

    public void setGenValueSetValues(List genValueSetValues) {

        this.genValueSetValues = genValueSetValues;
    }

    public void addGenValueSetValue(LocalGenValueSetValue entity) {

        try {
            entity.setGenQualifier(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGenValueSetValue(LocalGenValueSetValue entity) {

        try {
            entity.setGenQualifier(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
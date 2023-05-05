package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "ArAutoAccounting")
@Table(name = "AR_AT_ACCNTNG")
public class LocalArAutoAccounting extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AR_AA_CODE", nullable = false)
    private Integer aaCode;

    @Column(name = "AA_ACCNT_TYP", columnDefinition = "VARCHAR")
    private String aaAccountType;

    @Column(name = "AA_AD_CMPNY", columnDefinition = "INT")
    private Integer aaAdCompany;

    @OneToMany(mappedBy = "arAutoAccounting", fetch = FetchType.LAZY)
    private List<LocalArAutoAccountingSegment> arAutoAccountingSegments;

    public Integer getAaCode() {

        return aaCode;
    }

    public void setAaCode(Integer AR_AA_CODE) {

        this.aaCode = AR_AA_CODE;
    }

    public String getAaAccountType() {

        return aaAccountType;
    }

    public void setAaAccountType(String AA_ACCNT_TYP) {

        this.aaAccountType = AA_ACCNT_TYP;
    }

    public Integer getAaAdCompany() {

        return aaAdCompany;
    }

    public void setAaAdCompany(Integer AA_AD_CMPNY) {

        this.aaAdCompany = AA_AD_CMPNY;
    }

    @XmlTransient
    public List getArAutoAccountingSegments() {

        return arAutoAccountingSegments;
    }

    public void setArAutoAccountingSegments(List arAutoAccountingSegments) {

        this.arAutoAccountingSegments = arAutoAccountingSegments;
    }

    public void addArAutoAccountingSegment(LocalArAutoAccountingSegment entity) {

        try {
            entity.setArAutoAccounting(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArAutoAccountingSegment(LocalArAutoAccountingSegment entity) {

        try {
            entity.setArAutoAccounting(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
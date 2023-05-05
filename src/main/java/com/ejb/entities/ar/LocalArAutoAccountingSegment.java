package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "ArAutoAccountingSegment")
@Table(name = "AR_AT_ACCNTNG_SGMNT")
public class LocalArAutoAccountingSegment extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AAS_CODE", nullable = false)
    private Integer aasCode;

    @Column(name = "AAS_SGMNT_NMBR", columnDefinition = "SMALLINT")
    private short aasSegmentNumber;

    @Column(name = "AAS_AD_CMPNY", columnDefinition = "INT")
    private Integer aasAdCompany;

    @Column(name = "AAS_CLSS_TYP", columnDefinition = "VARCHAR")
    private String aasClassType;

    @JoinColumn(name = "AR_AUTO_ACCOUNTING", referencedColumnName = "AR_AA_CODE")
    @ManyToOne
    private LocalArAutoAccounting arAutoAccounting;

    public Integer getAasCode() {

        return aasCode;
    }

    public void setAasCode(Integer AAS_CODE) {

        this.aasCode = AAS_CODE;
    }

    public short getAasSegmentNumber() {

        return aasSegmentNumber;
    }

    public void setAasSegmentNumber(short AAS_SGMNT_NMBR) {

        this.aasSegmentNumber = AAS_SGMNT_NMBR;
    }

    public Integer getAasAdCompany() {

        return aasAdCompany;
    }

    public void setAasAdCompany(Integer AAS_AD_CMPNY) {

        this.aasAdCompany = AAS_AD_CMPNY;
    }

    public String getAasClassType() {

        return aasClassType;
    }

    public void setAasClassType(String AAS_CLSS_TYP) {

        this.aasClassType = AAS_CLSS_TYP;
    }

    public LocalArAutoAccounting getArAutoAccounting() {

        return arAutoAccounting;
    }

    public void setArAutoAccounting(LocalArAutoAccounting arAutoAccounting) {

        this.arAutoAccounting = arAutoAccounting;
    }

}
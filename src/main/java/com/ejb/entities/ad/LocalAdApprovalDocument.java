package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;

@Entity(name = "AdApprovalDocument")
@Table(name = "AD_APPRVL_DCMNT")
public class LocalAdApprovalDocument extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADC_CODE", nullable = false)
    private Integer adcCode;

    @Column(name = "ADC_TYP", columnDefinition = "VARCHAR")
    private String adcType;

    @Column(name = "ADC_PRNT_OPTN", columnDefinition = "VARCHAR")
    private String adcPrintOption;

    @Column(name = "ADC_ALLW_DPLCT", columnDefinition = "TINYINT")
    private byte adcAllowDuplicate;

    @Column(name = "ADC_TRCK_DPLCT", columnDefinition = "TINYINT")
    private byte adcTrackDuplicate;

    @Column(name = "ADC_ENBL_CRDT_LMT_CHCKNG", columnDefinition = "TINYINT")
    private byte adcEnableCreditLimitChecking;

    @Column(name = "ADC_AD_CMPNY", columnDefinition = "INT")
    private Integer adcAdCompany;

    @OneToMany(mappedBy = "adApprovalDocument", fetch = FetchType.LAZY)
    private List<LocalAdAmountLimit> adAmountLimits;

    public Integer getAdcCode() {

        return adcCode;
    }

    public void setAdcCode(Integer ADC_CODE) {

        this.adcCode = ADC_CODE;
    }

    public String getAdcType() {

        return adcType;
    }

    public void setAdcType(String ADC_TYP) {

        this.adcType = ADC_TYP;
    }

    public String getAdcPrintOption() {

        return adcPrintOption;
    }

    public void setAdcPrintOption(String ADC_PRNT_OPTN) {

        this.adcPrintOption = ADC_PRNT_OPTN;
    }

    public byte getAdcAllowDuplicate() {

        return adcAllowDuplicate;
    }

    public void setAdcAllowDuplicate(byte ADC_ALLW_DPLCT) {

        this.adcAllowDuplicate = ADC_ALLW_DPLCT;
    }

    public byte getAdcTrackDuplicate() {

        return adcTrackDuplicate;
    }

    public void setAdcTrackDuplicate(byte ADC_TRCK_DPLCT) {

        this.adcTrackDuplicate = ADC_TRCK_DPLCT;
    }

    public byte getAdcEnableCreditLimitChecking() {

        return adcEnableCreditLimitChecking;
    }

    public void setAdcEnableCreditLimitChecking(byte ADC_ENBL_CRDT_LMT_CHCKNG) {

        this.adcEnableCreditLimitChecking = ADC_ENBL_CRDT_LMT_CHCKNG;
    }

    public Integer getAdcAdCompany() {

        return adcAdCompany;
    }

    public void setAdcAdCompany(Integer ADC_AD_CMPNY) {

        this.adcAdCompany = ADC_AD_CMPNY;
    }

    @XmlTransient
    public List getAdAmountLimits() {

        return adAmountLimits;
    }

    public void setAdAmountLimits(List adAmountLimits) {

        this.adAmountLimits = adAmountLimits;
    }

    public void addAdAmountLimit(LocalAdAmountLimit entity) {

        try {
            entity.setAdApprovalDocument(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdAmountLimit(LocalAdAmountLimit entity) {

        try {
            entity.setAdApprovalDocument(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
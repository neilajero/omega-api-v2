package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "InvDepreciationLedger")
@Table(name = "INV_DPRCTN_LDGR")
public class LocalInvDepreciationLedger extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DL_CODE", nullable = false)
    private Integer dlCode;

    @Column(name = "DL_DT", columnDefinition = "DATETIME")
    private Date dlDate;

    @Column(name = "DL_ACQSTN_CST", columnDefinition = "DOUBLE")
    private double dlAcquisitionCost = 0;

    @Column(name = "DL_DPRCTN_AMT", columnDefinition = "DOUBLE")
    private double dlDepreciationAmount = 0;

    @Column(name = "DL_MNTH_LF_SPN", columnDefinition = "DOUBLE")
    private double dlMonthLifeSpan = 0;

    @Column(name = "DL_CRRNT_BLNC", columnDefinition = "DOUBLE")
    private double dlCurrentBalance = 0;

    @Column(name = "DL_AD_CMPNY", columnDefinition = "INT")
    private Integer dlAdCompany;

    @JoinColumn(name = "INV_TAG", referencedColumnName = "TG_CODE")
    @ManyToOne
    private LocalInvTag invTag;

    public Integer getDlCode() {

        return dlCode;
    }

    public void setDlCode(Integer DL_CODE) {

        this.dlCode = DL_CODE;
    }

    public Date getDlDate() {

        return dlDate;
    }

    public void setDlDate(Date DL_DT) {

        this.dlDate = DL_DT;
    }

    public double getDlAcquisitionCost() {

        return dlAcquisitionCost;
    }

    public void setDlAcquisitionCost(double DL_ACQSTN_CST) {

        this.dlAcquisitionCost = DL_ACQSTN_CST;
    }

    public double getDlDepreciationAmount() {

        return dlDepreciationAmount;
    }

    public void setDlDepreciationAmount(double DL_DPRCTN_AMT) {

        this.dlDepreciationAmount = DL_DPRCTN_AMT;
    }

    public double getDlMonthLifeSpan() {

        return dlMonthLifeSpan;
    }

    public void setDlMonthLifeSpan(double DL_MNTH_LF_SPN) {

        this.dlMonthLifeSpan = DL_MNTH_LF_SPN;
    }

    public double getDlCurrentBalance() {

        return dlCurrentBalance;
    }

    public void setDlCurrentBalance(double DL_CRRNT_BLNC) {

        this.dlCurrentBalance = DL_CRRNT_BLNC;
    }

    public Integer getDlAdCompany() {

        return dlAdCompany;
    }

    public void setDlAdCompany(Integer DL_AD_CMPNY) {

        this.dlAdCompany = DL_AD_CMPNY;
    }

    public LocalInvTag getInvTag() {

        return invTag;
    }

    public void setInvTag(LocalInvTag invTag) {

        this.invTag = invTag;
    }

}
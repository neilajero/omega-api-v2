package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import com.ejb.entities.cm.LocalCmAdjustment;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "ArAppliedCredit")
@Table(name = "AR_APPLD_CRDT")
public class LocalArAppliedCredit extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AR_AC_CODE", nullable = false)
    private Integer acCode;

    @Column(name = "AC_APPLY_CRDT", columnDefinition = "DOUBLE")
    private double acApplyCredit = 0;

    @Column(name = "AC_AD_CMPNY", columnDefinition = "INT")
    private Integer acAdCompany;

    @JoinColumn(name = "AR_APPLIED_INVOICE", referencedColumnName = "AI_CODE")
    @ManyToOne
    private LocalArAppliedInvoice arAppliedInvoice;

    @JoinColumn(name = "CM_ADJUSTMENT", referencedColumnName = "CM_ADJ_CODE")
    @ManyToOne
    private LocalCmAdjustment cmAdjustment;

    public Integer getAcCode() {

        return acCode;
    }

    public void setAcCode(Integer AC_CODE) {

        this.acCode = AC_CODE;
    }

    public double getAcApplyCredit() {

        return acApplyCredit;
    }

    public void setAcApplyCredit(double AC_APPLY_CRDT) {

        this.acApplyCredit = AC_APPLY_CRDT;
    }

    public Integer getAcAdCompany() {

        return acAdCompany;
    }

    public void setAcAdCompany(Integer AC_AD_CMPNY) {

        this.acAdCompany = AC_AD_CMPNY;
    }

    public LocalArAppliedInvoice getArAppliedInvoice() {

        return arAppliedInvoice;
    }

    public void setArAppliedInvoice(LocalArAppliedInvoice arAppliedInvoice) {

        this.arAppliedInvoice = arAppliedInvoice;
    }

    public LocalCmAdjustment getCmAdjustment() {

        return cmAdjustment;
    }

    public void setCmAdjustment(LocalCmAdjustment cmAdjustment) {

        this.cmAdjustment = cmAdjustment;
    }

}
package com.ejb.entities.cm;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ar.LocalArReceipt;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "CmFundTransferReceipt")
@Table(name = "CM_FND_TRNSFR_RCPT")
public class LocalCmFundTransferReceipt extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FTR_CODE", nullable = false)
    private Integer ftrCode;

    @Column(name = "FTR_AMNT_DPSTD", columnDefinition = "VARCHAR")
    private double ftrAmountDeposited = 0;

    @Column(name = "FTR_AD_CMPNY", columnDefinition = "INT")
    private Integer ftrAdCompany;

    @JoinColumn(name = "AR_RECEIPT", referencedColumnName = "RCT_CODE")
    @ManyToOne
    private LocalArReceipt arReceipt;

    @JoinColumn(name = "CM_FUND_TRANSFER", referencedColumnName = "FT_CODE")
    @ManyToOne
    private LocalCmFundTransfer cmFundTransfer;

    public Integer getFtrCode() {

        return ftrCode;
    }

    public void setFtrCode(Integer FTR_CODE) {

        this.ftrCode = FTR_CODE;
    }

    public double getFtrAmountDeposited() {

        return ftrAmountDeposited;
    }

    public void setFtrAmountDeposited(double FTR_AMNT_DPSTD) {

        this.ftrAmountDeposited = FTR_AMNT_DPSTD;
    }

    public Integer getFtrAdCompany() {

        return ftrAdCompany;
    }

    public void setFtrAdCompany(Integer FTR_AD_CMPNY) {

        this.ftrAdCompany = FTR_AD_CMPNY;
    }

    public LocalArReceipt getArReceipt() {

        return arReceipt;
    }

    public void setArReceipt(LocalArReceipt arReceipt) {

        this.arReceipt = arReceipt;
    }

    public LocalCmFundTransfer getCmFundTransfer() {

        return cmFundTransfer;
    }

    public void setCmFundTransfer(LocalCmFundTransfer cmFundTransfer) {

        this.cmFundTransfer = cmFundTransfer;
    }

}
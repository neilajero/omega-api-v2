package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "ArAppliedInvoice")
@Table(name = "AR_APPLD_INVC")
public class LocalArAppliedInvoice extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AI_CODE", nullable = false)
    private Integer aiCode;

    @Column(name = "AI_APPLY_AMNT", columnDefinition = "DOUBLE")
    private double aiApplyAmount = 0;

    @Column(name = "AI_PNTLY_APPLY_AMNT", columnDefinition = "DOUBLE")
    private double aiPenaltyApplyAmount = 0;

    @Column(name = "AI_CRDTBL_W_TX", columnDefinition = "DOUBLE")
    private double aiCreditableWTax = 0;

    @Column(name = "AI_DSCNT_AMNT", columnDefinition = "DOUBLE")
    private double aiDiscountAmount = 0;

    @Column(name = "AI_RBT", columnDefinition = "DOUBLE")
    private double aiRebate = 0;

    @Column(name = "AI_APPLD_DPST", columnDefinition = "DOUBLE")
    private double aiAppliedDeposit = 0;

    @Column(name = "AI_CRDT_BLNC_PD", columnDefinition = "DOUBLE")
    private double aiCreditBalancePaid = 0;

    @Column(name = "AI_ALLCTD_PYMNT_AMNT", columnDefinition = "DOUBLE")
    private double aiAllocatedPaymentAmount = 0;

    @Column(name = "AI_FRX_GN_LSS", columnDefinition = "DOUBLE")
    private double aiForexGainLoss = 0;

    @Column(name = "AI_APPLY_RBT", columnDefinition = "TINYINT")
    private byte aiApplyRebate;

    @Column(name = "AI_AD_CMPNY", columnDefinition = "INT")
    private Integer aiAdCompany;

    @JoinColumn(name = "AR_INVOICE_PAYMENT_SCHEDULE", referencedColumnName = "IPS_CODE")
    @ManyToOne
    private LocalArInvoicePaymentSchedule arInvoicePaymentSchedule;

    @JoinColumn(name = "AR_PDC", referencedColumnName = "PDC_CODE")
    @ManyToOne
    private LocalArPdc arPdc;

    @JoinColumn(name = "AR_RECEIPT", referencedColumnName = "RCT_CODE")
    @ManyToOne
    private LocalArReceipt arReceipt;

    @OneToMany(mappedBy = "arAppliedInvoice", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalArDistributionRecord> arDistributionRecords;

    @OneToMany(mappedBy = "arAppliedInvoice", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalArAppliedCredit> arAppliedCredits;

    public Integer getAiCode() {

        return aiCode;
    }

    public void setAiCode(Integer AI_CODE) {

        this.aiCode = AI_CODE;
    }

    public double getAiApplyAmount() {

        return aiApplyAmount;
    }

    public void setAiApplyAmount(double AI_APPLY_AMNT) {

        this.aiApplyAmount = AI_APPLY_AMNT;
    }

    public double getAiPenaltyApplyAmount() {

        return aiPenaltyApplyAmount;
    }

    public void setAiPenaltyApplyAmount(double AI_PNTLY_APPLY_AMNT) {

        this.aiPenaltyApplyAmount = AI_PNTLY_APPLY_AMNT;
    }

    public double getAiCreditableWTax() {

        return aiCreditableWTax;
    }

    public void setAiCreditableWTax(double AI_CRDTBL_W_TX) {

        this.aiCreditableWTax = AI_CRDTBL_W_TX;
    }

    public double getAiDiscountAmount() {

        return aiDiscountAmount;
    }

    public void setAiDiscountAmount(double AI_DSCNT_AMNT) {

        this.aiDiscountAmount = AI_DSCNT_AMNT;
    }

    public double getAiRebate() {

        return aiRebate;
    }

    public void setAiRebate(double AI_RBT) {

        this.aiRebate = AI_RBT;
    }

    public double getAiAppliedDeposit() {

        return aiAppliedDeposit;
    }

    public void setAiAppliedDeposit(double AI_APPLD_DPST) {

        this.aiAppliedDeposit = AI_APPLD_DPST;
    }

    public double getAiCreditBalancePaid() {

        return aiCreditBalancePaid;
    }

    public void setAiCreditBalancePaid(double AI_CRDT_BLNC_PD) {

        this.aiCreditBalancePaid = AI_CRDT_BLNC_PD;
    }

    public double getAiAllocatedPaymentAmount() {

        return aiAllocatedPaymentAmount;
    }

    public void setAiAllocatedPaymentAmount(double AI_ALLCTD_PYMNT_AMNT) {

        this.aiAllocatedPaymentAmount = AI_ALLCTD_PYMNT_AMNT;
    }

    public double getAiForexGainLoss() {

        return aiForexGainLoss;
    }

    public void setAiForexGainLoss(double AI_FRX_GN_LSS) {

        this.aiForexGainLoss = AI_FRX_GN_LSS;
    }

    public byte getAiApplyRebate() {

        return aiApplyRebate;
    }

    public void setAiApplyRebate(byte AI_APPLY_RBT) {

        this.aiApplyRebate = AI_APPLY_RBT;
    }

    public Integer getAiAdCompany() {

        return aiAdCompany;
    }

    public void setAiAdCompany(Integer AI_AD_CMPNY) {

        this.aiAdCompany = AI_AD_CMPNY;
    }

    public LocalArInvoicePaymentSchedule getArInvoicePaymentSchedule() {

        return arInvoicePaymentSchedule;
    }

    public void setArInvoicePaymentSchedule(LocalArInvoicePaymentSchedule arInvoicePaymentSchedule) {

        this.arInvoicePaymentSchedule = arInvoicePaymentSchedule;
    }

    public LocalArPdc getArPdc() {

        return arPdc;
    }

    public void setArPdc(LocalArPdc arPdc) {

        this.arPdc = arPdc;
    }

    public LocalArReceipt getArReceipt() {

        return arReceipt;
    }

    public void setArReceipt(LocalArReceipt arReceipt) {

        this.arReceipt = arReceipt;
    }

    @XmlTransient
    public List getArDistributionRecords() {

        return arDistributionRecords;
    }

    public void setArDistributionRecords(List arDistributionRecords) {

        this.arDistributionRecords = arDistributionRecords;
    }

    @XmlTransient
    public List getArAppliedCredits() {

        return arAppliedCredits;
    }

    public void setArAppliedCredits(List arAppliedCredits) {

        this.arAppliedCredits = arAppliedCredits;
    }

    public void addArDistributionRecord(LocalArDistributionRecord entity) {

        try {
            entity.setArAppliedInvoice(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArDistributionRecord(LocalArDistributionRecord entity) {

        try {
            entity.setArAppliedInvoice(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArAppliedCredit(LocalArAppliedCredit entity) {

        try {
            entity.setArAppliedInvoice(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArAppliedCredit(LocalArAppliedCredit entity) {

        try {
            entity.setArAppliedInvoice(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
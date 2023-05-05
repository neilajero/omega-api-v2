package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "ArCustomerClass")
@Table(name = "AR_CSTMR_CLSS")
public class LocalArCustomerClass extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CC_CODE", nullable = false)
    private Integer ccCode;

    @Column(name = "CC_NM", columnDefinition = "VARCHAR")
    private String ccName;

    @Column(name = "CC_DESC", columnDefinition = "VARCHAR")
    private String ccDescription;

    @Column(name = "CC_NXT_CSTMR_CODE", columnDefinition = "VARCHAR")
    private String ccNextCustomerCode;

    @Column(name = "CC_CSTMR_BTCH")
    private String ccCustomerBatch;

    @Column(name = "CC_DL_PRC", columnDefinition = "VARCHAR")
    private String ccDealPrice;

    @Column(name = "CC_MNTHLY_INT_RT", columnDefinition = "DOUBLE")
    private double ccMonthlyInterestRate = 0;

    @Column(name = "CC_MNMM_FNNC_CHRG", columnDefinition = "DOUBLE")
    private double ccMinimumFinanceCharge = 0;

    @Column(name = "CC_GRC_PRD_DY", columnDefinition = "SMALLINT")
    private short ccGracePeriodDay;

    @Column(name = "CC_DYS_IN_PRD", columnDefinition = "SMALLINT")
    private short ccDaysInPeriod;

    @Column(name = "CC_GL_COA_CHRG_ACCNT", columnDefinition = "INT")
    private Integer ccGlCoaChargeAccount;

    @Column(name = "CC_CHRG_BY_DUE_DT", columnDefinition = "TINYINT")
    private byte ccChargeByDueDate;

    @Column(name = "CC_GL_COA_RCVBL_ACCNT", columnDefinition = "INT")
    private Integer ccGlCoaReceivableAccount;

    @Column(name = "CC_GL_COA_RVNUE_ACCNT", columnDefinition = "INT")
    private Integer ccGlCoaRevenueAccount;

    @Column(name = "CC_GL_COA_UNERND_INT_ACCNT", columnDefinition = "INT")
    private Integer ccGlCoaUnEarnedInterestAccount;

    @Column(name = "CC_GL_COA_ERND_INT_ACCNT", columnDefinition = "INT")
    private Integer ccGlCoaEarnedInterestAccount;

    @Column(name = "CC_GL_COA_UNERND_PNT_ACCNT", columnDefinition = "INT")
    private Integer ccGlCoaUnEarnedPenaltyAccount;

    @Column(name = "CC_GL_COA_ERND_PNT_ACCNT", columnDefinition = "INT")
    private Integer ccGlCoaEarnedPenaltyAccount;

    @Column(name = "CC_ENBL", columnDefinition = "TINYINT")
    private byte ccEnable;

    @Column(name = "CC_ENBL_RBT", columnDefinition = "TINYINT")
    private byte ccEnableRebate;

    @Column(name = "CC_AUTO_CMPUTE_INT", columnDefinition = "TINYINT")
    private byte ccAutoComputeInterest;

    @Column(name = "CC_AUTO_CMPUTE_PNT", columnDefinition = "TINYINT")
    private byte ccAutoComputePenalty;

    @Column(name = "CC_CRDT_LMT", columnDefinition = "DOUBLE")
    private double ccCreditLimit = 0;

    @Column(name = "CC_AD_CMPNY", columnDefinition = "INT")
    private Integer ccAdCompany;

    @JoinColumn(name = "AR_TAX_CODE", referencedColumnName = "AR_TC_CODE")
    @ManyToOne
    private LocalArTaxCode arTaxCode;

    @JoinColumn(name = "AR_WITHHOLDING_TAX_CODE", referencedColumnName = "AR_WTC_CODE")
    @ManyToOne
    private LocalArWithholdingTaxCode arWithholdingTaxCode;

    @OneToMany(mappedBy = "arCustomerClass", fetch = FetchType.LAZY)
    private List<LocalArCustomer> arCustomers;

    @OneToMany(mappedBy = "arCustomerClass", fetch = FetchType.LAZY)
    private List<LocalArStandardMemoLineClass> arStandardMemoLineClasses;

    public Integer getCcCode() {

        return ccCode;
    }

    public void setCcCode(Integer CC_CODE) {

        this.ccCode = CC_CODE;
    }

    public String getCcName() {

        return ccName;
    }

    public void setCcName(String CC_NM) {

        this.ccName = CC_NM;
    }

    public String getCcDescription() {

        return ccDescription;
    }

    public void setCcDescription(String CC_DESC) {

        this.ccDescription = CC_DESC;
    }

    public String getCcNextCustomerCode() {

        return ccNextCustomerCode;
    }

    public void setCcNextCustomerCode(String CC_NXT_CSTMR_CODE) {

        this.ccNextCustomerCode = CC_NXT_CSTMR_CODE;
    }

    public String getCcCustomerBatch() {

        return ccCustomerBatch;
    }

    public void setCcCustomerBatch(String CC_CSTMR_BTCH) {

        this.ccCustomerBatch = CC_CSTMR_BTCH;
    }

    public String getCcDealPrice() {

        return ccDealPrice;
    }

    public void setCcDealPrice(String CC_DL_PRC) {

        this.ccDealPrice = CC_DL_PRC;
    }

    public double getCcMonthlyInterestRate() {

        return ccMonthlyInterestRate;
    }

    public void setCcMonthlyInterestRate(double CC_MNTHLY_INT_RT) {

        this.ccMonthlyInterestRate = CC_MNTHLY_INT_RT;
    }

    public double getCcMinimumFinanceCharge() {

        return ccMinimumFinanceCharge;
    }

    public void setCcMinimumFinanceCharge(double CC_MNMM_FNNC_CHRG) {

        this.ccMinimumFinanceCharge = CC_MNMM_FNNC_CHRG;
    }

    public short getCcGracePeriodDay() {

        return ccGracePeriodDay;
    }

    public void setCcGracePeriodDay(short CC_GRC_PRD_DY) {

        this.ccGracePeriodDay = CC_GRC_PRD_DY;
    }

    public short getCcDaysInPeriod() {

        return ccDaysInPeriod;
    }

    public void setCcDaysInPeriod(short CC_DYS_IN_PRD) {

        this.ccDaysInPeriod = CC_DYS_IN_PRD;
    }

    public Integer getCcGlCoaChargeAccount() {

        return ccGlCoaChargeAccount;
    }

    public void setCcGlCoaChargeAccount(Integer CC_GL_COA_CHRG_ACCNT) {

        this.ccGlCoaChargeAccount = CC_GL_COA_CHRG_ACCNT;
    }

    public byte getCcChargeByDueDate() {

        return ccChargeByDueDate;
    }

    public void setCcChargeByDueDate(byte CC_CHRG_BY_DUE_DT) {

        this.ccChargeByDueDate = CC_CHRG_BY_DUE_DT;
    }

    public Integer getCcGlCoaReceivableAccount() {

        return ccGlCoaReceivableAccount;
    }

    public void setCcGlCoaReceivableAccount(Integer CC_GL_COA_RCVBL_ACCNT) {

        this.ccGlCoaReceivableAccount = CC_GL_COA_RCVBL_ACCNT;
    }

    public Integer getCcGlCoaRevenueAccount() {

        return ccGlCoaRevenueAccount;
    }

    public void setCcGlCoaRevenueAccount(Integer CC_GL_COA_RVNUE_ACCNT) {

        this.ccGlCoaRevenueAccount = CC_GL_COA_RVNUE_ACCNT;
    }

    public Integer getCcGlCoaUnEarnedInterestAccount() {

        return ccGlCoaUnEarnedInterestAccount;
    }

    public void setCcGlCoaUnEarnedInterestAccount(Integer CC_GL_COA_UNERND_INT_ACCNT) {

        this.ccGlCoaUnEarnedInterestAccount = CC_GL_COA_UNERND_INT_ACCNT;
    }

    public Integer getCcGlCoaEarnedInterestAccount() {

        return ccGlCoaEarnedInterestAccount;
    }

    public void setCcGlCoaEarnedInterestAccount(Integer CC_GL_COA_ERND_INT_ACCNT) {

        this.ccGlCoaEarnedInterestAccount = CC_GL_COA_ERND_INT_ACCNT;
    }

    public Integer getCcGlCoaUnEarnedPenaltyAccount() {

        return ccGlCoaUnEarnedPenaltyAccount;
    }

    public void setCcGlCoaUnEarnedPenaltyAccount(Integer CC_GL_COA_UNERND_PNT_ACCNT) {

        this.ccGlCoaUnEarnedPenaltyAccount = CC_GL_COA_UNERND_PNT_ACCNT;
    }

    public Integer getCcGlCoaEarnedPenaltyAccount() {

        return ccGlCoaEarnedPenaltyAccount;
    }

    public void setCcGlCoaEarnedPenaltyAccount(Integer CC_GL_COA_ERND_PNT_ACCNT) {

        this.ccGlCoaEarnedPenaltyAccount = CC_GL_COA_ERND_PNT_ACCNT;
    }

    public byte getCcEnable() {

        return ccEnable;
    }

    public void setCcEnable(byte CC_ENBL) {

        this.ccEnable = CC_ENBL;
    }

    public byte getCcEnableRebate() {

        return ccEnableRebate;
    }

    public void setCcEnableRebate(byte CC_ENBL_RBT) {

        this.ccEnableRebate = CC_ENBL_RBT;
    }

    public byte getCcAutoComputeInterest() {

        return ccAutoComputeInterest;
    }

    public void setCcAutoComputeInterest(byte CC_AUTO_CMPUTE_INT) {

        this.ccAutoComputeInterest = CC_AUTO_CMPUTE_INT;
    }

    public byte getCcAutoComputePenalty() {

        return ccAutoComputePenalty;
    }

    public void setCcAutoComputePenalty(byte CC_AUTO_CMPUTE_PNT) {

        this.ccAutoComputePenalty = CC_AUTO_CMPUTE_PNT;
    }

    public double getCcCreditLimit() {

        return ccCreditLimit;
    }

    public void setCcCreditLimit(double CC_CRDT_LMT) {

        this.ccCreditLimit = CC_CRDT_LMT;
    }

    public Integer getCcAdCompany() {

        return ccAdCompany;
    }

    public void setCcAdCompany(Integer CC_AD_CMPNY) {

        this.ccAdCompany = CC_AD_CMPNY;
    }

    public LocalArTaxCode getArTaxCode() {

        return arTaxCode;
    }

    public void setArTaxCode(LocalArTaxCode arTaxCode) {

        this.arTaxCode = arTaxCode;
    }

    public LocalArWithholdingTaxCode getArWithholdingTaxCode() {

        return arWithholdingTaxCode;
    }

    public void setArWithholdingTaxCode(LocalArWithholdingTaxCode arWithholdingTaxCode) {

        this.arWithholdingTaxCode = arWithholdingTaxCode;
    }

    @XmlTransient
    public List getArCustomers() {

        return arCustomers;
    }

    public void setArCustomers(List arCustomers) {

        this.arCustomers = arCustomers;
    }

    @XmlTransient
    public List getArStandardMemoLineClasses() {

        return arStandardMemoLineClasses;
    }

    public void setArStandardMemoLineClasses(List arStandardMemoLineClasses) {

        this.arStandardMemoLineClasses = arStandardMemoLineClasses;
    }

    public void addArCustomer(LocalArCustomer entity) {

        try {
            entity.setArCustomerClass(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArCustomer(LocalArCustomer entity) {

        try {
            entity.setArCustomerClass(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArStandardMemoLineClass(LocalArStandardMemoLineClass entity) {

        try {
            entity.setArCustomerClass(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArStandardMemoLineClass(LocalArStandardMemoLineClass entity) {

        try {
            entity.setArCustomerClass(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
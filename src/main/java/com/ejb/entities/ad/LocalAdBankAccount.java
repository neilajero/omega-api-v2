package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ap.LocalApCheck;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ap.LocalApSupplierType;
import com.ejb.entities.ar.LocalArCustomer;
import com.ejb.entities.ar.LocalArCustomerType;
import com.ejb.entities.ar.LocalArPdc;
import com.ejb.entities.ar.LocalArReceipt;
import com.ejb.entities.cm.LocalCmAdjustment;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "AdBankAccount")
@Table(name = "AD_BNK_ACCNT")
public class LocalAdBankAccount extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BA_CODE", nullable = false)
    private Integer baCode;

    @Column(name = "BA_NM", columnDefinition = "VARCHAR")
    private String baName;

    @Column(name = "BA_DESC", columnDefinition = "VARCHAR")
    private String baDescription;

    @Column(name = "BA_ACCNT_TYP", columnDefinition = "VARCHAR")
    private String baAccountType;

    @Column(name = "BA_ACCNT_NMBR", columnDefinition = "VARCHAR")
    private String baAccountNumber;

    @Column(name = "BA_ACCNT_USE", columnDefinition = "VARCHAR")
    private String baAccountUse;

    @Column(name = "BA_COA_GL_CSH_ACCNT", columnDefinition = "INT")
    private Integer baCoaGlCashAccount;

    @Column(name = "BA_COA_GL_ON_ACCNT_RCPT", columnDefinition = "INT")
    private Integer baCoaGlOnAccountReceipt;

    @Column(name = "BA_COA_GL_UNPPLD_RCPT", columnDefinition = "INT")
    private Integer baCoaGlUnappliedReceipt;

    @Column(name = "BA_COA_GL_UNPPLD_CHK", columnDefinition = "INT")
    private Integer baCoaGlUnappliedCheck;

    @Column(name = "BA_COA_GL_BNK_CHRG_ACCNT", columnDefinition = "INT")
    private Integer baCoaGlBankChargeAccount;

    @Column(name = "BA_COA_GL_CLRNG_ACCNT", columnDefinition = "INT")
    private Integer baCoaGlClearingAccount;

    @Column(name = "BA_COA_GL_INTRST_ACCNT", columnDefinition = "INT")
    private Integer baCoaGlInterestAccount;

    @Column(name = "BA_COA_GL_ADJSTMNT_ACCNT", columnDefinition = "INT")
    private Integer baCoaGlAdjustmentAccount;

    @Column(name = "BA_COA_GL_CSH_DSCNT", columnDefinition = "INT")
    private Integer baCoaGlCashDiscount;

    @Column(name = "BA_COA_GL_SLS_DSCNT", columnDefinition = "INT")
    private Integer baCoaGlSalesDiscount;

    @Column(name = "BA_COA_GL_ADVNC_ACCNT", columnDefinition = "INT")
    private Integer baCoaGlAdvanceAccount;

    @Column(name = "BA_FLT_BLNC", columnDefinition = "DOUBLE")
    private double baFloatBalance = 0;

    @Column(name = "BA_LST_RCNCLD_DT", columnDefinition = "DATETIME")
    private Date baLastReconciledDate;

    @Column(name = "BA_LST_RCNCLD_BLNC", columnDefinition = "DOUBLE")
    private double baLastReconciledBalance = 0;

    @Column(name = "BA_NXT_CHK_NMBR", columnDefinition = "VARCHAR")
    private String baNextCheckNumber;

    @Column(name = "BA_ENBL", columnDefinition = "TINYINT")
    private byte baEnable;

    @Column(name = "BA_ACCNT_NMBR_SHW", columnDefinition = "TINYINT")
    private byte baAccountNumberShow;

    @Column(name = "BA_ACCNT_NMBR_TP", columnDefinition = "INT")
    private Integer baAccountNumberTop;

    @Column(name = "BA_ACCNT_NMBR_LFT", columnDefinition = "INT")
    private Integer baAccountNumberLeft;

    @Column(name = "BA_ACCNT_NM_SHW", columnDefinition = "TINYINT")
    private byte baAccountNameShow;

    @Column(name = "BA_ACCNT_NM_TP", columnDefinition = "INT")
    private Integer baAccountNameTop;

    @Column(name = "BA_ACCNT_NM_LFT", columnDefinition = "INT")
    private Integer baAccountNameLeft;

    @Column(name = "BA_NMBR_SHW", columnDefinition = "TINYINT")
    private byte baNumberShow;

    @Column(name = "BA_NMBR_TP", columnDefinition = "INT")
    private Integer baNumberTop;

    @Column(name = "BA_NMBR_LFT", columnDefinition = "INT")
    private Integer baNumberLeft;

    @Column(name = "BA_DT_SHW", columnDefinition = "TINYINT")
    private byte baDateShow;

    @Column(name = "BA_DT_TP", columnDefinition = "INT")
    private Integer baDateTop;

    @Column(name = "BA_DT_LFT", columnDefinition = "INT")
    private Integer baDateLeft;

    @Column(name = "BA_PY_SHW", columnDefinition = "TINYINT")
    private byte baPayeeShow;

    @Column(name = "BA_PY_TP", columnDefinition = "INT")
    private Integer baPayeeTop;

    @Column(name = "BA_PY_LFT", columnDefinition = "INT")
    private Integer baPayeeLeft;

    @Column(name = "BA_AMNT_SHW", columnDefinition = "TINYINT")
    private byte baAmountShow;

    @Column(name = "BA_AMNT_TP", columnDefinition = "INT")
    private Integer baAmountTop;

    @Column(name = "BA_AMNT_LFT", columnDefinition = "INT")
    private Integer baAmountLeft;

    @Column(name = "BA_WRD_AMNT_SHW", columnDefinition = "TINYINT")
    private byte baWordAmountShow;

    @Column(name = "BA_WRD_AMNT_TP", columnDefinition = "INT")
    private Integer baWordAmountTop;

    @Column(name = "BA_WRD_AMNT_LFT", columnDefinition = "INT")
    private Integer baWordAmountLeft;

    @Column(name = "BA_CRRNCY_SHW", columnDefinition = "TINYINT")
    private byte baCurrencyShow;

    @Column(name = "BA_CRRNCY_TP", columnDefinition = "INT")
    private Integer baCurrencyTop;

    @Column(name = "BA_CRRNCY_LFT", columnDefinition = "INT")
    private Integer baCurrencyLeft;

    @Column(name = "BA_ADDRSS_SHW", columnDefinition = "TINYINT")
    private byte baAddressShow;

    @Column(name = "BA_ADDRSS_TP", columnDefinition = "INT")
    private Integer baAddressTop;

    @Column(name = "BA_ADDRSS_LFT", columnDefinition = "INT")
    private Integer baAddressLeft;

    @Column(name = "BA_MM_SHW", columnDefinition = "TINYINT")
    private byte baMemoShow;

    @Column(name = "BA_MM_TP", columnDefinition = "INT")
    private Integer baMemoTop;

    @Column(name = "BA_MM_LFT", columnDefinition = "INT")
    private Integer baMemoLeft;

    @Column(name = "BA_DC_NMBR_SHW", columnDefinition = "TINYINT")
    private byte badocNumberShow;

    @Column(name = "BA_DC_NMBR_TP", columnDefinition = "INT")
    private Integer badocNumberTop;

    @Column(name = "BA_DC_NMBR_LFT", columnDefinition = "INT")
    private Integer badocNumberLeft;

    @Column(name = "BA_FNT_SZ", columnDefinition = "INT")
    private Integer baFontSize;

    @Column(name = "BA_FNT_STYL", columnDefinition = "VARCHAR")
    private String baFontStyle;

    @Column(name = "BA_IS_CSH_ACCNT", columnDefinition = "TINYINT")
    private byte baIsCashAccount;

    @Column(name = "BA_AD_CMPNY", columnDefinition = "INT")
    private Integer baAdCompany;

    @JoinColumn(name = "AD_BANK", referencedColumnName = "BNK_CODE")
    @ManyToOne
    private LocalAdBank adBank;

    @JoinColumn(name = "GL_FUNCTIONAL_CURRENCY", referencedColumnName = "FC_CODE")
    @ManyToOne
    private LocalGlFunctionalCurrency glFunctionalCurrency;

    @OneToMany(mappedBy = "adBankAccount", fetch = FetchType.LAZY)
    private List<LocalCmAdjustment> cmAdjustments;

    @OneToMany(mappedBy = "adBankAccount", fetch = FetchType.LAZY)
    private List<LocalApSupplierType> apSupplierTypes;

    @OneToMany(mappedBy = "adBankAccount", fetch = FetchType.LAZY)
    private List<LocalApCheck> apChecks = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "adBankAccount", fetch = FetchType.LAZY)
    private List<LocalArCustomerType> arCustomerTypes;

    @OneToMany(mappedBy = "adBankAccount", fetch = FetchType.LAZY)
    private List<LocalArReceipt> arReceipts = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "adBankAccountCard1", fetch = FetchType.LAZY)
    private List<LocalArReceipt> arCard1Receipts = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "adBankAccountCard2", fetch = FetchType.LAZY)
    private List<LocalArReceipt> arCard2Receipts = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "adBankAccountCard3", fetch = FetchType.LAZY)
    private List<LocalArReceipt> arCard3Receipts = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "adBankAccount", fetch = FetchType.LAZY)
    private List<LocalArPdc> arPdcs = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "adBankAccount", fetch = FetchType.LAZY)
    private List<LocalApSupplier> apSuppliers = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "adBankAccount", fetch = FetchType.LAZY)
    private List<LocalArCustomer> arCustomers = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "adBankAccount", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalAdBranchBankAccount> adBranchBankAccounts;

    @OneToMany(mappedBy = "adBankAccount", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalAdBankAccountBalance> adBankAccountBalances;

    public Integer getBaCode() {

        return baCode;
    }

    public void setBaCode(Integer BA_CODE) {

        this.baCode = BA_CODE;
    }

    public String getBaName() {

        return baName;
    }

    public void setBaName(String BA_NM) {

        this.baName = BA_NM;
    }

    public String getBaDescription() {

        return baDescription;
    }

    public void setBaDescription(String BA_DESC) {

        this.baDescription = BA_DESC;
    }

    public String getBaAccountType() {

        return baAccountType;
    }

    public void setBaAccountType(String BA_ACCNT_TYP) {

        this.baAccountType = BA_ACCNT_TYP;
    }

    public String getBaAccountNumber() {

        return baAccountNumber;
    }

    public void setBaAccountNumber(String BA_ACCNT_NMBR) {

        this.baAccountNumber = BA_ACCNT_NMBR;
    }

    public String getBaAccountUse() {

        return baAccountUse;
    }

    public void setBaAccountUse(String BA_ACCNT_USE) {

        this.baAccountUse = BA_ACCNT_USE;
    }

    public Integer getBaCoaGlCashAccount() {

        return baCoaGlCashAccount;
    }

    public void setBaCoaGlCashAccount(Integer BA_COA_GL_CSH_ACCNT) {

        this.baCoaGlCashAccount = BA_COA_GL_CSH_ACCNT;
    }

    public Integer getBaCoaGlOnAccountReceipt() {

        return baCoaGlOnAccountReceipt;
    }

    public void setBaCoaGlOnAccountReceipt(Integer BA_COA_GL_ON_ACCNT_RCPT) {

        this.baCoaGlOnAccountReceipt = BA_COA_GL_ON_ACCNT_RCPT;
    }

    public Integer getBaCoaGlUnappliedReceipt() {

        return baCoaGlUnappliedReceipt;
    }

    public void setBaCoaGlUnappliedReceipt(Integer BA_COA_GL_UNPPLD_RCPT) {

        this.baCoaGlUnappliedReceipt = BA_COA_GL_UNPPLD_RCPT;
    }

    public Integer getBaCoaGlUnappliedCheck() {

        return baCoaGlUnappliedCheck;
    }

    public void setBaCoaGlUnappliedCheck(Integer BA_COA_GL_UNPPLD_CHK) {

        this.baCoaGlUnappliedCheck = BA_COA_GL_UNPPLD_CHK;
    }

    public Integer getBaCoaGlBankChargeAccount() {

        return baCoaGlBankChargeAccount;
    }

    public void setBaCoaGlBankChargeAccount(Integer BA_COA_GL_BNK_CHRG_ACCNT) {

        this.baCoaGlBankChargeAccount = BA_COA_GL_BNK_CHRG_ACCNT;
    }

    public Integer getBaCoaGlClearingAccount() {

        return baCoaGlClearingAccount;
    }

    public void setBaCoaGlClearingAccount(Integer BA_COA_GL_CLRNG_ACCNT) {

        this.baCoaGlClearingAccount = BA_COA_GL_CLRNG_ACCNT;
    }

    public Integer getBaCoaGlInterestAccount() {

        return baCoaGlInterestAccount;
    }

    public void setBaCoaGlInterestAccount(Integer BA_COA_GL_INTRST_ACCNT) {

        this.baCoaGlInterestAccount = BA_COA_GL_INTRST_ACCNT;
    }

    public Integer getBaCoaGlAdjustmentAccount() {

        return baCoaGlAdjustmentAccount;
    }

    public void setBaCoaGlAdjustmentAccount(Integer BA_COA_GL_ADJSTMNT_ACCNT) {

        this.baCoaGlAdjustmentAccount = BA_COA_GL_ADJSTMNT_ACCNT;
    }

    public Integer getBaCoaGlCashDiscount() {

        return baCoaGlCashDiscount;
    }

    public void setBaCoaGlCashDiscount(Integer BA_COA_GL_CSH_DSCNT) {

        this.baCoaGlCashDiscount = BA_COA_GL_CSH_DSCNT;
    }

    public Integer getBaCoaGlSalesDiscount() {

        return baCoaGlSalesDiscount;
    }

    public void setBaCoaGlSalesDiscount(Integer BA_COA_GL_SLS_DSCNT) {

        this.baCoaGlSalesDiscount = BA_COA_GL_SLS_DSCNT;
    }

    public Integer getBaCoaGlAdvanceAccount() {

        return baCoaGlAdvanceAccount;
    }

    public void setBaCoaGlAdvanceAccount(Integer BA_COA_GL_ADVNC_ACCNT) {

        this.baCoaGlAdvanceAccount = BA_COA_GL_ADVNC_ACCNT;
    }

    public double getBaFloatBalance() {

        return baFloatBalance;
    }

    public void setBaFloatBalance(double BA_FLT_BLNC) {

        this.baFloatBalance = BA_FLT_BLNC;
    }

    public Date getBaLastReconciledDate() {

        return baLastReconciledDate;
    }

    public void setBaLastReconciledDate(Date BA_LST_RCNCLD_DT) {

        this.baLastReconciledDate = BA_LST_RCNCLD_DT;
    }

    public double getBaLastReconciledBalance() {

        return baLastReconciledBalance;
    }

    public void setBaLastReconciledBalance(double BA_LST_RCNCLD_BLNC) {

        this.baLastReconciledBalance = BA_LST_RCNCLD_BLNC;
    }

    public String getBaNextCheckNumber() {

        return baNextCheckNumber;
    }

    public void setBaNextCheckNumber(String BA_NXT_CHK_NMBR) {

        this.baNextCheckNumber = BA_NXT_CHK_NMBR;
    }

    public byte getBaEnable() {

        return baEnable;
    }

    public void setBaEnable(byte BA_ENBL) {

        this.baEnable = BA_ENBL;
    }

    public byte getBaAccountNumberShow() {

        return baAccountNumberShow;
    }

    public void setBaAccountNumberShow(byte BA_ACCNT_NMBR_SHW) {

        this.baAccountNumberShow = BA_ACCNT_NMBR_SHW;
    }

    public Integer getBaAccountNumberTop() {

        return baAccountNumberTop;
    }

    public void setBaAccountNumberTop(Integer BA_ACCNT_NMBR_TP) {

        this.baAccountNumberTop = BA_ACCNT_NMBR_TP;
    }

    public Integer getBaAccountNumberLeft() {

        return baAccountNumberLeft;
    }

    public void setBaAccountNumberLeft(Integer BA_ACCNT_NMBR_LFT) {

        this.baAccountNumberLeft = BA_ACCNT_NMBR_LFT;
    }

    public byte getBaAccountNameShow() {

        return baAccountNameShow;
    }

    public void setBaAccountNameShow(byte BA_ACCNT_NM_SHW) {

        this.baAccountNameShow = BA_ACCNT_NM_SHW;
    }

    public Integer getBaAccountNameTop() {

        return baAccountNameTop;
    }

    public void setBaAccountNameTop(Integer BA_ACCNT_NM_TP) {

        this.baAccountNameTop = BA_ACCNT_NM_TP;
    }

    public Integer getBaAccountNameLeft() {

        return baAccountNameLeft;
    }

    public void setBaAccountNameLeft(Integer BA_ACCNT_NM_LFT) {

        this.baAccountNameLeft = BA_ACCNT_NM_LFT;
    }

    public byte getBaNumberShow() {

        return baNumberShow;
    }

    public void setBaNumberShow(byte BA_NMBR_SHW) {

        this.baNumberShow = BA_NMBR_SHW;
    }

    public Integer getBaNumberTop() {

        return baNumberTop;
    }

    public void setBaNumberTop(Integer BA_NMBR_TP) {

        this.baNumberTop = BA_NMBR_TP;
    }

    public Integer getBaNumberLeft() {

        return baNumberLeft;
    }

    public void setBaNumberLeft(Integer BA_NMBR_LFT) {

        this.baNumberLeft = BA_NMBR_LFT;
    }

    public byte getBaDateShow() {

        return baDateShow;
    }

    public void setBaDateShow(byte BA_DT_SHW) {

        this.baDateShow = BA_DT_SHW;
    }

    public Integer getBaDateTop() {

        return baDateTop;
    }

    public void setBaDateTop(Integer BA_DT_TP) {

        this.baDateTop = BA_DT_TP;
    }

    public Integer getBaDateLeft() {

        return baDateLeft;
    }

    public void setBaDateLeft(Integer BA_DT_LFT) {

        this.baDateLeft = BA_DT_LFT;
    }

    public byte getBaPayeeShow() {

        return baPayeeShow;
    }

    public void setBaPayeeShow(byte BA_PY_SHW) {

        this.baPayeeShow = BA_PY_SHW;
    }

    public Integer getBaPayeeTop() {

        return baPayeeTop;
    }

    public void setBaPayeeTop(Integer BA_PY_TP) {

        this.baPayeeTop = BA_PY_TP;
    }

    public Integer getBaPayeeLeft() {

        return baPayeeLeft;
    }

    public void setBaPayeeLeft(Integer BA_PY_LFT) {

        this.baPayeeLeft = BA_PY_LFT;
    }

    public byte getBaAmountShow() {

        return baAmountShow;
    }

    public void setBaAmountShow(byte BA_AMNT_SHW) {

        this.baAmountShow = BA_AMNT_SHW;
    }

    public Integer getBaAmountTop() {

        return baAmountTop;
    }

    public void setBaAmountTop(Integer BA_AMNT_TP) {

        this.baAmountTop = BA_AMNT_TP;
    }

    public Integer getBaAmountLeft() {

        return baAmountLeft;
    }

    public void setBaAmountLeft(Integer BA_AMNT_LFT) {

        this.baAmountLeft = BA_AMNT_LFT;
    }

    public byte getBaWordAmountShow() {

        return baWordAmountShow;
    }

    public void setBaWordAmountShow(byte BA_WRD_AMNT_SHW) {

        this.baWordAmountShow = BA_WRD_AMNT_SHW;
    }

    public Integer getBaWordAmountTop() {

        return baWordAmountTop;
    }

    public void setBaWordAmountTop(Integer BA_WRD_AMNT_TP) {

        this.baWordAmountTop = BA_WRD_AMNT_TP;
    }

    public Integer getBaWordAmountLeft() {

        return baWordAmountLeft;
    }

    public void setBaWordAmountLeft(Integer BA_WRD_AMNT_LFT) {

        this.baWordAmountLeft = BA_WRD_AMNT_LFT;
    }

    public byte getBaCurrencyShow() {

        return baCurrencyShow;
    }

    public void setBaCurrencyShow(byte BA_CRRNCY_SHW) {

        this.baCurrencyShow = BA_CRRNCY_SHW;
    }

    public Integer getBaCurrencyTop() {

        return baCurrencyTop;
    }

    public void setBaCurrencyTop(Integer BA_CRRNCY_TP) {

        this.baCurrencyTop = BA_CRRNCY_TP;
    }

    public Integer getBaCurrencyLeft() {

        return baCurrencyLeft;
    }

    public void setBaCurrencyLeft(Integer BA_CRRNCY_LFT) {

        this.baCurrencyLeft = BA_CRRNCY_LFT;
    }

    public byte getBaAddressShow() {

        return baAddressShow;
    }

    public void setBaAddressShow(byte BA_ADDRSS_SHW) {

        this.baAddressShow = BA_ADDRSS_SHW;
    }

    public Integer getBaAddressTop() {

        return baAddressTop;
    }

    public void setBaAddressTop(Integer BA_ADDRSS_TP) {

        this.baAddressTop = BA_ADDRSS_TP;
    }

    public Integer getBaAddressLeft() {

        return baAddressLeft;
    }

    public void setBaAddressLeft(Integer BA_ADDRSS_LFT) {

        this.baAddressLeft = BA_ADDRSS_LFT;
    }

    public byte getBaMemoShow() {

        return baMemoShow;
    }

    public void setBaMemoShow(byte BA_MM_SHW) {

        this.baMemoShow = BA_MM_SHW;
    }

    public Integer getBaMemoTop() {

        return baMemoTop;
    }

    public void setBaMemoTop(Integer BA_MM_TP) {

        this.baMemoTop = BA_MM_TP;
    }

    public Integer getBaMemoLeft() {

        return baMemoLeft;
    }

    public void setBaMemoLeft(Integer BA_MM_LFT) {

        this.baMemoLeft = BA_MM_LFT;
    }

    public byte getBadocNumberShow() {

        return badocNumberShow;
    }

    public void setBadocNumberShow(byte BA_DC_NMBR_SHW) {

        this.badocNumberShow = BA_DC_NMBR_SHW;
    }

    public Integer getBadocNumberTop() {

        return badocNumberTop;
    }

    public void setBadocNumberTop(Integer BA_DC_NMBR_TP) {

        this.badocNumberTop = BA_DC_NMBR_TP;
    }

    public Integer getBadocNumberLeft() {

        return badocNumberLeft;
    }

    public void setBadocNumberLeft(Integer BA_DC_NMBR_LFT) {

        this.badocNumberLeft = BA_DC_NMBR_LFT;
    }

    public Integer getBaFontSize() {

        return baFontSize;
    }

    public void setBaFontSize(Integer BA_FNT_SZ) {

        this.baFontSize = BA_FNT_SZ;
    }

    public String getBaFontStyle() {

        return baFontStyle;
    }

    public void setBaFontStyle(String BA_FNT_STYL) {

        this.baFontStyle = BA_FNT_STYL;
    }

    public byte getBaIsCashAccount() {

        return baIsCashAccount;
    }

    public void setBaIsCashAccount(byte BA_IS_CSH_ACCNT) {

        this.baIsCashAccount = BA_IS_CSH_ACCNT;
    }

    public Integer getBaAdCompany() {

        return baAdCompany;
    }

    public void setBaAdCompany(Integer BA_AD_CMPNY) {

        this.baAdCompany = BA_AD_CMPNY;
    }

    public LocalAdBank getAdBank() {

        return adBank;
    }

    public void setAdBank(LocalAdBank adBank) {

        this.adBank = adBank;
    }

    public LocalGlFunctionalCurrency getGlFunctionalCurrency() {

        return glFunctionalCurrency;
    }

    public void setGlFunctionalCurrency(LocalGlFunctionalCurrency glFunctionalCurrency) {

        this.glFunctionalCurrency = glFunctionalCurrency;
    }

    @XmlTransient
    public List getCmAdjustments() {

        return cmAdjustments;
    }

    public void setCmAdjustments(List cmAdjustments) {

        this.cmAdjustments = cmAdjustments;
    }

    @XmlTransient
    public List getApSupplierTypes() {

        return apSupplierTypes;
    }

    public void setApSupplierTypes(List apSupplierTypes) {

        this.apSupplierTypes = apSupplierTypes;
    }

    @XmlTransient
    public List getApChecks() {

        return apChecks;
    }

    public void setApChecks(List apChecks) {

        this.apChecks = apChecks;
    }

    @XmlTransient
    public List getArCustomerTypes() {

        return arCustomerTypes;
    }

    public void setArCustomerTypes(List arCustomerTypes) {

        this.arCustomerTypes = arCustomerTypes;
    }

    @XmlTransient
    public List getArReceipts() {

        return arReceipts;
    }

    public void setArReceipts(List arReceipts) {

        this.arReceipts = arReceipts;
    }

    @XmlTransient
    public List getArCard1Receipts() {

        return arCard1Receipts;
    }

    public void setArCard1Receipts(List arCard1Receipts) {

        this.arCard1Receipts = arCard1Receipts;
    }

    @XmlTransient
    public List getArCard2Receipts() {

        return arCard2Receipts;
    }

    public void setArCard2Receipts(List arCard2Receipts) {

        this.arCard2Receipts = arCard2Receipts;
    }

    @XmlTransient
    public List getArCard3Receipts() {

        return arCard3Receipts;
    }

    public void setArCard3Receipts(List arCard3Receipts) {

        this.arCard3Receipts = arCard3Receipts;
    }

    @XmlTransient
    public List getArPdcs() {

        return arPdcs;
    }

    public void setArPdcs(List arPdcs) {

        this.arPdcs = arPdcs;
    }

    @XmlTransient
    public List getApSuppliers() {

        return apSuppliers;
    }

    public void setApSuppliers(List apSuppliers) {

        this.apSuppliers = apSuppliers;
    }

    @XmlTransient
    public List getArCustomers() {

        return arCustomers;
    }

    public void setArCustomers(List arCustomers) {

        this.arCustomers = arCustomers;
    }

    @XmlTransient
    public List getAdBranchBankAccounts() {

        return adBranchBankAccounts;
    }

    public void setAdBranchBankAccounts(List adBranchBankAccounts) {

        this.adBranchBankAccounts = adBranchBankAccounts;
    }

    @XmlTransient
    public List getAdBankAccountBalances() {

        return adBankAccountBalances;
    }

    public void setAdBankAccountBalances(List adBankAccountBalances) {

        this.adBankAccountBalances = adBankAccountBalances;
    }

    public void addCmAdjustment(LocalCmAdjustment entity) {

        try {
            entity.setAdBankAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropCmAdjustment(LocalCmAdjustment entity) {

        try {
            entity.setAdBankAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApSupplierType(LocalApSupplierType entity) {

        try {
            entity.setAdBankAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApSupplierType(LocalApSupplierType entity) {

        try {
            entity.setAdBankAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApCheck(LocalApCheck entity) {

        try {
            entity.setAdBankAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApCheck(LocalApCheck entity) {

        try {
            entity.setAdBankAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArCustomerType(LocalArCustomerType entity) {

        try {
            entity.setAdBankAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArCustomerType(LocalArCustomerType entity) {

        try {
            entity.setAdBankAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArReceipt(LocalArReceipt entity) {

        try {
            entity.setAdBankAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArReceipt(LocalArReceipt entity) {

        try {
            entity.setAdBankAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApSupplier(LocalApSupplier entity) {

        try {
            entity.setAdBankAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApSupplier(LocalApSupplier entity) {

        try {
            entity.setAdBankAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArCustomer(LocalArCustomer entity) {

        try {
            entity.setAdBankAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArCustomer(LocalArCustomer entity) {

        try {
            entity.setAdBankAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdBranchBankAccount(LocalAdBranchBankAccount entity) {

        try {
            entity.setAdBankAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBranchBankAccount(LocalAdBranchBankAccount entity) {

        try {
            entity.setAdBankAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdBankAccountBalance(LocalAdBankAccountBalance entity) {

        try {
            entity.setAdBankAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBankAccountBalance(LocalAdBankAccountBalance entity) {

        try {
            entity.setAdBankAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArPdc(LocalArPdc entity) {

        try {
            entity.setAdBankAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArPdc(LocalArPdc entity) {

        try {
            entity.setAdBankAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
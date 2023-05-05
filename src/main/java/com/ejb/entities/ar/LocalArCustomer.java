package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.entities.ad.LocalAdBranchCustomer;
import com.ejb.entities.ad.LocalAdPaymentTerm;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.cm.LocalCmAdjustment;
import com.ejb.entities.inv.LocalInvItem;
import com.ejb.entities.inv.LocalInvLineItemTemplate;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "ArCustomer")
@Table(name = "AR_CSTMR")
public class LocalArCustomer extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AR_CST_CODE", nullable = false)
    private Integer cstCode;

    @Column(name = "CST_CSTMR_CODE", columnDefinition = "VARCHAR")
    private String cstCustomerCode;

    @Column(name = "CST_REF_CC", columnDefinition = "VARCHAR")
    private String cstRefCustomerCode;

    @Column(name = "CST_NM", columnDefinition = "VARCHAR")
    private String cstName;

    @Column(name = "CST_DESC", columnDefinition = "VARCHAR")
    private String cstDescription;

    @Column(name = "CST_PYMNT_MTHD", columnDefinition = "VARCHAR")
    private String cstPaymentMethod;

    @Column(name = "CST_CRDT_LMT", columnDefinition = "DOUBLE")
    private double cstCreditLimit = 0;

    @Column(name = "CST_ADDRSS", columnDefinition = "VARCHAR")
    private String cstAddress;

    @Column(name = "CST_CTY", columnDefinition = "VARCHAR")
    private String cstCity;

    @Column(name = "CST_STT_PRVNC", columnDefinition = "VARCHAR")
    private String cstStateProvince;

    @Column(name = "CST_PSTL_CD", columnDefinition = "VARCHAR")
    private String cstPostalCode;

    @Column(name = "CST_CNTRY", columnDefinition = "VARCHAR")
    private String cstCountry;

    @Column(name = "CST_CNTCT", columnDefinition = "VARCHAR")
    private String cstContact;

    @Column(name = "CST_EMP_ID", columnDefinition = "VARCHAR")
    private String cstEmployeeID;

    @Column(name = "CST_ACCNT_NO", columnDefinition = "VARCHAR")
    private String cstAccountNumber;

    @Column(name = "CST_PHN", columnDefinition = "VARCHAR")
    private String cstPhone;

    @Column(name = "CST_FX", columnDefinition = "VARCHAR")
    private String cstFax;

    @Column(name = "CST_ALTRNT_PHN", columnDefinition = "VARCHAR")
    private String cstAlternatePhone;

    @Column(name = "CST_ALTRNT_CNTCT", columnDefinition = "VARCHAR")
    private String cstAlternateContact;

    @Column(name = "CST_EML", columnDefinition = "VARCHAR")
    private String cstEmail;

    @Column(name = "CST_BLL_TO_ADDRSS", columnDefinition = "VARCHAR")
    private String cstBillToAddress;

    @Column(name = "CST_BLL_TO_CNTCT", columnDefinition = "VARCHAR")
    private String cstBillToContact;

    @Column(name = "CST_BLL_TO_ALT_CNTCT", columnDefinition = "VARCHAR")
    private String cstBillToAltContact;

    @Column(name = "CST_BLL_TO_PHN", columnDefinition = "VARCHAR")
    private String cstBillToPhone;

    @Column(name = "CST_BLLNG_HDR", columnDefinition = "VARCHAR")
    private String cstBillingHeader;

    @Column(name = "CST_BLLNG_FTR", columnDefinition = "VARCHAR")
    private String cstBillingFooter;

    @Column(name = "CST_BLLNG_HDR2", columnDefinition = "VARCHAR")
    private String cstBillingHeader2;

    @Column(name = "CST_BLLNG_FTR2", columnDefinition = "VARCHAR")
    private String cstBillingFooter2;

    @Column(name = "CST_BLLNG_HDR3", columnDefinition = "VARCHAR")
    private String cstBillingHeader3;

    @Column(name = "CST_BLLNG_FTR3", columnDefinition = "VARCHAR")
    private String cstBillingFooter3;

    @Column(name = "CST_BLLNG_SGNTRY", columnDefinition = "VARCHAR")
    private String cstBillingSignatory;

    @Column(name = "CST_SGNTRY_TTL", columnDefinition = "VARCHAR")
    private String cstSignatoryTitle;

    @Column(name = "CST_SHP_TO_ADDRSS", columnDefinition = "VARCHAR")
    private String cstShipToAddress;

    @Column(name = "CST_SHP_TO_CNTCT", columnDefinition = "VARCHAR")
    private String cstShipToContact;

    @Column(name = "CST_SHP_TO_ALT_CNTCT", columnDefinition = "VARCHAR")
    private String cstShipToAltContact;

    @Column(name = "CST_SHP_TO_PHN", columnDefinition = "VARCHAR")
    private String cstShipToPhone;

    @Column(name = "CST_TIN", columnDefinition = "VARCHAR")
    private String cstTin;

    @Column(name = "CST_GL_COA_RCVBL_ACCNT", columnDefinition = "INT")
    private Integer cstGlCoaReceivableAccount;

    @Column(name = "CST_GL_COA_RVNUE_ACCNT", columnDefinition = "INT")
    private Integer cstGlCoaRevenueAccount;

    @Column(name = "CST_GL_COA_UNERND_INT_ACCNT", columnDefinition = "INT")
    private Integer cstGlCoaUnEarnedInterestAccount;

    @Column(name = "CST_GL_COA_ERND_INT_ACCNT", columnDefinition = "INT")
    private Integer cstGlCoaEarnedInterestAccount;

    @Column(name = "CST_GL_COA_UNERND_PNT_ACCNT", columnDefinition = "INT")
    private Integer cstGlCoaUnEarnedPenaltyAccount;

    @Column(name = "CST_GL_COA_ERND_PNT_ACCNT", columnDefinition = "INT")
    private Integer cstGlCoaEarnedPenaltyAccount;

    @Column(name = "CST_ENBL", columnDefinition = "TINYINT")
    private byte cstEnable;

    @Column(name = "CST_ENBL_PYRLL", columnDefinition = "TINYINT")
    private byte cstEnablePayroll;

    @Column(name = "CST_ENBL_RTL_CSHR", columnDefinition = "TINYINT")
    private byte cstEnableRetailCashier;

    @Column(name = "CST_ENBL_RBT", columnDefinition = "TINYINT")
    private byte cstEnableRebate;

    @Column(name = "CST_AUTO_CMPUTE_INT", columnDefinition = "TINYINT")
    private byte cstAutoComputeInterest;

    @Column(name = "CST_AUTO_CMPUTE_PNT", columnDefinition = "TINYINT")
    private byte cstAutoComputePenalty;

    @Column(name = "CST_MBL_PHN", columnDefinition = "VARCHAR")
    private String cstMobilePhone;

    @Column(name = "CST_ALTRNT_MBL_PHN", columnDefinition = "VARCHAR")
    private String cstAlternateMobilePhone;

    @Column(name = "CST_BRTHDY", columnDefinition = "VARCHAR")
    private Date cstBirthday;

    @Column(name = "CST_DL_PRC", columnDefinition = "VARCHAR")
    private String cstDealPrice;

    @Column(name = "CST_AREA", columnDefinition = "VARCHAR")
    private String cstArea;

    @Column(name = "CST_SQ_MTR", columnDefinition = "DOUBLE")
    private double cstSquareMeter = 0;

    @Column(name = "CST_NO_PRKNG", columnDefinition = "DOUBLE")
    private double cstNumbersParking = 0;

    @Column(name = "CST_MNTHLY_INT_RT", columnDefinition = "DOUBLE")
    private double cstMonthlyInterestRate = 0;

    @Column(name = "CST_PRKNG_ID", columnDefinition = "VARCHAR")
    private String cstParkingID;

    @Column(name = "CST_WP_CSTMR_ID", columnDefinition = "VARCHAR")
    private String cstWordPressCustomerID;

    @Column(name = "CST_ASD_RT", columnDefinition = "DOUBLE")
    private double cstAssociationDuesRate = 0;

    @Column(name = "CST_RPT_RT", columnDefinition = "DOUBLE")
    private double cstRealPropertyTaxRate = 0;

    @Column(name = "CST_ENTRY_DT", columnDefinition = "DATETIME")
    private Date cstEntryDate;

    @Column(name = "CST_EFFCTVTY_DYS", columnDefinition = "SMALLINT")
    private short cstEffectivityDays;

    @Column(name = "CST_AD_LV_RGN", columnDefinition = "VARCHAR")
    private String cstAdLvRegion;

    @Column(name = "CST_MMO", columnDefinition = "VARCHAR")
    private String cstMemo;

    @Column(name = "CST_CSTMR_BTCH", columnDefinition = "VARCHAR")
    private String cstCustomerBatch;

    @Column(name = "CST_CSTMR_DPRTMNT", columnDefinition = "VARCHAR")
    private String cstCustomerDepartment;

    @Column(name = "AR_SALESPERSON2", columnDefinition = "VARCHAR")
    private Integer cstArSalesperson2;

    @Column(name = "CST_HR_ENBL_CB", columnDefinition = "TINYINT")
    private byte cstHrEnableCashBond;

    @Column(name = "CST_HR_CB_AMNT", columnDefinition = "DOUBLE")
    private double cstHrCashBondAmount = 0;

    @Column(name = "CST_HR_ENBL_IS", columnDefinition = "TINYINT")
    private byte cstHrEnableInsMisc;

    @Column(name = "CST_HR_IS_AMNT", columnDefinition = "DOUBLE")
    private double cstHrInsMiscAmount = 0;

    @Column(name = "CST_APPRVL_STATUS", columnDefinition = "VARCHAR")
    private String cstApprovalStatus;

    @Column(name = "CST_RSN_FR_RJCTN", columnDefinition = "VARCHAR")
    private String cstReasonForRejection;

    @Column(name = "CST_PSTD", columnDefinition = "TINYINT")
    private byte cstPosted;

    @Column(name = "CST_CRTD_BY", columnDefinition = "VARCHAR")
    private String cstCreatedBy;

    @Column(name = "CST_DT_CRTD", columnDefinition = "DATETIME")
    private Date cstDateCreated;

    @Column(name = "CST_LST_MDFD_BY", columnDefinition = "VARCHAR")
    private String cstLastModifiedBy;

    @Column(name = "CST_DT_LST_MDFD", columnDefinition = "DATETIME")
    private Date cstDateLastModified;

    @Column(name = "CST_APPRVD_RJCTD_BY", columnDefinition = "VARCHAR")
    private String cstApprovedRejectedBy;

    @Column(name = "CST_DT_APPRVD_RJCTD", columnDefinition = "DATETIME")
    private Date cstDateApprovedRejected;

    @Column(name = "CST_PSTD_BY", columnDefinition = "VARCHAR")
    private String cstPostedBy;

    @Column(name = "CST_DT_PSTD", columnDefinition = "DATETIME")
    private Date cstDatePosted;

    @Column(name = "CST_AD_BRNCH", columnDefinition = "INT")
    private Integer cstAdBranch;

    @Column(name = "CST_AD_CMPNY", columnDefinition = "INT")
    private Integer cstAdCompany;

    @JoinColumn(name = "AD_BANK_ACCOUNT", referencedColumnName = "BA_CODE")
    @ManyToOne
    private LocalAdBankAccount adBankAccount;

    @JoinColumn(name = "AD_PAYMENT_TERM", referencedColumnName = "PYT_CODE")
    @ManyToOne
    private LocalAdPaymentTerm adPaymentTerm;

    @JoinColumn(name = "AP_SUPPLIER", referencedColumnName = "SPL_CODE")
    @ManyToOne
    private LocalApSupplier apSupplier;

    @JoinColumn(name = "AR_CUSTOMER_CLASS", referencedColumnName = "CC_CODE")
    @ManyToOne
    private LocalArCustomerClass arCustomerClass;

    @JoinColumn(name = "AR_CUSTOMER_TYPE", referencedColumnName = "CT_CODE")
    @ManyToOne
    private LocalArCustomerType arCustomerType;

    @JoinColumn(name = "AR_SALESPERSON", referencedColumnName = "SLP_CODE")
    @ManyToOne
    private LocalArSalesperson arSalesperson;

    @JoinColumn(name = "INV_LINE_ITEM_TEMPLATE", referencedColumnName = "LIT_CODE")
    @ManyToOne
    private LocalInvLineItemTemplate invLineItemTemplate;

    @OneToMany(mappedBy = "arCustomer", fetch = FetchType.LAZY)
    private List<LocalArInvoice> arInvoices;

    @OneToMany(mappedBy = "arCustomer", fetch = FetchType.LAZY)
    private List<LocalArReceipt> arReceipts;

    @OneToMany(mappedBy = "arCustomer", fetch = FetchType.LAZY)
    private List<LocalArCustomerBalance> arCustomerBalances;

    @OneToMany(mappedBy = "arCustomer", fetch = FetchType.LAZY)
    private List<LocalArPdc> arPdcs;

    @OneToMany(mappedBy = "arCustomer", fetch = FetchType.LAZY)
    private List<LocalArSalesOrder> arSalesOrders;

    @OneToMany(mappedBy = "arCustomer", fetch = FetchType.LAZY)
    private List<LocalArJobOrder> arJobOrders;

    @OneToMany(mappedBy = "arCustomer", fetch = FetchType.LAZY)
    private List<LocalAdBranchCustomer> adBranchCustomers;

    @OneToMany(mappedBy = "arCustomer", fetch = FetchType.LAZY)
    private List<LocalInvItem> invItems;

    @OneToMany(mappedBy = "arCustomer", fetch = FetchType.LAZY)
    private List<LocalCmAdjustment> cmAdjustments;

    @OneToMany(mappedBy = "arCustomer", fetch = FetchType.LAZY)
    private List<LocalArStandardMemoLineClass> arStandardMemoLineClasses;

    public Integer getCstCode() {

        return cstCode;
    }

    public void setCstCode(Integer AR_CST_CODE) {

        this.cstCode = AR_CST_CODE;
    }

    public String getCstCustomerCode() {

        return cstCustomerCode;
    }

    public void setCstCustomerCode(String CST_CSTMR_CODE) {

        this.cstCustomerCode = CST_CSTMR_CODE;
    }

    public String getCstRefCustomerCode() {

        return cstRefCustomerCode;
    }

    public void setCstRefCustomerCode(String CST_REF_CC) {

        this.cstRefCustomerCode = CST_REF_CC;
    }

    public String getCstName() {

        return cstName;
    }

    public void setCstName(String CST_NM) {

        this.cstName = CST_NM;
    }

    public String getCstDescription() {

        return cstDescription;
    }

    public void setCstDescription(String CST_DESC) {

        this.cstDescription = CST_DESC;
    }

    public String getCstPaymentMethod() {

        return cstPaymentMethod;
    }

    public void setCstPaymentMethod(String CST_PYMNT_MTHD) {

        this.cstPaymentMethod = CST_PYMNT_MTHD;
    }

    public double getCstCreditLimit() {

        return cstCreditLimit;
    }

    public void setCstCreditLimit(double CST_CRDT_LMT) {

        this.cstCreditLimit = CST_CRDT_LMT;
    }

    public String getCstAddress() {

        return cstAddress;
    }

    public void setCstAddress(String CST_ADDRSS) {

        this.cstAddress = CST_ADDRSS;
    }

    public String getCstCity() {

        return cstCity;
    }

    public void setCstCity(String CST_CTY) {

        this.cstCity = CST_CTY;
    }

    public String getCstStateProvince() {

        return cstStateProvince;
    }

    public void setCstStateProvince(String CST_STT_PRVNC) {

        this.cstStateProvince = CST_STT_PRVNC;
    }

    public String getCstPostalCode() {

        return cstPostalCode;
    }

    public void setCstPostalCode(String CST_PSTL_CD) {

        this.cstPostalCode = CST_PSTL_CD;
    }

    public String getCstCountry() {

        return cstCountry;
    }

    public void setCstCountry(String CST_CNTRY) {

        this.cstCountry = CST_CNTRY;
    }

    public String getCstContact() {

        return cstContact;
    }

    public void setCstContact(String CST_CNTCT) {

        this.cstContact = CST_CNTCT;
    }

    public String getCstEmployeeID() {

        return cstEmployeeID;
    }

    public void setCstEmployeeID(String CST_EMP_ID) {

        this.cstEmployeeID = CST_EMP_ID;
    }

    public String getCstAccountNumber() {

        return cstAccountNumber;
    }

    public void setCstAccountNumber(String CST_ACCNT_NO) {

        this.cstAccountNumber = CST_ACCNT_NO;
    }

    public String getCstPhone() {

        return cstPhone;
    }

    public void setCstPhone(String CST_PHN) {

        this.cstPhone = CST_PHN;
    }

    public String getCstFax() {

        return cstFax;
    }

    public void setCstFax(String CST_FX) {

        this.cstFax = CST_FX;
    }

    public String getCstAlternatePhone() {

        return cstAlternatePhone;
    }

    public void setCstAlternatePhone(String CST_ALTRNT_PHN) {

        this.cstAlternatePhone = CST_ALTRNT_PHN;
    }

    public String getCstAlternateContact() {

        return cstAlternateContact;
    }

    public void setCstAlternateContact(String CST_ALTRNT_CNTCT) {

        this.cstAlternateContact = CST_ALTRNT_CNTCT;
    }

    public String getCstEmail() {

        return cstEmail;
    }

    public void setCstEmail(String CST_EML) {

        this.cstEmail = CST_EML;
    }

    public String getCstBillToAddress() {

        return cstBillToAddress;
    }

    public void setCstBillToAddress(String CST_BLL_TO_ADDRSS) {

        this.cstBillToAddress = CST_BLL_TO_ADDRSS;
    }

    public String getCstBillToContact() {

        return cstBillToContact;
    }

    public void setCstBillToContact(String CST_BLL_TO_CNTCT) {

        this.cstBillToContact = CST_BLL_TO_CNTCT;
    }

    public String getCstBillToAltContact() {

        return cstBillToAltContact;
    }

    public void setCstBillToAltContact(String CST_BLL_TO_ALT_CNTCT) {

        this.cstBillToAltContact = CST_BLL_TO_ALT_CNTCT;
    }

    public String getCstBillToPhone() {

        return cstBillToPhone;
    }

    public void setCstBillToPhone(String CST_BLL_TO_PHN) {

        this.cstBillToPhone = CST_BLL_TO_PHN;
    }

    public String getCstBillingHeader() {

        return cstBillingHeader;
    }

    public void setCstBillingHeader(String CST_BLLNG_HDR) {

        this.cstBillingHeader = CST_BLLNG_HDR;
    }

    public String getCstBillingFooter() {

        return cstBillingFooter;
    }

    public void setCstBillingFooter(String CST_BLLNG_FTR) {

        this.cstBillingFooter = CST_BLLNG_FTR;
    }

    public String getCstBillingHeader2() {

        return cstBillingHeader2;
    }

    public void setCstBillingHeader2(String CST_BLLNG_HDR2) {

        this.cstBillingHeader2 = CST_BLLNG_HDR2;
    }

    public String getCstBillingFooter2() {

        return cstBillingFooter2;
    }

    public void setCstBillingFooter2(String CST_BLLNG_FTR2) {

        this.cstBillingFooter2 = CST_BLLNG_FTR2;
    }

    public String getCstBillingHeader3() {

        return cstBillingHeader3;
    }

    public void setCstBillingHeader3(String CST_BLLNG_HDR3) {

        this.cstBillingHeader3 = CST_BLLNG_HDR3;
    }

    public String getCstBillingFooter3() {

        return cstBillingFooter3;
    }

    public void setCstBillingFooter3(String CST_BLLNG_FTR3) {

        this.cstBillingFooter3 = CST_BLLNG_FTR3;
    }

    public String getCstBillingSignatory() {

        return cstBillingSignatory;
    }

    public void setCstBillingSignatory(String CST_BLLNG_SGNTRY) {

        this.cstBillingSignatory = CST_BLLNG_SGNTRY;
    }

    public String getCstSignatoryTitle() {

        return cstSignatoryTitle;
    }

    public void setCstSignatoryTitle(String CST_SGNTRY_TTL) {

        this.cstSignatoryTitle = CST_SGNTRY_TTL;
    }

    public String getCstShipToAddress() {

        return cstShipToAddress;
    }

    public void setCstShipToAddress(String CST_SHP_TO_ADDRSS) {

        this.cstShipToAddress = CST_SHP_TO_ADDRSS;
    }

    public String getCstShipToContact() {

        return cstShipToContact;
    }

    public void setCstShipToContact(String CST_SHP_TO_CNTCT) {

        this.cstShipToContact = CST_SHP_TO_CNTCT;
    }

    public String getCstShipToAltContact() {

        return cstShipToAltContact;
    }

    public void setCstShipToAltContact(String CST_SHP_TO_ALT_CNTCT) {

        this.cstShipToAltContact = CST_SHP_TO_ALT_CNTCT;
    }

    public String getCstShipToPhone() {

        return cstShipToPhone;
    }

    public void setCstShipToPhone(String CST_SHP_TO_PHN) {

        this.cstShipToPhone = CST_SHP_TO_PHN;
    }

    public String getCstTin() {

        return cstTin;
    }

    public void setCstTin(String CST_TIN) {

        this.cstTin = CST_TIN;
    }

    public Integer getCstGlCoaReceivableAccount() {

        return cstGlCoaReceivableAccount;
    }

    public void setCstGlCoaReceivableAccount(Integer CST_GL_COA_RCVBL_ACCNT) {

        this.cstGlCoaReceivableAccount = CST_GL_COA_RCVBL_ACCNT;
    }

    public Integer getCstGlCoaRevenueAccount() {

        return cstGlCoaRevenueAccount;
    }

    public void setCstGlCoaRevenueAccount(Integer CST_GL_COA_RVNUE_ACCNT) {

        this.cstGlCoaRevenueAccount = CST_GL_COA_RVNUE_ACCNT;
    }

    public Integer getCstGlCoaUnEarnedInterestAccount() {

        return cstGlCoaUnEarnedInterestAccount;
    }

    public void setCstGlCoaUnEarnedInterestAccount(Integer CST_GL_COA_UNERND_INT_ACCNT) {

        this.cstGlCoaUnEarnedInterestAccount = CST_GL_COA_UNERND_INT_ACCNT;
    }

    public Integer getCstGlCoaEarnedInterestAccount() {

        return cstGlCoaEarnedInterestAccount;
    }

    public void setCstGlCoaEarnedInterestAccount(Integer CST_GL_COA_ERND_INT_ACCNT) {

        this.cstGlCoaEarnedInterestAccount = CST_GL_COA_ERND_INT_ACCNT;
    }

    public Integer getCstGlCoaUnEarnedPenaltyAccount() {

        return cstGlCoaUnEarnedPenaltyAccount;
    }

    public void setCstGlCoaUnEarnedPenaltyAccount(Integer CST_GL_COA_UNERND_PNT_ACCNT) {

        this.cstGlCoaUnEarnedPenaltyAccount = CST_GL_COA_UNERND_PNT_ACCNT;
    }

    public Integer getCstGlCoaEarnedPenaltyAccount() {

        return cstGlCoaEarnedPenaltyAccount;
    }

    public void setCstGlCoaEarnedPenaltyAccount(Integer CST_GL_COA_ERND_PNT_ACCNT) {

        this.cstGlCoaEarnedPenaltyAccount = CST_GL_COA_ERND_PNT_ACCNT;
    }

    public byte getCstEnable() {

        return cstEnable;
    }

    public void setCstEnable(byte CST_ENBL) {

        this.cstEnable = CST_ENBL;
    }

    public byte getCstEnablePayroll() {

        return cstEnablePayroll;
    }

    public void setCstEnablePayroll(byte CST_ENBL_PYRLL) {

        this.cstEnablePayroll = CST_ENBL_PYRLL;
    }

    public byte getCstEnableRetailCashier() {

        return cstEnableRetailCashier;
    }

    public void setCstEnableRetailCashier(byte CST_ENBL_RTL_CSHR) {

        this.cstEnableRetailCashier = CST_ENBL_RTL_CSHR;
    }

    public byte getCstEnableRebate() {

        return cstEnableRebate;
    }

    public void setCstEnableRebate(byte CST_ENBL_RBT) {

        this.cstEnableRebate = CST_ENBL_RBT;
    }

    public byte getCstAutoComputeInterest() {

        return cstAutoComputeInterest;
    }

    public void setCstAutoComputeInterest(byte CST_AUTO_CMPUTE_INT) {

        this.cstAutoComputeInterest = CST_AUTO_CMPUTE_INT;
    }

    public byte getCstAutoComputePenalty() {

        return cstAutoComputePenalty;
    }

    public void setCstAutoComputePenalty(byte CST_AUTO_CMPUTE_PNT) {

        this.cstAutoComputePenalty = CST_AUTO_CMPUTE_PNT;
    }

    public String getCstMobilePhone() {

        return cstMobilePhone;
    }

    public void setCstMobilePhone(String CST_MBL_PHN) {

        this.cstMobilePhone = CST_MBL_PHN;
    }

    public String getCstAlternateMobilePhone() {

        return cstAlternateMobilePhone;
    }

    public void setCstAlternateMobilePhone(String CST_ALTRNT_MBL_PHN) {

        this.cstAlternateMobilePhone = CST_ALTRNT_MBL_PHN;
    }

    public Date getCstBirthday() {

        return cstBirthday;
    }

    public void setCstBirthday(Date CST_BRTHDY) {

        this.cstBirthday = CST_BRTHDY;
    }

    public String getCstDealPrice() {

        return cstDealPrice;
    }

    public void setCstDealPrice(String CST_DL_PRC) {

        this.cstDealPrice = CST_DL_PRC;
    }

    public String getCstArea() {

        return cstArea;
    }

    public void setCstArea(String CST_AREA) {

        this.cstArea = CST_AREA;
    }

    public double getCstSquareMeter() {

        return cstSquareMeter;
    }

    public void setCstSquareMeter(double CST_SQ_MTR) {

        this.cstSquareMeter = CST_SQ_MTR;
    }

    public double getCstNumbersParking() {

        return cstNumbersParking;
    }

    public void setCstNumbersParking(double CST_NO_PRKNG) {

        this.cstNumbersParking = CST_NO_PRKNG;
    }

    public double getCstMonthlyInterestRate() {

        return cstMonthlyInterestRate;
    }

    public void setCstMonthlyInterestRate(double CST_MNTHLY_INT_RT) {

        this.cstMonthlyInterestRate = CST_MNTHLY_INT_RT;
    }

    public String getCstParkingID() {

        return cstParkingID;
    }

    public void setCstParkingID(String CST_PRKNG_ID) {

        this.cstParkingID = CST_PRKNG_ID;
    }

    public String getCstWordPressCustomerID() {

        return cstWordPressCustomerID;
    }

    public void setCstWordPressCustomerID(String CST_WP_CSTMR_ID) {

        this.cstWordPressCustomerID = CST_WP_CSTMR_ID;
    }

    public double getCstAssociationDuesRate() {

        return cstAssociationDuesRate;
    }

    public void setCstAssociationDuesRate(double CST_ASD_RT) {

        this.cstAssociationDuesRate = CST_ASD_RT;
    }

    public double getCstRealPropertyTaxRate() {

        return cstRealPropertyTaxRate;
    }

    public void setCstRealPropertyTaxRate(double CST_RPT_RT) {

        this.cstRealPropertyTaxRate = CST_RPT_RT;
    }

    public Date getCstEntryDate() {

        return cstEntryDate;
    }

    public void setCstEntryDate(Date CST_ENTRY_DT) {

        this.cstEntryDate = CST_ENTRY_DT;
    }

    public short getCstEffectivityDays() {

        return cstEffectivityDays;
    }

    public void setCstEffectivityDays(short CST_EFFCTVTY_DYS) {

        this.cstEffectivityDays = CST_EFFCTVTY_DYS;
    }

    public String getCstAdLvRegion() {

        return cstAdLvRegion;
    }

    public void setCstAdLvRegion(String CST_AD_LV_RGN) {

        this.cstAdLvRegion = CST_AD_LV_RGN;
    }

    public String getCstMemo() {

        return cstMemo;
    }

    public void setCstMemo(String CST_MMO) {

        this.cstMemo = CST_MMO;
    }

    public String getCstCustomerBatch() {

        return cstCustomerBatch;
    }

    public void setCstCustomerBatch(String CST_CSTMR_BTCH) {

        this.cstCustomerBatch = CST_CSTMR_BTCH;
    }

    public String getCstCustomerDepartment() {

        return cstCustomerDepartment;
    }

    public void setCstCustomerDepartment(String CST_CSTMR_DPRTMNT) {

        this.cstCustomerDepartment = CST_CSTMR_DPRTMNT;
    }

    public Integer getCstArSalesperson2() {

        return cstArSalesperson2;
    }

    public void setCstArSalesperson2(Integer AR_SALESPERSON2) {

        this.cstArSalesperson2 = AR_SALESPERSON2;
    }

    public byte getCstHrEnableCashBond() {

        return cstHrEnableCashBond;
    }

    public void setCstHrEnableCashBond(byte CST_HR_ENBL_CB) {

        this.cstHrEnableCashBond = CST_HR_ENBL_CB;
    }

    public double getCstHrCashBondAmount() {

        return cstHrCashBondAmount;
    }

    public void setCstHrCashBondAmount(double CST_HR_CB_AMNT) {

        this.cstHrCashBondAmount = CST_HR_CB_AMNT;
    }

    public byte getCstHrEnableInsMisc() {

        return cstHrEnableInsMisc;
    }

    public void setCstHrEnableInsMisc(byte CST_HR_ENBL_IS) {

        this.cstHrEnableInsMisc = CST_HR_ENBL_IS;
    }

    public double getCstHrInsMiscAmount() {

        return cstHrInsMiscAmount;
    }

    public void setCstHrInsMiscAmount(double CST_HR_IS_AMNT) {

        this.cstHrInsMiscAmount = CST_HR_IS_AMNT;
    }

    public String getCstApprovalStatus() {

        return cstApprovalStatus;
    }

    public void setCstApprovalStatus(String CST_APPRVL_STATUS) {

        this.cstApprovalStatus = CST_APPRVL_STATUS;
    }

    public String getCstReasonForRejection() {

        return cstReasonForRejection;
    }

    public void setCstReasonForRejection(String CST_RSN_FR_RJCTN) {

        this.cstReasonForRejection = CST_RSN_FR_RJCTN;
    }

    public byte getCstPosted() {

        return cstPosted;
    }

    public void setCstPosted(byte CST_PSTD) {

        this.cstPosted = CST_PSTD;
    }

    public String getCstCreatedBy() {

        return cstCreatedBy;
    }

    public void setCstCreatedBy(String CST_CRTD_BY) {

        this.cstCreatedBy = CST_CRTD_BY;
    }

    public Date getCstDateCreated() {

        return cstDateCreated;
    }

    public void setCstDateCreated(Date CST_DT_CRTD) {

        this.cstDateCreated = CST_DT_CRTD;
    }

    public String getCstLastModifiedBy() {

        return cstLastModifiedBy;
    }

    public void setCstLastModifiedBy(String CST_LST_MDFD_BY) {

        this.cstLastModifiedBy = CST_LST_MDFD_BY;
    }

    public Date getCstDateLastModified() {

        return cstDateLastModified;
    }

    public void setCstDateLastModified(Date CST_DT_LST_MDFD) {

        this.cstDateLastModified = CST_DT_LST_MDFD;
    }

    public String getCstApprovedRejectedBy() {

        return cstApprovedRejectedBy;
    }

    public void setCstApprovedRejectedBy(String CST_APPRVD_RJCTD_BY) {

        this.cstApprovedRejectedBy = CST_APPRVD_RJCTD_BY;
    }

    public Date getCstDateApprovedRejected() {

        return cstDateApprovedRejected;
    }

    public void setCstDateApprovedRejected(Date CST_DT_APPRVD_RJCTD) {

        this.cstDateApprovedRejected = CST_DT_APPRVD_RJCTD;
    }

    public String getCstPostedBy() {

        return cstPostedBy;
    }

    public void setCstPostedBy(String CST_PSTD_BY) {

        this.cstPostedBy = CST_PSTD_BY;
    }

    public Date getCstDatePosted() {

        return cstDatePosted;
    }

    public void setCstDatePosted(Date CST_DT_PSTD) {

        this.cstDatePosted = CST_DT_PSTD;
    }

    public Integer getCstAdBranch() {

        return cstAdBranch;
    }

    public void setCstAdBranch(Integer CST_AD_BRNCH) {

        this.cstAdBranch = CST_AD_BRNCH;
    }

    public Integer getCstAdCompany() {

        return cstAdCompany;
    }

    public void setCstAdCompany(Integer CST_AD_CMPNY) {

        this.cstAdCompany = CST_AD_CMPNY;
    }

    public LocalAdBankAccount getAdBankAccount() {

        return adBankAccount;
    }

    public void setAdBankAccount(LocalAdBankAccount adBankAccount) {

        this.adBankAccount = adBankAccount;
    }

    public LocalAdPaymentTerm getAdPaymentTerm() {

        return adPaymentTerm;
    }

    public void setAdPaymentTerm(LocalAdPaymentTerm adPaymentTerm) {

        this.adPaymentTerm = adPaymentTerm;
    }

    public LocalApSupplier getApSupplier() {

        return apSupplier;
    }

    public void setApSupplier(LocalApSupplier apSupplier) {

        this.apSupplier = apSupplier;
    }

    public LocalArCustomerClass getArCustomerClass() {

        return arCustomerClass;
    }

    public void setArCustomerClass(LocalArCustomerClass arCustomerClass) {

        this.arCustomerClass = arCustomerClass;
    }

    public LocalArCustomerType getArCustomerType() {

        return arCustomerType;
    }

    public void setArCustomerType(LocalArCustomerType arCustomerType) {

        this.arCustomerType = arCustomerType;
    }

    public LocalArSalesperson getArSalesperson() {

        return arSalesperson;
    }

    public void setArSalesperson(LocalArSalesperson arSalesperson) {

        this.arSalesperson = arSalesperson;
    }

    public LocalInvLineItemTemplate getInvLineItemTemplate() {

        return invLineItemTemplate;
    }

    public void setInvLineItemTemplate(LocalInvLineItemTemplate invLineItemTemplate) {

        this.invLineItemTemplate = invLineItemTemplate;
    }

    @XmlTransient
    public List getArInvoices() {

        return arInvoices;
    }

    public void setArInvoices(List arInvoices) {

        this.arInvoices = arInvoices;
    }

    @XmlTransient
    public List getArReceipts() {

        return arReceipts;
    }

    public void setArReceipts(List arReceipts) {

        this.arReceipts = arReceipts;
    }

    @XmlTransient
    public List getArCustomerBalances() {

        return arCustomerBalances;
    }

    public void setArCustomerBalances(List arCustomerBalances) {

        this.arCustomerBalances = arCustomerBalances;
    }

    @XmlTransient
    public List getArPdcs() {

        return arPdcs;
    }

    public void setArPdcs(List arPdcs) {

        this.arPdcs = arPdcs;
    }

    @XmlTransient
    public List getArSalesOrders() {

        return arSalesOrders;
    }

    public void setArSalesOrders(List arSalesOrders) {

        this.arSalesOrders = arSalesOrders;
    }

    @XmlTransient
    public List getArJobOrders() {

        return arJobOrders;
    }

    public void setArJobOrders(List arJobOrders) {

        this.arJobOrders = arJobOrders;
    }

    @XmlTransient
    public List getAdBranchCustomers() {

        return adBranchCustomers;
    }

    public void setAdBranchCustomers(List adBranchCustomers) {

        this.adBranchCustomers = adBranchCustomers;
    }

    @XmlTransient
    public List getInvItems() {

        return invItems;
    }

    public void setInvItems(List invItems) {

        this.invItems = invItems;
    }

    @XmlTransient
    public List getCmAdjustments() {

        return cmAdjustments;
    }

    public void setCmAdjustments(List cmAdjustments) {

        this.cmAdjustments = cmAdjustments;
    }

    @XmlTransient
    public List getArStandardMemoLineClasses() {

        return arStandardMemoLineClasses;
    }

    public void setArStandardMemoLineClasses(List arStandardMemoLineClasses) {

        this.arStandardMemoLineClasses = arStandardMemoLineClasses;
    }

    public void addInvItem(LocalInvItem entity) {

        try {
            entity.setArCustomer(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvItem(LocalInvItem entity) {

        try {
            entity.setArCustomer(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addCmAdjustment(LocalCmAdjustment entity) {

        try {
            entity.setArCustomer(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropCmAdjustment(LocalCmAdjustment entity) {

        try {
            entity.setArCustomer(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArInvoice(LocalArInvoice entity) {

        try {
            entity.setArCustomer(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArInvoice(LocalArInvoice entity) {

        try {
            entity.setArCustomer(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArReceipt(LocalArReceipt entity) {

        try {
            entity.setArCustomer(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArReceipt(LocalArReceipt entity) {

        try {
            entity.setArCustomer(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArCustomerBalance(LocalArCustomerBalance entity) {

        try {
            entity.setArCustomer(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArCustomerBalance(LocalArCustomerBalance entity) {

        try {
            entity.setArCustomer(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArPdc(LocalArPdc entity) {

        try {
            entity.setArCustomer(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArPdc(LocalArPdc entity) {

        try {
            entity.setArCustomer(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArSalesOrder(LocalArSalesOrder entity) {

        try {
            entity.setArCustomer(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArSalesOrder(LocalArSalesOrder entity) {

        try {
            entity.setArCustomer(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdBranchCustomer(LocalAdBranchCustomer entity) {

        try {
            entity.setArCustomer(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBranchCustomer(LocalAdBranchCustomer entity) {

        try {
            entity.setArCustomer(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
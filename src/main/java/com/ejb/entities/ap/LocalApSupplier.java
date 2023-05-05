package com.ejb.entities.ap;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.entities.ad.LocalAdBranchSupplier;
import com.ejb.entities.ad.LocalAdPaymentTerm;
import com.ejb.entities.ar.LocalArCustomer;
import com.ejb.entities.gl.LocalGlInvestorAccountBalance;
import com.ejb.entities.inv.LocalInvAdjustment;
import com.ejb.entities.inv.LocalInvItem;
import com.ejb.entities.inv.LocalInvLineItemTemplate;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;

@Entity(name = "ApSupplier")
@Table(name = "AP_SPPLR")
public class LocalApSupplier extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SPL_CODE", nullable = false)
    private Integer splCode;

    @Column(name = "SPL_SPPLR_CODE", columnDefinition = "VARCHAR")
    private String splSupplierCode;

    @Column(name = "SPL_ACCNT_NMBR", columnDefinition = "VARCHAR")
    private String splAccountNumber;

    @Column(name = "SPL_NM", columnDefinition = "VARCHAR")
    private String splName;

    @Column(name = "SPL_ADDRSS", columnDefinition = "VARCHAR")
    private String splAddress;

    @Column(name = "SPL_CTY", columnDefinition = "VARCHAR")
    private String splCity;

    @Column(name = "SPL_STT_PRVNC", columnDefinition = "VARCHAR")
    private String splStateProvince;

    @Column(name = "SPL_PSTL_CD", columnDefinition = "VARCHAR")
    private String splPostalCode;

    @Column(name = "SPL_CNTRY", columnDefinition = "VARCHAR")
    private String splCountry;

    @Column(name = "SPL_CNTCT", columnDefinition = "VARCHAR")
    private String splContact;

    @Column(name = "SPL_PHN", columnDefinition = "VARCHAR")
    private String splPhone;

    @Column(name = "SPL_FX", columnDefinition = "VARCHAR")
    private String splFax;

    @Column(name = "SPL_ALTRNT_PHN", columnDefinition = "VARCHAR")
    private String splAlternatePhone;

    @Column(name = "SPL_ALTRNT_CNTCT", columnDefinition = "VARCHAR")
    private String splAlternateContact;

    @Column(name = "SPL_EML", columnDefinition = "VARCHAR")
    private String splEmail;

    @Column(name = "SPL_TIN", columnDefinition = "VARCHAR")
    private String splTin;

    @Column(name = "SPL_COA_GL_PYBL_ACCNT", columnDefinition = "INT")
    private Integer splCoaGlPayableAccount;

    @Column(name = "SPL_COA_GL_EXPNS_ACCNT", columnDefinition = "INT")
    private Integer splCoaGlExpenseAccount;

    @Column(name = "SPL_ENBL", columnDefinition = "TINYINT")
    private byte splEnable;

    @Column(name = "SPL_RMRKS", columnDefinition = "VARCHAR")
    private String splRemarks;

    @Column(name = "SPL_AD_CMPNY", columnDefinition = "INT")
    private Integer splAdCompany;

    @JoinColumn(name = "AD_BANK_ACCOUNT", referencedColumnName = "BA_CODE")
    @ManyToOne
    private LocalAdBankAccount adBankAccount;

    @JoinColumn(name = "AD_PAYMENT_TERM", referencedColumnName = "PYT_CODE")
    @ManyToOne
    private LocalAdPaymentTerm adPaymentTerm;

    @JoinColumn(name = "AP_SUPPLIER_CLASS", referencedColumnName = "SC_CODE")
    @ManyToOne
    private LocalApSupplierClass apSupplierClass;

    @JoinColumn(name = "AP_SUPPLIER_TYPE", referencedColumnName = "AP_ST_CODE")
    @ManyToOne
    private LocalApSupplierType apSupplierType;

    @JoinColumn(name = "INV_LINE_ITEM_TEMPLATE", referencedColumnName = "LIT_CODE")
    @ManyToOne
    private LocalInvLineItemTemplate invLineItemTemplate;

    @OneToMany(mappedBy = "apSupplier", fetch = FetchType.LAZY)
    private List<LocalArCustomer> arCustomers;

    @OneToMany(mappedBy = "apSupplier", fetch = FetchType.LAZY)
    private List<LocalGlInvestorAccountBalance> glInvestorAccountBalances;

    @OneToMany(mappedBy = "apSupplier", fetch = FetchType.LAZY)
    private List<LocalApVoucher> apVouchers;

    @OneToMany(mappedBy = "apSupplier", fetch = FetchType.LAZY)
    private List<LocalApCheck> apChecks;

    @OneToMany(mappedBy = "apSupplier", fetch = FetchType.LAZY)
    private List<LocalApRecurringVoucher> apRecurringVouchers;

    @OneToMany(mappedBy = "apSupplier", fetch = FetchType.LAZY)
    private List<LocalApSupplierBalance> apSupplierBalances;

    @OneToMany(mappedBy = "apSupplier", fetch = FetchType.LAZY)
    private List<LocalApPurchaseOrder> apPurchaseOrders;

    @OneToMany(mappedBy = "apSupplier", fetch = FetchType.LAZY)
    private List<LocalInvAdjustment> invAdjustments;

    @OneToMany(mappedBy = "apSupplier", fetch = FetchType.LAZY)
    private List<LocalAdBranchSupplier> adBranchSuppliers;

    @OneToMany(mappedBy = "apSupplier", fetch = FetchType.LAZY)
    private List<LocalApCanvass> apCanvasses;

    @OneToMany(mappedBy = "apSupplier", fetch = FetchType.LAZY)
    private List<LocalInvItem> invItems;

    public Integer getSplCode() {

        return splCode;
    }

    public void setSplCode(Integer SPL_CODE) {

        this.splCode = SPL_CODE;
    }

    public String getSplSupplierCode() {

        return splSupplierCode;
    }

    public void setSplSupplierCode(String SPL_SPPLR_CODE) {

        this.splSupplierCode = SPL_SPPLR_CODE;
    }

    public String getSplAccountNumber() {

        return splAccountNumber;
    }

    public void setSplAccountNumber(String SPL_ACCNT_NMBR) {

        this.splAccountNumber = SPL_ACCNT_NMBR;
    }

    public String getSplName() {

        return splName;
    }

    public void setSplName(String SPL_NM) {

        this.splName = SPL_NM;
    }

    public String getSplAddress() {

        return splAddress;
    }

    public void setSplAddress(String SPL_ADDRSS) {

        this.splAddress = SPL_ADDRSS;
    }

    public String getSplCity() {

        return splCity;
    }

    public void setSplCity(String SPL_CTY) {

        this.splCity = SPL_CTY;
    }

    public String getSplStateProvince() {

        return splStateProvince;
    }

    public void setSplStateProvince(String SPL_STT_PRVNC) {

        this.splStateProvince = SPL_STT_PRVNC;
    }

    public String getSplPostalCode() {

        return splPostalCode;
    }

    public void setSplPostalCode(String SPL_PSTL_CD) {

        this.splPostalCode = SPL_PSTL_CD;
    }

    public String getSplCountry() {

        return splCountry;
    }

    public void setSplCountry(String SPL_CNTRY) {

        this.splCountry = SPL_CNTRY;
    }

    public String getSplContact() {

        return splContact;
    }

    public void setSplContact(String SPL_CNTCT) {

        this.splContact = SPL_CNTCT;
    }

    public String getSplPhone() {

        return splPhone;
    }

    public void setSplPhone(String SPL_PHN) {

        this.splPhone = SPL_PHN;
    }

    public String getSplFax() {

        return splFax;
    }

    public void setSplFax(String SPL_FX) {

        this.splFax = SPL_FX;
    }

    public String getSplAlternatePhone() {

        return splAlternatePhone;
    }

    public void setSplAlternatePhone(String SPL_ALTRNT_PHN) {

        this.splAlternatePhone = SPL_ALTRNT_PHN;
    }

    public String getSplAlternateContact() {

        return splAlternateContact;
    }

    public void setSplAlternateContact(String SPL_ALTRNT_CNTCT) {

        this.splAlternateContact = SPL_ALTRNT_CNTCT;
    }

    public String getSplEmail() {

        return splEmail;
    }

    public void setSplEmail(String SPL_EML) {

        this.splEmail = SPL_EML;
    }

    public String getSplTin() {

        return splTin;
    }

    public void setSplTin(String SPL_TIN) {

        this.splTin = SPL_TIN;
    }

    public Integer getSplCoaGlPayableAccount() {

        return splCoaGlPayableAccount;
    }

    public void setSplCoaGlPayableAccount(Integer SPL_COA_GL_PYBL_ACCNT) {

        this.splCoaGlPayableAccount = SPL_COA_GL_PYBL_ACCNT;
    }

    public Integer getSplCoaGlExpenseAccount() {

        return splCoaGlExpenseAccount;
    }

    public void setSplCoaGlExpenseAccount(Integer SPL_COA_GL_EXPNS_ACCNT) {

        this.splCoaGlExpenseAccount = SPL_COA_GL_EXPNS_ACCNT;
    }

    public byte getSplEnable() {

        return splEnable;
    }

    public void setSplEnable(byte SPL_ENBL) {

        this.splEnable = SPL_ENBL;
    }

    public String getSplRemarks() {

        return splRemarks;
    }

    public void setSplRemarks(String SPL_RMRKS) {

        this.splRemarks = SPL_RMRKS;
    }

    public Integer getSplAdCompany() {

        return splAdCompany;
    }

    public void setSplAdCompany(Integer SPL_AD_CMPNY) {

        this.splAdCompany = SPL_AD_CMPNY;
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

    public LocalApSupplierClass getApSupplierClass() {

        return apSupplierClass;
    }

    public void setApSupplierClass(LocalApSupplierClass apSupplierClass) {

        this.apSupplierClass = apSupplierClass;
    }

    public LocalApSupplierType getApSupplierType() {

        return apSupplierType;
    }

    public void setApSupplierType(LocalApSupplierType apSupplierType) {

        this.apSupplierType = apSupplierType;
    }

    public LocalInvLineItemTemplate getInvLineItemTemplate() {

        return invLineItemTemplate;
    }

    public void setInvLineItemTemplate(LocalInvLineItemTemplate invLineItemTemplate) {

        this.invLineItemTemplate = invLineItemTemplate;
    }

    @XmlTransient
    public List getArCustomers() {

        return arCustomers;
    }

    public void setArCustomers(List arCustomers) {

        this.arCustomers = arCustomers;
    }

    @XmlTransient
    public List getGlInvestorAccountBalances() {

        return glInvestorAccountBalances;
    }

    public void setGlInvestorAccountBalances(List glInvestorAccountBalances) {

        this.glInvestorAccountBalances = glInvestorAccountBalances;
    }

    @XmlTransient
    public List getApVouchers() {

        return apVouchers;
    }

    public void setApVouchers(List apVouchers) {

        this.apVouchers = apVouchers;
    }

    @XmlTransient
    public List getApChecks() {

        return apChecks;
    }

    public void setApChecks(List apChecks) {

        this.apChecks = apChecks;
    }

    @XmlTransient
    public List getApRecurringVouchers() {

        return apRecurringVouchers;
    }

    public void setApRecurringVouchers(List apRecurringVouchers) {

        this.apRecurringVouchers = apRecurringVouchers;
    }

    @XmlTransient
    public List getApSupplierBalances() {

        return apSupplierBalances;
    }

    public void setApSupplierBalances(List apSupplierBalances) {

        this.apSupplierBalances = apSupplierBalances;
    }

    @XmlTransient
    public List getApPurchaseOrders() {

        return apPurchaseOrders;
    }

    public void setApPurchaseOrders(List apPurchaseOrders) {

        this.apPurchaseOrders = apPurchaseOrders;
    }

    @XmlTransient
    public List getInvAdjustments() {

        return invAdjustments;
    }

    public void setInvAdjustments(List invAdjustments) {

        this.invAdjustments = invAdjustments;
    }

    @XmlTransient
    public List getAdBranchSuppliers() {

        return adBranchSuppliers;
    }

    public void setAdBranchSuppliers(List adBranchSuppliers) {

        this.adBranchSuppliers = adBranchSuppliers;
    }

    @XmlTransient
    public List getApCanvasses() {

        return apCanvasses;
    }

    public void setApCanvasses(List apCanvasses) {

        this.apCanvasses = apCanvasses;
    }

    @XmlTransient
    public List getInvItems() {

        return invItems;
    }

    public void setInvItems(List invItems) {

        this.invItems = invItems;
    }

    public void addGlInvestorAccountBalance(LocalGlInvestorAccountBalance entity) {

        try {
            entity.setApSupplier(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlInvestorAccountBalance(LocalGlInvestorAccountBalance entity) {

        try {
            entity.setApSupplier(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApVoucher(LocalApVoucher entity) {

        try {
            entity.setApSupplier(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApVoucher(LocalApVoucher entity) {

        try {
            entity.setApSupplier(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApCheck(LocalApCheck entity) {

        try {
            entity.setApSupplier(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApCheck(LocalApCheck entity) {

        try {
            entity.setApSupplier(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApRecurringVoucher(LocalApRecurringVoucher entity) {

        try {
            entity.setApSupplier(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApRecurringVoucher(LocalApRecurringVoucher entity) {

        try {
            entity.setApSupplier(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApSupplierBalance(LocalApSupplierBalance entity) {

        try {
            entity.setApSupplier(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApSupplierBalance(LocalApSupplierBalance entity) {

        try {
            entity.setApSupplier(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApPurchaseOrder(LocalApPurchaseOrder entity) {

        try {
            entity.setApSupplier(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApPurchaseOrder(LocalApPurchaseOrder entity) {

        try {
            entity.setApSupplier(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdBranchSupplier(LocalAdBranchSupplier entity) {

        try {
            entity.setApSupplier(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBranchSupplier(LocalAdBranchSupplier entity) {

        try {
            entity.setApSupplier(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApCanvass(LocalApCanvass entity) {

        try {
            entity.setApSupplier(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApCanvass(LocalApCanvass entity) {

        try {
            entity.setApSupplier(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addInvItem(LocalInvItem entity) {

        try {
            entity.setApSupplier(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvItem(LocalInvItem entity) {

        try {
            entity.setApSupplier(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArCustomer(LocalArCustomer entity) {

        try {
            entity.setApSupplier(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArCustomer(LocalArCustomer entity) {

        try {
            entity.setApSupplier(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
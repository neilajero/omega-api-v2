package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ad.LocalAdBankAccount;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ap.*;
import com.ejb.entities.ar.*;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "GlFunctionalCurrency")
@Table(name = "GL_FNCTNL_CRRNCY")
public class LocalGlFunctionalCurrency extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FC_CODE", nullable = false)
    private Integer fcCode;

    @Column(name = "FC_NM", columnDefinition = "VARCHAR")
    private String fcName;

    @Column(name = "FC_DESC", columnDefinition = "VARCHAR")
    private String fcDescription;

    @Column(name = "FC_CNTRY", columnDefinition = "VARCHAR")
    private String fcCountry;

    @Column(name = "FC_SYMBL", columnDefinition = "VARCHAR")
    private char fcSymbol;

    @Column(name = "FC_PRCSN", columnDefinition = "SMALLINT")
    private short fcPrecision;

    @Column(name = "FC_EXTNDD_PRCSN", columnDefinition = "SMALLINT")
    private short fcExtendedPrecision;

    @Column(name = "FC_MIN_ACCNT_UNT", columnDefinition = "DOUBLE")
    private double fcMinimumAccountUnit = 0;

    @Column(name = "FC_DT_FRM", columnDefinition = "DATETIME")
    private Date fcDateFrom;

    @Column(name = "FC_DT_TO", columnDefinition = "DATETIME")
    private Date fcDateTo;

    @Column(name = "FC_ENBL", columnDefinition = "TINYINT")
    private byte fcEnable;

    @Column(name = "FC_AD_CMPNY", columnDefinition = "INT")
    private Integer fcAdCompany;

    @OneToMany(mappedBy = "glFunctionalCurrency", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalGlFunctionalCurrencyRate> glFunctionalCurrencyRates;

    @OneToMany(mappedBy = "glFunctionalCurrency", fetch = FetchType.LAZY)
    private List<LocalGlJournal> glJournals;

    @OneToMany(mappedBy = "glFunctionalCurrency", fetch = FetchType.LAZY)
    private List<LocalAdBankAccount> adBankAccounts;

    @OneToMany(mappedBy = "glFunctionalCurrency", fetch = FetchType.LAZY)
    private List<LocalAdCompany> adCompanies;

    @OneToMany(mappedBy = "glFunctionalCurrency", fetch = FetchType.LAZY)
    private List<LocalApRecurringVoucher> apRecurringVouchers;

    @OneToMany(mappedBy = "glFunctionalCurrency", fetch = FetchType.LAZY)
    private List<LocalApVoucher> apVouchers;

    @OneToMany(mappedBy = "glFunctionalCurrency", fetch = FetchType.LAZY)
    private List<LocalApCheck> apChecks;

    @OneToMany(mappedBy = "glFunctionalCurrency", fetch = FetchType.LAZY)
    private List<LocalArInvoice> arInvoices;

    @OneToMany(mappedBy = "glFunctionalCurrency", fetch = FetchType.LAZY)
    private List<LocalArReceipt> arReceipts;

    @OneToMany(mappedBy = "glFunctionalCurrency", fetch = FetchType.LAZY)
    private List<LocalApPurchaseOrder> apPurchaseOrders;

    @OneToMany(mappedBy = "glFunctionalCurrency", fetch = FetchType.LAZY)
    private List<LocalArPdc> arPdcs;

    @OneToMany(mappedBy = "glFunctionalCurrency", fetch = FetchType.LAZY)
    private List<LocalArSalesOrder> arSalesOrders;

    @OneToMany(mappedBy = "glFunctionalCurrency", fetch = FetchType.LAZY)
    private List<LocalApPurchaseRequisition> apPurchaseRequisitions;

    @OneToMany(mappedBy = "glFunctionalCurrency", fetch = FetchType.LAZY)
    private List<LocalArJobOrder> arJobOrders;

    @OneToMany(mappedBy = "glFunctionalCurrency", fetch = FetchType.LAZY)
    private List<LocalGlChartOfAccount> glChartOfAccounts;

    @OneToMany(mappedBy = "glFunctionalCurrency", fetch = FetchType.LAZY)
    private List<LocalGlFrgColumn> glFrgColumns;

    public Integer getFcCode() {

        return fcCode;
    }

    public void setFcCode(Integer FC_CODE) {

        this.fcCode = FC_CODE;
    }

    public String getFcName() {

        return fcName;
    }

    public void setFcName(String FC_NM) {

        this.fcName = FC_NM;
    }

    public String getFcDescription() {

        return fcDescription;
    }

    public void setFcDescription(String FC_DESC) {

        this.fcDescription = FC_DESC;
    }

    public String getFcCountry() {

        return fcCountry;
    }

    public void setFcCountry(String FC_CNTRY) {

        this.fcCountry = FC_CNTRY;
    }

    public char getFcSymbol() {

        return fcSymbol;
    }

    public void setFcSymbol(char FC_SYMBL) {

        this.fcSymbol = FC_SYMBL;
    }

    public short getFcPrecision() {

        return fcPrecision;
    }

    public void setFcPrecision(short FC_PRCSN) {

        this.fcPrecision = FC_PRCSN;
    }

    public short getFcExtendedPrecision() {

        return fcExtendedPrecision;
    }

    public void setFcExtendedPrecision(short FC_EXTNDD_PRCSN) {

        this.fcExtendedPrecision = FC_EXTNDD_PRCSN;
    }

    public double getFcMinimumAccountUnit() {

        return fcMinimumAccountUnit;
    }

    public void setFcMinimumAccountUnit(double FC_MIN_ACCNT_UNT) {

        this.fcMinimumAccountUnit = FC_MIN_ACCNT_UNT;
    }

    public Date getFcDateFrom() {

        return fcDateFrom;
    }

    public void setFcDateFrom(Date FC_DT_FRM) {

        this.fcDateFrom = FC_DT_FRM;
    }

    public Date getFcDateTo() {

        return fcDateTo;
    }

    public void setFcDateTo(Date FC_DT_TO) {

        this.fcDateTo = FC_DT_TO;
    }

    public byte getFcEnable() {

        return fcEnable;
    }

    public void setFcEnable(byte FC_ENBL) {

        this.fcEnable = FC_ENBL;
    }

    public Integer getFcAdCompany() {

        return fcAdCompany;
    }

    public void setFcAdCompany(Integer FC_AD_CMPNY) {

        this.fcAdCompany = FC_AD_CMPNY;
    }

    @XmlTransient
    public List getGlFunctionalCurrencyRates() {

        return glFunctionalCurrencyRates;
    }

    public void setGlFunctionalCurrencyRates(List glFunctionalCurrencyRates) {

        this.glFunctionalCurrencyRates = glFunctionalCurrencyRates;
    }

    @XmlTransient
    public List getGlJournals() {

        return glJournals;
    }

    public void setGlJournals(List glJournals) {

        this.glJournals = glJournals;
    }

    @XmlTransient
    public List getAdBankAccounts() {

        return adBankAccounts;
    }

    public void setAdBankAccounts(List adBankAccounts) {

        this.adBankAccounts = adBankAccounts;
    }

    @XmlTransient
    public List getAdCompanies() {

        return adCompanies;
    }

    public void setAdCompanies(List adCompanies) {

        this.adCompanies = adCompanies;
    }

    @XmlTransient
    public List getApRecurringVouchers() {

        return apRecurringVouchers;
    }

    public void setApRecurringVouchers(List apRecurringVouchers) {

        this.apRecurringVouchers = apRecurringVouchers;
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
    public List getApPurchaseOrders() {

        return apPurchaseOrders;
    }

    public void setApPurchaseOrders(List apPurchaseOrders) {

        this.apPurchaseOrders = apPurchaseOrders;
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
    public List getApPurchaseRequisitions() {

        return apPurchaseRequisitions;
    }

    public void setApPurchaseRequisitions(List apPurchaseRequisitions) {

        this.apPurchaseRequisitions = apPurchaseRequisitions;
    }

    @XmlTransient
    public List getArJobOrders() {

        return arJobOrders;
    }

    public void setArJobOrders(List arJobOrders) {

        this.arJobOrders = arJobOrders;
    }

    @XmlTransient
    public List getGlChartOfAccounts() {

        return glChartOfAccounts;
    }

    public void setGlChartOfAccounts(List glChartOfAccounts) {

        this.glChartOfAccounts = glChartOfAccounts;
    }

    @XmlTransient
    public List getGlFrgColumns() {

        return glFrgColumns;
    }

    public void setGlFrgColumns(List glFrgColumns) {

        this.glFrgColumns = glFrgColumns;
    }

    public void addGlFunctionalCurrencyRate(LocalGlFunctionalCurrencyRate entity) {

        try {
            entity.setGlFunctionalCurrency(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlFunctionalCurrencyRate(LocalGlFunctionalCurrencyRate entity) {

        try {
            entity.setGlFunctionalCurrency(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addGlJournal(LocalGlJournal entity) {

        try {
            entity.setGlFunctionalCurrency(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlJournal(LocalGlJournal entity) {

        try {
            entity.setGlFunctionalCurrency(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdBankAccount(LocalAdBankAccount entity) {

        try {
            entity.setGlFunctionalCurrency(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBankAccount(LocalAdBankAccount entity) {

        try {
            entity.setGlFunctionalCurrency(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdCompany(LocalAdCompany entity) {

        try {
            entity.setGlFunctionalCurrency(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdCompany(LocalAdCompany entity) {

        try {
            entity.setGlFunctionalCurrency(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApCheck(LocalApCheck entity) {

        try {
            entity.setGlFunctionalCurrency(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApCheck(LocalApCheck entity) {

        try {
            entity.setGlFunctionalCurrency(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApRecurringVoucher(LocalApRecurringVoucher entity) {

        try {
            entity.setGlFunctionalCurrency(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApRecurringVoucher(LocalApRecurringVoucher entity) {

        try {
            entity.setGlFunctionalCurrency(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApVoucher(LocalApVoucher entity) {

        try {
            entity.setGlFunctionalCurrency(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApVoucher(LocalApVoucher entity) {

        try {
            entity.setGlFunctionalCurrency(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArInvoice(LocalArInvoice entity) {

        try {
            entity.setGlFunctionalCurrency(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArInvoice(LocalArInvoice entity) {

        try {
            entity.setGlFunctionalCurrency(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArReceipt(LocalArReceipt entity) {

        try {
            entity.setGlFunctionalCurrency(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArReceipt(LocalArReceipt entity) {

        try {
            entity.setGlFunctionalCurrency(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApPurchaseOrder(LocalApPurchaseOrder entity) {

        try {
            entity.setGlFunctionalCurrency(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApPurchaseOrder(LocalApPurchaseOrder entity) {

        try {
            entity.setGlFunctionalCurrency(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArPdc(LocalArPdc entity) {

        try {
            entity.setGlFunctionalCurrency(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArPdc(LocalArPdc entity) {

        try {
            entity.setGlFunctionalCurrency(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArSalesOrder(LocalArSalesOrder entity) {

        try {
            entity.setGlFunctionalCurrency(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArSalesOrder(LocalArSalesOrder entity) {

        try {
            entity.setGlFunctionalCurrency(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApPurchaseRequisition(LocalApPurchaseRequisition entity) {

        try {
            entity.setGlFunctionalCurrency(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApPurchaseRequisition(LocalApPurchaseRequisition entity) {

        try {
            entity.setGlFunctionalCurrency(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addGlChartOfAccount(LocalGlChartOfAccount entity) {

        try {
            entity.setGlFunctionalCurrency(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlChartOfAccount(LocalGlChartOfAccount entity) {

        try {
            entity.setGlFunctionalCurrency(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addGlFrgColumn(LocalGlFrgColumn entity) {

        try {
            entity.setGlFunctionalCurrency(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlFrgColumn(LocalGlFrgColumn entity) {

        try {
            entity.setGlFunctionalCurrency(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
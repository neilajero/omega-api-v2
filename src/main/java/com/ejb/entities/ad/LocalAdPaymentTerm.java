package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ap.*;
import com.ejb.entities.ar.*;
import com.ejb.entities.gl.LocalGlChartOfAccount;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "AdPaymentTerm")
@Table(name = "AD_PYMNT_TRM")
public class LocalAdPaymentTerm extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PYT_CODE", nullable = false)
    private Integer pytCode;

    @Column(name = "PYT_NM", columnDefinition = "VARCHAR")
    private String pytName;

    @Column(name = "PYT_DESC", columnDefinition = "VARCHAR")
    private String pytDescription;

    @Column(name = "PYT_BS_AMNT", columnDefinition = "DOUBLE")
    private double pytBaseAmount = 0;

    @Column(name = "PYT_MNTHLY_INT_RT", columnDefinition = "DOUBLE")
    private double pytMonthlyInterestRate = 0;

    @Column(name = "PYT_ENBL", columnDefinition = "TINYINT")
    private byte pytEnable;

    @Column(name = "PYT_ENBL_RBT", columnDefinition = "TINYINT")
    private byte pytEnableRebate;

    @Column(name = "PYT_ENBL_INT", columnDefinition = "TINYINT")
    private byte pytEnableInterest;

    @Column(name = "PYT_DSCNT_ON_INVC", columnDefinition = "TINYINT")
    private byte pytDiscountOnInvoice;

    @Column(name = "PYT_DSCNT_DESC", columnDefinition = "VARCHAR")
    private String pytDiscountDescription;

    @Column(name = "PYT_SCHDL_BSS", columnDefinition = "VARCHAR")
    private String pytScheduleBasis;

    @Column(name = "PYT_AD_CMPNY", columnDefinition = "INT")
    private Integer pytAdCompany;

    @JoinColumn(name = "GL_CHART_OF_ACCOUNT", referencedColumnName = "COA_CODE")
    @ManyToOne
    private LocalGlChartOfAccount glChartOfAccount;

    @OneToMany(mappedBy = "adPaymentTerm", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalAdPaymentSchedule> adPaymentSchedules;

    @OneToMany(mappedBy = "adPaymentTerm", fetch = FetchType.LAZY)
    private List<LocalApSupplier> apSuppliers;
    @OneToMany(mappedBy = "adPaymentTerm", fetch = FetchType.LAZY)
    private List<LocalApVoucher> apVouchers;
    @OneToMany(mappedBy = "adPaymentTerm2", fetch = FetchType.LAZY)
    private List<LocalApVoucher> apVouchers2;
    @OneToMany(mappedBy = "adPaymentTerm", fetch = FetchType.LAZY)
    private List<LocalApCheck> apChecks;
    @OneToMany(mappedBy = "adPaymentTerm", fetch = FetchType.LAZY)
    private List<LocalApRecurringVoucher> apRecurringVouchers;

    @OneToMany(mappedBy = "adPaymentTerm", fetch = FetchType.LAZY)
    private List<LocalArCustomer> arCustomers;
    @OneToMany(mappedBy = "adPaymentTerm", fetch = FetchType.LAZY)
    private List<LocalArInvoice> arInvoices;
    @OneToMany(mappedBy = "adPaymentTerm", fetch = FetchType.LAZY)
    private List<LocalApPurchaseOrder> apPurchaseOrders;

    @OneToMany(mappedBy = "adPaymentTerm", fetch = FetchType.LAZY)
    private List<LocalArPdc> arPdcs;
    @OneToMany(mappedBy = "adPaymentTerm", fetch = FetchType.LAZY)
    private List<LocalArSalesOrder> arSalesOrders;

    @OneToMany(mappedBy = "adPaymentTerm", fetch = FetchType.LAZY)
    private List<LocalArJobOrder> arJobOrders;
    public Integer getPytCode() {

        return pytCode;
    }

    public void setPytCode(Integer PYT_CODE) {

        this.pytCode = PYT_CODE;
    }

    public String getPytName() {

        return pytName;
    }

    public void setPytName(String PYT_NM) {

        this.pytName = PYT_NM;
    }

    public String getPytDescription() {

        return pytDescription;
    }

    public void setPytDescription(String PYT_DESC) {

        this.pytDescription = PYT_DESC;
    }

    public double getPytBaseAmount() {

        return pytBaseAmount;
    }

    public void setPytBaseAmount(double PYT_BS_AMNT) {

        this.pytBaseAmount = PYT_BS_AMNT;
    }

    public double getPytMonthlyInterestRate() {

        return pytMonthlyInterestRate;
    }

    public void setPytMonthlyInterestRate(double PYT_MNTHLY_INT_RT) {

        this.pytMonthlyInterestRate = PYT_MNTHLY_INT_RT;
    }

    public byte getPytEnable() {

        return pytEnable;
    }

    public void setPytEnable(byte PYT_ENBL) {

        this.pytEnable = PYT_ENBL;
    }

    public byte getPytEnableRebate() {

        return pytEnableRebate;
    }

    public void setPytEnableRebate(byte PYT_ENBL_RBT) {

        this.pytEnableRebate = PYT_ENBL_RBT;
    }

    public byte getPytEnableInterest() {

        return pytEnableInterest;
    }

    public void setPytEnableInterest(byte PYT_ENBL_INT) {

        this.pytEnableInterest = PYT_ENBL_INT;
    }

    public byte getPytDiscountOnInvoice() {

        return pytDiscountOnInvoice;
    }

    public void setPytDiscountOnInvoice(byte PYT_DSCNT_ON_INVC) {

        this.pytDiscountOnInvoice = PYT_DSCNT_ON_INVC;
    }

    public String getPytDiscountDescription() {

        return pytDiscountDescription;
    }

    public void setPytDiscountDescription(String PYT_DSCNT_DESC) {

        this.pytDiscountDescription = PYT_DSCNT_DESC;
    }

    public String getPytScheduleBasis() {

        return pytScheduleBasis;
    }

    public void setPytScheduleBasis(String PYT_SCHDL_BSS) {

        this.pytScheduleBasis = PYT_SCHDL_BSS;
    }

    public Integer getPytAdCompany() {

        return pytAdCompany;
    }

    public void setPytAdCompany(Integer PYT_AD_CMPNY) {

        this.pytAdCompany = PYT_AD_CMPNY;
    }

    public LocalGlChartOfAccount getGlChartOfAccount() {

        return glChartOfAccount;
    }

    public void setGlChartOfAccount(LocalGlChartOfAccount glChartOfAccount) {

        this.glChartOfAccount = glChartOfAccount;
    }

    @XmlTransient
    public List getAdPaymentSchedules() {

        return adPaymentSchedules;
    }

    public void setAdPaymentSchedules(List adPaymentSchedules) {

        this.adPaymentSchedules = adPaymentSchedules;
    }

    @XmlTransient
    public List getApSuppliers() {

        return apSuppliers;
    }

    public void setApSuppliers(List apSuppliers) {

        this.apSuppliers = apSuppliers;
    }

    @XmlTransient
    public List getApVouchers() {

        return apVouchers;
    }

    public void setApVouchers(List apVouchers) {

        this.apVouchers = apVouchers;
    }

    @XmlTransient
    public List getApVouchers2() {

        return apVouchers2;
    }

    public void setApVouchers2(List apVouchers2) {

        this.apVouchers2 = apVouchers2;
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
    public List getArCustomers() {

        return arCustomers;
    }

    public void setArCustomers(List arCustomers) {

        this.arCustomers = arCustomers;
    }

    @XmlTransient
    public List getArInvoices() {

        return arInvoices;
    }

    public void setArInvoices(List arInvoices) {

        this.arInvoices = arInvoices;
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
    public List getArJobOrders() {

        return arJobOrders;
    }

    public void setArJobOrders(List arJobOrders) {

        this.arJobOrders = arJobOrders;
    }

    public void addAdPaymentSchedule(LocalAdPaymentSchedule entity) {

        try {
            entity.setAdPaymentTerm(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdPaymentSchedule(LocalAdPaymentSchedule entity) {

        try {
            entity.setAdPaymentTerm(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApSupplier(LocalApSupplier entity) {

        try {
            entity.setAdPaymentTerm(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApSupplier(LocalApSupplier entity) {

        try {
            entity.setAdPaymentTerm(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApVoucher(LocalApVoucher entity) {

        try {
            entity.setAdPaymentTerm(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApVoucher(LocalApVoucher entity) {

        try {
            entity.setAdPaymentTerm(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApCheck(LocalApCheck entity) {

        try {
            entity.setAdPaymentTerm(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApCheck(LocalApCheck entity) {

        try {
            entity.setAdPaymentTerm(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApRecurringVoucher(LocalApRecurringVoucher entity) {

        try {
            entity.setAdPaymentTerm(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApRecurringVoucher(LocalApRecurringVoucher entity) {

        try {
            entity.setAdPaymentTerm(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArCustomer(LocalArCustomer entity) {

        try {
            entity.setAdPaymentTerm(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArCustomer(LocalArCustomer entity) {

        try {
            entity.setAdPaymentTerm(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArInvoice(LocalArInvoice entity) {

        try {
            entity.setAdPaymentTerm(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArInvoice(LocalArInvoice entity) {

        try {
            entity.setAdPaymentTerm(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApPurchaseOrder(LocalApPurchaseOrder entity) {

        try {
            entity.setAdPaymentTerm(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApPurchaseOrder(LocalApPurchaseOrder entity) {

        try {
            entity.setAdPaymentTerm(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArPdc(LocalArPdc entity) {

        try {
            entity.setAdPaymentTerm(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArPdc(LocalArPdc entity) {

        try {
            entity.setAdPaymentTerm(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArSalesOrder(LocalArSalesOrder entity) {

        try {
            entity.setAdPaymentTerm(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArSalesOrder(LocalArSalesOrder entity) {

        try {
            entity.setAdPaymentTerm(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ad.LocalAdBranchSalesperson;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "ArSalesperson")
@Table(name = "AR_SLSPRSN")
public class LocalArSalesperson extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SLP_CODE", nullable = false)
    private Integer slpCode;

    @Column(name = "SLP_SLSPRSN_CODE", columnDefinition = "VARCHAR")
    private String slpSalespersonCode;

    @Column(name = "SLP_NM", columnDefinition = "VARCHAR")
    private String slpName;

    @Column(name = "SLP_ENTRY_DT", columnDefinition = "DATETIME")
    private Date slpEntryDate;

    @Column(name = "SLP_ADDRSS", columnDefinition = "VARCHAR")
    private String slpAddress;

    @Column(name = "SLP_PHN", columnDefinition = "VARCHAR")
    private String slpPhone;

    @Column(name = "SLP_MBL_PHN", columnDefinition = "VARCHAR")
    private String slpMobilePhone;

    @Column(name = "SLP_EML", columnDefinition = "VARCHAR")
    private String slpEmail;

    @Column(name = "SLP_AD_CMPNY", columnDefinition = "INT")
    private Integer slpAdCompany;

    @OneToMany(mappedBy = "arSalesperson", fetch = FetchType.LAZY)
    private List<LocalAdBranchSalesperson> adBranchSalespersons;

    @OneToMany(mappedBy = "arSalesperson", fetch = FetchType.LAZY)
    private List<LocalArInvoice> arInvoices;

    @OneToMany(mappedBy = "arSalesperson", fetch = FetchType.LAZY)
    private List<LocalArReceipt> arReceipts;

    @OneToMany(mappedBy = "arSalesperson", fetch = FetchType.LAZY)
    private List<LocalArCustomer> arCustomers;

    @OneToMany(mappedBy = "arSalesperson", fetch = FetchType.LAZY)
    private List<LocalArJobOrder> arJobOrders;

    @OneToMany(mappedBy = "arSalesperson", fetch = FetchType.LAZY)
    private List<LocalArSalesOrder> arSalesOrders;

    public Integer getSlpCode() {

        return slpCode;
    }

    public void setSlpCode(Integer SLP_CODE) {

        this.slpCode = SLP_CODE;
    }

    public String getSlpSalespersonCode() {

        return slpSalespersonCode;
    }

    public void setSlpSalespersonCode(String SLP_SLSPRSN_CODE) {

        this.slpSalespersonCode = SLP_SLSPRSN_CODE;
    }

    public String getSlpName() {

        return slpName;
    }

    public void setSlpName(String SLP_NM) {

        this.slpName = SLP_NM;
    }

    public Date getSlpEntryDate() {

        return slpEntryDate;
    }

    public void setSlpEntryDate(Date SLP_ENTRY_DT) {

        this.slpEntryDate = SLP_ENTRY_DT;
    }

    public String getSlpAddress() {

        return slpAddress;
    }

    public void setSlpAddress(String SLP_ADDRSS) {

        this.slpAddress = SLP_ADDRSS;
    }

    public String getSlpPhone() {

        return slpPhone;
    }

    public void setSlpPhone(String SLP_PHN) {

        this.slpPhone = SLP_PHN;
    }

    public String getSlpMobilePhone() {

        return slpMobilePhone;
    }

    public void setSlpMobilePhone(String SLP_MBL_PHN) {

        this.slpMobilePhone = SLP_MBL_PHN;
    }

    public String getSlpEmail() {

        return slpEmail;
    }

    public void setSlpEmail(String SLP_EML) {

        this.slpEmail = SLP_EML;
    }

    public Integer getSlpAdCompany() {

        return slpAdCompany;
    }

    public void setSlpAdCompany(Integer SLP_AD_CMPNY) {

        this.slpAdCompany = SLP_AD_CMPNY;
    }

    @XmlTransient
    public List getAdBranchSalespersons() {

        return adBranchSalespersons;
    }

    public void setAdBranchSalespersons(List adBranchSalespersons) {

        this.adBranchSalespersons = adBranchSalespersons;
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
    public List getArCustomers() {

        return arCustomers;
    }

    public void setArCustomers(List arCustomers) {

        this.arCustomers = arCustomers;
    }

    @XmlTransient
    public List getArJobOrders() {

        return arJobOrders;
    }

    public void setArJobOrders(List arJobOrders) {

        this.arJobOrders = arJobOrders;
    }

    @XmlTransient
    public List getArSalesOrders() {

        return arSalesOrders;
    }

    public void setArSalesOrders(List arSalesOrders) {

        this.arSalesOrders = arSalesOrders;
    }

    public void addAdBranchSalesperson(LocalAdBranchSalesperson entity) {

        try {
            entity.setArSalesperson(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBranchSalesperson(LocalAdBranchSalesperson entity) {

        try {
            entity.setArSalesperson(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArInvoice(LocalArInvoice entity) {

        try {
            entity.setArSalesperson(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArInvoice(LocalArInvoice entity) {

        try {
            entity.setArSalesperson(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArReceipt(LocalArReceipt entity) {

        try {
            entity.setArSalesperson(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArReceipt(LocalArReceipt entity) {

        try {
            entity.setArSalesperson(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArCustomer(LocalArCustomer entity) {

        try {
            entity.setArSalesperson(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArCustomer(LocalArCustomer entity) {

        try {
            entity.setArSalesperson(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArSalesOrder(LocalArSalesOrder entity) {

        try {
            entity.setArSalesperson(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArSalesOrder(LocalArSalesOrder entity) {

        try {
            entity.setArSalesperson(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
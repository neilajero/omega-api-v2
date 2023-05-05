package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ar.LocalArCustomer;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;

@Entity(name = "InvLineItemTemplate")
@Table(name = "INV_LN_ITM_TMPLT")
public class LocalInvLineItemTemplate extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LIT_CODE", nullable = false)
    private Integer litCode;

    @Column(name = "LIT_NM", columnDefinition = "VARCHAR")
    private String litName;

    @Column(name = "LIT_DESC", columnDefinition = "VARCHAR")
    private String litDescription;

    @Column(name = "LIT_AD_CMPNY", columnDefinition = "INT")
    private Integer litAdCompany;

    @OneToMany(mappedBy = "invLineItemTemplate", fetch = FetchType.LAZY)
    private List<LocalArCustomer> arCustomers;

    @OneToMany(mappedBy = "invLineItemTemplate", fetch = FetchType.LAZY)
    private List<LocalApSupplier> apSuppliers;

    @OneToMany(mappedBy = "invLineItemTemplate", fetch = FetchType.LAZY)
    private List<LocalInvLineItem> invLineItems;

    public Integer getLitCode() {

        return litCode;
    }

    public void setLitCode(Integer LIT_CODE) {

        this.litCode = LIT_CODE;
    }

    public String getLitName() {

        return litName;
    }

    public void setLitName(String LIT_NM) {

        this.litName = LIT_NM;
    }

    public String getLitDescription() {

        return litDescription;
    }

    public void setLitDescription(String LIT_DESC) {

        this.litDescription = LIT_DESC;
    }

    public Integer getLitAdCompany() {

        return litAdCompany;
    }

    public void setLitAdCompany(Integer LIT_AD_CMPNY) {

        this.litAdCompany = LIT_AD_CMPNY;
    }

    @XmlTransient
    public List getArCustomers() {

        return arCustomers;
    }

    public void setArCustomers(List arCustomers) {

        this.arCustomers = arCustomers;
    }

    @XmlTransient
    public List getApSuppliers() {

        return apSuppliers;
    }

    public void setApSuppliers(List apSuppliers) {

        this.apSuppliers = apSuppliers;
    }

    @XmlTransient
    public List getInvLineItems() {

        return invLineItems;
    }

    public void setInvLineItems(List invLineItems) {

        this.invLineItems = invLineItems;
    }

    public void addArCustomer(LocalArCustomer entity) {

        try {
            entity.setInvLineItemTemplate(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArCustomer(LocalArCustomer entity) {

        try {
            entity.setInvLineItemTemplate(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApSupplier(LocalApSupplier entity) {

        try {
            entity.setInvLineItemTemplate(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApSupplier(LocalApSupplier entity) {

        try {
            entity.setInvLineItemTemplate(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addInvLineItem(LocalInvLineItem entity) {

        try {
            entity.setInvLineItemTemplate(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvLineItem(LocalInvLineItem entity) {

        try {
            entity.setInvLineItemTemplate(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
package com.ejb.restfulapi.inv.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.Collection;

@Entity(name = "InvUnitOfMeasureModel")
@Table(name = "INV_UNT_OF_MSR")
public class LocalInvUnitOfMeasureModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "UOM_CODE", nullable = false)
    @JsonProperty("id")
    private Integer uomCode;

    @Column(name = "UOM_NM")
    @JsonProperty("name")
    private java.lang.String uomName;

    @Column(name = "UOM_DESC")
    @JsonProperty("description")
    @JsonIgnore
    private java.lang.String uomDescription;

    @Column(name = "UOM_AD_CMPNY")
    @JsonProperty("companyId")
    @JsonIgnore
    private java.lang.Integer uomAdCompany;

    @OneToMany(mappedBy = "invUnitOfMeasure", fetch = FetchType.LAZY)
    private Collection<LocalInvItemModel> invItems = new java.util.ArrayList<>();

    public Integer getUomCode() {

        return uomCode;
    }

    public void setUomCode(Integer uomCode) {

        this.uomCode = uomCode;
    }

    public java.lang.String getUomName() {

        return uomName;
    }

    public void setUomName(java.lang.String uomName) {

        this.uomName = uomName;
    }

    public java.lang.String getUomDescription() {

        return uomDescription;
    }

    public void setUomDescription(java.lang.String uomDescription) {

        this.uomDescription = uomDescription;
    }

    public java.lang.Integer getUomAdCompany() {

        return uomAdCompany;
    }

    public void setUomAdCompany(java.lang.Integer uomAdCompany) {

        this.uomAdCompany = uomAdCompany;
    }

    @XmlTransient
    public Collection<LocalInvItemModel> getInvItems() {

        return invItems;
    }

    public void setInvItems(Collection<LocalInvItemModel> invItems) {

        this.invItems = invItems;
    }

}
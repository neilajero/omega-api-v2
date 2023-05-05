package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.*;

@Entity(name = "InvPhysicalInventory")
@Table(name = "INV_PHYSCL_INVNTRY")
public class LocalInvPhysicalInventory extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PI_CODE", nullable = false)
    private Integer piCode;

    @Column(name = "PI_DT", columnDefinition = "DATETIME")
    private Date piDate;

    @Column(name = "PI_RFRNC_NMBR", columnDefinition = "VARCHAR")
    private String piReferenceNumber;

    @Column(name = "PI_DESC", columnDefinition = "VARCHAR")
    private String piDescription;

    @Column(name = "PI_CRTD_BY", columnDefinition = "VARCHAR")
    private String piCreatedBy;

    @Column(name = "PI_DT_CRTD", columnDefinition = "DATETIME")
    private Date piDateCreated;

    @Column(name = "PI_LST_MDFD_BY", columnDefinition = "VARCHAR")
    private String piLastModifiedBy;

    @Column(name = "PI_DT_LST_MDFD", columnDefinition = "DATETIME")
    private Date piDateLastModified;

    @Column(name = "PI_AD_LV_CTGRY", columnDefinition = "VARCHAR")
    private String piAdLvCategory;

    @Column(name = "PI_VRNC_ADJSTD", columnDefinition = "TINYINT")
    private byte piVarianceAdjusted;

    @Column(name = "PI_WSTG_ADJSTD", columnDefinition = "TINYINT")
    private byte piWastageAdjusted;

    @Column(name = "PI_AD_BRNCH", columnDefinition = "INT")
    private Integer piAdBranch;

    @Column(name = "PI_AD_CMPNY", columnDefinition = "INT")
    private Integer piAdCompany;

    @JoinColumn(name = "INV_LOCATION", referencedColumnName = "LOC_CODE")
    @ManyToOne
    private LocalInvLocation invLocation;

    @OneToMany(mappedBy = "invPhysicalInventory", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalInvPhysicalInventoryLine> invPhysicalInventoryLines;

    public Integer getPiCode() {

        return piCode;
    }

    public void setPiCode(Integer PI_CODE) {

        this.piCode = PI_CODE;
    }

    public Date getPiDate() {

        return piDate;
    }

    public void setPiDate(Date PI_DT) {

        this.piDate = PI_DT;
    }

    public String getPiReferenceNumber() {

        return piReferenceNumber;
    }

    public void setPiReferenceNumber(String PI_RFRNC_NMBR) {

        this.piReferenceNumber = PI_RFRNC_NMBR;
    }

    public String getPiDescription() {

        return piDescription;
    }

    public void setPiDescription(String PI_DESC) {

        this.piDescription = PI_DESC;
    }

    public String getPiCreatedBy() {

        return piCreatedBy;
    }

    public void setPiCreatedBy(String PI_CRTD_BY) {

        this.piCreatedBy = PI_CRTD_BY;
    }

    public Date getPiDateCreated() {

        return piDateCreated;
    }

    public void setPiDateCreated(Date PI_DT_CRTD) {

        this.piDateCreated = PI_DT_CRTD;
    }

    public String getPiLastModifiedBy() {

        return piLastModifiedBy;
    }

    public void setPiLastModifiedBy(String PI_LST_MDFD_BY) {

        this.piLastModifiedBy = PI_LST_MDFD_BY;
    }

    public Date getPiDateLastModified() {

        return piDateLastModified;
    }

    public void setPiDateLastModified(Date PI_DT_LST_MDFD) {

        this.piDateLastModified = PI_DT_LST_MDFD;
    }

    public String getPiAdLvCategory() {

        return piAdLvCategory;
    }

    public void setPiAdLvCategory(String PI_AD_LV_CTGRY) {

        this.piAdLvCategory = PI_AD_LV_CTGRY;
    }

    public byte getPiVarianceAdjusted() {

        return piVarianceAdjusted;
    }

    public void setPiVarianceAdjusted(byte PI_VRNC_ADJSTD) {

        this.piVarianceAdjusted = PI_VRNC_ADJSTD;
    }

    public byte getPiWastageAdjusted() {

        return piWastageAdjusted;
    }

    public void setPiWastageAdjusted(byte PI_WSTG_ADJSTD) {

        this.piWastageAdjusted = PI_WSTG_ADJSTD;
    }

    public Integer getPiAdBranch() {

        return piAdBranch;
    }

    public void setPiAdBranch(Integer PI_AD_BRNCH) {

        this.piAdBranch = PI_AD_BRNCH;
    }

    public Integer getPiAdCompany() {

        return piAdCompany;
    }

    public void setPiAdCompany(Integer PI_AD_CMPNY) {

        this.piAdCompany = PI_AD_CMPNY;
    }

    public LocalInvLocation getInvLocation() {

        return invLocation;
    }

    public void setInvLocation(LocalInvLocation invLocation) {

        this.invLocation = invLocation;
    }

    @XmlTransient
    public List getInvPhysicalInventoryLines() {

        return invPhysicalInventoryLines;
    }

    public void setInvPhysicalInventoryLines(List invPhysicalInventoryLines) {

        this.invPhysicalInventoryLines = invPhysicalInventoryLines;
    }

    public void addInvPhysicalInventoryLine(LocalInvPhysicalInventoryLine entity) {

        try {
            entity.setInvPhysicalInventory(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvPhysicalInventoryLine(LocalInvPhysicalInventoryLine entity) {

        try {
            entity.setInvPhysicalInventory(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "InvLocation")
@Table(name = "INV_LCTN")
public class LocalInvLocation extends NativeQueryHome implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOC_CODE", nullable = false)
    private Integer locCode;

    @Column(name = "LOC_NM", columnDefinition = "VARCHAR")
    private String locName;

    @Column(name = "LOC_DESC", columnDefinition = "VARCHAR")
    private String locDescription;

    @Column(name = "LOC_LV_TYP", columnDefinition = "VARCHAR")
    private String locLvType;

    @Column(name = "LOC_DPRTMNT", columnDefinition = "VARCHAR")
    private String locDepartment;

    @Column(name = "LOC_PSTN", columnDefinition = "VARCHAR")
    private String locPosition;

    @Column(name = "LOC_DT_HRD", columnDefinition = "VARCHAR")
    private String locDateHired;

    @Column(name = "LOC_EMPLYMNT_STTS", columnDefinition = "VARCHAR")
    private String locEmploymentStatus;

    @Column(name = "LOC_BRNCH", columnDefinition = "VARCHAR")
    private String locBranch;

    @Column(name = "LOC_ADDRSS", columnDefinition = "VARCHAR")
    private String locAddress;

    @Column(name = "LOC_CNTCT_PRSN", columnDefinition = "VARCHAR")
    private String locContactPerson;

    @Column(name = "LOC_CNTCT_NMBR", columnDefinition = "VARCHAR")
    private String locContactNumber;

    @Column(name = "LOC_EML_ADDRSS", columnDefinition = "VARCHAR")
    private String locEmailAddress;

    @Column(name = "LOC_AD_CMPNY", columnDefinition = "INT")
    private Integer locAdCompany;

    @OneToMany(mappedBy = "invLocation", fetch = FetchType.LAZY)
    private List<LocalInvItemLocation> invItemLocations;

    @OneToMany(mappedBy = "invLocation", fetch = FetchType.LAZY)
    private List<LocalInvPhysicalInventory> invPhysicalInventories;

    @OneToMany(mappedBy = "invLocation", fetch = FetchType.LAZY)
    private List<LocalInvBranchStockTransfer> invBranchStockTransfers;

    public Integer getLocCode() {

        return locCode;
    }

    public void setLocCode(Integer LOC_CODE) {

        this.locCode = LOC_CODE;
    }

    public String getLocName() {

        return locName;
    }

    public void setLocName(String LOC_NM) {

        this.locName = LOC_NM;
    }

    public String getLocDescription() {

        return locDescription;
    }

    public void setLocDescription(String LOC_DESC) {

        this.locDescription = LOC_DESC;
    }

    public String getLocLvType() {

        return locLvType;
    }

    public void setLocLvType(String LOC_LV_TYP) {

        this.locLvType = LOC_LV_TYP;
    }

    public String getLocDepartment() {

        return locDepartment;
    }

    public void setLocDepartment(String LOC_DPRTMNT) {

        this.locDepartment = LOC_DPRTMNT;
    }

    public String getLocPosition() {

        return locPosition;
    }

    public void setLocPosition(String LOC_PSTN) {

        this.locPosition = LOC_PSTN;
    }

    public String getLocDateHired() {

        return locDateHired;
    }

    public void setLocDateHired(String LOC_DT_HRD) {

        this.locDateHired = LOC_DT_HRD;
    }

    public String getLocEmploymentStatus() {

        return locEmploymentStatus;
    }

    public void setLocEmploymentStatus(String LOC_EMPLYMNT_STTS) {

        this.locEmploymentStatus = LOC_EMPLYMNT_STTS;
    }

    public String getLocBranch() {

        return locBranch;
    }

    public void setLocBranch(String LOC_BRNCH) {

        this.locBranch = LOC_BRNCH;
    }

    public String getLocAddress() {

        return locAddress;
    }

    public void setLocAddress(String LOC_ADDRSS) {

        this.locAddress = LOC_ADDRSS;
    }

    public String getLocContactPerson() {

        return locContactPerson;
    }

    public void setLocContactPerson(String LOC_CNTCT_PRSN) {

        this.locContactPerson = LOC_CNTCT_PRSN;
    }

    public String getLocContactNumber() {

        return locContactNumber;
    }

    public void setLocContactNumber(String LOC_CNTCT_NMBR) {

        this.locContactNumber = LOC_CNTCT_NMBR;
    }

    public String getLocEmailAddress() {

        return locEmailAddress;
    }

    public void setLocEmailAddress(String LOC_EML_ADDRSS) {

        this.locEmailAddress = LOC_EML_ADDRSS;
    }

    public Integer getLocAdCompany() {

        return locAdCompany;
    }

    public void setLocAdCompany(Integer LOC_AD_CMPNY) {

        this.locAdCompany = LOC_AD_CMPNY;
    }

    @XmlTransient
    public List getInvItemLocations() {

        return invItemLocations;
    }

    public void setInvItemLocations(List invItemLocations) {

        this.invItemLocations = invItemLocations;
    }

    @XmlTransient
    public List getInvPhysicalInventories() {

        return invPhysicalInventories;
    }

    public void setInvPhysicalInventories(List invPhysicalInventories) {

        this.invPhysicalInventories = invPhysicalInventories;
    }

    @XmlTransient
    public List getInvBranchStockTransfers() {

        return invBranchStockTransfers;
    }

    public void setInvBranchStockTransfers(List invBranchStockTransfers) {

        this.invBranchStockTransfers = invBranchStockTransfers;
    }

    public void addInvItemLocation(LocalInvItemLocation entity) {

        try {
            entity.setInvLocation(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvItemLocation(LocalInvItemLocation entity) {

        try {
            entity.setInvLocation(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addInvBranchStockTransfer(LocalInvBranchStockTransfer entity) {

        try {
            entity.setInvLocation(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvBranchStockTransfer(LocalInvBranchStockTransfer entity) {

        try {
            entity.setInvLocation(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addInvPhysicalInventory(LocalInvPhysicalInventory entity) {

        try {
            entity.setInvLocation(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvPhysicalInventory(LocalInvPhysicalInventory entity) {

        try {
            entity.setInvLocation(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
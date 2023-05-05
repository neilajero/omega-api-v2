package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.inv.LocalInvBranchStockTransfer;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "AdBranch")
@Table(name = "AD_BRNCH")
public class LocalAdBranch extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BR_CODE", nullable = false)
    private Integer brCode;

    @Column(name = "BR_BRNCH_CODE", columnDefinition = "VARCHAR")
    private String brBranchCode;

    @Column(name = "BR_NM", columnDefinition = "VARCHAR")
    private String brName;

    @Column(name = "BR_DESC", columnDefinition = "VARCHAR")
    private String brDescription;

    @Column(name = "BR_TYP", columnDefinition = "VARCHAR")
    private String brType;

    @Column(name = "BR_REGION_NM", columnDefinition = "VARCHAR")
    private String brRegionName;

    @Column(name = "BR_IS_BAU_BR", columnDefinition = "TINYINT")
    private byte brIsBauBranch;

    @Column(name = "BR_BAU_NM", columnDefinition = "VARCHAR")
    private String brBauName;

    @Column(name = "BR_HD_QTR", columnDefinition = "TINYINT")
    private byte brHeadQuarter;

    @Column(name = "BR_ADDRSS", columnDefinition = "VARCHAR")
    private String brAddress;

    @Column(name = "BR_CNTCT_PRSN", columnDefinition = "VARCHAR")
    private String brContactPerson;

    @Column(name = "BR_CNTCT_NMBR", columnDefinition = "VARCHAR")
    private String brContactNumber;

    @Column(name = "BR_COA_SGMNT", columnDefinition = "VARCHAR")
    private String brCoaSegment;

    @Column(name = "BR_DWNLD_STATUS", columnDefinition = "VARCHAR")
    private char brDownloadStatus;

    @Column(name = "BR_APPLY_SHPPNG", columnDefinition = "TINYINT")
    private byte brApplyShipping;

    @Column(name = "BR_PRCNT_MRKP", columnDefinition = "DOUBLE")
    private double brPercentMarkup = 0;

    @Column(name = "BR_AD_CMPNY", columnDefinition = "INT")
    private Integer brAdCompany;

    @JoinColumn(name = "GL_CHART_OF_ACCOUNT", referencedColumnName = "COA_CODE")
    @ManyToOne
    private LocalGlChartOfAccount glChartOfAccount;

    @OneToMany(mappedBy = "adBranch", fetch = FetchType.LAZY)
    private List<LocalAdBranchResponsibility> adBranchResponsibilities;

    @OneToMany(mappedBy = "adBranch", fetch = FetchType.LAZY)
    private List<LocalAdBranchDocumentSequenceAssignment> adBranchDocumentSequenceAssignments;

    @OneToMany(mappedBy = "adBranch", fetch = FetchType.LAZY)
    private List<LocalAdBranchCoa> adBranchCoas;

    @OneToMany(mappedBy = "adBranch", fetch = FetchType.LAZY)
    private List<LocalAdBranchBankAccount> adBranchBankAccounts;

    @OneToMany(mappedBy = "adBranch", fetch = FetchType.LAZY)
    private List<LocalAdBranchArTaxCode> adBranchArTaxCodes;

    @OneToMany(mappedBy = "adBranch", fetch = FetchType.LAZY)
    private List<LocalAdBranchApTaxCode> adBranchApTaxCodes;

    @OneToMany(mappedBy = "adBranch", fetch = FetchType.LAZY)
    private List<LocalAdBranchCustomer> adBranchCustomer;

    @OneToMany(mappedBy = "adBranch", fetch = FetchType.LAZY)
    private List<LocalAdBranchStandardMemoLine> adBranchStandardMemoLine;

    @OneToMany(mappedBy = "adBranch", fetch = FetchType.LAZY)
    private List<LocalAdBranchSupplier> adBranchSupplier;

    @OneToMany(mappedBy = "adBranch", fetch = FetchType.LAZY)
    private List<LocalAdBranchItemLocation> adBranchItemLocation;

    @OneToMany(mappedBy = "adBranch", fetch = FetchType.LAZY)
    private List<LocalAdBranchSalesperson> adBranchSalespersons;

    @OneToMany(mappedBy = "adBranch", fetch = FetchType.LAZY)
    private List<LocalInvBranchStockTransfer> invBranchStockTransfers;

    @OneToMany(mappedBy = "adBranch", fetch = FetchType.LAZY)
    private List<LocalAdBranchProjectTypeType> adBranchProjectTypeType;

    public Integer getBrCode() {

        return brCode;
    }

    public void setBrCode(Integer BR_CODE) {

        this.brCode = BR_CODE;
    }

    public String getBrBranchCode() {

        return brBranchCode;
    }

    public void setBrBranchCode(String BR_BRNCH_CODE) {

        this.brBranchCode = BR_BRNCH_CODE;
    }

    public String getBrName() {

        return brName;
    }

    public void setBrName(String BR_NM) {

        this.brName = BR_NM;
    }

    public String getBrDescription() {

        return brDescription;
    }

    public void setBrDescription(String BR_DESC) {

        this.brDescription = BR_DESC;
    }

    public String getBrType() {

        return brType;
    }

    public void setBrType(String BR_TYP) {

        this.brType = BR_TYP;
    }

    public String getBrRegionName() {

        return brRegionName;
    }

    public void setBrRegionName(String BR_REGION_NM) {

        this.brRegionName = BR_REGION_NM;
    }

    public byte getBrIsBauBranch() {

        return brIsBauBranch;
    }

    public void setBrIsBauBranch(byte BR_IS_BAU) {

        this.brIsBauBranch = BR_IS_BAU;
    }

    public String getBrBauName() {

        return brBauName;
    }

    public void setBrBauName(String BR_BAU_NM) {

        this.brBauName = BR_BAU_NM;
    }

    public byte getBrHeadQuarter() {

        return brHeadQuarter;
    }

    public void setBrHeadQuarter(byte BR_HD_QTR) {

        this.brHeadQuarter = BR_HD_QTR;
    }

    public String getBrAddress() {

        return brAddress;
    }

    public void setBrAddress(String BR_ADDRSS) {

        this.brAddress = BR_ADDRSS;
    }

    public String getBrContactPerson() {

        return brContactPerson;
    }

    public void setBrContactPerson(String BR_CNTCT_PRSN) {

        this.brContactPerson = BR_CNTCT_PRSN;
    }

    public String getBrContactNumber() {

        return brContactNumber;
    }

    public void setBrContactNumber(String BR_CNTCT_NMBR) {

        this.brContactNumber = BR_CNTCT_NMBR;
    }

    public String getBrCoaSegment() {

        return brCoaSegment;
    }

    public void setBrCoaSegment(String BR_COA_SGMNT) {

        this.brCoaSegment = BR_COA_SGMNT;
    }

    public char getBrDownloadStatus() {

        return brDownloadStatus;
    }

    public void setBrDownloadStatus(char BR_DWNLD_STATUS) {

        this.brDownloadStatus = BR_DWNLD_STATUS;
    }

    public byte getBrApplyShipping() {

        return brApplyShipping;
    }

    public void setBrApplyShipping(byte BR_APPLY_SHPPNG) {

        this.brApplyShipping = BR_APPLY_SHPPNG;
    }

    public double getBrPercentMarkup() {

        return brPercentMarkup;
    }

    public void setBrPercentMarkup(double BR_PRCNT_MRKP) {

        this.brPercentMarkup = BR_PRCNT_MRKP;
    }

    public Integer getBrAdCompany() {

        return brAdCompany;
    }

    public void setBrAdCompany(Integer BR_AD_CMPNY) {

        this.brAdCompany = BR_AD_CMPNY;
    }

    public LocalGlChartOfAccount getGlChartOfAccount() {

        return glChartOfAccount;
    }

    public void setGlChartOfAccount(LocalGlChartOfAccount glChartOfAccount) {

        this.glChartOfAccount = glChartOfAccount;
    }

    @XmlTransient
    public List getAdBranchResponsibilities() {

        return adBranchResponsibilities;
    }

    public void setAdBranchResponsibilities(List adBranchResponsibilities) {

        this.adBranchResponsibilities = adBranchResponsibilities;
    }

    @XmlTransient
    public List getAdBranchDocumentSequenceAssignments() {

        return adBranchDocumentSequenceAssignments;
    }

    @XmlTransient
    public List getAdBranchCoas() {

        return adBranchCoas;
    }

    @XmlTransient
    public List getAdBranchBankAccounts() {

        return adBranchBankAccounts;
    }

    @XmlTransient
    public List getAdBranchCustomer() {

        return adBranchCustomer;
    }

    public void setAdBranchCustomer(List adBranchCustomer) {

        this.adBranchCustomer = adBranchCustomer;
    }

    @XmlTransient
    public List getAdBranchStandardMemoLine() {

        return adBranchStandardMemoLine;
    }

    public void setAdBranchStandardMemoLine(List adBranchStandardMemoLine) {

        this.adBranchStandardMemoLine = adBranchStandardMemoLine;
    }

    @XmlTransient
    public List getAdBranchSupplier() {

        return adBranchSupplier;
    }

    public void setAdBranchSupplier(List adBranchSupplier) {

        this.adBranchSupplier = adBranchSupplier;
    }

    @XmlTransient
    public List getAdBranchItemLocation() {

        return adBranchItemLocation;
    }

    public void setAdBranchItemLocation(List adBranchItemLocation) {

        this.adBranchItemLocation = adBranchItemLocation;
    }

    @XmlTransient
    public List getInvBranchStockTransfers() {

        return invBranchStockTransfers;
    }

    public void setInvBranchStockTransfers(List invBranchStockTransfers) {

        this.invBranchStockTransfers = invBranchStockTransfers;
    }

    public void addAdBranchResponsibility(LocalAdBranchResponsibility entity) {

        try {
            entity.setAdBranch(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBranchResponsibility(LocalAdBranchResponsibility entity) {

        try {
            entity.setAdBranch(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdBranchDocumentSequenceAssignments(LocalAdBranchDocumentSequenceAssignment entity) {

        try {
            entity.setAdBranch(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBranchDocumentSequenceAssignments(LocalAdBranchDocumentSequenceAssignment entity) {

        try {
            entity.setAdBranch(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdBranchCoa(LocalAdBranchCoa entity) {

        try {
            entity.setAdBranch(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBranchCoa(LocalAdBranchCoa entity) {

        try {
            entity.setAdBranch(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdBranchBankAccount(LocalAdBranchBankAccount entity) {

        try {
            entity.setAdBranch(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBranchBankAccount(LocalAdBranchBankAccount entity) {

        try {
            entity.setAdBranch(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdBranchArTaxCode(LocalAdBranchArTaxCode entity) {

        try {
            entity.setAdBranch(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBranchArTaxCode(LocalAdBranchArTaxCode entity) {

        try {
            entity.setAdBranch(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdBranchApTaxCode(LocalAdBranchApTaxCode entity) {

        try {
            entity.setAdBranch(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBranchApTaxCode(LocalAdBranchApTaxCode entity) {

        try {
            entity.setAdBranch(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBranchCustomer(LocalAdBranchCustomer entity) {

        try {
            entity.setAdBranch(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdBranchStandardMemoLine(LocalAdBranchStandardMemoLine entity) {

        try {
            entity.setAdBranch(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBranchStandardMemoLine(LocalAdBranchStandardMemoLine entity) {

        try {
            entity.setAdBranch(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBranchSupplier(LocalAdBranchSupplier entity) {

        try {
            entity.setAdBranch(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdBranchItemLocation(LocalAdBranchItemLocation entity) {

        try {
            entity.setAdBranch(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBranchItemLocation(LocalAdBranchItemLocation entity) {

        try {
            entity.setAdBranch(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdBranchSalesperson(LocalAdBranchSalesperson entity) {

        try {
            entity.setAdBranch(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBranchSalesperson(LocalAdBranchSalesperson entity) {

        try {
            entity.setAdBranch(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addInvBranchStockTransfer(LocalInvBranchStockTransfer entity) {

        try {
            entity.setAdBranch(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
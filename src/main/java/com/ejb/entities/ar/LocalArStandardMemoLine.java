package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ad.LocalAdBranchStandardMemoLine;
import com.ejb.entities.gl.LocalGlChartOfAccount;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;

@Entity(name = "ArStandardMemoLine")
@Table(name = "AR_STNDRD_MMO_LN")
public class LocalArStandardMemoLine extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SML_CODE", nullable = false)
    private Integer smlCode;

    @Column(name = "SML_TYP", columnDefinition = "VARCHAR")
    private String smlType;

    @Column(name = "SML_NM", columnDefinition = "VARCHAR")
    private String smlName;

    @Column(name = "SML_DESC", columnDefinition = "VARCHAR")
    private String smlDescription;

    @Column(name = "SML_WP_PRDCT_ID", columnDefinition = "VARCHAR")
    private String smlWordPressProductID;

    @Column(name = "SML_UNT_PRC", columnDefinition = "DOUBLE")
    private double smlUnitPrice = 0;

    @Column(name = "SML_TX", columnDefinition = "TINYINT")
    private byte smlTax;

    @Column(name = "SML_ENBL", columnDefinition = "TINYINT")
    private byte smlEnable;

    @Column(name = "SML_SBJCT_TO_CMMSSN", columnDefinition = "TINYINT")
    private byte smlSubjectToCommission;

    @Column(name = "SML_INTRM_ACCNT", columnDefinition = "INT")
    private Integer smlInterimAccount;

    @Column(name = "SML_UNT_OF_MSR", columnDefinition = "VARCHAR")
    private String smlUnitOfMeasure;

    @Column(name = "SML_GL_COA_RCVBL_ACCNT", columnDefinition = "INT")
    private Integer smlGlCoaReceivableAccount;

    @Column(name = "SML_GL_COA_RVNUE_ACCNT", columnDefinition = "INT")
    private Integer smlGlCoaRevenueAccount;

    @Column(name = "SML_AD_CMPNY", columnDefinition = "INT")
    private Integer smlAdCompany;

    @JoinColumn(name = "GL_CHART_OF_ACCOUNT", referencedColumnName = "COA_CODE")
    @ManyToOne
    private LocalGlChartOfAccount glChartOfAccount;

    @OneToMany(mappedBy = "arStandardMemoLine", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalAdBranchStandardMemoLine> adBranchStandardMemoLines;

    @OneToMany(mappedBy = "arStandardMemoLine", fetch = FetchType.LAZY)
    private List<LocalArInvoiceLine> arInvoiceLines;

    @OneToMany(mappedBy = "arStandardMemoLine", fetch = FetchType.LAZY)
    private List<LocalArStandardMemoLineClass> arStandardMemoLineClasses;

    public Integer getSmlCode() {

        return smlCode;
    }

    public void setSmlCode(Integer SML_CODE) {

        this.smlCode = SML_CODE;
    }

    public String getSmlType() {

        return smlType;
    }

    public void setSmlType(String SML_TYP) {

        this.smlType = SML_TYP;
    }

    public String getSmlName() {

        return smlName;
    }

    public void setSmlName(String SML_NM) {

        this.smlName = SML_NM;
    }

    public String getSmlDescription() {

        return smlDescription;
    }

    public void setSmlDescription(String SML_DESC) {

        this.smlDescription = SML_DESC;
    }

    public String getSmlWordPressProductID() {

        return smlWordPressProductID;
    }

    public void setSmlWordPressProductID(String SML_WP_PRDCT_ID) {

        this.smlWordPressProductID = SML_WP_PRDCT_ID;
    }

    public double getSmlUnitPrice() {

        return smlUnitPrice;
    }

    public void setSmlUnitPrice(double SML_UNT_PRC) {

        this.smlUnitPrice = SML_UNT_PRC;
    }

    public byte getSmlTax() {

        return smlTax;
    }

    public void setSmlTax(byte SML_TX) {

        this.smlTax = SML_TX;
    }

    public byte getSmlEnable() {

        return smlEnable;
    }

    public void setSmlEnable(byte SML_ENBL) {

        this.smlEnable = SML_ENBL;
    }

    public byte getSmlSubjectToCommission() {

        return smlSubjectToCommission;
    }

    public void setSmlSubjectToCommission(byte SML_SBJCT_TO_CMMSSN) {

        this.smlSubjectToCommission = SML_SBJCT_TO_CMMSSN;
    }

    public Integer getSmlInterimAccount() {

        return smlInterimAccount;
    }

    public void setSmlInterimAccount(Integer SML_INTRM_ACCNT) {

        this.smlInterimAccount = SML_INTRM_ACCNT;
    }

    public String getSmlUnitOfMeasure() {

        return smlUnitOfMeasure;
    }

    public void setSmlUnitOfMeasure(String SML_UNT_OF_MSR) {

        this.smlUnitOfMeasure = SML_UNT_OF_MSR;
    }

    public Integer getSmlGlCoaReceivableAccount() {

        return smlGlCoaReceivableAccount;
    }

    public void setSmlGlCoaReceivableAccount(Integer SML_GL_COA_RCVBL_ACCNT) {

        this.smlGlCoaReceivableAccount = SML_GL_COA_RCVBL_ACCNT;
    }

    public Integer getSmlGlCoaRevenueAccount() {

        return smlGlCoaRevenueAccount;
    }

    public void setSmlGlCoaRevenueAccount(Integer SML_GL_COA_RVNUE_ACCNT) {

        this.smlGlCoaRevenueAccount = SML_GL_COA_RVNUE_ACCNT;
    }

    public Integer getSmlAdCompany() {

        return smlAdCompany;
    }

    public void setSmlAdCompany(Integer SML_AD_CMPNY) {

        this.smlAdCompany = SML_AD_CMPNY;
    }

    public LocalGlChartOfAccount getGlChartOfAccount() {

        return glChartOfAccount;
    }

    public void setGlChartOfAccount(LocalGlChartOfAccount glChartOfAccount) {

        this.glChartOfAccount = glChartOfAccount;
    }

    @XmlTransient
    public List getAdBranchStandardMemoLines() {

        return adBranchStandardMemoLines;
    }

    public void setAdBranchStandardMemoLines(List adBranchStandardMemoLines) {

        this.adBranchStandardMemoLines = adBranchStandardMemoLines;
    }

    @XmlTransient
    public List getArInvoiceLines() {

        return arInvoiceLines;
    }

    public void setArInvoiceLines(List arInvoiceLines) {

        this.arInvoiceLines = arInvoiceLines;
    }

    @XmlTransient
    public List getArStandardMemoLineClasses() {

        return arStandardMemoLineClasses;
    }

    public void setArStandardMemoLineClasses(List arStandardMemoLineClasses) {

        this.arStandardMemoLineClasses = arStandardMemoLineClasses;
    }

    public void addArInvoiceLine(LocalArInvoiceLine entity) {

        try {
            entity.setArStandardMemoLine(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArInvoiceLine(LocalArInvoiceLine entity) {

        try {
            entity.setArStandardMemoLine(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdBranchStandardMemoLine(LocalAdBranchStandardMemoLine entity) {

        try {
            entity.setArStandardMemoLine(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBranchStandardMemoLine(LocalAdBranchStandardMemoLine entity) {

        try {
            entity.setArStandardMemoLine(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArStandardMemoLineClass(LocalArStandardMemoLineClass entity) {

        try {
            entity.setArStandardMemoLine(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArStandardMemoLineClass(LocalArStandardMemoLineClass entity) {

        try {
            entity.setArStandardMemoLine(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
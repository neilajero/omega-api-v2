package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "GlJournalInterface")
@Table(name = "GL_JRNL_INTRFC")
public class LocalGlJournalInterface extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JRI_CODE", nullable = false)
    private Integer jriCode;

    @Column(name = "JRI_NM", columnDefinition = "VARCHAR")
    private String jriName;

    @Column(name = "JRI_DESC", columnDefinition = "VARCHAR")
    private String jriDescription;

    @Column(name = "JRI_EFFCTV_DT", columnDefinition = "DATETIME")
    private Date jriEffectiveDate;

    @Column(name = "JRI_JRNL_CTGRY", columnDefinition = "VARCHAR")
    private String jriJournalCategory;

    @Column(name = "JRI_JRNL_SRC", columnDefinition = "VARCHAR")
    private String jriJournalSource;

    @Column(name = "JRI_FNCTNL_CRRNCY", columnDefinition = "VARCHAR")
    private String jriFunctionalCurrency;

    @Column(name = "JRI_DT_RVRSL", columnDefinition = "DATETIME")
    private Date jriDateReversal;

    @Column(name = "JRI_DCMNT_NMBR", columnDefinition = "VARCHAR")
    private String jriDocumentNumber;

    @Column(name = "JRI_CNVRSN_DT", columnDefinition = "DATETIME")
    private Date jriConversionDate;

    @Column(name = "JRI_CNVRSN_RT", columnDefinition = "DOUBLE")
    private double jriConversionRate = 0;

    @Column(name = "JRI_FND_STATUS", columnDefinition = "VARCHAR")
    private char jriFundStatus;

    @Column(name = "JRI_RVRSD", columnDefinition = "TINYINT")
    private byte jriReversed;

    @Column(name = "JRI_TIN", columnDefinition = "VARCHAR")
    private String jriTin;

    @Column(name = "JRI_SUB_LDGR", columnDefinition = "VARCHAR")
    private String jriSubLedger;

    @Column(name = "JRI_RFRNC_NMBR", columnDefinition = "VARCHAR")
    private String jriReferenceNumber;

    @Column(name = "JRI_AD_BRNCH", columnDefinition = "INT")
    private Integer jriAdBranch;

    @Column(name = "JRI_AD_CMPNY", columnDefinition = "INT")
    private Integer jriAdCompany;

    @OneToMany(mappedBy = "glJournalInterface", fetch = FetchType.LAZY)
    private List<LocalGlJournalLineInterface> glJournalLineInterfaces;

    public Integer getJriCode() {

        return jriCode;
    }

    public void setJriCode(Integer JRI_CODE) {

        this.jriCode = JRI_CODE;
    }

    public String getJriName() {

        return jriName;
    }

    public void setJriName(String JRI_NM) {

        this.jriName = JRI_NM;
    }

    public String getJriDescription() {

        return jriDescription;
    }

    public void setJriDescription(String JRI_DESC) {

        this.jriDescription = JRI_DESC;
    }

    public Date getJriEffectiveDate() {

        return jriEffectiveDate;
    }

    public void setJriEffectiveDate(Date JRI_EFFCTV_DT) {

        this.jriEffectiveDate = JRI_EFFCTV_DT;
    }

    public String getJriJournalCategory() {

        return jriJournalCategory;
    }

    public void setJriJournalCategory(String JRI_JRNL_CTGRY) {

        this.jriJournalCategory = JRI_JRNL_CTGRY;
    }

    public String getJriJournalSource() {

        return jriJournalSource;
    }

    public void setJriJournalSource(String JRI_JRNL_SRC) {

        this.jriJournalSource = JRI_JRNL_SRC;
    }

    public String getJriFunctionalCurrency() {

        return jriFunctionalCurrency;
    }

    public void setJriFunctionalCurrency(String JRI_FNCTNL_CRRNCY) {

        this.jriFunctionalCurrency = JRI_FNCTNL_CRRNCY;
    }

    public Date getJriDateReversal() {

        return jriDateReversal;
    }

    public void setJriDateReversal(Date JRI_DT_RVRSL) {

        this.jriDateReversal = JRI_DT_RVRSL;
    }

    public String getJriDocumentNumber() {

        return jriDocumentNumber;
    }

    public void setJriDocumentNumber(String JRI_DCMNT_NMBR) {

        this.jriDocumentNumber = JRI_DCMNT_NMBR;
    }

    public Date getJriConversionDate() {

        return jriConversionDate;
    }

    public void setJriConversionDate(Date JRI_CNVRSN_DT) {

        this.jriConversionDate = JRI_CNVRSN_DT;
    }

    public double getJriConversionRate() {

        return jriConversionRate;
    }

    public void setJriConversionRate(double JRI_CNVRSN_RT) {

        this.jriConversionRate = JRI_CNVRSN_RT;
    }

    public char getJriFundStatus() {

        return jriFundStatus;
    }

    public void setJriFundStatus(char JRI_FND_STATUS) {

        this.jriFundStatus = JRI_FND_STATUS;
    }

    public byte getJriReversed() {

        return jriReversed;
    }

    public void setJriReversed(byte JRI_RVRSD) {

        this.jriReversed = JRI_RVRSD;
    }

    public String getJriTin() {

        return jriTin;
    }

    public void setJriTin(String JRI_TIN) {

        this.jriTin = JRI_TIN;
    }

    public String getJriSubLedger() {

        return jriSubLedger;
    }

    public void setJriSubLedger(String JRI_SUB_LDGR) {

        this.jriSubLedger = JRI_SUB_LDGR;
    }

    public String getJriReferenceNumber() {

        return jriReferenceNumber;
    }

    public void setJriReferenceNumber(String JRI_RFRNC_NMBR) {

        this.jriReferenceNumber = JRI_RFRNC_NMBR;
    }

    public Integer getJriAdBranch() {

        return jriAdBranch;
    }

    public void setJriAdBranch(Integer JRI_AD_BRNCH) {

        this.jriAdBranch = JRI_AD_BRNCH;
    }

    public Integer getJriAdCompany() {

        return jriAdCompany;
    }

    public void setJriAdCompany(Integer JRI_AD_CMPNY) {

        this.jriAdCompany = JRI_AD_CMPNY;
    }

    @XmlTransient
    public List getGlJournalLineInterfaces() {

        return glJournalLineInterfaces;
    }

    public void setGlJournalLineInterfaces(List glJournalLineInterfaces) {

        this.glJournalLineInterfaces = glJournalLineInterfaces;
    }

    public void addGlJournalLineInterface(LocalGlJournalLineInterface entity) {

        try {
            entity.setGlJournalInterface(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlJournalLineInterface(LocalGlJournalLineInterface entity) {

        try {
            entity.setGlJournalInterface(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
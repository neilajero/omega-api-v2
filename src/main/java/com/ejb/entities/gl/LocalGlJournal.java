package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ad.LocalAdDocumentSequenceAssignment;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "GlJournal")
@Table(name = "GL_JRNL")
public class LocalGlJournal extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JR_CODE", nullable = false)
    private Integer jrCode;

    @Column(name = "JR_NM", columnDefinition = "VARCHAR")
    private String jrName;

    @Column(name = "JR_DESC", columnDefinition = "VARCHAR")
    private String jrDescription;

    @Column(name = "JR_EFFCTV_DT", columnDefinition = "DATETIME")
    private Date jrEffectiveDate;

    @Column(name = "JR_CNTRL_TTL", columnDefinition = "DOUBLE")
    private double jrControlTotal = 0;

    @Column(name = "JR_DT_RVRSL", columnDefinition = "DATETIME")
    private Date jrDateReversal;

    @Column(name = "JR_DCMNT_NMBR", columnDefinition = "VARCHAR")
    private String jrDocumentNumber;

    @Column(name = "JR_CNVRSN_DT", columnDefinition = "DATETIME")
    private Date jrConversionDate;

    @Column(name = "JR_CNVRSN_RT", columnDefinition = "DOUBLE")
    private double jrConversionRate = 0;

    @Column(name = "JR_APPRVL_STATUS", columnDefinition = "VARCHAR")
    private String jrApprovalStatus;

    @Column(name = "JR_RSN_FR_RJCTN", columnDefinition = "VARCHAR")
    private String jrReasonForRejection;

    @Column(name = "JR_FND_STATUS", columnDefinition = "VARCHAR")
    private char jrFundStatus;

    @Column(name = "JR_PSTD", columnDefinition = "TINYINT")
    private byte jrPosted;

    @Column(name = "JR_RVRSD", columnDefinition = "TINYINT")
    private byte jrReversed;

    @Column(name = "JR_CRTD_BY", columnDefinition = "VARCHAR")
    private String jrCreatedBy;

    @Column(name = "JR_DT_CRTD", columnDefinition = "DATETIME")
    private Date jrDateCreated;

    @Column(name = "JR_LST_MDFD_BY", columnDefinition = "VARCHAR")
    private String jrLastModifiedBy;

    @Column(name = "JR_DT_LST_MDFD", columnDefinition = "DATETIME")
    private Date jrDateLastModified;

    @Column(name = "JR_APPRVD_RJCTD_BY", columnDefinition = "VARCHAR")
    private String jrApprovedRejectedBy;

    @Column(name = "JR_DT_APPRVD_RJCTD", columnDefinition = "DATETIME")
    private Date jrDateApprovedRejected;

    @Column(name = "JR_PSTD_BY", columnDefinition = "VARCHAR")
    private String jrPostedBy;

    @Column(name = "JR_DT_PSTD", columnDefinition = "DATETIME")
    private Date jrDatePosted;

    @Column(name = "JR_TIN", columnDefinition = "VARCHAR")
    private String jrTin;

    @Column(name = "JR_SUB_LDGR", columnDefinition = "VARCHAR")
    private String jrSubLedger;

    @Column(name = "JR_PRNTD", columnDefinition = "TINYINT")
    private byte jrPrinted;

    @Column(name = "JR_RFRNC_NMBR", columnDefinition = "VARCHAR")
    private String jrReferenceNumber;

    @Column(name = "JR_AD_BRNCH", columnDefinition = "INT")
    private Integer jrAdBranch;

    @Column(name = "JR_AD_CMPNY", columnDefinition = "INT")
    private Integer jrAdCompany;

    @JoinColumn(name = "AD_DOCUMENT_SEQUENCE_ASSIGNMENT", referencedColumnName = "DSA_CODE")
    @ManyToOne
    private LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment;

    @JoinColumn(name = "GL_FUNCTIONAL_CURRENCY", referencedColumnName = "FC_CODE")
    @ManyToOne
    private LocalGlFunctionalCurrency glFunctionalCurrency;

    @JoinColumn(name = "GL_JOURNAL_BATCH", referencedColumnName = "JB_CODE")
    @ManyToOne
    private LocalGlJournalBatch glJournalBatch;

    @JoinColumn(name = "GL_JOURNAL_CATEGORY", referencedColumnName = "JC_CODE")
    @ManyToOne
    private LocalGlJournalCategory glJournalCategory;

    @JoinColumn(name = "GL_JOURNAL_SOURCE", referencedColumnName = "JS_CODE")
    @ManyToOne
    private LocalGlJournalSource glJournalSource;

    @OneToMany(mappedBy = "glJournal", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalGlJournalLine> glJournalLines;

    public Integer getJrCode() {

        return jrCode;
    }

    public void setJrCode(Integer JR_CODE) {

        this.jrCode = JR_CODE;
    }

    public String getJrName() {

        return jrName;
    }

    public void setJrName(String JR_NM) {

        this.jrName = JR_NM;
    }

    public String getJrDescription() {

        return jrDescription;
    }

    public void setJrDescription(String JR_DESC) {

        this.jrDescription = JR_DESC;
    }

    public Date getJrEffectiveDate() {

        return jrEffectiveDate;
    }

    public void setJrEffectiveDate(Date JR_EFFCTV_DT) {

        this.jrEffectiveDate = JR_EFFCTV_DT;
    }

    public double getJrControlTotal() {

        return jrControlTotal;
    }

    public void setJrControlTotal(double JR_CNTRL_TTL) {

        this.jrControlTotal = JR_CNTRL_TTL;
    }

    public Date getJrDateReversal() {

        return jrDateReversal;
    }

    public void setJrDateReversal(Date JR_DT_RVRSL) {

        this.jrDateReversal = JR_DT_RVRSL;
    }

    public String getJrDocumentNumber() {

        return jrDocumentNumber;
    }

    public void setJrDocumentNumber(String JR_DCMNT_NMBR) {

        this.jrDocumentNumber = JR_DCMNT_NMBR;
    }

    public Date getJrConversionDate() {

        return jrConversionDate;
    }

    public void setJrConversionDate(Date JR_CNVRSN_DT) {

        this.jrConversionDate = JR_CNVRSN_DT;
    }

    public double getJrConversionRate() {

        return jrConversionRate;
    }

    public void setJrConversionRate(double JR_CNVRSN_RT) {

        this.jrConversionRate = JR_CNVRSN_RT;
    }

    public String getJrApprovalStatus() {

        return jrApprovalStatus;
    }

    public void setJrApprovalStatus(String JR_APPRVL_STATUS) {

        this.jrApprovalStatus = JR_APPRVL_STATUS;
    }

    public String getJrReasonForRejection() {

        return jrReasonForRejection;
    }

    public void setJrReasonForRejection(String JR_RSN_FR_RJCTN) {

        this.jrReasonForRejection = JR_RSN_FR_RJCTN;
    }

    public char getJrFundStatus() {

        return jrFundStatus;
    }

    public void setJrFundStatus(char JR_FND_STATUS) {

        this.jrFundStatus = JR_FND_STATUS;
    }

    public byte getJrPosted() {

        return jrPosted;
    }

    public void setJrPosted(byte JR_PSTD) {

        this.jrPosted = JR_PSTD;
    }

    public byte getJrReversed() {

        return jrReversed;
    }

    public void setJrReversed(byte JR_RVRSD) {

        this.jrReversed = JR_RVRSD;
    }

    public String getJrCreatedBy() {

        return jrCreatedBy;
    }

    public void setJrCreatedBy(String JR_CRTD_BY) {

        this.jrCreatedBy = JR_CRTD_BY;
    }

    public Date getJrDateCreated() {

        return jrDateCreated;
    }

    public void setJrDateCreated(Date JR_DT_CRTD) {

        this.jrDateCreated = JR_DT_CRTD;
    }

    public String getJrLastModifiedBy() {

        return jrLastModifiedBy;
    }

    public void setJrLastModifiedBy(String JR_LST_MDFD_BY) {

        this.jrLastModifiedBy = JR_LST_MDFD_BY;
    }

    public Date getJrDateLastModified() {

        return jrDateLastModified;
    }

    public void setJrDateLastModified(Date JR_DT_LST_MDFD) {

        this.jrDateLastModified = JR_DT_LST_MDFD;
    }

    public String getJrApprovedRejectedBy() {

        return jrApprovedRejectedBy;
    }

    public void setJrApprovedRejectedBy(String JR_APPRVD_RJCTD_BY) {

        this.jrApprovedRejectedBy = JR_APPRVD_RJCTD_BY;
    }

    public Date getJrDateApprovedRejected() {

        return jrDateApprovedRejected;
    }

    public void setJrDateApprovedRejected(Date JR_DT_APPRVD_RJCTD) {

        this.jrDateApprovedRejected = JR_DT_APPRVD_RJCTD;
    }

    public String getJrPostedBy() {

        return jrPostedBy;
    }

    public void setJrPostedBy(String JR_PSTD_BY) {

        this.jrPostedBy = JR_PSTD_BY;
    }

    public Date getJrDatePosted() {

        return jrDatePosted;
    }

    public void setJrDatePosted(Date JR_DT_PSTD) {

        this.jrDatePosted = JR_DT_PSTD;
    }

    public String getJrTin() {

        return jrTin;
    }

    public void setJrTin(String JR_TIN) {

        this.jrTin = JR_TIN;
    }

    public String getJrSubLedger() {

        return jrSubLedger;
    }

    public void setJrSubLedger(String JR_SUB_LDGR) {

        this.jrSubLedger = JR_SUB_LDGR;
    }

    public byte getJrPrinted() {

        return jrPrinted;
    }

    public void setJrPrinted(byte JR_PRNTD) {

        this.jrPrinted = JR_PRNTD;
    }

    public String getJrReferenceNumber() {

        return jrReferenceNumber;
    }

    public void setJrReferenceNumber(String JR_RFRNC_NMBR) {

        this.jrReferenceNumber = JR_RFRNC_NMBR;
    }

    public Integer getJrAdBranch() {

        return jrAdBranch;
    }

    public void setJrAdBranch(Integer JR_AD_BRNCH) {

        this.jrAdBranch = JR_AD_BRNCH;
    }

    public Integer getJrAdCompany() {

        return jrAdCompany;
    }

    public void setJrAdCompany(Integer JR_AD_CMPNY) {

        this.jrAdCompany = JR_AD_CMPNY;
    }

    public LocalAdDocumentSequenceAssignment getAdDocumentSequenceAssignment() {

        return adDocumentSequenceAssignment;
    }

    public void setAdDocumentSequenceAssignment(
            LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment) {

        this.adDocumentSequenceAssignment = adDocumentSequenceAssignment;
    }

    public LocalGlFunctionalCurrency getGlFunctionalCurrency() {

        return glFunctionalCurrency;
    }

    public void setGlFunctionalCurrency(LocalGlFunctionalCurrency glFunctionalCurrency) {

        this.glFunctionalCurrency = glFunctionalCurrency;
    }

    public LocalGlJournalBatch getGlJournalBatch() {

        return glJournalBatch;
    }

    public void setGlJournalBatch(LocalGlJournalBatch glJournalBatch) {

        this.glJournalBatch = glJournalBatch;
    }

    public LocalGlJournalCategory getGlJournalCategory() {

        return glJournalCategory;
    }

    public void setGlJournalCategory(LocalGlJournalCategory glJournalCategory) {

        this.glJournalCategory = glJournalCategory;
    }

    public LocalGlJournalSource getGlJournalSource() {

        return glJournalSource;
    }

    public void setGlJournalSource(LocalGlJournalSource glJournalSource) {

        this.glJournalSource = glJournalSource;
    }

    @XmlTransient
    public List getGlJournalLines() {

        return glJournalLines;
    }

    public void setGlJournalLines(List glJournalLines) {

        this.glJournalLines = glJournalLines;
    }

    public void addGlJournalLine(LocalGlJournalLine entity) {

        try {
            entity.setGlJournal(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlJournalLine(LocalGlJournalLine entity) {

        try {
            entity.setGlJournal(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
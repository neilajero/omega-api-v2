package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ad.LocalAdApprovalCoaLine;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchCoa;
import com.ejb.entities.ad.LocalAdPaymentTerm;
import com.ejb.entities.ap.LocalApDistributionRecord;
import com.ejb.entities.ap.LocalApTaxCode;
import com.ejb.entities.ap.LocalApWithholdingTaxCode;
import com.ejb.entities.ar.LocalArDistributionRecord;
import com.ejb.entities.ar.LocalArStandardMemoLine;
import com.ejb.entities.ar.LocalArTaxCode;
import com.ejb.entities.ar.LocalArWithholdingTaxCode;
import com.ejb.entities.cm.LocalCmDistributionRecord;
import com.ejb.entities.inv.LocalInvAdjustment;
import com.ejb.entities.inv.LocalInvDistributionRecord;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "GlChartOfAccount")
@Table(name = "GL_COA")
public class LocalGlChartOfAccount extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COA_CODE", nullable = false)
    private Integer coaCode;

    @Column(name = "COA_ACCNT_NMBR", columnDefinition = "VARCHAR")
    private String coaAccountNumber;

    @Column(name = "COA_ACCNT_DESC", columnDefinition = "VARCHAR")
    private String coaAccountDescription;

    @Column(name = "COA_ACCNT_TYP", columnDefinition = "VARCHAR")
    private String coaAccountType;

    @Column(name = "COA_TX_TYP", columnDefinition = "VARCHAR")
    private String coaTaxType;

    @Column(name = "COA_CIT_CTGRY", columnDefinition = "VARCHAR")
    private String coaCitCategory;

    @Column(name = "COA_SAW_CTGRY", columnDefinition = "VARCHAR")
    private String coaSawCategory;

    @Column(name = "COA_IIT_CTGRY", columnDefinition = "VARCHAR")
    private String coaIitCategory;

    @Column(name = "COA_DT_FRM", columnDefinition = "VARCHAR")
    private Date coaDateFrom;

    @Column(name = "COA_DT_TO", columnDefinition = "DATETIME")
    private Date coaDateTo;

    @Column(name = "COA_SGMNT1", columnDefinition = "VARCHAR")
    private String coaSegment1;

    @Column(name = "COA_SGMNT2", columnDefinition = "VARCHAR")
    private String coaSegment2;

    @Column(name = "COA_SGMNT3", columnDefinition = "VARCHAR")
    private String coaSegment3;

    @Column(name = "COA_SGMNT4", columnDefinition = "VARCHAR")
    private String coaSegment4;

    @Column(name = "COA_SGMNT5", columnDefinition = "VARCHAR")
    private String coaSegment5;

    @Column(name = "COA_SGMNT6", columnDefinition = "VARCHAR")
    private String coaSegment6;

    @Column(name = "COA_SGMNT7", columnDefinition = "VARCHAR")
    private String coaSegment7;

    @Column(name = "COA_SGMNT8", columnDefinition = "VARCHAR")
    private String coaSegment8;

    @Column(name = "COA_SGMNT9", columnDefinition = "VARCHAR")
    private String coaSegment9;

    @Column(name = "COA_SGMNT10", columnDefinition = "VARCHAR")
    private String coaSegment10;

    @Column(name = "COA_ENBL", columnDefinition = "TINYINT")
    private byte coaEnable;

    @Column(name = "COA_FR_RVLTN", columnDefinition = "TINYINT")
    private byte coaForRevaluation;

    @Column(name = "COA_AP_TAG", columnDefinition = "TINYINT")
    private byte coaApTag;

    @Column(name = "COA_AD_CMPNY", columnDefinition = "INT")
    private Integer coaAdCompany;

    @JoinColumn(name = "GL_FUNCTIONAL_CURRENCY", referencedColumnName = "FC_CODE")
    @ManyToOne
    private LocalGlFunctionalCurrency glFunctionalCurrency;

    @OneToMany(mappedBy = "glChartOfAccount", fetch = FetchType.LAZY)
    private List<LocalGlSuspenseAccount> glSuspenseAccounts;

    @OneToMany(mappedBy = "glChartOfAccount", fetch = FetchType.LAZY)
    private List<LocalGlChartOfAccountBalance> glChartOfAccountBalances;

    @OneToMany(mappedBy = "glChartOfAccount", fetch = FetchType.LAZY)
    private List<LocalGlRecurringJournalLine> glRecurringJournalLines;

    @OneToMany(mappedBy = "glChartOfAccount", fetch = FetchType.LAZY)
    private List<LocalGlJournalLine> glJournalLines;

    @OneToMany(mappedBy = "glChartOfAccount", fetch = FetchType.LAZY)
    private List<LocalApDistributionRecord> apDistributionRecords;

    @OneToMany(mappedBy = "glChartOfAccount", fetch = FetchType.LAZY)
    private List<LocalCmDistributionRecord> cmDistributionRecords;

    @OneToMany(mappedBy = "glChartOfAccount", fetch = FetchType.LAZY)
    private List<LocalApTaxCode> apTaxCodes;

    @OneToMany(mappedBy = "glChartOfAccount", fetch = FetchType.LAZY)
    private List<LocalApWithholdingTaxCode> apWithholdingTaxCodes;

    @OneToMany(mappedBy = "glChartOfAccount", fetch = FetchType.LAZY)
    private List<LocalArTaxCode> arTaxCodes;

    @OneToMany(mappedBy = "glChartOfAccount", fetch = FetchType.LAZY)
    private List<LocalArWithholdingTaxCode> arWithholdingTaxCodes;

    @OneToMany(mappedBy = "glChartOfAccount", fetch = FetchType.LAZY)
    private List<LocalArDistributionRecord> arDistributionRecords;

    @OneToMany(mappedBy = "glChartOfAccount", fetch = FetchType.LAZY)
    private List<LocalArStandardMemoLine> arStandardMemoLines;

    @OneToMany(mappedBy = "glChartOfAccount", fetch = FetchType.LAZY)
    private List<LocalAdApprovalCoaLine> adApprovalCoaLines;

    @OneToMany(mappedBy = "glChartOfAccount", fetch = FetchType.LAZY)
    private List<LocalGlBudgetAmountCoa> glBudgetAmountCoas;

    @OneToMany(mappedBy = "glChartOfAccount", fetch = FetchType.LAZY)
    private List<LocalInvAdjustment> invAdjustments;

    @OneToMany(mappedBy = "invChartOfAccount", fetch = FetchType.LAZY)
    private List<LocalInvDistributionRecord> invDistributionRecords;

    @OneToMany(mappedBy = "glChartOfAccount", fetch = FetchType.LAZY)
    private List<LocalAdPaymentTerm> adPaymentTerms;

    @OneToMany(mappedBy = "glChartOfAccount", fetch = FetchType.LAZY)
    private List<LocalAdBranchCoa> adBranchCoas;

    @OneToMany(mappedBy = "glChartOfAccount", fetch = FetchType.LAZY)
    private List<LocalGlForexLedger> glForexLedgers;

    @OneToMany(mappedBy = "glChartOfAccount", fetch = FetchType.LAZY)
    private List<LocalGlReportValue> glReportValues;

    @OneToMany(mappedBy = "glChartOfAccount", fetch = FetchType.LAZY)
    private List<LocalAdBranch> adBranches;

    public Integer getCoaCode() {

        return coaCode;
    }

    public void setCoaCode(Integer COA_CODE) {

        this.coaCode = COA_CODE;
    }

    public String getCoaAccountNumber() {

        return coaAccountNumber;
    }

    public void setCoaAccountNumber(String COA_ACCNT_NMBR) {

        this.coaAccountNumber = COA_ACCNT_NMBR;
    }

    public String getCoaAccountDescription() {

        return coaAccountDescription;
    }

    public void setCoaAccountDescription(String COA_ACCNT_DESC) {

        this.coaAccountDescription = COA_ACCNT_DESC;
    }

    public String getCoaAccountType() {

        return coaAccountType;
    }

    public void setCoaAccountType(String COA_ACCNT_TYP) {

        this.coaAccountType = COA_ACCNT_TYP;
    }

    public String getCoaTaxType() {

        return coaTaxType;
    }

    public void setCoaTaxType(String COA_TX_TYP) {

        this.coaTaxType = COA_TX_TYP;
    }

    public String getCoaCitCategory() {

        return coaCitCategory;
    }

    public void setCoaCitCategory(String COA_CIT_CTGRY) {

        this.coaCitCategory = COA_CIT_CTGRY;
    }

    public String getCoaSawCategory() {

        return coaSawCategory;
    }

    public void setCoaSawCategory(String COA_SAW_CTGRY) {

        this.coaSawCategory = COA_SAW_CTGRY;
    }

    public String getCoaIitCategory() {

        return coaIitCategory;
    }

    public void setCoaIitCategory(String COA_IIT_CTGRY) {

        this.coaIitCategory = COA_IIT_CTGRY;
    }

    public Date getCoaDateFrom() {

        return coaDateFrom;
    }

    public void setCoaDateFrom(Date COA_DT_FRM) {

        this.coaDateFrom = COA_DT_FRM;
    }

    public Date getCoaDateTo() {

        return coaDateTo;
    }

    public void setCoaDateTo(Date COA_DT_TO) {

        this.coaDateTo = COA_DT_TO;
    }

    public String getCoaSegment1() {

        return coaSegment1;
    }

    public void setCoaSegment1(String COA_SGMNT1) {

        this.coaSegment1 = COA_SGMNT1;
    }

    public String getCoaSegment2() {

        return coaSegment2;
    }

    public void setCoaSegment2(String COA_SGMNT2) {

        this.coaSegment2 = COA_SGMNT2;
    }

    public String getCoaSegment3() {

        return coaSegment3;
    }

    public void setCoaSegment3(String COA_SGMNT3) {

        this.coaSegment3 = COA_SGMNT3;
    }

    public String getCoaSegment4() {

        return coaSegment4;
    }

    public void setCoaSegment4(String COA_SGMNT4) {

        this.coaSegment4 = COA_SGMNT4;
    }

    public String getCoaSegment5() {

        return coaSegment5;
    }

    public void setCoaSegment5(String COA_SGMNT5) {

        this.coaSegment5 = COA_SGMNT5;
    }

    public String getCoaSegment6() {

        return coaSegment6;
    }

    public void setCoaSegment6(String COA_SGMNT6) {

        this.coaSegment6 = COA_SGMNT6;
    }

    public String getCoaSegment7() {

        return coaSegment7;
    }

    public void setCoaSegment7(String COA_SGMNT7) {

        this.coaSegment7 = COA_SGMNT7;
    }

    public String getCoaSegment8() {

        return coaSegment8;
    }

    public void setCoaSegment8(String COA_SGMNT8) {

        this.coaSegment8 = COA_SGMNT8;
    }

    public String getCoaSegment9() {

        return coaSegment9;
    }

    public void setCoaSegment9(String COA_SGMNT9) {

        this.coaSegment9 = COA_SGMNT9;
    }

    public String getCoaSegment10() {

        return coaSegment10;
    }

    public void setCoaSegment10(String COA_SGMNT10) {

        this.coaSegment10 = COA_SGMNT10;
    }

    public byte getCoaEnable() {

        return coaEnable;
    }

    public void setCoaEnable(byte COA_ENBL) {

        this.coaEnable = COA_ENBL;
    }

    public byte getCoaForRevaluation() {

        return coaForRevaluation;
    }

    public void setCoaForRevaluation(byte COA_FR_RVLTN) {

        this.coaForRevaluation = COA_FR_RVLTN;
    }

    public byte getCoaApTag() {

        return coaApTag;
    }

    public void setCoaApTag(byte COA_AP_TAG) {

        this.coaApTag = COA_AP_TAG;
    }

    public Integer getCoaAdCompany() {

        return coaAdCompany;
    }

    public void setCoaAdCompany(Integer COA_AD_CMPNY) {

        this.coaAdCompany = COA_AD_CMPNY;
    }

    public LocalGlFunctionalCurrency getGlFunctionalCurrency() {

        return glFunctionalCurrency;
    }

    public void setGlFunctionalCurrency(LocalGlFunctionalCurrency glFunctionalCurrency) {

        this.glFunctionalCurrency = glFunctionalCurrency;
    }

    @XmlTransient
    public List getGlSuspenseAccounts() {

        return glSuspenseAccounts;
    }

    public void setGlSuspenseAccounts(List glSuspenseAccounts) {

        this.glSuspenseAccounts = glSuspenseAccounts;
    }

    @XmlTransient
    public List getGlChartOfAccountBalances() {

        return glChartOfAccountBalances;
    }

    public void setGlChartOfAccountBalances(List glChartOfAccountBalances) {

        this.glChartOfAccountBalances = glChartOfAccountBalances;
    }

    @XmlTransient
    public List getGlRecurringJournalLines() {

        return glRecurringJournalLines;
    }

    public void setGlRecurringJournalLines(List glRecurringJournalLines) {

        this.glRecurringJournalLines = glRecurringJournalLines;
    }

    @XmlTransient
    public List getGlJournalLines() {

        return glJournalLines;
    }

    public void setGlJournalLines(List glJournalLines) {

        this.glJournalLines = glJournalLines;
    }

    @XmlTransient
    public List getApDistributionRecords() {

        return apDistributionRecords;
    }

    public void setApDistributionRecords(List apDistributionRecords) {

        this.apDistributionRecords = apDistributionRecords;
    }

    @XmlTransient
    public List getCmDistributionRecords() {

        return cmDistributionRecords;
    }

    public void setCmDistributionRecords(List cmDistributionRecords) {

        this.cmDistributionRecords = cmDistributionRecords;
    }

    @XmlTransient
    public List getApTaxCodes() {

        return apTaxCodes;
    }

    public void setApTaxCodes(List apTaxCodes) {

        this.apTaxCodes = apTaxCodes;
    }

    @XmlTransient
    public List getApWithholdingTaxCodes() {

        return apWithholdingTaxCodes;
    }

    public void setApWithholdingTaxCodes(List apWithholdingTaxCodes) {

        this.apWithholdingTaxCodes = apWithholdingTaxCodes;
    }

    @XmlTransient
    public List getArTaxCodes() {

        return arTaxCodes;
    }

    public void setArTaxCodes(List arTaxCodes) {

        this.arTaxCodes = arTaxCodes;
    }

    @XmlTransient
    public List getArWithholdingTaxCodes() {

        return arWithholdingTaxCodes;
    }

    public void setArWithholdingTaxCodes(List arWithholdingTaxCodes) {

        this.arWithholdingTaxCodes = arWithholdingTaxCodes;
    }

    @XmlTransient
    public List getArDistributionRecords() {

        return arDistributionRecords;
    }

    public void setArDistributionRecords(List arDistributionRecords) {

        this.arDistributionRecords = arDistributionRecords;
    }

    @XmlTransient
    public List getArStandardMemoLines() {

        return arStandardMemoLines;
    }

    public void setArStandardMemoLines(List arStandardMemoLines) {

        this.arStandardMemoLines = arStandardMemoLines;
    }

    @XmlTransient
    public List getAdApprovalCoaLines() {

        return adApprovalCoaLines;
    }

    public void setAdApprovalCoaLines(List adApprovalCoaLines) {

        this.adApprovalCoaLines = adApprovalCoaLines;
    }

    @XmlTransient
    public List getGlBudgetAmountCoas() {

        return glBudgetAmountCoas;
    }

    public void setGlBudgetAmountCoas(List glBudgetAmountCoas) {

        this.glBudgetAmountCoas = glBudgetAmountCoas;
    }

    @XmlTransient
    public List getInvAdjustments() {

        return invAdjustments;
    }

    public void setInvAdjustments(List invAdjustments) {

        this.invAdjustments = invAdjustments;
    }

    @XmlTransient
    public List getInvDistributionRecords() {

        return invDistributionRecords;
    }

    public void setInvDistributionRecords(List invDistributionRecords) {

        this.invDistributionRecords = invDistributionRecords;
    }

    @XmlTransient
    public List getAdPaymentTerms() {

        return adPaymentTerms;
    }

    public void setAdPaymentTerms(List adPaymentTerms) {

        this.adPaymentTerms = adPaymentTerms;
    }

    @XmlTransient
    public List getAdBranchCoas() {

        return adBranchCoas;
    }

    public void setAdBranchCoas(List adBranchCoas) {

        this.adBranchCoas = adBranchCoas;
    }

    @XmlTransient
    public List getGlForexLedgers() {

        return glForexLedgers;
    }

    public void setGlForexLedgers(List glForexLedgers) {

        this.glForexLedgers = glForexLedgers;
    }

    @XmlTransient
    public List getGlReportValues() {

        return glReportValues;
    }

    public void setGlReportValues(List glReportValues) {

        this.glReportValues = glReportValues;
    }

    @XmlTransient
    public List getAdBranches() {

        return adBranches;
    }

    public void setAdBranches(List adBranches) {

        this.adBranches = adBranches;
    }

    public void addGlSuspenseAccount(LocalGlSuspenseAccount entity) {

        try {
            entity.setGlChartOfAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlSuspenseAccount(LocalGlSuspenseAccount entity) {

        try {
            entity.setGlChartOfAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addGlChartOfAccountBalance(LocalGlChartOfAccountBalance entity) {

        try {
            entity.setGlChartOfAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlChartOfAccountBalance(LocalGlChartOfAccountBalance entity) {

        try {
            entity.setGlChartOfAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addGlRecurringJournalLine(LocalGlRecurringJournalLine entity) {

        try {
            entity.setGlChartOfAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlRecurringJournalLine(LocalGlRecurringJournalLine entity) {

        try {
            entity.setGlChartOfAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addGlJournalLine(LocalGlJournalLine entity) {

        try {
            entity.setGlChartOfAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlJournalLine(LocalGlJournalLine entity) {

        try {
            entity.setGlChartOfAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addCmDistributionRecord(LocalCmDistributionRecord entity) {

        try {
            entity.setGlChartOfAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropCmDistributionRecord(LocalCmDistributionRecord entity) {

        try {
            entity.setGlChartOfAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApTaxCode(LocalApTaxCode entity) {

        try {
            entity.setGlChartOfAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApTaxCode(LocalApTaxCode entity) {

        try {
            entity.setGlChartOfAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApWithholdingTaxCode(LocalApWithholdingTaxCode entity) {

        try {
            entity.setGlChartOfAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApWithholdingTaxCode(LocalApWithholdingTaxCode entity) {

        try {
            entity.setGlChartOfAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addApDistributionRecord(LocalApDistributionRecord entity) {

        try {
            entity.setGlChartOfAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApDistributionRecord(LocalApDistributionRecord entity) {

        try {
            entity.setGlChartOfAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArTaxCode(LocalArTaxCode entity) {

        try {
            entity.setGlChartOfAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArTaxCode(LocalArTaxCode entity) {

        try {
            entity.setGlChartOfAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArWithholdingTaxCode(LocalArWithholdingTaxCode entity) {

        try {
            entity.setGlChartOfAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArWithholdingTaxCode(LocalArWithholdingTaxCode entity) {

        try {
            entity.setGlChartOfAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArDistributionRecord(LocalArDistributionRecord entity) {

        try {
            entity.setGlChartOfAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArDistributionRecord(LocalArDistributionRecord entity) {

        try {
            entity.setGlChartOfAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addArStandardMemoLine(LocalArStandardMemoLine entity) {

        try {
            entity.setGlChartOfAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArStandardMemoLine(LocalArStandardMemoLine entity) {

        try {
            entity.setGlChartOfAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdApprovalCoaLine(LocalAdApprovalCoaLine entity) {

        try {
            entity.setGlChartOfAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdApprovalCoaLine(LocalAdApprovalCoaLine entity) {

        try {
            entity.setGlChartOfAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addGlBudgetAmountCoa(LocalGlBudgetAmountCoa entity) {

        try {
            entity.setGlChartOfAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlBudgetAmountCoa(LocalGlBudgetAmountCoa entity) {

        try {
            entity.setGlChartOfAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addInvAdjustment(LocalInvAdjustment entity) {

        try {
            entity.setGlChartOfAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvAdjustment(LocalInvAdjustment entity) {

        try {
            entity.setGlChartOfAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addInvDistributionRecord(LocalInvDistributionRecord entity) {

        try {
            entity.setInvChartOfAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropInvDistributionRecord(LocalInvDistributionRecord entity) {

        try {
            entity.setInvChartOfAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdPaymentTerm(LocalAdPaymentTerm entity) {

        try {
            entity.setGlChartOfAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdPaymentTerm(LocalAdPaymentTerm entity) {

        try {
            entity.setGlChartOfAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdBranchCoa(LocalAdBranchCoa entity) {

        try {
            entity.setGlChartOfAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBranchCoa(LocalAdBranchCoa entity) {

        try {
            entity.setGlChartOfAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addGlForexLedger(LocalGlForexLedger entity) {

        try {
            entity.setGlChartOfAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlForexLedger(LocalGlForexLedger entity) {

        try {
            entity.setGlChartOfAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addAdBranch(LocalAdBranch entity) {

        try {
            entity.setGlChartOfAccount(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropAdBranch(LocalAdBranch entity) {

        try {
            entity.setGlChartOfAccount(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
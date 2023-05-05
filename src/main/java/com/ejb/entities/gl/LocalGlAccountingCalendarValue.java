package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "GlAccountingCalendarValue")
@Table(name = "GL_ACCNTNG_CLNDR_VL")
public class LocalGlAccountingCalendarValue extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACV_CODE", nullable = false)
    private Integer acvCode;

    @Column(name = "ACV_PRD_PRFX", columnDefinition = "VARCHAR")
    private String acvPeriodPrefix;

    @Column(name = "ACV_QRTR", columnDefinition = "SMALLINT")
    private short acvQuarter;

    @Column(name = "ACV_PRD_NMBR", columnDefinition = "SMALLINT")
    private short acvPeriodNumber;

    @Column(name = "ACV_DT_FRM", columnDefinition = "DATETIME")
    private Date acvDateFrom;

    @Column(name = "ACV_DT_TO", columnDefinition = "SMALLINT")
    private Date acvDateTo;

    @Column(name = "ACV_STATUS", columnDefinition = "VARCHAR")
    private char acvStatus;

    @Column(name = "ACV_DT_OPND", columnDefinition = "DATETIME")
    private Date acvDateOpened;

    @Column(name = "ACV_DT_CLSD", columnDefinition = "DATETIME")
    private Date acvDateClosed;

    @Column(name = "ACV_DT_PRMNNTLY_CLSD", columnDefinition = "DATETIME")
    private Date acvDatePermanentlyClosed;

    @Column(name = "ACV_DT_FTR_ENTRD", columnDefinition = "DATETIME")
    private Date acvDateFutureEntered;

    @Column(name = "ACV_AD_CMPNY", columnDefinition = "INT")
    private Integer acvAdCompany;

    @JoinColumn(name = "GL_ACCOUNTING_CALENDAR", referencedColumnName = "GL_AC_CODE")
    @ManyToOne
    private LocalGlAccountingCalendar glAccountingCalendar;

    @OneToMany(mappedBy = "glAccountingCalendarValue", fetch = FetchType.LAZY)
    private List<LocalGlChartOfAccountBalance> glChartOfAccountBalances;

    @OneToMany(mappedBy = "glAccountingCalendarValue", fetch = FetchType.LAZY)
    private List<LocalGlInvestorAccountBalance> glInvestorAccountBalances;

    @OneToMany(mappedBy = "glAccountingCalendarValue", fetch = FetchType.LAZY)
    private List<LocalGlBudgetAmountPeriod> glBudgetAmountPeriods;

    public Integer getAcvCode() {

        return acvCode;
    }

    public void setAcvCode(Integer ACV_CODE) {

        this.acvCode = ACV_CODE;
    }

    public String getAcvPeriodPrefix() {

        return acvPeriodPrefix;
    }

    public void setAcvPeriodPrefix(String ACV_PRD_PRFX) {

        this.acvPeriodPrefix = ACV_PRD_PRFX;
    }

    public short getAcvQuarter() {

        return acvQuarter;
    }

    public void setAcvQuarter(short ACV_QRTR) {

        this.acvQuarter = ACV_QRTR;
    }

    public short getAcvPeriodNumber() {

        return acvPeriodNumber;
    }

    public void setAcvPeriodNumber(short ACV_PRD_NMBR) {

        this.acvPeriodNumber = ACV_PRD_NMBR;
    }

    public Date getAcvDateFrom() {

        return acvDateFrom;
    }

    public void setAcvDateFrom(Date ACV_DT_FRM) {

        this.acvDateFrom = ACV_DT_FRM;
    }

    public Date getAcvDateTo() {

        return acvDateTo;
    }

    public void setAcvDateTo(Date ACV_DT_TO) {

        this.acvDateTo = ACV_DT_TO;
    }

    public char getAcvStatus() {

        return acvStatus;
    }

    public void setAcvStatus(char ACV_STATUS) {

        this.acvStatus = ACV_STATUS;
    }

    public Date getAcvDateOpened() {

        return acvDateOpened;
    }

    public void setAcvDateOpened(Date ACV_DT_OPND) {

        this.acvDateOpened = ACV_DT_OPND;
    }

    public Date getAcvDateClosed() {

        return acvDateClosed;
    }

    public void setAcvDateClosed(Date ACV_DT_CLSD) {

        this.acvDateClosed = ACV_DT_CLSD;
    }

    public Date getAcvDatePermanentlyClosed() {

        return acvDatePermanentlyClosed;
    }

    public void setAcvDatePermanentlyClosed(Date ACV_DT_PRMNNTLY_CLSD) {

        this.acvDatePermanentlyClosed = ACV_DT_PRMNNTLY_CLSD;
    }

    public Date getAcvDateFutureEntered() {

        return acvDateFutureEntered;
    }

    public void setAcvDateFutureEntered(Date ACV_DT_FTR_ENTRD) {

        this.acvDateFutureEntered = ACV_DT_FTR_ENTRD;
    }

    public Integer getAcvAdCompany() {

        return acvAdCompany;
    }

    public void setAcvAdCompany(Integer ACV_AD_CMPNY) {

        this.acvAdCompany = ACV_AD_CMPNY;
    }

    public LocalGlAccountingCalendar getGlAccountingCalendar() {

        return glAccountingCalendar;
    }

    public void setGlAccountingCalendar(LocalGlAccountingCalendar glAccountingCalendar) {

        this.glAccountingCalendar = glAccountingCalendar;
    }

    @XmlTransient
    public List getGlChartOfAccountBalances() {

        return glChartOfAccountBalances;
    }

    public void setGlChartOfAccountBalances(List glChartOfAccountBalances) {

        this.glChartOfAccountBalances = glChartOfAccountBalances;
    }

    @XmlTransient
    public List getGlInvestorAccountBalances() {

        return glInvestorAccountBalances;
    }

    public void setGlInvestorAccountBalances(List glInvestorAccountBalances) {

        this.glInvestorAccountBalances = glInvestorAccountBalances;
    }

    @XmlTransient
    public List getGlBudgetAmountPeriods() {

        return glBudgetAmountPeriods;
    }

    public void setGlBudgetAmountPeriods(List glBudgetAmountPeriods) {

        this.glBudgetAmountPeriods = glBudgetAmountPeriods;
    }

    public void addGlChartOfAccountBalance(LocalGlChartOfAccountBalance entity) {

        try {
            entity.setGlAccountingCalendarValue(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlChartOfAccountBalance(LocalGlChartOfAccountBalance entity) {

        try {
            entity.setGlAccountingCalendarValue(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addGlInvestorAccountBalance(LocalGlInvestorAccountBalance entity) {

        try {
            entity.setGlAccountingCalendarValue(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlInvestorAccountBalance(LocalGlInvestorAccountBalance entity) {

        try {
            entity.setGlAccountingCalendarValue(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addGlBudgetAmountPeriod(LocalGlBudgetAmountPeriod entity) {

        try {
            entity.setGlAccountingCalendarValue(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlBudgetAmountPeriod(LocalGlBudgetAmountPeriod entity) {

        try {
            entity.setGlAccountingCalendarValue(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
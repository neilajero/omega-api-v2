package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "GlBudgetAmountPeriod")
@Table(name = "GL_BDGT_AMNT_PRD")
public class LocalGlBudgetAmountPeriod extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BAP_CODE", nullable = false)
    private Integer bapCode;

    @Column(name = "BAP_AMNT", columnDefinition = "DOUBLE")
    private double bapAmount = 0;

    @Column(name = "BAP_AD_CMPNY", columnDefinition = "INT")
    private Integer bapAdCompany;

    @JoinColumn(name = "GL_ACCOUNTING_CALENDAR_VALUE", referencedColumnName = "ACV_CODE")
    @ManyToOne
    private LocalGlAccountingCalendarValue glAccountingCalendarValue;

    @JoinColumn(name = "GL_BUDGET_AMOUNT_COA", referencedColumnName = "BC_CODE")
    @ManyToOne
    private LocalGlBudgetAmountCoa glBudgetAmountCoa;

    public Integer getBapCode() {

        return bapCode;
    }

    public void setBapCode(Integer BAP_CODE) {

        this.bapCode = BAP_CODE;
    }

    public double getBapAmount() {

        return bapAmount;
    }

    public void setBapAmount(double BAP_AMNT) {

        this.bapAmount = BAP_AMNT;
    }

    public Integer getBapAdCompany() {

        return bapAdCompany;
    }

    public void setBapAdCompany(Integer BAP_AD_CMPNY) {

        this.bapAdCompany = BAP_AD_CMPNY;
    }

    public LocalGlAccountingCalendarValue getGlAccountingCalendarValue() {

        return glAccountingCalendarValue;
    }

    public void setGlAccountingCalendarValue(
            LocalGlAccountingCalendarValue glAccountingCalendarValue) {

        this.glAccountingCalendarValue = glAccountingCalendarValue;
    }

    public LocalGlBudgetAmountCoa getGlBudgetAmountCoa() {

        return glBudgetAmountCoa;
    }

    public void setGlBudgetAmountCoa(LocalGlBudgetAmountCoa glBudgetAmountCoa) {

        this.glBudgetAmountCoa = glBudgetAmountCoa;
    }

}
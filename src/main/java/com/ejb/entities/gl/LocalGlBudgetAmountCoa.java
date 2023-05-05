package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "GlBudgetAmountCoa")
@Table(name = "GL_BDGT_AMNT_COA")
public class LocalGlBudgetAmountCoa extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BC_CODE", nullable = false)
    private Integer bcCode;

    @Column(name = "BC_RL_AMNT", columnDefinition = "DOUBLE")
    private double bcRuleAmount = 0;

    @Column(name = "BC_RL_PRCNTG1", columnDefinition = "DOUBLE")
    private double bcRulePercentage1 = 0;

    @Column(name = "BC_RL_PRCNTG2", columnDefinition = "DOUBLE")
    private double bcRulePercentage2 = 0;

    @Column(name = "BC_DESC", columnDefinition = "VARCHAR")
    private String bcDescription;

    @Column(name = "BC_RL_TYP", columnDefinition = "VARCHAR")
    private String bcRuleType;

    @Column(name = "BC_AD_CMPNY", columnDefinition = "INT")
    private Integer bcAdCompany;

    @JoinColumn(name = "GL_BUDGET_AMOUNT", referencedColumnName = "BGA_CODE")
    @ManyToOne
    private LocalGlBudgetAmount glBudgetAmount;

    @JoinColumn(name = "GL_CHART_OF_ACCOUNT", referencedColumnName = "COA_CODE")
    @ManyToOne
    private LocalGlChartOfAccount glChartOfAccount;

    @OneToMany(mappedBy = "glBudgetAmountCoa", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalGlBudgetAmountPeriod> glBudgetAmountPeriods;

    public Integer getBcCode() {

        return bcCode;
    }

    public void setBcCode(Integer BC_CODE) {

        this.bcCode = BC_CODE;
    }

    public double getBcRuleAmount() {

        return bcRuleAmount;
    }

    public void setBcRuleAmount(double BC_RL_AMNT) {

        this.bcRuleAmount = BC_RL_AMNT;
    }

    public double getBcRulePercentage1() {

        return bcRulePercentage1;
    }

    public void setBcRulePercentage1(double BC_RL_PRCNTG1) {

        this.bcRulePercentage1 = BC_RL_PRCNTG1;
    }

    public double getBcRulePercentage2() {

        return bcRulePercentage2;
    }

    public void setBcRulePercentage2(double BC_RL_PRCNTG2) {

        this.bcRulePercentage2 = BC_RL_PRCNTG2;
    }

    public String getBcDescription() {

        return bcDescription;
    }

    public void setBcDescription(String BC_DESC) {

        this.bcDescription = BC_DESC;
    }

    public String getBcRuleType() {

        return bcRuleType;
    }

    public void setBcRuleType(String BC_RL_TYP) {

        this.bcRuleType = BC_RL_TYP;
    }

    public Integer getBcAdCompany() {

        return bcAdCompany;
    }

    public void setBcAdCompany(Integer BC_AD_CMPNY) {

        this.bcAdCompany = BC_AD_CMPNY;
    }

    public LocalGlBudgetAmount getGlBudgetAmount() {

        return glBudgetAmount;
    }

    public void setGlBudgetAmount(LocalGlBudgetAmount glBudgetAmount) {

        this.glBudgetAmount = glBudgetAmount;
    }

    public LocalGlChartOfAccount getGlChartOfAccount() {

        return glChartOfAccount;
    }

    public void setGlChartOfAccount(LocalGlChartOfAccount glChartOfAccount) {

        this.glChartOfAccount = glChartOfAccount;
    }

    @XmlTransient
    public List getGlBudgetAmountPeriods() {

        return glBudgetAmountPeriods;
    }

    public void setGlBudgetAmountPeriods(List glBudgetAmountPeriods) {

        this.glBudgetAmountPeriods = glBudgetAmountPeriods;
    }

    public void addGlBudgetAmountPeriod(LocalGlBudgetAmountPeriod entity) {

        try {
            entity.setGlBudgetAmountCoa(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGlBudgetAmountPeriod(LocalGlBudgetAmountPeriod entity) {

        try {
            entity.setGlBudgetAmountCoa(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
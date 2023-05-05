package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "AdStoredProcedure")
@Table(name = "AD_STRD_PRC")
public class LocalAdStoredProcedure extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SP_CODE", nullable = false)
    private Integer spCode;

    @Column(name = "SP_ENBL_GL_RPT", columnDefinition = "TINYINT")
    private byte spEnableGlGeneralLedgerReport;

    @Column(name = "SP_NM_GL_RPT", columnDefinition = "VARCHAR")
    private String spNameGlGeneralLedgerReport;

    @Column(name = "SP_ENBL_TB_RPT", columnDefinition = "TINYINT")
    private byte spEnableGlTrialBalanceReport;

    @Column(name = "SP_NM_TB_RPT", columnDefinition = "VARCHAR")
    private String spNameGlTrialBalanceReport;

    @Column(name = "SP_ENBL_IS_RPT", columnDefinition = "TINYINT")
    private byte spEnableGlIncomeStatementReport;

    @Column(name = "SP_NM_IS_RPT", columnDefinition = "VARCHAR")
    private String spNameGlIncomeStatementReport;

    @Column(name = "SP_ENBL_BS_RPT", columnDefinition = "TINYINT")
    private byte spEnableGlBalanceSheetReport;

    @Column(name = "SP_NM_BS_RPT", columnDefinition = "VARCHAR")
    private String spNameGlBalanceSheetReport;

    @Column(name = "SP_ENBL_SOA_RPT", columnDefinition = "TINYINT")
    private byte spEnableArStatementOfAccountReport;

    @Column(name = "SP_NM_SOA_RPT", columnDefinition = "VARCHAR")
    private String spNameArStatementOfAccountReport;

    @Column(name = "SP_AD_CMPNY", columnDefinition = "INT")
    private Integer spAdCompany;

    public Integer getSpCode() {

        return spCode;
    }

    public void setSpCode(Integer SP_CODE) {

        this.spCode = SP_CODE;
    }

    public byte getSpEnableGlGeneralLedgerReport() {

        return spEnableGlGeneralLedgerReport;
    }

    public void setSpEnableGlGeneralLedgerReport(byte SP_ENBL_GL_RPT) {

        this.spEnableGlGeneralLedgerReport = SP_ENBL_GL_RPT;
    }

    public String getSpNameGlGeneralLedgerReport() {

        return spNameGlGeneralLedgerReport;
    }

    public void setSpNameGlGeneralLedgerReport(String SP_NM_GL_RPT) {

        this.spNameGlGeneralLedgerReport = SP_NM_GL_RPT;
    }

    public byte getSpEnableGlTrialBalanceReport() {

        return spEnableGlTrialBalanceReport;
    }

    public void setSpEnableGlTrialBalanceReport(byte SP_ENBL_TB_RPT) {

        this.spEnableGlTrialBalanceReport = SP_ENBL_TB_RPT;
    }

    public String getSpNameGlTrialBalanceReport() {

        return spNameGlTrialBalanceReport;
    }

    public void setSpNameGlTrialBalanceReport(String SP_NM_TB_RPT) {

        this.spNameGlTrialBalanceReport = SP_NM_TB_RPT;
    }

    public byte getSpEnableGlIncomeStatementReport() {

        return spEnableGlIncomeStatementReport;
    }

    public void setSpEnableGlIncomeStatementReport(byte SP_ENBL_IS_RPT) {

        this.spEnableGlIncomeStatementReport = SP_ENBL_IS_RPT;
    }

    public String getSpNameGlIncomeStatementReport() {

        return spNameGlIncomeStatementReport;
    }

    public void setSpNameGlIncomeStatementReport(String SP_NM_IS_RPT) {

        this.spNameGlIncomeStatementReport = SP_NM_IS_RPT;
    }

    public byte getSpEnableGlBalanceSheetReport() {

        return spEnableGlBalanceSheetReport;
    }

    public void setSpEnableGlBalanceSheetReport(byte SP_ENBL_BS_RPT) {

        this.spEnableGlBalanceSheetReport = SP_ENBL_BS_RPT;
    }

    public String getSpNameGlBalanceSheetReport() {

        return spNameGlBalanceSheetReport;
    }

    public void setSpNameGlBalanceSheetReport(String SP_NM_BS_RPT) {

        this.spNameGlBalanceSheetReport = SP_NM_BS_RPT;
    }

    public byte getSpEnableArStatementOfAccountReport() {

        return spEnableArStatementOfAccountReport;
    }

    public void setSpEnableArStatementOfAccountReport(byte SP_ENBL_SOA_RPT) {

        this.spEnableArStatementOfAccountReport = SP_ENBL_SOA_RPT;
    }

    public String getSpNameArStatementOfAccountReport() {

        return spNameArStatementOfAccountReport;
    }

    public void setSpNameArStatementOfAccountReport(String SP_NM_SOA_RPT) {

        this.spNameArStatementOfAccountReport = SP_NM_SOA_RPT;
    }

    public Integer getSpAdCompany() {

        return spAdCompany;
    }

    public void setSpAdCompany(Integer SP_AD_CMPNY) {

        this.spAdCompany = SP_AD_CMPNY;
    }

}
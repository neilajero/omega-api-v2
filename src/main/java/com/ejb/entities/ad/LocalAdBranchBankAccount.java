package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity(name = "AdBranchBankAccount")
@Table(name = "AD_BR_BA")
public class LocalAdBranchBankAccount extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BBA_CODE", nullable = false)
    private Integer bbaCode;

    @Column(name = "BBA_COA_GL_CSH_ACCNT", columnDefinition = "INT")
    private Integer bbaGlCoaCashAccount;

    @Column(name = "BBA_COA_GL_BNK_CHRG_ACCNT", columnDefinition = "INT")
    private Integer bbaGlCoaBankChargeAccount;

    @Column(name = "BBA_COA_GL_INTRST_ACCNT", columnDefinition = "INT")
    private Integer bbaGlCoaInterestAccount;

    @Column(name = "BBA_COA_GL_ADJSTMNT_ACCNT", columnDefinition = "INT")
    private Integer bbaGlCoaAdjustmentAccount;

    @Column(name = "BBA_COA_GL_SLS_DSCNT_ACCNT", columnDefinition = "INT")
    private Integer bbaGlCoaSalesDiscountAccount;

    @Column(name = "BBA_COA_GL_ADVNC_ACCNT", columnDefinition = "INT")
    private Integer bbaGlCoaAdvanceAccount;

    @Column(name = "BBA_DWNLD_STATUS", columnDefinition = "VARCHAR")
    private char bbaDownloadStatus;

    @Column(name = "BBA_AD_CMPNY", columnDefinition = "INT")
    private Integer bbaAdCompany;

    @JoinColumn(name = "AD_BANK_ACCOUNT", referencedColumnName = "BA_CODE")
    @ManyToOne
    private LocalAdBankAccount adBankAccount;

    @JoinColumn(name = "AD_BRANCH", referencedColumnName = "BR_CODE")
    @ManyToOne
    private LocalAdBranch adBranch;

    public Integer getBbaCode() {

        return bbaCode;
    }

    public void setBbaCode(Integer BBA_CODE) {

        this.bbaCode = BBA_CODE;
    }

    public Integer getBbaGlCoaCashAccount() {

        return bbaGlCoaCashAccount;
    }

    public void setBbaGlCoaCashAccount(Integer BBA_COA_GL_CSH_ACCNT) {

        this.bbaGlCoaCashAccount = BBA_COA_GL_CSH_ACCNT;
    }

    public Integer getBbaGlCoaBankChargeAccount() {

        return bbaGlCoaBankChargeAccount;
    }

    public void setBbaGlCoaBankChargeAccount(Integer BBA_COA_GL_BNK_CHRG_ACCNT) {

        this.bbaGlCoaBankChargeAccount = BBA_COA_GL_BNK_CHRG_ACCNT;
    }

    public Integer getBbaGlCoaInterestAccount() {

        return bbaGlCoaInterestAccount;
    }

    public void setBbaGlCoaInterestAccount(Integer BBA_COA_GL_INTRST_ACCNT) {

        this.bbaGlCoaInterestAccount = BBA_COA_GL_INTRST_ACCNT;
    }

    public Integer getBbaGlCoaAdjustmentAccount() {

        return bbaGlCoaAdjustmentAccount;
    }

    public void setBbaGlCoaAdjustmentAccount(Integer BBA_COA_GL_ADJSTMNT_ACCNT) {

        this.bbaGlCoaAdjustmentAccount = BBA_COA_GL_ADJSTMNT_ACCNT;
    }

    public Integer getBbaGlCoaSalesDiscountAccount() {

        return bbaGlCoaSalesDiscountAccount;
    }

    public void setBbaGlCoaSalesDiscountAccount(Integer BBA_COA_GL_SLS_DSCNT_ACCNT) {

        this.bbaGlCoaSalesDiscountAccount = BBA_COA_GL_SLS_DSCNT_ACCNT;
    }

    public Integer getBbaGlCoaAdvanceAccount() {

        return bbaGlCoaAdvanceAccount;
    }

    public void setBbaGlCoaAdvanceAccount(Integer BBA_COA_GL_ADVNC_ACCNT) {

        this.bbaGlCoaAdvanceAccount = BBA_COA_GL_ADVNC_ACCNT;
    }

    public char getBbaDownloadStatus() {

        return bbaDownloadStatus;
    }

    public void setBbaDownloadStatus(char BBA_DWNLD_STATUS) {

        this.bbaDownloadStatus = BBA_DWNLD_STATUS;
    }

    public Integer getBbaAdCompany() {

        return bbaAdCompany;
    }

    public void setBbaAdCompany(Integer BBA_AD_CMPNY) {

        this.bbaAdCompany = BBA_AD_CMPNY;
    }

    public LocalAdBankAccount getAdBankAccount() {

        return adBankAccount;
    }

    public void setAdBankAccount(LocalAdBankAccount adBankAccount) {

        this.adBankAccount = adBankAccount;
    }

    public LocalAdBranch getAdBranch() {

        return adBranch;
    }

    public void setAdBranch(LocalAdBranch adBranch) {

        this.adBranch = adBranch;
    }

}
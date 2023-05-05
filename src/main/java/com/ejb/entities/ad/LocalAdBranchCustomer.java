package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ar.LocalArCustomer;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "AdBranchCustomer")
@Table(name = "AD_BR_CST")
public class LocalAdBranchCustomer extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BCST_CODE", nullable = false)
    private Integer bcstCode;

    @Column(name = "BCST_GL_COA_RCVBL_ACCNT", columnDefinition = "INT")
    private Integer bcstGlCoaReceivableAccount;

    @Column(name = "BCST_GL_COA_RVN_ACCNT", columnDefinition = "INT")
    private Integer bcstGlCoaRevenueAccount;

    @Column(name = "BCST_GL_COA_UNERND_INT_ACCNT", columnDefinition = "INT")
    private Integer bcstGlCoaUnEarnedInterestAccount;

    @Column(name = "BCST_GL_COA_ERND_INT_ACCNT", columnDefinition = "INT")
    private Integer bcstGlCoaEarnedInterestAccount;

    @Column(name = "BCST_GL_COA_UNERND_PNT_ACCNT", columnDefinition = "INT")
    private Integer bcstGlCoaUnEarnedPenaltyAccount;

    @Column(name = "BCST_GL_COA_ERND_PNT_ACCNT", columnDefinition = "INT")
    private Integer bcstGlCoaEarnedPenaltyAccount;

    @Column(name = "BCST_DS_CSTMR", columnDefinition = "VARCHAR")
    private char bcstCustomerDownloadStatus;

    @Column(name = "BCST_AD_CMPNY", columnDefinition = "INT")
    private Integer bcstAdCompany;

    @JoinColumn(name = "AD_BRANCH", referencedColumnName = "BR_CODE")
    @ManyToOne
    private LocalAdBranch adBranch;

    @JoinColumn(name = "AR_CUSTOMER", referencedColumnName = "AR_CST_CODE")
    @ManyToOne
    private LocalArCustomer arCustomer;

    public Integer getBcstCode() {

        return bcstCode;
    }

    public void setBcstCode(Integer BCST_CODE) {

        this.bcstCode = BCST_CODE;
    }

    public Integer getBcstGlCoaReceivableAccount() {

        return bcstGlCoaReceivableAccount;
    }

    public void setBcstGlCoaReceivableAccount(Integer BCST_GL_COA_RCVBL_ACCNT) {

        this.bcstGlCoaReceivableAccount = BCST_GL_COA_RCVBL_ACCNT;
    }

    public Integer getBcstGlCoaRevenueAccount() {

        return bcstGlCoaRevenueAccount;
    }

    public void setBcstGlCoaRevenueAccount(Integer BCST_GL_COA_RVN_ACCNT) {

        this.bcstGlCoaRevenueAccount = BCST_GL_COA_RVN_ACCNT;
    }

    public Integer getBcstGlCoaUnEarnedInterestAccount() {

        return bcstGlCoaUnEarnedInterestAccount;
    }

    public void setBcstGlCoaUnEarnedInterestAccount(Integer BCST_GL_COA_UNERND_INT_ACCNT) {

        this.bcstGlCoaUnEarnedInterestAccount = BCST_GL_COA_UNERND_INT_ACCNT;
    }

    public Integer getBcstGlCoaEarnedInterestAccount() {

        return bcstGlCoaEarnedInterestAccount;
    }

    public void setBcstGlCoaEarnedInterestAccount(Integer BCST_GL_COA_ERND_INT_ACCNT) {

        this.bcstGlCoaEarnedInterestAccount = BCST_GL_COA_ERND_INT_ACCNT;
    }

    public Integer getBcstGlCoaUnEarnedPenaltyAccount() {

        return bcstGlCoaUnEarnedPenaltyAccount;
    }

    public void setBcstGlCoaUnEarnedPenaltyAccount(Integer BCST_GL_COA_UNERND_PNT_ACCNT) {

        this.bcstGlCoaUnEarnedPenaltyAccount = BCST_GL_COA_UNERND_PNT_ACCNT;
    }

    public Integer getBcstGlCoaEarnedPenaltyAccount() {

        return bcstGlCoaEarnedPenaltyAccount;
    }

    public void setBcstGlCoaEarnedPenaltyAccount(Integer BCST_GL_COA_ERND_PNT_ACCNT) {

        this.bcstGlCoaEarnedPenaltyAccount = BCST_GL_COA_ERND_PNT_ACCNT;
    }

    public char getBcstCustomerDownloadStatus() {

        return bcstCustomerDownloadStatus;
    }

    public void setBcstCustomerDownloadStatus(char BCST_DS_CSTMR) {

        this.bcstCustomerDownloadStatus = BCST_DS_CSTMR;
    }

    public Integer getBcstAdCompany() {

        return bcstAdCompany;
    }

    public void setBcstAdCompany(Integer BCST_AD_CMPNY) {

        this.bcstAdCompany = BCST_AD_CMPNY;
    }

    public LocalAdBranch getAdBranch() {

        return adBranch;
    }

    public void setAdBranch(LocalAdBranch adBranch) {

        this.adBranch = adBranch;
    }

    public LocalArCustomer getArCustomer() {

        return arCustomer;
    }

    public void setArCustomer(LocalArCustomer arCustomer) {

        this.arCustomer = arCustomer;
    }

}
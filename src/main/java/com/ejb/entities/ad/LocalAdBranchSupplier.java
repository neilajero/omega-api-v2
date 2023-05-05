package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ap.LocalApSupplier;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "AdBranchSupplier")
@Table(name = "AD_BR_SPL")
public class LocalAdBranchSupplier extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BSPL_CODE", nullable = false)
    private Integer bsplCode;

    @Column(name = "BSPL_GL_COA_PYBL_ACCNT", columnDefinition = "INT")
    private Integer bsplGlCoaPayableAccount;

    @Column(name = "BSPL_GL_COA_EXPNS_ACCNT", columnDefinition = "INT")
    private Integer bsplGlCoaExpenseAccount;

    @Column(name = "BSPL_DWNLD_STATUS", columnDefinition = "VARCHAR")
    private char bsplSupplierDownloadStatus;

    @Column(name = "BSPL_AD_CMPNY", columnDefinition = "INT")
    private Integer bsplAdCompany;

    @JoinColumn(name = "AD_BRANCH", referencedColumnName = "BR_CODE")
    @ManyToOne
    private LocalAdBranch adBranch;

    @JoinColumn(name = "AP_SUPPLIER", referencedColumnName = "SPL_CODE")
    @ManyToOne
    private LocalApSupplier apSupplier;

    public Integer getBsplCode() {

        return bsplCode;
    }

    public void setBsplCode(Integer BSPL_CODE) {

        this.bsplCode = BSPL_CODE;
    }

    public Integer getBsplGlCoaPayableAccount() {

        return bsplGlCoaPayableAccount;
    }

    public void setBsplGlCoaPayableAccount(Integer BSPL_GL_COA_PYBL_ACCNT) {

        this.bsplGlCoaPayableAccount = BSPL_GL_COA_PYBL_ACCNT;
    }

    public Integer getBsplGlCoaExpenseAccount() {

        return bsplGlCoaExpenseAccount;
    }

    public void setBsplGlCoaExpenseAccount(Integer BSPL_GL_COA_EXPNS_ACCNT) {

        this.bsplGlCoaExpenseAccount = BSPL_GL_COA_EXPNS_ACCNT;
    }

    public char getBsplSupplierDownloadStatus() {

        return bsplSupplierDownloadStatus;
    }

    public void setBsplSupplierDownloadStatus(char BSPL_DWNLD_STATUS) {

        this.bsplSupplierDownloadStatus = BSPL_DWNLD_STATUS;
    }

    public Integer getBsplAdCompany() {

        return bsplAdCompany;
    }

    public void setBsplAdCompany(Integer BSPL_AD_CMPNY) {

        this.bsplAdCompany = BSPL_AD_CMPNY;
    }

    public LocalAdBranch getAdBranch() {

        return adBranch;
    }

    public void setAdBranch(LocalAdBranch adBranch) {

        this.adBranch = adBranch;
    }

    public LocalApSupplier getApSupplier() {

        return apSupplier;
    }

    public void setApSupplier(LocalApSupplier apSupplier) {

        this.apSupplier = apSupplier;
    }

}
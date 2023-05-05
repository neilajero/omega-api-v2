package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ar.LocalArStandardMemoLine;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "AdBranchStandardMemoLine")
@Table(name = "AD_BR_SML")
public class LocalAdBranchStandardMemoLine extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BSML_CODE", nullable = false)
    private Integer bsmlCode;

    @Column(name = "BSML_GL_ACCNT", columnDefinition = "INT")
    private Integer bsmlGlAccount;

    @Column(name = "BSML_GL_COA_RCVBL_ACCNT", columnDefinition = "INT")
    private Integer bsmlGlCoaReceivableAccount;

    @Column(name = "BSML_GL_COA_RVNUE_ACCNT", columnDefinition = "INT")
    private Integer bsmlGlCoaRevenueAccount;

    @Column(name = "BSML_SBJCT_TO_CMMSSN", columnDefinition = "TINYINT")
    private byte bsmlSubjectToCommission;

    @Column(name = "BSML_AD_CMPNY", columnDefinition = "INT")
    private Integer bsmlAdCompany;

    @Column(name = "BSML_DWNLD_STATUS", columnDefinition = "VARCHAR")
    private char bsmlStandardMemoLineDownloadStatus;

    @JoinColumn(name = "AD_BRANCH", referencedColumnName = "BR_CODE")
    @ManyToOne
    private LocalAdBranch adBranch;

    @JoinColumn(name = "AR_STANDARD_MEMO_LINE", referencedColumnName = "SML_CODE")
    @ManyToOne
    private LocalArStandardMemoLine arStandardMemoLine;

    public Integer getBsmlCode() {

        return bsmlCode;
    }

    public void setBsmlCode(Integer BSML_CODE) {

        this.bsmlCode = BSML_CODE;
    }

    public Integer getBsmlGlAccount() {

        return bsmlGlAccount;
    }

    public void setBsmlGlAccount(Integer BSML_GL_ACCNT) {

        this.bsmlGlAccount = BSML_GL_ACCNT;
    }

    public Integer getBsmlGlCoaReceivableAccount() {

        return bsmlGlCoaReceivableAccount;
    }

    public void setBsmlGlCoaReceivableAccount(Integer BSML_GL_COA_RCVBL_ACCNT) {

        this.bsmlGlCoaReceivableAccount = BSML_GL_COA_RCVBL_ACCNT;
    }

    public Integer getBsmlGlCoaRevenueAccount() {

        return bsmlGlCoaRevenueAccount;
    }

    public void setBsmlGlCoaRevenueAccount(Integer BSML_GL_COA_RVNUE_ACCNT) {

        this.bsmlGlCoaRevenueAccount = BSML_GL_COA_RVNUE_ACCNT;
    }

    public byte getBsmlSubjectToCommission() {

        return bsmlSubjectToCommission;
    }

    public void setBsmlSubjectToCommission(byte BSML_SBJCT_TO_CMMSSN) {

        this.bsmlSubjectToCommission = BSML_SBJCT_TO_CMMSSN;
    }

    public Integer getBsmlAdCompany() {

        return bsmlAdCompany;
    }

    public void setBsmlAdCompany(Integer BSML_AD_CMPNY) {

        this.bsmlAdCompany = BSML_AD_CMPNY;
    }

    public char getBsmlStandardMemoLineDownloadStatus() {

        return bsmlStandardMemoLineDownloadStatus;
    }

    public void setBsmlStandardMemoLineDownloadStatus(char BSML_DWNLD_STATUS) {

        this.bsmlStandardMemoLineDownloadStatus = BSML_DWNLD_STATUS;
    }

    public LocalAdBranch getAdBranch() {

        return adBranch;
    }

    public void setAdBranch(LocalAdBranch adBranch) {

        this.adBranch = adBranch;
    }

    public LocalArStandardMemoLine getArStandardMemoLine() {

        return arStandardMemoLine;
    }

    public void setArStandardMemoLine(LocalArStandardMemoLine arStandardMemoLine) {

        this.arStandardMemoLine = arStandardMemoLine;
    }

}
package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "AdApprovalQueue")
@Table(name = "AD_APPRVL_QUEUE")
public class LocalAdApprovalQueue extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AQ_CODE", nullable = false)
    private Integer aqCode;

    @Column(name = "AQ_DEPT", columnDefinition = "VARCHAR")
    private String aqDepartment;

    @Column(name = "AQ_LVL", columnDefinition = "VARCHAR")
    private String aqLevel;

    @Column(name = "AQ_FOR_APPRVL", columnDefinition = "TINYINT")
    private byte aqForApproval;

    @Column(name = "AQ_NXT_LVL", columnDefinition = "VARCHAR")
    private String aqNextLevel;

    @Column(name = "AQ_DCMNT", columnDefinition = "VARCHAR")
    private String aqDocument;

    @Column(name = "AQ_DCMNT_CODE", columnDefinition = "INT")
    private Integer aqDocumentCode;

    @Column(name = "AQ_DCMNT_NMBR", columnDefinition = "VARCHAR")
    private String aqDocumentNumber;

    @Column(name = "AQ_DT", columnDefinition = "DATETIME")
    private Date aqDate;

    @Column(name = "AQ_APPRVD_DT", columnDefinition = "DATETIME")
    private Date aqApprovedDate;

    @Column(name = "AQ_AND_OR", columnDefinition = "VARCHAR")
    private String aqAndOr;

    @Column(name = "AQ_USR_OR", columnDefinition = "TINYINT")
    private byte aqUserOr;

    @Column(name = "AQ_EML_SNT", columnDefinition = "TINYINT")
    private byte aqEmailSent;

    @Column(name = "AQ_APPRVD", columnDefinition = "TINYINT")
    private byte aqApproved;

    @Column(name = "AQ_RQSTR_NM", columnDefinition = "VARCHAR")
    private String aqRequesterName;

    @Column(name = "AQ_AD_BRNCH", columnDefinition = "INT")
    private Integer aqAdBranch;

    @Column(name = "AQ_AD_CMPNY", columnDefinition = "INT")
    private Integer aqAdCompany;

    @JoinColumn(name = "AD_USER", referencedColumnName = "USR_CODE")
    @ManyToOne
    private LocalAdUser adUser;

    public Integer getAqCode() {

        return aqCode;
    }

    public void setAqCode(Integer AQ_CODE) {

        this.aqCode = AQ_CODE;
    }

    public String getAqDepartment() {

        return aqDepartment;
    }

    public void setAqDepartment(String AQ_DEPT) {

        this.aqDepartment = AQ_DEPT;
    }

    public String getAqLevel() {

        return aqLevel;
    }

    public void setAqLevel(String AQ_LVL) {

        this.aqLevel = AQ_LVL;
    }

    public byte getAqForApproval() {

        return aqForApproval;
    }

    public void setAqForApproval(byte AQ_FOR_APPRVL) {

        this.aqForApproval = AQ_FOR_APPRVL;
    }

    public String getAqNextLevel() {

        return aqNextLevel;
    }

    public void setAqNextLevel(String AQ_NXT_LVL) {

        this.aqNextLevel = AQ_NXT_LVL;
    }

    public String getAqDocument() {

        return aqDocument;
    }

    public void setAqDocument(String AQ_DCMNT) {

        this.aqDocument = AQ_DCMNT;
    }

    public Integer getAqDocumentCode() {

        return aqDocumentCode;
    }

    public void setAqDocumentCode(Integer AQ_DCMNT_CODE) {

        this.aqDocumentCode = AQ_DCMNT_CODE;
    }

    public String getAqDocumentNumber() {

        return aqDocumentNumber;
    }

    public void setAqDocumentNumber(String AQ_DCMNT_NMBR) {

        this.aqDocumentNumber = AQ_DCMNT_NMBR;
    }

    public Date getAqDate() {

        return aqDate;
    }

    public void setAqDate(Date AQ_DT) {

        this.aqDate = AQ_DT;
    }

    public Date getAqApprovedDate() {

        return aqApprovedDate;
    }

    public void setAqApprovedDate(Date AQ_APPRVD_DT) {

        this.aqApprovedDate = AQ_APPRVD_DT;
    }

    public String getAqAndOr() {

        return aqAndOr;
    }

    public void setAqAndOr(String AQ_AND_OR) {

        this.aqAndOr = AQ_AND_OR;
    }

    public byte getAqUserOr() {

        return aqUserOr;
    }

    public void setAqUserOr(byte AQ_USR_OR) {

        this.aqUserOr = AQ_USR_OR;
    }

    public byte getAqEmailSent() {

        return aqEmailSent;
    }

    public void setAqEmailSent(byte AQ_EML_SNT) {

        this.aqEmailSent = AQ_EML_SNT;
    }

    public byte getAqApproved() {

        return aqApproved;
    }

    public void setAqApproved(byte AQ_APPRVD) {

        this.aqApproved = AQ_APPRVD;
    }

    public String getAqRequesterName() {

        return aqRequesterName;
    }

    public void setAqRequesterName(String AQ_RQSTR_NM) {

        this.aqRequesterName = AQ_RQSTR_NM;
    }

    public Integer getAqAdBranch() {

        return aqAdBranch;
    }

    public void setAqAdBranch(Integer AQ_AD_BRNCH) {

        this.aqAdBranch = AQ_AD_BRNCH;
    }

    public Integer getAqAdCompany() {

        return aqAdCompany;
    }

    public void setAqAdCompany(Integer AQ_AD_CMPNY) {

        this.aqAdCompany = AQ_AD_CMPNY;
    }

    public LocalAdUser getAdUser() {

        return adUser;
    }

    public void setAdUser(LocalAdUser adUser) {

        this.adUser = adUser;
    }

}
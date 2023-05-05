package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "AdDeleteAuditTrail")
@Table(name = "AD_DLT_ADT_TRL")
public class LocalAdDeleteAuditTrail extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DAT_CODE", nullable = false)
    private Integer datCode;

    @Column(name = "DAT_TYP", columnDefinition = "VARCHAR")
    private String datType;

    @Column(name = "DAT_TXN_DT", columnDefinition = "DATETIME")
    private Date datTxnDate;

    @Column(name = "DAT_DCMNT_NMBR", columnDefinition = "VARCHAR")
    private String datDocumentNumber;

    @Column(name = "DAT_RFRNC_NMBR", columnDefinition = "VARCHAR")
    private String datReferenceNumber;

    @Column(name = "DAT_AMNT", columnDefinition = "DOUBLE")
    private double datAmount = 0;

    @Column(name = "DAT_USR", columnDefinition = "VARCHAR")
    private String datUser;

    @Column(name = "DAT_DT", columnDefinition = "DATETIME")
    private Date datDate;

    @Column(name = "DAT_AD_CMPNY", columnDefinition = "INT")
    private Integer datAdCompany;

    public Integer getDatCode() {

        return datCode;
    }

    public void setDatCode(Integer DAT_CODE) {

        this.datCode = DAT_CODE;
    }

    public String getDatType() {

        return datType;
    }

    public void setDatType(String DAT_TYP) {

        this.datType = DAT_TYP;
    }

    public Date getDatTxnDate() {

        return datTxnDate;
    }

    public void setDatTxnDate(Date DAT_TXN_DT) {

        this.datTxnDate = DAT_TXN_DT;
    }

    public String getDatDocumentNumber() {

        return datDocumentNumber;
    }

    public void setDatDocumentNumber(String DAT_DCMNT_NMBR) {

        this.datDocumentNumber = DAT_DCMNT_NMBR;
    }

    public String getDatReferenceNumber() {

        return datReferenceNumber;
    }

    public void setDatReferenceNumber(String DAT_RFRNC_NMBR) {

        this.datReferenceNumber = DAT_RFRNC_NMBR;
    }

    public double getDatAmount() {

        return datAmount;
    }

    public void setDatAmount(double DAT_AMNT) {

        this.datAmount = DAT_AMNT;
    }

    public String getDatUser() {

        return datUser;
    }

    public void setDatUser(String DAT_USR) {

        this.datUser = DAT_USR;
    }

    public Date getDatDate() {

        return datDate;
    }

    public void setDatDate(Date DAT_DT) {

        this.datDate = DAT_DT;
    }

    public Integer getDatAdCompany() {

        return datAdCompany;
    }

    public void setDatAdCompany(Integer DAT_AD_CMPNY) {

        this.datAdCompany = DAT_AD_CMPNY;
    }

}
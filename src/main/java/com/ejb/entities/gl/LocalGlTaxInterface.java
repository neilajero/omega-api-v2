package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "GlTaxInterface")
@Table(name = "GL_TX_INTRFC")
public class LocalGlTaxInterface extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TI_CODE", nullable = false)
    private Integer tiCode;

    @Column(name = "TI_DCMNT_TYP", columnDefinition = "VARCHAR")
    private String tiDocumentType;

    @Column(name = "TI_SRC", columnDefinition = "VARCHAR")
    private String tiSource;

    @Column(name = "TI_NET_AMNT", columnDefinition = "DOUBLE")
    private double tiNetAmount = 0;

    @Column(name = "TI_TX_AMNT", columnDefinition = "DOUBLE")
    private double tiTaxAmount = 0;

    @Column(name = "TI_SLS_AMNT", columnDefinition = "DOUBLE")
    private double tiSalesAmount = 0;

    @Column(name = "TI_SRVCS_AMNT", columnDefinition = "DOUBLE")
    private double tiServicesAmount = 0;

    @Column(name = "TI_CPTL_GDS_AMNT", columnDefinition = "DOUBLE")
    private double tiCapitalGoodsAmount = 0;

    @Column(name = "TI_OTHR_CPTL_GDS_AMNT", columnDefinition = "DOUBLE")
    private double tiOtherCapitalGoodsAmount = 0;

    @Column(name = "TI_TXN_CODE", columnDefinition = "INT")
    private Integer tiTxnCode;

    @Column(name = "TI_TXN_DT", columnDefinition = "DATETIME")
    private Date tiTxnDate;

    @Column(name = "TI_TXN_DCMNT_NMBR", columnDefinition = "VARCHAR")
    private String tiTxnDocumentNumber;

    @Column(name = "TI_TXN_RFRNC_NMBR", columnDefinition = "VARCHAR")
    private String tiTxnReferenceNumber;

    @Column(name = "TI_TX_EXMPT", columnDefinition = "DOUBLE")
    private double tiTaxExempt = 0;

    @Column(name = "TI_TX_ZR_RTD", columnDefinition = "DOUBLE")
    private double tiTaxZeroRated = 0;

    @Column(name = "TI_TXL_CODE", columnDefinition = "INT")
    private Integer tiTxlCode;

    @Column(name = "TI_TXL_COA_CODE", columnDefinition = "INT")
    private Integer tiTxlCoaCode;

    @Column(name = "TI_TC_CODE", columnDefinition = "INT")
    private Integer tiTcCode;

    @Column(name = "TI_WTC_CODE", columnDefinition = "INT")
    private Integer tiWtcCode;

    @Column(name = "TI_SL_CODE", columnDefinition = "INT")
    private Integer tiSlCode;

    @Column(name = "TI_SL_TIN")
    private String tiSlTin;

    @Column(name = "TI_SL_SBLDGR_CODE", columnDefinition = "VARCHAR")
    private String tiSlSubledgerCode;

    @Column(name = "TI_SL_NM", columnDefinition = "VARCHAR")
    private String tiSlName;

    @Column(name = "TI_SL_ADDRSS", columnDefinition = "VARCHAR")
    private String tiSlAddress;

    @Column(name = "TI_SL_ADDRSS2", columnDefinition = "VARCHAR")
    private String tiSlAddress2;

    @Column(name = "TI_IS_AR_DCMNT", columnDefinition = "TINYINT")
    private byte tiIsArDocument;

    @Column(name = "TI_AD_BRNCH", columnDefinition = "INT")
    private Integer tiAdBranch;

    @Column(name = "TI_AD_CMPNY", columnDefinition = "INT")
    private Integer tiAdCompany;

    public Integer getTiCode() {

        return tiCode;
    }

    public void setTiCode(Integer TI_CODE) {

        this.tiCode = TI_CODE;
    }

    public String getTiDocumentType() {

        return tiDocumentType;
    }

    public void setTiDocumentType(String TI_DCMNT_TYP) {

        this.tiDocumentType = TI_DCMNT_TYP;
    }

    public String getTiSource() {

        return tiSource;
    }

    public void setTiSource(String TI_SRC) {

        this.tiSource = TI_SRC;
    }

    public double getTiNetAmount() {

        return tiNetAmount;
    }

    public void setTiNetAmount(double TI_NET_AMNT) {

        this.tiNetAmount = TI_NET_AMNT;
    }

    public double getTiTaxAmount() {

        return tiTaxAmount;
    }

    public void setTiTaxAmount(double TI_TX_AMNT) {

        this.tiTaxAmount = TI_TX_AMNT;
    }

    public double getTiSalesAmount() {

        return tiSalesAmount;
    }

    public void setTiSalesAmount(double TI_SLS_AMNT) {

        this.tiSalesAmount = TI_SLS_AMNT;
    }

    public double getTiServicesAmount() {

        return tiServicesAmount;
    }

    public void setTiServicesAmount(double TI_SRVCS_AMNT) {

        this.tiServicesAmount = TI_SRVCS_AMNT;
    }

    public double getTiCapitalGoodsAmount() {

        return tiCapitalGoodsAmount;
    }

    public void setTiCapitalGoodsAmount(double TI_CPTL_GDS_AMNT) {

        this.tiCapitalGoodsAmount = TI_CPTL_GDS_AMNT;
    }

    public double getTiOtherCapitalGoodsAmount() {

        return tiOtherCapitalGoodsAmount;
    }

    public void setTiOtherCapitalGoodsAmount(double TI_OTHR_CPTL_GDS_AMNT) {

        this.tiOtherCapitalGoodsAmount = TI_OTHR_CPTL_GDS_AMNT;
    }

    public Integer getTiTxnCode() {

        return tiTxnCode;
    }

    public void setTiTxnCode(Integer TI_TXN_CODE) {

        this.tiTxnCode = TI_TXN_CODE;
    }

    public Date getTiTxnDate() {

        return tiTxnDate;
    }

    public void setTiTxnDate(Date TI_TXN_DT) {

        this.tiTxnDate = TI_TXN_DT;
    }

    public String getTiTxnDocumentNumber() {

        return tiTxnDocumentNumber;
    }

    public void setTiTxnDocumentNumber(String TI_TXN_DCMNT_NMBR) {

        this.tiTxnDocumentNumber = TI_TXN_DCMNT_NMBR;
    }

    public String getTiTxnReferenceNumber() {

        return tiTxnReferenceNumber;
    }

    public void setTiTxnReferenceNumber(String TI_TXN_RFRNC_NMBR) {

        this.tiTxnReferenceNumber = TI_TXN_RFRNC_NMBR;
    }

    public double getTiTaxExempt() {

        return tiTaxExempt;
    }

    public void setTiTaxExempt(double TI_TX_EXMPT) {

        this.tiTaxExempt = TI_TX_EXMPT;
    }

    public double getTiTaxZeroRated() {

        return tiTaxZeroRated;
    }

    public void setTiTaxZeroRated(double TI_TX_ZR_RTD) {

        this.tiTaxZeroRated = TI_TX_ZR_RTD;
    }

    public Integer getTiTxlCode() {

        return tiTxlCode;
    }

    public void setTiTxlCode(Integer TI_TXL_CODE) {

        this.tiTxlCode = TI_TXL_CODE;
    }

    public Integer getTiTxlCoaCode() {

        return tiTxlCoaCode;
    }

    public void setTiTxlCoaCode(Integer TI_TXL_COA_CODE) {

        this.tiTxlCoaCode = TI_TXL_COA_CODE;
    }

    public Integer getTiTcCode() {

        return tiTcCode;
    }

    public void setTiTcCode(Integer TI_TC_CODE) {

        this.tiTcCode = TI_TC_CODE;
    }

    public Integer getTiWtcCode() {

        return tiWtcCode;
    }

    public void setTiWtcCode(Integer TI_WTC_CODE) {

        this.tiWtcCode = TI_WTC_CODE;
    }

    public Integer getTiSlCode() {

        return tiSlCode;
    }

    public void setTiSlCode(Integer TI_SL_CODE) {

        this.tiSlCode = TI_SL_CODE;
    }

    public String getTiSlTin() {

        return tiSlTin;
    }

    public void setTiSlTin(String TI_SL_TIN) {

        this.tiSlTin = TI_SL_TIN;
    }

    public String getTiSlSubledgerCode() {

        return tiSlSubledgerCode;
    }

    public void setTiSlSubledgerCode(String TI_SL_SBLDGR_CODE) {

        this.tiSlSubledgerCode = TI_SL_SBLDGR_CODE;
    }

    public String getTiSlName() {

        return tiSlName;
    }

    public void setTiSlName(String TI_SL_NM) {

        this.tiSlName = TI_SL_NM;
    }

    public String getTiSlAddress() {

        return tiSlAddress;
    }

    public void setTiSlAddress(String TI_SL_ADDRSS) {

        this.tiSlAddress = TI_SL_ADDRSS;
    }

    public String getTiSlAddress2() {

        return tiSlAddress2;
    }

    public void setTiSlAddress2(String TI_SL_ADDRSS2) {

        this.tiSlAddress2 = TI_SL_ADDRSS2;
    }

    public byte getTiIsArDocument() {

        return tiIsArDocument;
    }

    public void setTiIsArDocument(byte TI_IS_AR_DCMNT) {

        this.tiIsArDocument = TI_IS_AR_DCMNT;
    }

    public Integer getTiAdBranch() {

        return tiAdBranch;
    }

    public void setTiAdBranch(Integer TI_AD_BRNCH) {

        this.tiAdBranch = TI_AD_BRNCH;
    }

    public Integer getTiAdCompany() {

        return tiAdCompany;
    }

    public void setTiAdCompany(Integer TI_AD_CMPNY) {

        this.tiAdCompany = TI_AD_CMPNY;
    }

}
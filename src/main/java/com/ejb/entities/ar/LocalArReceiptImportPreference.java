package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "ArReceiptImportPreference")
@Table(name = "AR_RCPT_IMPRT_PRFRNC")
public class LocalArReceiptImportPreference extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RIP_CODE", nullable = false)
    private Integer ripCode;

    @Column(name = "RIP_TYP", columnDefinition = "DOUBLE")
    private String ripType;

    @Column(name = "RIP_IS_SMMRZD", columnDefinition = "TINYINT")
    private byte ripIsSummarized;

    @Column(name = "RIP_CSTMR_CLMN", columnDefinition = "SMALLINT")
    private short ripCustomerColumn;

    @Column(name = "RIP_CSTMR_FILE", columnDefinition = "VARCHAR")
    private String ripCustomerFile;

    @Column(name = "RIP_DT_CLMN", columnDefinition = "SMALLINT")
    private short ripDateColumn;

    @Column(name = "RIP_DT_FILE", columnDefinition = "VARCHAR")
    private String ripDateFile;

    @Column(name = "RIP_RCPT_NMBR_CLMN", columnDefinition = "SMALLINT")
    private short ripReceiptNumberColumn;

    @Column(name = "RIP_RCPT_NMBR_FILE", columnDefinition = "VARCHAR")
    private String ripReceiptNumberFile;

    @Column(name = "RIP_RCPT_NMBR_LN_CLMN", columnDefinition = "SMALLINT")
    private short ripReceiptNumberLineColumn;

    @Column(name = "RIP_RCPT_NMBR_LN_FILE", columnDefinition = "VARCHAR")
    private String ripReceiptNumberLineFile;

    @Column(name = "RIP_RCPT_AMNT_CLMN", columnDefinition = "SMALLINT")
    private short ripReceiptAmountColumn;

    @Column(name = "RIP_RCPT_AMNT_FILE", columnDefinition = "VARCHAR")
    private String ripReceiptAmountFile;

    @Column(name = "RIP_TC_CLMN", columnDefinition = "SMALLINT")
    private short ripTcColumn;

    @Column(name = "RIP_TC_FILE", columnDefinition = "VARCHAR")
    private String ripTcFile;

    @Column(name = "RIP_WTC_CLMN", columnDefinition = "SMALLINT")
    private short ripWtcColumn;

    @Column(name = "RIP_WTC_FILE", columnDefinition = "VARCHAR")
    private String ripWtcFile;

    @Column(name = "RIP_ITM_NM_CLMN", columnDefinition = "SMALLINT")
    private short ripItemNameColumn;

    @Column(name = "RIP_ITM_NM_FILE", columnDefinition = "VARCHAR")
    private String ripItemNameFile;

    @Column(name = "RIP_LCTN_NM_CLMN", columnDefinition = "SMALLINT")
    private short ripLocationNameColumn;

    @Column(name = "RIP_LCTN_NM_FILE", columnDefinition = "VARCHAR")
    private String ripLocationNameFile;

    @Column(name = "RIP_UOM_NM_CLMN", columnDefinition = "SMALLINT")
    private short ripUomNameColumn;

    @Column(name = "RIP_UOM_NM_FILE", columnDefinition = "VARCHAR")
    private String ripUomNameFile;

    @Column(name = "RIP_QTY_CLMN", columnDefinition = "SMALLINT")
    private short ripQuantityColumn;

    @Column(name = "RIP_QTY_FILE", columnDefinition = "VARCHAR")
    private String ripQuantityFile;

    @Column(name = "RIP_UNT_PRC_CLMN", columnDefinition = "SMALLINT")
    private short ripUnitPriceColumn;

    @Column(name = "RIP_UNT_PRC_FILE", columnDefinition = "VARCHAR")
    private String ripUnitPriceFile;

    @Column(name = "RIP_TOTAL_CLMN", columnDefinition = "SMALLINT")
    private short ripTotalColumn;

    @Column(name = "RIP_TOTAL_FILE", columnDefinition = "VARCHAR")
    private String ripTotalFile;

    @Column(name = "RIP_MEMO_LN_CLMN", columnDefinition = "SMALLINT")
    private short ripMemoLineColumn;

    @Column(name = "RIP_MEMO_LN_FILE", columnDefinition = "VARCHAR")
    private String ripMemoLineFile;

    @Column(name = "RIP_INVC_NMBR_CLMN", columnDefinition = "SMALLINT")
    private short ripInvoiceNumberColumn;

    @Column(name = "RIP_INVC_NMBR_FILE", columnDefinition = "VARCHAR")
    private String ripInvoiceNumberFile;

    @Column(name = "RIP_APPLY_AMNT_CLMN", columnDefinition = "SMALLINT")
    private short ripApplyAmountColumn;

    @Column(name = "RIP_APPLY_AMNT_FILE", columnDefinition = "VARCHAR")
    private String ripApplyAmountFile;

    @Column(name = "RIP_APPLY_DSCNT_CLMN", columnDefinition = "SMALLINT")
    private short ripApplyDiscountColumn;

    @Column(name = "RIP_APPLY_DSCNT_FILE", columnDefinition = "VARCHAR")
    private String ripApplyDiscountFile;

    @Column(name = "RIP_AD_CMPNY", columnDefinition = "INT")
    private Integer ripAdCompany;

    @OneToMany(mappedBy = "arReceiptImportPreference", fetch = FetchType.LAZY)
    private List<LocalArReceiptImportPreferenceLine> arReceiptImportPreferenceLines;

    public Integer getRipCode() {

        return ripCode;
    }

    public void setRipCode(Integer RIP_CODE) {

        this.ripCode = RIP_CODE;
    }

    public String getRipType() {

        return ripType;
    }

    public void setRipType(String RIP_TYP) {

        this.ripType = RIP_TYP;
    }

    public byte getRipIsSummarized() {

        return ripIsSummarized;
    }

    public void setRipIsSummarized(byte RIP_IS_SMMRZD) {

        this.ripIsSummarized = RIP_IS_SMMRZD;
    }

    public short getRipCustomerColumn() {

        return ripCustomerColumn;
    }

    public void setRipCustomerColumn(short RIP_CSTMR_CLMN) {

        this.ripCustomerColumn = RIP_CSTMR_CLMN;
    }

    public String getRipCustomerFile() {

        return ripCustomerFile;
    }

    public void setRipCustomerFile(String RIP_CSTMR_FILE) {

        this.ripCustomerFile = RIP_CSTMR_FILE;
    }

    public short getRipDateColumn() {

        return ripDateColumn;
    }

    public void setRipDateColumn(short RIP_DT_CLMN) {

        this.ripDateColumn = RIP_DT_CLMN;
    }

    public String getRipDateFile() {

        return ripDateFile;
    }

    public void setRipDateFile(String RIP_DT_FILE) {

        this.ripDateFile = RIP_DT_FILE;
    }

    public short getRipReceiptNumberColumn() {

        return ripReceiptNumberColumn;
    }

    public void setRipReceiptNumberColumn(short RIP_RCPT_NMBR_CLMN) {

        this.ripReceiptNumberColumn = RIP_RCPT_NMBR_CLMN;
    }

    public String getRipReceiptNumberFile() {

        return ripReceiptNumberFile;
    }

    public void setRipReceiptNumberFile(String RIP_RCPT_NMBR_FILE) {

        this.ripReceiptNumberFile = RIP_RCPT_NMBR_FILE;
    }

    public short getRipReceiptNumberLineColumn() {

        return ripReceiptNumberLineColumn;
    }

    public void setRipReceiptNumberLineColumn(short RIP_RCPT_NMBR_LN_CLMN) {

        this.ripReceiptNumberLineColumn = RIP_RCPT_NMBR_LN_CLMN;
    }

    public String getRipReceiptNumberLineFile() {

        return ripReceiptNumberLineFile;
    }

    public void setRipReceiptNumberLineFile(String RIP_RCPT_NMBR_LN_FILE) {

        this.ripReceiptNumberLineFile = RIP_RCPT_NMBR_LN_FILE;
    }

    public short getRipReceiptAmountColumn() {

        return ripReceiptAmountColumn;
    }

    public void setRipReceiptAmountColumn(short RIP_RCPT_AMNT_CLMN) {

        this.ripReceiptAmountColumn = RIP_RCPT_AMNT_CLMN;
    }

    public String getRipReceiptAmountFile() {

        return ripReceiptAmountFile;
    }

    public void setRipReceiptAmountFile(String RIP_RCPT_AMNT_FILE) {

        this.ripReceiptAmountFile = RIP_RCPT_AMNT_FILE;
    }

    public short getRipTcColumn() {

        return ripTcColumn;
    }

    public void setRipTcColumn(short RIP_TC_CLMN) {

        this.ripTcColumn = RIP_TC_CLMN;
    }

    public String getRipTcFile() {

        return ripTcFile;
    }

    public void setRipTcFile(String RIP_TC_FILE) {

        this.ripTcFile = RIP_TC_FILE;
    }

    public short getRipWtcColumn() {

        return ripWtcColumn;
    }

    public void setRipWtcColumn(short RIP_WTC_CLMN) {

        this.ripWtcColumn = RIP_WTC_CLMN;
    }

    public String getRipWtcFile() {

        return ripWtcFile;
    }

    public void setRipWtcFile(String RIP_WTC_FILE) {

        this.ripWtcFile = RIP_WTC_FILE;
    }

    public short getRipItemNameColumn() {

        return ripItemNameColumn;
    }

    public void setRipItemNameColumn(short RIP_ITM_NM_CLMN) {

        this.ripItemNameColumn = RIP_ITM_NM_CLMN;
    }

    public String getRipItemNameFile() {

        return ripItemNameFile;
    }

    public void setRipItemNameFile(String RIP_ITM_NM_FILE) {

        this.ripItemNameFile = RIP_ITM_NM_FILE;
    }

    public short getRipLocationNameColumn() {

        return ripLocationNameColumn;
    }

    public void setRipLocationNameColumn(short RIP_LCTN_NM_CLMN) {

        this.ripLocationNameColumn = RIP_LCTN_NM_CLMN;
    }

    public String getRipLocationNameFile() {

        return ripLocationNameFile;
    }

    public void setRipLocationNameFile(String RIP_LCTN_NM_FILE) {

        this.ripLocationNameFile = RIP_LCTN_NM_FILE;
    }

    public short getRipUomNameColumn() {

        return ripUomNameColumn;
    }

    public void setRipUomNameColumn(short RIP_UOM_NM_CLMN) {

        this.ripUomNameColumn = RIP_UOM_NM_CLMN;
    }

    public String getRipUomNameFile() {

        return ripUomNameFile;
    }

    public void setRipUomNameFile(String RIP_UOM_NM_FILE) {

        this.ripUomNameFile = RIP_UOM_NM_FILE;
    }

    public short getRipQuantityColumn() {

        return ripQuantityColumn;
    }

    public void setRipQuantityColumn(short RIP_QTY_CLMN) {

        this.ripQuantityColumn = RIP_QTY_CLMN;
    }

    public String getRipQuantityFile() {

        return ripQuantityFile;
    }

    public void setRipQuantityFile(String RIP_QTY_FILE) {

        this.ripQuantityFile = RIP_QTY_FILE;
    }

    public short getRipUnitPriceColumn() {

        return ripUnitPriceColumn;
    }

    public void setRipUnitPriceColumn(short RIP_UNT_PRC_CLMN) {

        this.ripUnitPriceColumn = RIP_UNT_PRC_CLMN;
    }

    public String getRipUnitPriceFile() {

        return ripUnitPriceFile;
    }

    public void setRipUnitPriceFile(String RIP_UNT_PRC_FILE) {

        this.ripUnitPriceFile = RIP_UNT_PRC_FILE;
    }

    public short getRipTotalColumn() {

        return ripTotalColumn;
    }

    public void setRipTotalColumn(short RIP_TOTAL_CLMN) {

        this.ripTotalColumn = RIP_TOTAL_CLMN;
    }

    public String getRipTotalFile() {

        return ripTotalFile;
    }

    public void setRipTotalFile(String RIP_TOTAL_FILE) {

        this.ripTotalFile = RIP_TOTAL_FILE;
    }

    public short getRipMemoLineColumn() {

        return ripMemoLineColumn;
    }

    public void setRipMemoLineColumn(short RIP_MEMO_LN_CLMN) {

        this.ripMemoLineColumn = RIP_MEMO_LN_CLMN;
    }

    public String getRipMemoLineFile() {

        return ripMemoLineFile;
    }

    public void setRipMemoLineFile(String RIP_MEMO_LN_FILE) {

        this.ripMemoLineFile = RIP_MEMO_LN_FILE;
    }

    public short getRipInvoiceNumberColumn() {

        return ripInvoiceNumberColumn;
    }

    public void setRipInvoiceNumberColumn(short RIP_INVC_NMBR_CLMN) {

        this.ripInvoiceNumberColumn = RIP_INVC_NMBR_CLMN;
    }

    public String getRipInvoiceNumberFile() {

        return ripInvoiceNumberFile;
    }

    public void setRipInvoiceNumberFile(String RIP_INVC_NMBR_FILE) {

        this.ripInvoiceNumberFile = RIP_INVC_NMBR_FILE;
    }

    public short getRipApplyAmountColumn() {

        return ripApplyAmountColumn;
    }

    public void setRipApplyAmountColumn(short RIP_APPLY_AMNT_CLMN) {

        this.ripApplyAmountColumn = RIP_APPLY_AMNT_CLMN;
    }

    public String getRipApplyAmountFile() {

        return ripApplyAmountFile;
    }

    public void setRipApplyAmountFile(String RIP_APPLY_AMNT_FILE) {

        this.ripApplyAmountFile = RIP_APPLY_AMNT_FILE;
    }

    public short getRipApplyDiscountColumn() {

        return ripApplyDiscountColumn;
    }

    public void setRipApplyDiscountColumn(short RIP_APPLY_DSCNT_CLMN) {

        this.ripApplyDiscountColumn = RIP_APPLY_DSCNT_CLMN;
    }

    public String getRipApplyDiscountFile() {

        return ripApplyDiscountFile;
    }

    public void setRipApplyDiscountFile(String RIP_APPLY_DSCNT_FILE) {

        this.ripApplyDiscountFile = RIP_APPLY_DSCNT_FILE;
    }

    public Integer getRipAdCompany() {

        return ripAdCompany;
    }

    public void setRipAdCompany(Integer RIP_AD_CMPNY) {

        this.ripAdCompany = RIP_AD_CMPNY;
    }

    @XmlTransient
    public List getArReceiptImportPreferenceLines() {

        return arReceiptImportPreferenceLines;
    }

    public void setArReceiptImportPreferenceLines(List arReceiptImportPreferenceLines) {

        this.arReceiptImportPreferenceLines = arReceiptImportPreferenceLines;
    }

    public void addArReceiptImportPreferenceLine(LocalArReceiptImportPreferenceLine entity) {

        try {
            entity.setArReceiptImportPreference(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropArReceiptImportPreferenceLine(LocalArReceiptImportPreferenceLine entity) {

        try {
            entity.setArReceiptImportPreference(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
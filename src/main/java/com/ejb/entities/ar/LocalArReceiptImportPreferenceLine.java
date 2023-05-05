package com.ejb.entities.ar;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "ArReceiptImportPreferenceLine")
@Table(name = "AR_RCPT_IMPRT_PRFRNC_LN")
public class LocalArReceiptImportPreferenceLine extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RIL_CODE", nullable = false)
    private Integer rilCode;

    @Column(name = "RIL_TYP", columnDefinition = "VARCHAR")
    private String rilType;

    @Column(name = "RIL_NM", columnDefinition = "VARCHAR")
    private String rilName;

    @Column(name = "RIL_GL_ACCNT_NUM", columnDefinition = "VARCHAR")
    private String rilGlAccountNumber;

    @Column(name = "RIL_BNK_ACCNT_NM", columnDefinition = "VARCHAR")
    private String rilBankAccountName;

    @Column(name = "RIL_CLMN", columnDefinition = "SMALLINT")
    private short rilColumn;

    @Column(name = "RIL_FILE", columnDefinition = "VARCHAR")
    private String rilFile;

    @Column(name = "RIL_AMNT_CLMN", columnDefinition = "SMALLINT")
    private short rilAmountColumn;

    @Column(name = "RIL_AD_CMPNY", columnDefinition = "INT")
    private Integer rilAdCompany;

    @JoinColumn(name = "AR_RECEIPT_IMPORT_PREFERENCE", referencedColumnName = "RIP_CODE")
    @ManyToOne
    private LocalArReceiptImportPreference arReceiptImportPreference;

    public Integer getRilCode() {

        return rilCode;
    }

    public void setRilCode(Integer RIL_CODE) {

        this.rilCode = RIL_CODE;
    }

    public String getRilType() {

        return rilType;
    }

    public void setRilType(String RIL_TYP) {

        this.rilType = RIL_TYP;
    }

    public String getRilName() {

        return rilName;
    }

    public void setRilName(String RIL_NM) {

        this.rilName = RIL_NM;
    }

    public String getRilGlAccountNumber() {

        return rilGlAccountNumber;
    }

    public void setRilGlAccountNumber(String RIL_GL_ACCNT_NUM) {

        this.rilGlAccountNumber = RIL_GL_ACCNT_NUM;
    }

    public String getRilBankAccountName() {

        return rilBankAccountName;
    }

    public void setRilBankAccountName(String RIL_BNK_ACCNT_NM) {

        this.rilBankAccountName = RIL_BNK_ACCNT_NM;
    }

    public short getRilColumn() {

        return rilColumn;
    }

    public void setRilColumn(short RIL_CLMN) {

        this.rilColumn = RIL_CLMN;
    }

    public String getRilFile() {

        return rilFile;
    }

    public void setRilFile(String RIL_FILE) {

        this.rilFile = RIL_FILE;
    }

    public short getRilAmountColumn() {

        return rilAmountColumn;
    }

    public void setRilAmountColumn(short RIL_AMNT_CLMN) {

        this.rilAmountColumn = RIL_AMNT_CLMN;
    }

    public Integer getRilAdCompany() {

        return rilAdCompany;
    }

    public void setRilAdCompany(Integer RIL_AD_CMPNY) {

        this.rilAdCompany = RIL_AD_CMPNY;
    }

    public LocalArReceiptImportPreference getArReceiptImportPreference() {

        return arReceiptImportPreference;
    }

    public void setArReceiptImportPreference(
            LocalArReceiptImportPreference arReceiptImportPreference) {

        this.arReceiptImportPreference = arReceiptImportPreference;
    }

}
package com.ejb.entities.ap;

import com.ejb.NativeQueryHome;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity(name = "ApSupplierClass")
@Table(name = "AP_SPPLR_CLSS")
public class LocalApSupplierClass extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SC_CODE", nullable = false)
    private Integer scCode;

    @Column(name = "SC_NM", columnDefinition = "VARCHAR")
    private String scName;

    @Column(name = "SC_DESC", columnDefinition = "VARCHAR")
    private String scDescription;

    @Column(name = "SC_GL_COA_PYBL_ACCNT", columnDefinition = "INT")
    private Integer scGlCoaPayableAccount;

    @Column(name = "SC_GL_COA_EXPNS_ACCNT", columnDefinition = "INT")
    private Integer scGlCoaExpenseAccount;

    @Column(name = "SC_INVSTR_BNS_RT", columnDefinition = "DOUBLE")
    private double scInvestorBonusRate = 0;

    @Column(name = "SC_INVSTR_INT_RT", columnDefinition = "DOUBLE")
    private double scInvestorInterestRate = 0;

    @Column(name = "SC_ENBL", columnDefinition = "TINYINT")
    private byte scEnable;

    @Column(name = "SC_LDGR", columnDefinition = "TINYINT")
    private byte scLedger;

    @Column(name = "SC_INVT", columnDefinition = "TINYINT")
    private byte scIsInvestment;

    @Column(name = "SC_LN", columnDefinition = "TINYINT")
    private byte scIsLoan;

    @Column(name = "SC_VT_RLF_VCHR_ITM", columnDefinition = "TINYINT")
    private byte scIsVatReliefVoucherItem;

    @Column(name = "SC_LST_MDFD_BY", columnDefinition = "VARCHAR")
    private String scLastModifiedBy;

    @Column(name = "SC_DT_LST_MDFD", columnDefinition = "DATETIME")
    private Date scDateLastModified;

    @Column(name = "SC_AD_CMPNY", columnDefinition = "INT")
    private Integer scAdCompany;

    @JoinColumn(name = "AP_TAX_CODE", referencedColumnName = "AP_TC_CODE")
    @ManyToOne
    private LocalApTaxCode apTaxCode;

    @JoinColumn(name = "AP_WITHHOLDING_TAX_CODE", referencedColumnName = "AP_WTC_CODE")
    @ManyToOne
    private LocalApWithholdingTaxCode apWithholdingTaxCode;

    @OneToMany(mappedBy = "apSupplierClass", fetch = FetchType.LAZY)
    private List<LocalApSupplier> apSuppliers;

    public Integer getScCode() {

        return scCode;
    }

    public void setScCode(Integer SC_CODE) {

        this.scCode = SC_CODE;
    }

    public String getScName() {

        return scName;
    }

    public void setScName(String SC_NM) {

        this.scName = SC_NM;
    }

    public String getScDescription() {

        return scDescription;
    }

    public void setScDescription(String SC_DESC) {

        this.scDescription = SC_DESC;
    }

    public Integer getScGlCoaPayableAccount() {

        return scGlCoaPayableAccount;
    }

    public void setScGlCoaPayableAccount(Integer SC_GL_COA_PYBL_ACCNT) {

        this.scGlCoaPayableAccount = SC_GL_COA_PYBL_ACCNT;
    }

    public Integer getScGlCoaExpenseAccount() {

        return scGlCoaExpenseAccount;
    }

    public void setScGlCoaExpenseAccount(Integer SC_GL_COA_EXPNS_ACCNT) {

        this.scGlCoaExpenseAccount = SC_GL_COA_EXPNS_ACCNT;
    }

    public double getScInvestorBonusRate() {

        return scInvestorBonusRate;
    }

    public void setScInvestorBonusRate(double SC_INVSTR_BNS_RT) {

        this.scInvestorBonusRate = SC_INVSTR_BNS_RT;
    }

    public double getScInvestorInterestRate() {

        return scInvestorInterestRate;
    }

    public void setScInvestorInterestRate(double SC_INVSTR_INT_RT) {

        this.scInvestorInterestRate = SC_INVSTR_INT_RT;
    }

    public byte getScEnable() {

        return scEnable;
    }

    public void setScEnable(byte SC_ENBL) {

        this.scEnable = SC_ENBL;
    }

    public byte getScLedger() {

        return scLedger;
    }

    public void setScLedger(byte SC_LDGR) {

        this.scLedger = SC_LDGR;
    }

    public byte getScIsInvestment() {

        return scIsInvestment;
    }

    public void setScIsInvestment(byte SC_INVT) {

        this.scIsInvestment = SC_INVT;
    }

    public byte getScIsLoan() {

        return scIsLoan;
    }

    public void setScIsLoan(byte SC_LN) {

        this.scIsLoan = SC_LN;
    }

    public byte getScIsVatReliefVoucherItem() {

        return scIsVatReliefVoucherItem;
    }

    public void setScIsVatReliefVoucherItem(byte SC_VT_RLF_VCHR_ITM) {

        this.scIsVatReliefVoucherItem = SC_VT_RLF_VCHR_ITM;
    }

    public String getScLastModifiedBy() {

        return scLastModifiedBy;
    }

    public void setScLastModifiedBy(String SC_LST_MDFD_BY) {

        this.scLastModifiedBy = SC_LST_MDFD_BY;
    }

    public Date getScDateLastModified() {

        return scDateLastModified;
    }

    public void setScDateLastModified(Date SC_DT_LST_MDFD) {

        this.scDateLastModified = SC_DT_LST_MDFD;
    }

    public Integer getScAdCompany() {

        return scAdCompany;
    }

    public void setScAdCompany(Integer SC_AD_CMPNY) {

        this.scAdCompany = SC_AD_CMPNY;
    }

    public LocalApTaxCode getApTaxCode() {

        return apTaxCode;
    }

    public void setApTaxCode(LocalApTaxCode apTaxCode) {

        this.apTaxCode = apTaxCode;
    }

    public LocalApWithholdingTaxCode getApWithholdingTaxCode() {

        return apWithholdingTaxCode;
    }

    public void setApWithholdingTaxCode(LocalApWithholdingTaxCode apWithholdingTaxCode) {

        this.apWithholdingTaxCode = apWithholdingTaxCode;
    }

    @XmlTransient
    public List getApSuppliers() {

        return apSuppliers;
    }

    public void setApSuppliers(List apSuppliers) {

        this.apSuppliers = apSuppliers;
    }

    public void addApSupplier(LocalApSupplier entity) {

        try {
            entity.setApSupplierClass(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropApSupplier(LocalApSupplier entity) {

        try {
            entity.setApSupplierClass(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
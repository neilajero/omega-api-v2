package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import com.ejb.entities.gen.LocalGenField;
import com.ejb.entities.gl.LocalGlFunctionalCurrency;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "AdCompany")
@Table(name = "AD_CMPNY")
public class LocalAdCompany extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CMP_CODE", nullable = false)
    private Integer cmpCode;

    @Column(name = "CMP_NM", columnDefinition = "VARCHAR")
    private String cmpName;

    @Column(name = "CMP_SHRT_NM", columnDefinition = "VARCHAR")
    private String cmpShortName;

    @Column(name = "CMP_TX_PYR_NM", columnDefinition = "VARCHAR")
    private String cmpTaxPayerName;

    @Column(name = "CMP_CNTCT", columnDefinition = "VARCHAR")
    private String cmpContact;

    @Column(name = "CMP_WLCM_NT", columnDefinition = "VARCHAR")
    private String cmpWelcomeNote;

    @Column(name = "CMP_DESC", columnDefinition = "VARCHAR")
    private String cmpDescription;

    @Column(name = "CMP_ADDRSS", columnDefinition = "VARCHAR")
    private String cmpAddress;

    @Column(name = "CMP_CTY", columnDefinition = "VARCHAR")
    private String cmpCity;

    @Column(name = "CMP_ZP", columnDefinition = "VARCHAR")
    private String cmpZip;

    @Column(name = "CMP_CNTRY", columnDefinition = "VARCHAR")
    private String cmpCountry;

    @Column(name = "CMP_PHN", columnDefinition = "VARCHAR")
    private String cmpPhone;

    @Column(name = "CMP_FX", columnDefinition = "VARCHAR")
    private String cmpFax;

    @Column(name = "CMP_EML", columnDefinition = "VARCHAR")
    private String cmpEmail;

    @Column(name = "CMP_TIN", columnDefinition = "VARCHAR")
    private String cmpTin;

    @Column(name = "CMP_ML_SCTN_NO", columnDefinition = "VARCHAR")
    private String cmpMailSectionNo;

    @Column(name = "CMP_ML_LT_NO", columnDefinition = "VARCHAR")
    private String cmpMailLotNo;

    @Column(name = "CMP_ML_STRT", columnDefinition = "VARCHAR")
    private String cmpMailStreet;

    @Column(name = "CMP_ML_PO_BX", columnDefinition = "VARCHAR")
    private String cmpMailPoBox;

    @Column(name = "CMP_ML_CNTRY", columnDefinition = "VARCHAR")
    private String cmpMailCountry;

    @Column(name = "CMP_ML_PRVNC", columnDefinition = "VARCHAR")
    private String cmpMailProvince;

    @Column(name = "CMP_ML_PST_OFFC", columnDefinition = "VARCHAR")
    private String cmpMailPostOffice;

    @Column(name = "CMP_ML_CR_OFF", columnDefinition = "VARCHAR")
    private String cmpMailCareOff;

    @Column(name = "CMP_TX_PRD_FRM", columnDefinition = "VARCHAR")
    private String cmpTaxPeriodFrom;

    @Column(name = "CMP_TX_PRD_TO", columnDefinition = "VARCHAR")
    private String cmpTaxPeriodTo;

    @Column(name = "CMP_PBLC_OFFC_NM", columnDefinition = "VARCHAR")
    private String cmpPublicOfficeName;

    @Column(name = "CMP_DT_APPNTMNT", columnDefinition = "VARCHAR")
    private String cmpDateAppointment;

    @Column(name = "CMP_RVN_OFFC", columnDefinition = "VARCHAR")
    private String cmpRevenueOffice;

    @Column(name = "CMP_FSCL_YR_ENDNG", columnDefinition = "VARCHAR")
    private String cmpFiscalYearEnding;

    @Column(name = "CMP_INDSTRY", columnDefinition = "VARCHAR")
    private String cmpIndustry;

    @Column(name = "CMP_RTND_EARNNGS", columnDefinition = "VARCHAR")
    private String cmpRetainedEarnings;

    @JoinColumn(name = "GEN_FIELD", referencedColumnName = "FL_CODE")
    @ManyToOne
    private LocalGenField genField;

    @JoinColumn(name = "GL_FUNCTIONAL_CURRENCY", referencedColumnName = "FC_CODE")
    @ManyToOne
    private LocalGlFunctionalCurrency glFunctionalCurrency;

    public Integer getCmpCode() {

        return cmpCode;
    }

    public void setCmpCode(Integer CMP_CODE) {

        this.cmpCode = CMP_CODE;
    }

    public String getCmpName() {

        return cmpName;
    }

    public void setCmpName(String CMP_NM) {

        this.cmpName = CMP_NM;
    }

    public String getCmpShortName() {

        return cmpShortName;
    }

    public void setCmpShortName(String CMP_SHRT_NM) {

        this.cmpShortName = CMP_SHRT_NM;
    }

    public String getCmpTaxPayerName() {

        return cmpTaxPayerName;
    }

    public void setCmpTaxPayerName(String CMP_TX_PYR_NM) {

        this.cmpTaxPayerName = CMP_TX_PYR_NM;
    }

    public String getCmpContact() {

        return cmpContact;
    }

    public void setCmpContact(String CMP_CNTCT) {

        this.cmpContact = CMP_CNTCT;
    }

    public String getCmpWelcomeNote() {

        return cmpWelcomeNote;
    }

    public void setCmpWelcomeNote(String CMP_WLCM_NT) {

        this.cmpWelcomeNote = CMP_WLCM_NT;
    }

    public String getCmpDescription() {

        return cmpDescription;
    }

    public void setCmpDescription(String CMP_DESC) {

        this.cmpDescription = CMP_DESC;
    }

    public String getCmpAddress() {

        return cmpAddress;
    }

    public void setCmpAddress(String CMP_ADDRSS) {

        this.cmpAddress = CMP_ADDRSS;
    }

    public String getCmpCity() {

        return cmpCity;
    }

    public void setCmpCity(String CMP_CTY) {

        this.cmpCity = CMP_CTY;
    }

    public String getCmpZip() {

        return cmpZip;
    }

    public void setCmpZip(String CMP_ZP) {

        this.cmpZip = CMP_ZP;
    }

    public String getCmpCountry() {

        return cmpCountry;
    }

    public void setCmpCountry(String CMP_CNTRY) {

        this.cmpCountry = CMP_CNTRY;
    }

    public String getCmpPhone() {

        return cmpPhone;
    }

    public void setCmpPhone(String CMP_PHN) {

        this.cmpPhone = CMP_PHN;
    }

    public String getCmpFax() {

        return cmpFax;
    }

    public void setCmpFax(String CMP_FX) {

        this.cmpFax = CMP_FX;
    }

    public String getCmpEmail() {

        return cmpEmail;
    }

    public void setCmpEmail(String CMP_EML) {

        this.cmpEmail = CMP_EML;
    }

    public String getCmpTin() {

        return cmpTin;
    }

    public void setCmpTin(String CMP_TIN) {

        this.cmpTin = CMP_TIN;
    }

    public String getCmpMailSectionNo() {

        return cmpMailSectionNo;
    }

    public void setCmpMailSectionNo(String CMP_ML_SCTN_NO) {

        this.cmpMailSectionNo = CMP_ML_SCTN_NO;
    }

    public String getCmpMailLotNo() {

        return cmpMailLotNo;
    }

    public void setCmpMailLotNo(String CMP_ML_LT_NO) {

        this.cmpMailLotNo = CMP_ML_LT_NO;
    }

    public String getCmpMailStreet() {

        return cmpMailStreet;
    }

    public void setCmpMailStreet(String CMP_ML_STRT) {

        this.cmpMailStreet = CMP_ML_STRT;
    }

    public String getCmpMailPoBox() {

        return cmpMailPoBox;
    }

    public void setCmpMailPoBox(String CMP_ML_PO_BX) {

        this.cmpMailPoBox = CMP_ML_PO_BX;
    }

    public String getCmpMailCountry() {

        return cmpMailCountry;
    }

    public void setCmpMailCountry(String CMP_ML_CNTRY) {

        this.cmpMailCountry = CMP_ML_CNTRY;
    }

    public String getCmpMailProvince() {

        return cmpMailProvince;
    }

    public void setCmpMailProvince(String CMP_ML_PRVNC) {

        this.cmpMailProvince = CMP_ML_PRVNC;
    }

    public String getCmpMailPostOffice() {

        return cmpMailPostOffice;
    }

    public void setCmpMailPostOffice(String CMP_ML_PST_OFFC) {

        this.cmpMailPostOffice = CMP_ML_PST_OFFC;
    }

    public String getCmpMailCareOff() {

        return cmpMailCareOff;
    }

    public void setCmpMailCareOff(String CMP_ML_CR_OFF) {

        this.cmpMailCareOff = CMP_ML_CR_OFF;
    }

    public String getCmpTaxPeriodFrom() {

        return cmpTaxPeriodFrom;
    }

    public void setCmpTaxPeriodFrom(String CMP_TX_PRD_FRM) {

        this.cmpTaxPeriodFrom = CMP_TX_PRD_FRM;
    }

    public String getCmpTaxPeriodTo() {

        return cmpTaxPeriodTo;
    }

    public void setCmpTaxPeriodTo(String CMP_TX_PRD_TO) {

        this.cmpTaxPeriodTo = CMP_TX_PRD_TO;
    }

    public String getCmpPublicOfficeName() {

        return cmpPublicOfficeName;
    }

    public void setCmpPublicOfficeName(String CMP_PBLC_OFFC_NM) {

        this.cmpPublicOfficeName = CMP_PBLC_OFFC_NM;
    }

    public String getCmpDateAppointment() {

        return cmpDateAppointment;
    }

    public void setCmpDateAppointment(String CMP_DT_APPNTMNT) {

        this.cmpDateAppointment = CMP_DT_APPNTMNT;
    }

    public String getCmpRevenueOffice() {

        return cmpRevenueOffice;
    }

    public void setCmpRevenueOffice(String CMP_RVN_OFFC) {

        this.cmpRevenueOffice = CMP_RVN_OFFC;
    }

    public String getCmpFiscalYearEnding() {

        return cmpFiscalYearEnding;
    }

    public void setCmpFiscalYearEnding(String CMP_FSCL_YR_ENDNG) {

        this.cmpFiscalYearEnding = CMP_FSCL_YR_ENDNG;
    }

    public String getCmpIndustry() {

        return cmpIndustry;
    }

    public void setCmpIndustry(String CMP_INDSTRY) {

        this.cmpIndustry = CMP_INDSTRY;
    }

    public String getCmpRetainedEarnings() {

        return cmpRetainedEarnings;
    }

    public void setCmpRetainedEarnings(String CMP_RTND_EARNNGS) {

        this.cmpRetainedEarnings = CMP_RTND_EARNNGS;
    }

    public LocalGenField getGenField() {

        return genField;
    }

    public void setGenField(LocalGenField genField) {

        this.genField = genField;
    }

    public LocalGlFunctionalCurrency getGlFunctionalCurrency() {

        return glFunctionalCurrency;
    }

    public void setGlFunctionalCurrency(LocalGlFunctionalCurrency glFunctionalCurrency) {

        this.glFunctionalCurrency = glFunctionalCurrency;
    }

}
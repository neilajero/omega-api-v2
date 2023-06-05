package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ar.LocalArCustomer;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity(name = "InvItem")
@Table(name = "INV_ITM")
public class LocalInvItem extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "II_CODE", nullable = false)
    private Integer iiCode;

    @Column(name = "II_NM", columnDefinition = "VARCHAR")
    private String iiName;

    @Column(name = "II_DESC", columnDefinition = "VARCHAR")
    private String iiDescription;

    @Column(name = "II_PRT_NMBR", columnDefinition = "VARCHAR")
    private String iiPartNumber;

    @Column(name = "II_SHRT_NM", columnDefinition = "VARCHAR")
    private String iiShortName;

    @Column(name = "II_BR_CD_1", columnDefinition = "VARCHAR")
    private String iiBarCode1;

    @Column(name = "II_BR_CD_2", columnDefinition = "VARCHAR")
    private String iiBarCode2;

    @Column(name = "II_BR_CD_3", columnDefinition = "VARCHAR")
    private String iiBarCode3;

    @Column(name = "II_BRND", columnDefinition = "VARCHAR")
    private String iiBrand;

    @Column(name = "II_CLSS", columnDefinition = "VARCHAR")
    private String iiClass;

    @Column(name = "II_AD_LV_CTGRY", columnDefinition = "VARCHAR")
    private String iiAdLvCategory;

    @Column(name = "II_CST_MTHD", columnDefinition = "VARCHAR")
    private String iiCostMethod;

    @Column(name = "II_UNT_CST", columnDefinition = "DOUBLE")
    private double iiUnitCost = 0;

    @Column(name = "II_SLS_PRC", columnDefinition = "DOUBLE")
    private double iiSalesPrice = 0;

    @Column(name = "II_ENBL", columnDefinition = "TINYINT")
    private byte iiEnable;
    @Column(name = "II_VS_ITM", columnDefinition = "TINYINT")
    private byte iiVirtualStore;
    @Column(name = "II_ENBLE_AT_BLD", columnDefinition = "TINYINT")
    private byte iiEnableAutoBuild;
    @Column(name = "II_DNNSS", columnDefinition = "DOUBLE")
    private String iiDoneness;
    @Column(name = "II_SDNGS", columnDefinition = "DOUBLE")
    private String iiSidings;
    @Column(name = "II_RMRKS", columnDefinition = "DOUBLE")
    private String iiRemarks;
    @Column(name = "II_SRVC_CHRG", columnDefinition = "TINYINT")
    private byte iiServiceCharge;
    @Column(name = "II_DN_IN_CHRG", columnDefinition = "TINYINT")
    private byte iiNonInventoriable;
    @Column(name = "II_SRVCS", columnDefinition = "TINYINT")
    private byte iiServices;
    @Column(name = "II_INTRST_EXCPTN", columnDefinition = "TINYINT")
    private byte iiInterestException;
    @Column(name = "II_PYMNT_TRM_EXCPTN", columnDefinition = "TINYINT")
    private byte iiPaymentTermException;
    @Column(name = "II_JB_SRVCS", columnDefinition = "TINYINT")
    private byte iiJobServices;
    @Column(name = "II_IS_VAT_RLF", columnDefinition = "TINYINT")
    private byte iiIsVatRelief;
    @Column(name = "II_IS_TX", columnDefinition = "TINYINT")
    private byte iiIsTax;
    @Column(name = "II_IS_PRJCT", columnDefinition = "TINYINT")
    private byte iiIsProject;
    @Column(name = "II_PRCNT_MRKP", columnDefinition = "DOUBLE")
    private double iiPercentMarkup = 0;
    @Column(name = "II_SHPPNG_CST", columnDefinition = "DOUBLE")
    private double iiShippingCost = 0;
    @Column(name = "II_SPCFC_GRVTY", columnDefinition = "DOUBLE")
    private double iiSpecificGravity = 0;
    @Column(name = "II_STNDRD_FLL_SZ", columnDefinition = "DOUBLE")
    private double iiStandardFillSize = 0;
    @Column(name = "II_YLD", columnDefinition = "DOUBLE")
    private double iiYield = 0;
    @Column(name = "II_LBR_CST", columnDefinition = "DOUBLE")
    private double iiLaborCost = 0;
    @Column(name = "II_PWR_CST", columnDefinition = "DOUBLE")
    private double iiPowerCost = 0;
    @Column(name = "II_OHD_CST", columnDefinition = "DOUBLE")
    private double iiOverHeadCost = 0;
    @Column(name = "II_FRGHT_CST", columnDefinition = "DOUBLE")
    private double iiFreightCost = 0;
    @Column(name = "II_FXD_CST", columnDefinition = "DOUBLE")
    private double iiFixedCost = 0;
    @Column(name = "II_STD_LST_PRCNTG", columnDefinition = "DOUBLE")
    private double iiLossPercentage = 0;
    @Column(name = "II_MRKP_VL", columnDefinition = "DOUBLE")
    private double iiMarkupValue = 0;
    @Column(name = "II_MRKT", columnDefinition = "VARCHAR")
    private String iiMarket;
    @Column(name = "II_ENBL_PO", columnDefinition = "TINYINT")
    private byte iiEnablePo;
    @Column(name = "II_PO_CYCL", columnDefinition = "SMALLINT")
    private short iiPoCycle;
    @Column(name = "II_RETAIL_UOM", columnDefinition = "INT")
    private Integer iiRetailUom;
    @Column(name = "II_UMC_PCKGNG", columnDefinition = "VARCHAR")
    private String iiUmcPackaging;
    @Column(name = "II_OPN_PRDCT", columnDefinition = "TINYINT")
    private byte iiOpenProduct;
    @Column(name = "II_FXD_ASST", columnDefinition = "TINYINT")
    private byte iiFixedAsset;
    @Column(name = "II_DT_ACQRD", columnDefinition = "DATETIME")
    private Date iiDateAcquired;
    @Column(name = "II_DFLT_LCTN", columnDefinition = "INT")
    private Integer iiDefaultLocation;
    @Column(name = "II_TRC_MSC", columnDefinition = "TINYINT")
    private byte iiTraceMisc;
    @Column(name = "II_SC_SNDY", columnDefinition = "TINYINT")
    private byte iiScSunday;
    @Column(name = "II_SC_MNDY", columnDefinition = "TINYINT")
    private byte iiScMonday;
    @Column(name = "II_SC_TSDY", columnDefinition = "TINYINT")
    private byte iiScTuesday;
    @Column(name = "II_SC_WDNSDY", columnDefinition = "TINYINT")
    private byte iiScWednesday;
    @Column(name = "II_SC_THRSDY", columnDefinition = "TINYINT")
    private byte iiScThursday;
    @Column(name = "II_SC_FRDY", columnDefinition = "TINYINT")
    private byte iiScFriday;
    @Column(name = "II_SC_STRDY", columnDefinition = "TINYINT")
    private byte iiScSaturday;
    @Column(name = "II_ACQSTN_CST", columnDefinition = "DOUBLE")
    private double iiAcquisitionCost = 0;
    @Column(name = "II_LF_SPN", columnDefinition = "DOUBLE")
    private double iiLifeSpan = 0;
    @Column(name = "II_RSDL_VL", columnDefinition = "DOUBLE")
    private double iiResidualValue = 0;
    @Column(name = "II_GL_COA_FXD_ASST_ACCNT", columnDefinition = "VARCHAR")
    private String glCoaFixedAssetAccount;
    @Column(name = "II_GL_COA_DPRCTN_ACCNT", columnDefinition = "VARCHAR")
    private String glCoaDepreciationAccount;
    @Column(name = "II_GL_ACCMLTD_DPRCTN_ACCNT", columnDefinition = "VARCHAR")
    private String glCoaAccumulatedDepreciationAccount;
    @Column(name = "II_GL_LBR_CST_ACCNT", columnDefinition = "VARCHAR")
    private String glCoaLaborCostAccount;
    @Column(name = "II_GL_PWR_CST_ACCNT", columnDefinition = "VARCHAR")
    private String glCoaPowerCostAccount;
    @Column(name = "II_GL_OHD_CST_ACCNT", columnDefinition = "VARCHAR")
    private String glCoaOverHeadCostAccount;
    @Column(name = "II_GL_FRGHT_CST_ACCNT", columnDefinition = "VARCHAR")
    private String glCoaFreightCostAccount;
    @Column(name = "II_GL_FXD_CST_ACCNT", columnDefinition = "VARCHAR")
    private String glCoaFixedCostAccount;
    @Column(name = "II_CNDTN", columnDefinition = "VARCHAR")
    private String iiCondition;
    @Column(name = "II_TX_CD", columnDefinition = "VARCHAR")
    private String iiTaxCode;
    @Column(name = "II_AD_CMPNY", columnDefinition = "INT")
    private Integer iiAdCompany;
    @Column(name = "II_CRTD_BY", columnDefinition = "VARCHAR")
    private String iiCreatedBy;
    @Column(name = "II_DT_CRTD", columnDefinition = "DATETIME")
    private Date iiDateCreated;
    @Column(name = "II_LST_MDFD_BY", columnDefinition = "VARCHAR")
    private String iiLastModifiedBy;
    @Column(name = "II_DT_LST_MDFD", columnDefinition = "DATETIME")
    private Date iiDateLastModified;
    @JoinColumn(name = "AP_SUPPLIER", referencedColumnName = "SPL_CODE")
    @ManyToOne
    private LocalApSupplier apSupplier;
    @JoinColumn(name = "AR_CUSTOMER", referencedColumnName = "AR_CST_CODE")
    @ManyToOne
    private LocalArCustomer arCustomer;
    @JoinColumn(name = "INV_UNIT_OF_MEASURE", referencedColumnName = "UOM_CODE")
    @ManyToOne
    private LocalInvUnitOfMeasure invUnitOfMeasure;
    @OneToMany(mappedBy = "invItem", fetch = FetchType.LAZY)
    private List<LocalInvItemLocation> invItemLocations;
    @OneToMany(mappedBy = "invItem", fetch = FetchType.LAZY)
    private List<LocalInvTransactionalBudget> invTransactionalBudget;
    @OneToMany(mappedBy = "invItem", fetch = FetchType.LAZY)
    private List<LocalInvStockTransferLine> invStockTransferLines;
    @OneToMany(mappedBy = "invItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalInvPriceLevel> invPriceLevels;
    @OneToMany(mappedBy = "invItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalInvPriceLevelDate> invPriceLevelsDate;
    @OneToMany(mappedBy = "invItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalInvUnitOfMeasureConversion> invUnitOfMeasureConversions;
    @OneToMany(mappedBy = "invItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalInvBillOfMaterial> invBillOfMaterials;

    public List<LocalInvPriceLevelDate> getInvPriceLevelsDate() {

        return invPriceLevelsDate;
    }

    public void setInvPriceLevelsDate(List<LocalInvPriceLevelDate> invPriceLevelsDate) {

        this.invPriceLevelsDate = invPriceLevelsDate;
    }

    public byte getIiEnableAutoBuild() {

        return iiEnableAutoBuild;
    }

    public void setIiEnableAutoBuild(byte iiEnableAutoBuild) {

        this.iiEnableAutoBuild = iiEnableAutoBuild;
    }

    public List<LocalInvBillOfMaterial> getInvBillOfMaterials() {

        return invBillOfMaterials;
    }

    public void setInvBillOfMaterials(List<LocalInvBillOfMaterial> invBillOfMaterials) {

        this.invBillOfMaterials = invBillOfMaterials;
    }

    public Integer getIiCode() {

        return iiCode;
    }

    public void setIiCode(Integer II_CODE) {

        this.iiCode = II_CODE;
    }

    public String getIiName() {

        return iiName;
    }

    public void setIiName(String II_NM) {

        this.iiName = II_NM;
    }

    public String getIiDescription() {

        return iiDescription;
    }

    public void setIiDescription(String II_DESC) {

        this.iiDescription = II_DESC;
    }

    public String getIiPartNumber() {

        return iiPartNumber;
    }

    public void setIiPartNumber(String II_PRT_NMBR) {

        this.iiPartNumber = II_PRT_NMBR;
    }

    public String getIiShortName() {

        return iiShortName;
    }

    public void setIiShortName(String II_SHRT_NM) {

        this.iiShortName = II_SHRT_NM;
    }

    public String getIiBarCode1() {

        return iiBarCode1;
    }

    public void setIiBarCode1(String II_BR_CD_1) {

        this.iiBarCode1 = II_BR_CD_1;
    }

    public String getIiBarCode2() {

        return iiBarCode2;
    }

    public void setIiBarCode2(String II_BR_CD_2) {

        this.iiBarCode2 = II_BR_CD_2;
    }

    public String getIiBarCode3() {

        return iiBarCode3;
    }

    public void setIiBarCode3(String II_BR_CD_3) {

        this.iiBarCode3 = II_BR_CD_3;
    }

    public String getIiBrand() {

        return iiBrand;
    }

    public void setIiBrand(String II_BRND) {

        this.iiBrand = II_BRND;
    }

    public String getIiClass() {

        return iiClass;
    }

    public void setIiClass(String II_CLSS) {

        this.iiClass = II_CLSS;
    }

    public String getIiAdLvCategory() {

        return iiAdLvCategory;
    }

    public void setIiAdLvCategory(String II_AD_LV_CTGRY) {

        this.iiAdLvCategory = II_AD_LV_CTGRY;
    }

    public String getIiCostMethod() {

        return iiCostMethod;
    }

    public void setIiCostMethod(String II_CST_MTHD) {

        this.iiCostMethod = II_CST_MTHD;
    }

    public double getIiUnitCost() {

        return iiUnitCost;
    }

    public void setIiUnitCost(double II_UNT_CST) {

        this.iiUnitCost = II_UNT_CST;
    }

    public double getIiSalesPrice() {

        return iiSalesPrice;
    }

    public void setIiSalesPrice(double II_SLS_PRC) {

        this.iiSalesPrice = II_SLS_PRC;
    }

    public byte getIiEnable() {

        return iiEnable;
    }

    public void setIiEnable(byte II_ENBL) {

        this.iiEnable = II_ENBL;
    }

    public String getIiDoneness() {

        return iiDoneness;
    }

    public void setIiDoneness(String II_DNNSS) {

        this.iiDoneness = II_DNNSS;
    }

    public String getIiSidings() {

        return iiSidings;
    }

    public void setIiSidings(String II_SDNGS) {

        this.iiSidings = II_SDNGS;
    }

    public String getIiRemarks() {

        return iiRemarks;
    }

    public void setIiRemarks(String II_RMRKS) {

        this.iiRemarks = II_RMRKS;
    }

    public byte getIiServiceCharge() {

        return iiServiceCharge;
    }

    public void setIiServiceCharge(byte II_SRVC_CHRG) {

        this.iiServiceCharge = II_SRVC_CHRG;
    }

    public byte getIiVirtualStore() {

        return iiVirtualStore;
    }

    public void setIiVirtualStore(byte II_VS_ITM) {

        this.iiVirtualStore = II_VS_ITM;
    }

    public byte getIiNonInventoriable() {

        return iiNonInventoriable;
    }

    public void setIiNonInventoriable(byte II_DN_IN_CHRG) {

        this.iiNonInventoriable = II_DN_IN_CHRG;
    }

    public byte getIiServices() {

        return iiServices;
    }

    public void setIiServices(byte II_SRVCS) {

        this.iiServices = II_SRVCS;
    }

    public byte getIiInterestException() {

        return iiInterestException;
    }

    public void setIiInterestException(byte II_INTRST_EXCPTN) {

        this.iiInterestException = II_INTRST_EXCPTN;
    }

    public byte getIiPaymentTermException() {

        return iiPaymentTermException;
    }

    public void setIiPaymentTermException(byte II_PYMNT_TRM_EXCPTN) {

        this.iiPaymentTermException = II_PYMNT_TRM_EXCPTN;
    }

    public byte getIiJobServices() {

        return iiJobServices;
    }

    public void setIiJobServices(byte II_JB_SRVCS) {

        this.iiJobServices = II_JB_SRVCS;
    }

    public byte getIiIsVatRelief() {

        return iiIsVatRelief;
    }

    public void setIiIsVatRelief(byte II_IS_VAT_RLF) {

        this.iiIsVatRelief = II_IS_VAT_RLF;
    }

    public byte getIiIsTax() {

        return iiIsTax;
    }

    public void setIiIsTax(byte II_IS_TX) {

        this.iiIsTax = II_IS_TX;
    }

    public byte getIiIsProject() {

        return iiIsProject;
    }

    public void setIiIsProject(byte II_IS_PRJCT) {

        this.iiIsProject = II_IS_PRJCT;
    }

    public double getIiPercentMarkup() {

        return iiPercentMarkup;
    }

    public void setIiPercentMarkup(double II_PRCNT_MRKP) {

        this.iiPercentMarkup = II_PRCNT_MRKP;
    }

    public double getIiShippingCost() {

        return iiShippingCost;
    }

    public void setIiShippingCost(double II_SHPPNG_CST) {

        this.iiShippingCost = II_SHPPNG_CST;
    }

    public double getIiSpecificGravity() {

        return iiSpecificGravity;
    }

    public void setIiSpecificGravity(double II_SPCFC_GRVTY) {

        this.iiSpecificGravity = II_SPCFC_GRVTY;
    }

    public double getIiStandardFillSize() {

        return iiStandardFillSize;
    }

    public void setIiStandardFillSize(double II_STNDRD_FLL_SZ) {

        this.iiStandardFillSize = II_STNDRD_FLL_SZ;
    }

    public double getIiYield() {

        return iiYield;
    }

    public void setIiYield(double II_YLD) {

        this.iiYield = II_YLD;
    }

    public double getIiLaborCost() {

        return iiLaborCost;
    }

    public void setIiLaborCost(double II_LBR_CST) {

        this.iiLaborCost = II_LBR_CST;
    }

    public double getIiPowerCost() {

        return iiPowerCost;
    }

    public void setIiPowerCost(double II_PWR_CST) {

        this.iiPowerCost = II_PWR_CST;
    }

    public double getIiOverHeadCost() {

        return iiOverHeadCost;
    }

    public void setIiOverHeadCost(double II_OHD_CST) {

        this.iiOverHeadCost = II_OHD_CST;
    }

    public double getIiFreightCost() {

        return iiFreightCost;
    }

    public void setIiFreightCost(double II_FRGHT_CST) {

        this.iiFreightCost = II_FRGHT_CST;
    }

    public double getIiFixedCost() {

        return iiFixedCost;
    }

    public void setIiFixedCost(double II_FXD_CST) {

        this.iiFixedCost = II_FXD_CST;
    }

    public double getIiLossPercentage() {

        return iiLossPercentage;
    }

    public void setIiLossPercentage(double II_STD_LST_PRCNTG) {

        this.iiLossPercentage = II_STD_LST_PRCNTG;
    }

    public double getIiMarkupValue() {

        return iiMarkupValue;
    }

    public void setIiMarkupValue(double II_MRKP_VL) {

        this.iiMarkupValue = II_MRKP_VL;
    }

    public String getIiMarket() {

        return iiMarket;
    }

    public void setIiMarket(String II_MRKT) {

        this.iiMarket = II_MRKT;
    }

    public byte getIiEnablePo() {

        return iiEnablePo;
    }

    public void setIiEnablePo(byte II_ENBL_PO) {

        this.iiEnablePo = II_ENBL_PO;
    }

    public short getIiPoCycle() {

        return iiPoCycle;
    }

    public void setIiPoCycle(short II_PO_CYCL) {

        this.iiPoCycle = II_PO_CYCL;
    }

    public Integer getIiRetailUom() {

        return iiRetailUom;
    }

    public void setIiRetailUom(Integer II_RETAIL_UOM) {

        this.iiRetailUom = II_RETAIL_UOM;
    }

    public String getIiUmcPackaging() {

        return iiUmcPackaging;
    }

    public void setIiUmcPackaging(String II_UMC_PCKGNG) {

        this.iiUmcPackaging = II_UMC_PCKGNG;
    }

    public byte getIiOpenProduct() {

        return iiOpenProduct;
    }

    public void setIiOpenProduct(byte II_OPN_PRDCT) {

        this.iiOpenProduct = II_OPN_PRDCT;
    }

    public byte getIiFixedAsset() {

        return iiFixedAsset;
    }

    public void setIiFixedAsset(byte II_FXD_ASST) {

        this.iiFixedAsset = II_FXD_ASST;
    }

    public Date getIiDateAcquired() {

        return iiDateAcquired;
    }

    public void setIiDateAcquired(Date II_DT_ACQRD) {

        this.iiDateAcquired = II_DT_ACQRD;
    }

    public Integer getIiDefaultLocation() {

        return iiDefaultLocation;
    }

    public void setIiDefaultLocation(Integer II_DFLT_LCTN) {

        this.iiDefaultLocation = II_DFLT_LCTN;
    }

    public byte getIiTraceMisc() {

        return iiTraceMisc;
    }

    public void setIiTraceMisc(byte II_TRC_MSC) {

        this.iiTraceMisc = II_TRC_MSC;
    }

    public byte getIiScSunday() {

        return iiScSunday;
    }

    public void setIiScSunday(byte II_SC_SNDY) {

        this.iiScSunday = II_SC_SNDY;
    }

    public byte getIiScMonday() {

        return iiScMonday;
    }

    public void setIiScMonday(byte II_SC_MNDY) {

        this.iiScMonday = II_SC_MNDY;
    }

    public byte getIiScTuesday() {

        return iiScTuesday;
    }

    public void setIiScTuesday(byte II_SC_TSDY) {

        this.iiScTuesday = II_SC_TSDY;
    }

    public byte getIiScWednesday() {

        return iiScWednesday;
    }

    public void setIiScWednesday(byte II_SC_WDNSDY) {

        this.iiScWednesday = II_SC_WDNSDY;
    }

    public byte getIiScThursday() {

        return iiScThursday;
    }

    public void setIiScThursday(byte II_SC_THRSDY) {

        this.iiScThursday = II_SC_THRSDY;
    }

    public byte getIiScFriday() {

        return iiScFriday;
    }

    public void setIiScFriday(byte II_SC_FRDY) {

        this.iiScFriday = II_SC_FRDY;
    }

    public byte getIiScSaturday() {

        return iiScSaturday;
    }

    public void setIiScSaturday(byte II_SC_STRDY) {

        this.iiScSaturday = II_SC_STRDY;
    }

    public double getIiAcquisitionCost() {

        return iiAcquisitionCost;
    }

    public void setIiAcquisitionCost(double II_ACQSTN_CST) {

        this.iiAcquisitionCost = II_ACQSTN_CST;
    }

    public double getIiLifeSpan() {

        return iiLifeSpan;
    }

    public void setIiLifeSpan(double II_LF_SPN) {

        this.iiLifeSpan = II_LF_SPN;
    }

    public double getIiResidualValue() {

        return iiResidualValue;
    }

    public void setIiResidualValue(double II_RSDL_VL) {

        this.iiResidualValue = II_RSDL_VL;
    }

    public String getGlCoaFixedAssetAccount() {

        return glCoaFixedAssetAccount;
    }

    public void setGlCoaFixedAssetAccount(String II_GL_COA_FXD_ASST_ACCNT) {

        this.glCoaFixedAssetAccount = II_GL_COA_FXD_ASST_ACCNT;
    }

    public String getGlCoaDepreciationAccount() {

        return glCoaDepreciationAccount;
    }

    public void setGlCoaDepreciationAccount(String II_GL_COA_DPRCTN_ACCNT) {

        this.glCoaDepreciationAccount = II_GL_COA_DPRCTN_ACCNT;
    }

    public String getGlCoaAccumulatedDepreciationAccount() {

        return glCoaAccumulatedDepreciationAccount;
    }

    public void setGlCoaAccumulatedDepreciationAccount(String II_GL_ACCMLTD_DPRCTN_ACCNT) {

        this.glCoaAccumulatedDepreciationAccount = II_GL_ACCMLTD_DPRCTN_ACCNT;
    }

    public String getGlCoaLaborCostAccount() {

        return glCoaLaborCostAccount;
    }

    public void setGlCoaLaborCostAccount(String II_GL_LBR_CST_ACCNT) {

        this.glCoaLaborCostAccount = II_GL_LBR_CST_ACCNT;
    }

    public String getGlCoaPowerCostAccount() {

        return glCoaPowerCostAccount;
    }

    public void setGlCoaPowerCostAccount(String II_GL_PWR_CST_ACCNT) {

        this.glCoaPowerCostAccount = II_GL_PWR_CST_ACCNT;
    }

    public String getGlCoaOverHeadCostAccount() {

        return glCoaOverHeadCostAccount;
    }

    public void setGlCoaOverHeadCostAccount(String II_GL_OHD_CST_ACCNT) {

        this.glCoaOverHeadCostAccount = II_GL_OHD_CST_ACCNT;
    }

    public String getGlCoaFreightCostAccount() {

        return glCoaFreightCostAccount;
    }

    public void setGlCoaFreightCostAccount(String II_GL_FRGHT_CST_ACCNT) {

        this.glCoaFreightCostAccount = II_GL_FRGHT_CST_ACCNT;
    }

    public String getGlCoaFixedCostAccount() {

        return glCoaFixedCostAccount;
    }

    public void setGlCoaFixedCostAccount(String II_GL_FXD_CST_ACCNT) {

        this.glCoaFixedCostAccount = II_GL_FXD_CST_ACCNT;
    }

    public String getIiCondition() {

        return iiCondition;
    }

    public void setIiCondition(String II_CNDTN) {

        this.iiCondition = II_CNDTN;
    }

    public String getIiTaxCode() {

        return iiTaxCode;
    }

    public void setIiTaxCode(String II_TX_CD) {

        this.iiTaxCode = II_TX_CD;
    }

    public Integer getIiAdCompany() {

        return iiAdCompany;
    }

    public void setIiAdCompany(Integer II_AD_CMPNY) {

        this.iiAdCompany = II_AD_CMPNY;
    }

    public String getIiCreatedBy() {

        return iiCreatedBy;
    }

    public void setIiCreatedBy(String II_CRTD_BY) {

        this.iiCreatedBy = II_CRTD_BY;
    }

    public Date getIiDateCreated() {

        return iiDateCreated;
    }

    public void setIiDateCreated(Date II_DT_CRTD) {

        this.iiDateCreated = II_DT_CRTD;
    }

    public String getIiLastModifiedBy() {

        return iiLastModifiedBy;
    }

    public void setIiLastModifiedBy(String II_LST_MDFD_BY) {

        this.iiLastModifiedBy = II_LST_MDFD_BY;
    }

    public Date getIiDateLastModified() {

        return iiDateLastModified;
    }

    public void setIiDateLastModified(Date II_DT_LST_MDFD) {

        this.iiDateLastModified = II_DT_LST_MDFD;
    }

    public LocalApSupplier getApSupplier() {

        return apSupplier;
    }

    public void setApSupplier(LocalApSupplier apSupplier) {

        this.apSupplier = apSupplier;
    }

    public LocalArCustomer getArCustomer() {

        return arCustomer;
    }

    public void setArCustomer(LocalArCustomer arCustomer) {

        this.arCustomer = arCustomer;
    }

    public LocalInvUnitOfMeasure getInvUnitOfMeasure() {

        return invUnitOfMeasure;
    }

    public void setInvUnitOfMeasure(LocalInvUnitOfMeasure invUnitOfMeasure) {

        this.invUnitOfMeasure = invUnitOfMeasure;
    }

    @XmlTransient
    public List getInvItemLocations() {

        return invItemLocations;
    }

    public void setInvItemLocations(List invItemLocations) {

        this.invItemLocations = invItemLocations;
    }

    @XmlTransient
    public List getInvTransactionalBudget() {

        return invTransactionalBudget;
    }

    public void setInvTransactionalBudget(List invTransactionalBudget) {

        this.invTransactionalBudget = invTransactionalBudget;
    }

    @XmlTransient
    public List getInvStockTransferLines() {

        return invStockTransferLines;
    }

    public void setInvStockTransferLines(List invStockTransferLines) {

        this.invStockTransferLines = invStockTransferLines;
    }

    @XmlTransient
    public List getInvPriceLevels() {

        return invPriceLevels;
    }

    public void setInvPriceLevels(List invPriceLevels) {

        this.invPriceLevels = invPriceLevels;
    }

    @XmlTransient
    public List getInvUnitOfMeasureConversions() {

        return invUnitOfMeasureConversions;
    }

    public void setInvUnitOfMeasureConversions(List invUnitOfMeasureConversions) {

        this.invUnitOfMeasureConversions = invUnitOfMeasureConversions;
    }

    public void addInvStockTransferLine(LocalInvStockTransferLine entity) {

        try {
            entity.setInvItem(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addInvPriceLevel(LocalInvPriceLevel entity) {

        try {
            entity.setInvItem(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void addInvUnitOfMeasureConversion(LocalInvUnitOfMeasureConversion entity) {

        try {
            entity.setInvItem(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
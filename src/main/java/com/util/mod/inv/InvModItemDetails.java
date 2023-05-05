/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.inv;

import com.util.inv.InvItemDetails;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class InvModItemDetails extends InvItemDetails implements java.io.Serializable {
	
	private String II_UOM_NM;
	private ArrayList iiBomList;
	private String II_UOM_PCKGNG;
	private String II_SPL_SPPLR_CODE;
	private String II_CST_CSTMR_CODE;
	private String II_DL_PRC;
	private double II_AVE_CST;
	private double II_QOH;
	private String II_RETAIL_UOM_NM;
	private String II_DFLT_LCTN_NM;
	
	private byte II_FXD_ASST;
	private Date II_DT_ACQRD;
	private byte II_TRC_MSC;
	
	
	private double acquisitionCost = 0;
	private double ResidualValue=0;
	private double lifeSpan = 0;
	private String fixedAssetAccount = null;
	private String fixedAssetAccountDescription = null;
	private String depreciationAccount = null;
	private String depreciationAccountDescription = null;
	private String AccumulatedDepreciationAccount = null;
	private String AccumulatedDepreciationAccountDescription = null;
	
	private String laborCostAccount = null;
	private String laborCostAccountDescription = null;
	
	private String powerCostAccount = null;
	private String powerCostAccountDescription = null;

	private String overHeadCostAccount = null;
	private String overHeadCostAccountDescription = null;
	
	private String freightCostAccount = null;
	private String freightCostAccountDescription = null;
	
	private String fixedCostAccount = null;
	private String fixedCostAccountDescription = null;
	
	private String II_CNDTN;
	public InvModItemDetails () {
    }
	
	public String getIiUomName() {
		
		return II_UOM_NM;
		
	}
	
	public void setIiUomName(String II_UOM_NM) {
		
		this.II_UOM_NM = II_UOM_NM;
		
	}
	
	public ArrayList getIiBomList() {
		
		return iiBomList;
		
	}
	
	public void setIiBomList(ArrayList iiBomList) {
		
		this.iiBomList = iiBomList;
		
	}
	
	public String getIiSplSpplrCode() {
		
		return II_SPL_SPPLR_CODE;
		
	}
	
	public void setIiSplSpplrCode(String II_SPL_SPPLR_CODE) {
		
		this.II_SPL_SPPLR_CODE = II_SPL_SPPLR_CODE;
		
	}
	
	public String getIiCstCustomerCode() {
		
		return II_CST_CSTMR_CODE;
		
	}
	
	public void setIiCstCustomerCode(String II_CST_CSTMR_CODE) {
		
		this.II_CST_CSTMR_CODE = II_CST_CSTMR_CODE;
		
	}
	
public String getIiDealPrice() {
		
		return II_DL_PRC;
		
	}
	
	public void setIiDealPrice(String II_DL_PRC) {
		
		this.II_DL_PRC = II_DL_PRC;
		
	}
	
	public String getIiUomPackaging() {
		
		return II_UOM_PCKGNG;
		
	}
	
	public void setIiUomPackaging(String II_UOM_PCKGNG) {
		
		this.II_UOM_PCKGNG = II_UOM_PCKGNG;
		
	}
	
	public String getIiRetailUomName() {
		
		return II_RETAIL_UOM_NM;
		
	}
	
	public void setIiRetailUomName(String II_RETAIL_UOM_NM) {
		
		this.II_RETAIL_UOM_NM = II_RETAIL_UOM_NM;
		
	}
	
	public String getIiDefaultLocationName() {
		
		return II_DFLT_LCTN_NM;
		
	}
	
	public void setIiDefaultLocationName(String II_DFLT_LCTN_NM) {
		
		this.II_DFLT_LCTN_NM = II_DFLT_LCTN_NM;
		
	}
	
	public double getIiAveCost() {
		
		return II_AVE_CST;
		
	}
	
	public void setIiAveCost(double II_AVE_CST) {
		
		this.II_AVE_CST = II_AVE_CST;
		
	}
	
	public double getIiQuantityOnHand() {
		
		return II_QOH;
		
	}
	
	public void setIiQuantityOnHand(double II_QOH) {
		
		this.II_QOH = II_QOH;
		
	}
	
	public byte getIiFixedAsset() {
		
		return II_FXD_ASST;
		
	}
	
	public void setIiFixedAsset(byte II_FXD_ASST) {
		
		this.II_FXD_ASST = II_FXD_ASST;
		
	}
	
	public Date getIiDateAcquired() {
		
		return II_DT_ACQRD;
		
	}
	
	public void setIiDateAcquired(Date II_DT_ACQRD) {
		
		this.II_DT_ACQRD = II_DT_ACQRD;
		
	}
	
	public byte getIiTraceMisc() {
		
		return II_TRC_MSC;
		
	}
	
	public void setIiTraceMisc(byte II_TRC_MSC) {
		
		this.II_TRC_MSC = II_TRC_MSC;
		
	}
	
	public double getAcquisitionCost() {

		return acquisitionCost;
	}

	public void setAcquisitionCost(double acquisitionCost) {

		this.acquisitionCost = acquisitionCost;
	}
	
	public double getResidualValue() {

		return ResidualValue;
	}

	public void setResidualValue(double ResidualValue) {

		this.ResidualValue = ResidualValue;
	}
	
	public double getLifeSpan() {

		return lifeSpan;
	}

	public void setLifeSpan(double lifeSpan) {

		this.lifeSpan = lifeSpan;
	}
	
	public String getFixedAssetAccount() {

		return fixedAssetAccount;
	}

	public void setFixedAssetAccount(String fixedAssetAccount) {

		this.fixedAssetAccount = fixedAssetAccount;
	}
	
	public String getFixedAssetAccountDescription() {
		return fixedAssetAccountDescription;
	}
	
	public void setFixedAssetAccountDescription(String fixedAssetAccountDescription) {
		this.fixedAssetAccountDescription = fixedAssetAccountDescription;
	}

	public String getDepreciationAccount() {

		return depreciationAccount;
	}

	public void setDepreciationAccount(String depreciationAccount) {

		this.depreciationAccount = depreciationAccount;
	}
	
	public String getDepreciationAccountDescription() {
		return depreciationAccountDescription;
	}
	
	public void setDepreciationAccountDescription(String depreciationAccountDescription) {
		this.depreciationAccountDescription = depreciationAccountDescription;
	}
	
	public String getAccumulatedDepreciationAccount() {

		return AccumulatedDepreciationAccount;
	}

	public void setAccumulatedDepreciationAccount(String AccumulatedDepreciationAccount) {

		this.AccumulatedDepreciationAccount = AccumulatedDepreciationAccount;
	}
	
	public String getAccumulatedDepreciationAccountDescription() {
		return AccumulatedDepreciationAccountDescription;
	}
	
	public void setAccumulatedDepreciationAccountDescription(String AccumulatedDepreciationAccountDescription) {
		this.AccumulatedDepreciationAccountDescription = AccumulatedDepreciationAccountDescription;
	}
	
	
	
	
	
	
	
	public String getLaborCostAccount() {

		return laborCostAccount;
	}

	public void setLaborCostAccount(String laborCostAccount) {

		this.laborCostAccount = laborCostAccount;
	}
	
	public String getLaborCostAccountDescription() {
		return laborCostAccountDescription;
	}
	
	public void setLaborCostAccountDescription(String laborCostAccountDescription) {
		this.laborCostAccountDescription = laborCostAccountDescription;
	}
	
	
	
	
	public String getPowerCostAccount() {

		return powerCostAccount;
	}

	public void setPowerCostAccount(String powerCostAccount) {

		this.powerCostAccount = powerCostAccount;
	}
	
	public String getPowerCostAccountDescription() {
		return powerCostAccountDescription;
	}
	
	public void setPowerCostAccountDescription(String powerCostAccountDescription) {
		this.powerCostAccountDescription = powerCostAccountDescription;
	}
	

	
	
	public String getOverHeadCostAccount() {

		return overHeadCostAccount;
	}

	public void setOverHeadCostAccount(String overHeadCostAccount) {

		this.overHeadCostAccount = overHeadCostAccount;
	}
	
	public String getOverHeadCostAccountDescription() {
		return overHeadCostAccountDescription;
	}
	
	public void setOverHeadCostAccountDescription(String overHeadCostAccountDescription) {
		this.overHeadCostAccountDescription = overHeadCostAccountDescription;
	}
	
	
	
	
	public String getFreightCostAccount() {

		return freightCostAccount;
	}

	public void setFreightCostAccount(String freightCostAccount) {

		this.freightCostAccount = freightCostAccount;
	}
	
	public String getFreightCostAccountDescription() {
		return freightCostAccountDescription;
	}
	
	public void setFreightCostAccountDescription(String freightCostAccountDescription) {
		this.freightCostAccountDescription = freightCostAccountDescription;
	}

	
	
	public String getFixedCostAccount() {

		return fixedCostAccount;
	}

	public void setFixedCostAccount(String fixedCostAccount) {

		this.fixedCostAccount = fixedCostAccount;
	}
	
	public String getFixedCostAccountDescription() {
		return fixedCostAccountDescription;
	}
	
	public void setFixedCostAccountDescription(String fixedCostAccountDescription) {
		this.fixedCostAccountDescription = fixedCostAccountDescription;
	}


	public String getIiCondition() {

		return II_CNDTN;

	}

	public void setIiCondition(String II_CNDTN) {

		this.II_CNDTN = II_CNDTN;

	}
	
	
	
	
	public static Comparator ItemCategoryGroupComparator = (r1, r2) -> {

        String II_ITM_CTGRY1 = ((InvModItemDetails) r1).getIiAdLvCategory();
        String II_ITM_NM1 = ((InvModItemDetails) r1).getIiName();

        String II_ITM_CTGRY2 = ((InvModItemDetails) r2).getIiAdLvCategory();
        String II_ITM_NM2 = ((InvModItemDetails) r2).getIiName();

        if(!(II_ITM_CTGRY1.equals(II_ITM_CTGRY2))){

            return II_ITM_CTGRY1.compareTo(II_ITM_CTGRY2);

        } else {

            return II_ITM_NM1.compareTo(II_ITM_NM2);

        }

    };
	
} // InvModItemDetails class   
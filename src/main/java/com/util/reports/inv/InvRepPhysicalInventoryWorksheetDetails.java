/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.inv;


public class InvRepPhysicalInventoryWorksheetDetails implements java.io.Serializable {
    
    private String PIW_PI_AD_LV_CTGRY;
    private String PIW_PI_LOC_NM;
    private String PIW_PIL_ITM_NM;
    private String PIW_PIL_ITM_DESC;
    private String PIW_PIL_ITM_CTGRY;
    private String PIW_PIL_UOM_NM;
    private double PIW_PIL_ACTL_QTY;
    private double PIW_PIL_ENDNG_INVNTRY;
    private double PIW_PIL_PIL_WSTG;
    private double PIW_PIL_PIL_VRNC;
    private double PIW_PIL_ACTL_CST;
    private double PIW_PIL_SLS_PRC;
    
    

    public InvRepPhysicalInventoryWorksheetDetails() {
    }
    
    public String getPiwPiAdLvCategory() {
        
        return PIW_PI_AD_LV_CTGRY;
        
    }
    
    public void setPiwPiAdLvCategory(String PIW_PI_AD_LV_CTGRY) {
        
        this.PIW_PI_AD_LV_CTGRY = PIW_PI_AD_LV_CTGRY;
        
    }
    
    public String getPiwPiLocationName() {
        
        return PIW_PI_AD_LV_CTGRY;
        
    }
    
    public void setPiwPiLocationName(String PIW_PI_AD_LV_CTGRY) {
        
        this.PIW_PI_AD_LV_CTGRY = PIW_PI_AD_LV_CTGRY;
        
    }
    
    public String getPiwPilItemName() {
        
        return PIW_PIL_ITM_NM;
        
    }
    
    public void setPiwPilItemName(String PIW_PIL_ITM_NM) {
        
        this.PIW_PIL_ITM_NM = PIW_PIL_ITM_NM;
        
    }
    
    public String getPiwPilItemDescription() {
        
        return PIW_PIL_ITM_DESC;
        
    }
    
    public void setPiwPilItemDescription(String PIW_PIL_ITM_DESC) {
        
        this.PIW_PIL_ITM_DESC = PIW_PIL_ITM_DESC;
        
    }
    
public String getPiwPilItemCategory() {
        
        return PIW_PIL_ITM_CTGRY;
        
    }
    
    public void setPiwPilItemCategory(String PIW_PIL_ITM_CTGRY) {
        
        this.PIW_PIL_ITM_CTGRY = PIW_PIL_ITM_CTGRY;
        
    }
    
    public String getPiwPilUnitMeasureName() {
        
        return PIW_PIL_UOM_NM;
        
    }
    
    public void setPiwPilUnitMeasureName(String PIW_PIL_UOM_NM) {
        
        this.PIW_PIL_UOM_NM = PIW_PIL_UOM_NM;
        
    }
    
    public double getPiwPilActualQty() {
        
        return PIW_PIL_ACTL_QTY;
        
    }
    
    public void setPiwPilActualQty(double PIW_PIL_ACTL_QTY) {
        
        this.PIW_PIL_ACTL_QTY = PIW_PIL_ACTL_QTY;
        
    }
    
    public double getPiwPilEndingInv() {
        
        return PIW_PIL_ENDNG_INVNTRY;
        
    }
    
    public void setPiwPilEndingInv(double PIW_PIL_ENDNG_INVNTRY) {
        
        this.PIW_PIL_ENDNG_INVNTRY= PIW_PIL_ENDNG_INVNTRY;
        
    }
    public double getPiwPilWastage() {
        
        return PIW_PIL_PIL_WSTG;
        
    }
    
    public void setPiwPilWastage(double PIW_PIL_PIL_WSTG) {
        
        this.PIW_PIL_PIL_WSTG= PIW_PIL_PIL_WSTG;
        
    }
    
    public double getPiwPilVariance() {
        
        return PIW_PIL_PIL_VRNC;
        
    }
    
    public void setPiwPilVariance(double PIW_PIL_PIL_VRNC) {
        
        this.PIW_PIL_PIL_VRNC = PIW_PIL_PIL_VRNC;
        
    }

    public double getPiwPilActualCost() {

    	return PIW_PIL_ACTL_CST;

    }

    public void setPiwPilActualCost(double PIW_PIL_ACTL_CST) {

    	this.PIW_PIL_ACTL_CST = PIW_PIL_ACTL_CST;

    }
    
    public double getPiwPilSalesPrice() {

    	return PIW_PIL_SLS_PRC;

    }

    public void setPiwPilSalesPrice(double PIW_PIL_SLS_PRC) {

    	this.PIW_PIL_SLS_PRC = PIW_PIL_SLS_PRC;

    }
    
}  // InvPhysicalInventoryWorksheetPrintDetails
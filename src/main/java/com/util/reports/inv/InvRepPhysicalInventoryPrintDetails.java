/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.inv;

import java.util.Comparator;
import java.util.Date;


public class InvRepPhysicalInventoryPrintDetails implements java.io.Serializable {
    
    private Date PIP_PI_DT;
    private String PIP_PI_RFRNC_NMBR;
    private String PIP_PI_DESC;
    private String PIP_PI_GL_COA_ACCNT;
    private String PIP_PI_GL_COA_ACCNT_DESC;
    private String PIP_PI_CRTD_BY;

    
    private String PIP_PL_II_NM;
    private String PIP_PL_II_DESC;
    private String PIP_PL_LOC_NM;
    private String PIP_PL_UOM_NM;
    
    private double PIP_PL_UNT_CST;
    private double PIP_PL_ENDNG_INVNTRY;
    private double PIP_PL_WSTG;
    private double PIP_PL_VRNC;
    
    		
    public InvRepPhysicalInventoryPrintDetails() {
    }

    public Date getPipPiDate() {
        
        return PIP_PI_DT;
        
    }
    
    public void setPipPiDate(Date PIP_PI_DT) {
        
        this.PIP_PI_DT = PIP_PI_DT;
        
    }

    
    public String getPipPiReferenceNumber() {
        
        return PIP_PI_RFRNC_NMBR;
        
    }
    
    public void setPipPiReferenceNumber(String PIP_PI_RFRNC_NMBR) {
        
        this.PIP_PI_RFRNC_NMBR = PIP_PI_RFRNC_NMBR;
        
    }
    
    public String getPipPiDescription() {
        
        return PIP_PI_DESC;
        
    }
    
    public void setPipPiDescription(String PIP_PI_DESC) {
        
        this.PIP_PI_DESC = PIP_PI_DESC;
        
    }
    
    public String getPipPiGlCoaAccount() {
        
        return PIP_PI_GL_COA_ACCNT;
        
    }
    
    public void setPipPiGlCoaAccount(String PIP_PI_GL_COA_ACCNT) {
        
        this.PIP_PI_GL_COA_ACCNT = PIP_PI_GL_COA_ACCNT;
        
    }
    
    public String getPipPiGlCoaAccountDesc() {
        
        return PIP_PI_GL_COA_ACCNT_DESC;
        
    }
    
    public void setPipPiGlCoaAccountDesc(String PIP_PI_GL_COA_ACCNT_DESC) {
        
        this.PIP_PI_GL_COA_ACCNT_DESC = PIP_PI_GL_COA_ACCNT_DESC;
        
    }

    public String getPipPiCreatedBy() {
        
        return PIP_PI_CRTD_BY;
        
    }
    
    public void setPipPiCreatedBy(String PIP_PI_CRTD_BY) {
        
        this.PIP_PI_CRTD_BY = PIP_PI_CRTD_BY;
        
    }
    

    
    public String getPipPlIiName() {
        
        return PIP_PL_II_NM;
        
    }
    
    public void setPipPlIiName(String PIP_PL_II_NM) {
        
        this.PIP_PL_II_NM = PIP_PL_II_NM;
        
    }
    
    public String getPipPlIiDescription() {
        
        return PIP_PL_II_DESC;
        
    }
    
    public void setPipPlIiDescription(String PIP_PL_II_DESC) {
        
        this.PIP_PL_II_DESC = PIP_PL_II_DESC;
        
    }
    
    public String getPipPlLocName() {
        
        return PIP_PL_LOC_NM;
        
    }
    
    public void setPipPlLocName(String PIP_PL_LOC_NM) {
        
        this.PIP_PL_LOC_NM = PIP_PL_LOC_NM;
        
    }
    
    public String getPipPlUomName() {
        
        return PIP_PL_UOM_NM;
        
    }
    
    public void setPipPlUomName(String PIP_PL_UOM_NM) {
        
        this.PIP_PL_UOM_NM = PIP_PL_UOM_NM;
        
    }
    
    public double getPipPlUnitCost() {
        
        return PIP_PL_UNT_CST;
        
    }
    
    public void setPipPlUnitCost(double PIP_PL_UNT_CST) {
        
        this.PIP_PL_UNT_CST = PIP_PL_UNT_CST;
        
    }
    
    public double getPipPlEndingInventory() {
        
        return PIP_PL_ENDNG_INVNTRY;
        
    }
    
    public void setPipPlEndingInventory(double PIP_PL_ENDNG_INVNTRY) {
        
        this.PIP_PL_ENDNG_INVNTRY = PIP_PL_ENDNG_INVNTRY;
        
    }
    
    
    
   public double getPipPlWastage() {
        
        return PIP_PL_WSTG;
        
    }
    
    public void setPipPlWastage(double PIP_PL_WSTG) {
        
        this.PIP_PL_WSTG = PIP_PL_WSTG;
        
    }
    public double getPipPlVariance() {
        
        return PIP_PL_VRNC;
        
    }
    
    public void setPipPlVariance(double PIP_PL_VRNC) {
        
        this.PIP_PL_VRNC = PIP_PL_VRNC;
        
    }
    
    
 

    
    
	   public static Comparator ItemComparator = (ADJ, anotherADJ) -> {

           String ADJ_II_NM1 = ((InvRepPhysicalInventoryPrintDetails) ADJ).getPipPlIiName();


           String ADJ_II_NM2 = ((InvRepPhysicalInventoryPrintDetails) anotherADJ).getPipPlIiName();

           if (!(ADJ_II_NM1.equals(ADJ_II_NM2))) {

               return ADJ_II_NM1.compareTo(ADJ_II_NM2);

           } else {

               return ADJ_II_NM1.compareTo(ADJ_II_NM2);

           }

       };
    
}  // InvRepAdjustmentPrintDetails
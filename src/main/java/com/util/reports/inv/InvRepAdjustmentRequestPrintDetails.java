/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.inv;

import java.util.Date;


public class InvRepAdjustmentRequestPrintDetails implements java.io.Serializable {
    
    private String AP_ADJ_TYP;
    private Date AP_ADJ_DT;
    private String AP_ADJ_DCMNT_NMBR;
    private String AP_ADJ_RFRNC_NMBR;
    private String AP_ADJ_DESC;
    private String AP_ADJ_GL_COA_ACCNT;
    private String AP_ADJ_GL_COA_ACCNT_DESC;
    private String AP_ADJ_APPRVD_BY;
    private String AP_ADJ_CRTD_BY;
    
    private String AP_AL_II_NM;
    private String AP_AL_II_DESC;
    private String AP_AL_LOC_NM;
    private String AP_AL_UOM_NM;
    private String AP_AL_BR_NM;
    private double AP_AL_UNT_CST;
    private double AP_AL_ACTL_QNTTY;
    private double AP_AL_ADJST_QNTTY;
    private double AP_AL_AVE_CST;
    private double AP_AL_BR_CD;
    		
    public InvRepAdjustmentRequestPrintDetails() {
    }
    
    public String getApAdjType() {
        
        return AP_ADJ_TYP;
        
    }
    
    public void setApAdjType(String AP_ADJ_TYP) {
        
        this.AP_ADJ_TYP = AP_ADJ_TYP;
        
    }
    
    public Date getApAdjDate() {
        
        return AP_ADJ_DT;
        
    }
    
    public void setApAdjDate(Date AP_ADJ_DT) {
        
        this.AP_ADJ_DT = AP_ADJ_DT;
        
    }
    
    public String getApAdjDocumentNumber() {
        
        return AP_ADJ_DCMNT_NMBR;
        
    }
    
    public void setApAdjDocumentNumber(String AP_ADJ_DCMNT_NMBR) {
        
        this.AP_ADJ_DCMNT_NMBR = AP_ADJ_DCMNT_NMBR;
        
    }
    
    public String getApAdjReferenceNumber() {
        
        return AP_ADJ_RFRNC_NMBR;
        
    }
    
    public void setApAdjReferenceNumber(String AP_ADJ_RFRNC_NMBR) {
        
        this.AP_ADJ_RFRNC_NMBR = AP_ADJ_RFRNC_NMBR;
        
    }
    
    public String getApAdjDescription() {
        
        return AP_ADJ_DESC;
        
    }
    
    public void setApAdjDescription(String AP_ADJ_DESC) {
        
        this.AP_ADJ_DESC = AP_ADJ_DESC;
        
    }
    
    public String getApAdjGlCoaAccount() {
        
        return AP_ADJ_GL_COA_ACCNT;
        
    }
    
    public void setApAdjGlCoaAccount(String AP_ADJ_GL_COA_ACCNT) {
        
        this.AP_ADJ_GL_COA_ACCNT = AP_ADJ_GL_COA_ACCNT;
        
    }
    
    public String getApAdjGlCoaAccountDesc() {
        
        return AP_ADJ_GL_COA_ACCNT_DESC;
        
    }
    
    public void setApAdjGlCoaAccountDesc(String AP_ADJ_GL_COA_ACCNT_DESC) {
        
        this.AP_ADJ_GL_COA_ACCNT_DESC = AP_ADJ_GL_COA_ACCNT_DESC;
        
    }
    

    public String getApAdjApprovedBy() {
        
        return AP_ADJ_APPRVD_BY;
        
    }
    
    public void setApAdjApprovedBy(String AP_ADJ_APPRVD_BY) {
        
        this.AP_ADJ_APPRVD_BY = AP_ADJ_APPRVD_BY;
        
    }
    
    
    
    public String getApAdjCreatedBy() {
        
        return AP_ADJ_CRTD_BY;
        
    }
    
    public void setApAdjCreatedBy(String AP_ADJ_CRTD_BY) {
        
        this.AP_ADJ_CRTD_BY = AP_ADJ_CRTD_BY;
        
    }
    
    public String getApAlIiName() {
        
        return AP_AL_II_NM;
        
    }
    
    public void setApAlIiName(String AP_AL_II_NM) {
        
        this.AP_AL_II_NM = AP_AL_II_NM;
        
    }
    
    public String getApAlIiDescription() {
        
        return AP_AL_II_DESC;
        
    }
    
    public void setApAlIiDescription(String AP_AL_II_DESC) {
        
        this.AP_AL_II_DESC = AP_AL_II_DESC;
        
    }
    
    public String getApAlLocName() {
        
        return AP_AL_LOC_NM;
        
    }
    
    public void setApAlLocName(String AP_AL_LOC_NM) {
        
        this.AP_AL_LOC_NM = AP_AL_LOC_NM;
        
    }
    
    public String getApAlUomName() {
        
        return AP_AL_UOM_NM;
        
    }
    
    public void setApAlUomName(String AP_AL_UOM_NM) {
        
        this.AP_AL_UOM_NM = AP_AL_UOM_NM;
        
    }
    
   public String getApAlBranchName() {
        
        return AP_AL_BR_NM;
        
    }
    
    public void setApAlBranchName(String AP_AL_BR_NM) {
        
        this.AP_AL_BR_NM = AP_AL_BR_NM;
        
    }
    public double getApAlUnitCost() {
        
        return AP_AL_UNT_CST;
        
    }
    
    public void setApAlUnitCost(double AP_AL_UNT_CST) {
        
        this.AP_AL_UNT_CST = AP_AL_UNT_CST;
        
    }
    
   public double getApAlActualQuantity() {
        
        return AP_AL_ACTL_QNTTY;
        
    }
    
    public void setApAlActualQuantity(double AP_AL_ACTL_QNTTY) {
        
        this.AP_AL_ACTL_QNTTY = AP_AL_ACTL_QNTTY;
        
    }
    public double getApAlAdjustQuantity() {
        
        return AP_AL_ADJST_QNTTY;
        
    }
    
    public void setApAlAdjustQuantity(double AP_AL_ADJST_QNTTY) {
        
        this.AP_AL_ADJST_QNTTY = AP_AL_ADJST_QNTTY;
        
    }
    
    
    public double getApAlAveCost() {
        
        return AP_AL_AVE_CST;
        
    }
    
    public void setApAlAveCost(double AP_AL_AVE_CST) {
        
        this.AP_AL_AVE_CST = AP_AL_AVE_CST;
        
    }
   public double getApAlBranchCode() {
        
        return AP_AL_BR_CD;
        
    }
    
    public void setApAlBranchCode(double AP_AL_BR_CD) {
        
        this.AP_AL_BR_CD = AP_AL_BR_CD;
        
    }
    
}  // InvRepAdjustmentRequestPrintDetails
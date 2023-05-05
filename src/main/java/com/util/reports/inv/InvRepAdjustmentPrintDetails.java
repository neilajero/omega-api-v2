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


public class InvRepAdjustmentPrintDetails implements java.io.Serializable {
    
	 private String AP_ADJ_TYP;
	    private Date AP_ADJ_DT;
	    private String AP_ADJ_SPL_NM;
	    private String AP_ADJ_SPL_ADDRSS;
	    private String AP_ADJ_DCMNT_NMBR;
	    private String AP_ADJ_RFRNC_NMBR;
	    private String AP_ADJ_DESC;
	    private String AP_ADJ_GL_COA_ACCNT;
	    private String AP_ADJ_GL_COA_ACCNT_DESC;
	    private String AP_ADJ_APPRVD_BY;
	    private String AP_ADJ_CRTD_BY;
	    private String AP_ADJ_CRTD_BY_PSTN;
	    private String AP_ADJ_PSTD_BY;
	    private String AP_ADJ_NT_BY;
	    
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
	    
	    private boolean AP_AL_ISSERVICE;
		private boolean AP_AL_ISINVENTORIABLE;
	    private String AP_TG_SPCS;
		private String AP_TG_DCMNT_NMBR;
		private String AP_TG_SRL_NMBR;
		private Date AP_TG_EXPRY_DT;
		private String AP_TG_PRPRTY_CD;
		private String AP_TG_CSTDN;
		private String AP_TG_CSTDN_PSTN;
		private byte AP_ADJ_PSTD;
    		
    public InvRepAdjustmentPrintDetails() {
    }
    
    public Boolean getApAlIsService() {
		
		return AP_AL_ISSERVICE;
		
	}
	
	public void setApAlIsService(Boolean AP_AL_ISSERVICE) {
		
		this.AP_AL_ISSERVICE = AP_AL_ISSERVICE;
		
	}
	
	public Boolean getApAlIsInventoriable() {
		
		return AP_AL_ISINVENTORIABLE;
		
	}
	
	public void setApAlIsInventoriable(Boolean AP_AL_ISINVENTORIABLE) {
		
		this.AP_AL_ISINVENTORIABLE = AP_AL_ISINVENTORIABLE;
		
	}
	
    public String getApTgSpecs() {
        
        return AP_TG_SPCS;
        
    }
    
    public void setApTgSpecs(String AP_TG_SPCS) {
        
        this.AP_TG_SPCS = AP_TG_SPCS;
        
    }
    
    public String getApTgDocumentNumber() {
        
        return AP_TG_DCMNT_NMBR;
        
    }
    
    public void setApTgDocumentNumber(String AP_TG_DCMNT_NMBR) {
        
        this.AP_TG_DCMNT_NMBR = AP_TG_DCMNT_NMBR;
        
    }
    
    public String getApTgSerialNumber() {
        
        return AP_TG_SRL_NMBR;
        
    }
    
    public void setApTgSerialNumber(String AP_TG_SRL_NMBR) {
        
        this.AP_TG_SRL_NMBR = AP_TG_SRL_NMBR;
        
    }
    
    public Date getApTgExpiryDate() {
        
        return AP_TG_EXPRY_DT;
        
    }
    
    public void setApTgExpiryDate(Date AP_TG_EXPRY_DT) {
        
        this.AP_TG_EXPRY_DT = AP_TG_EXPRY_DT;
        
    }
    
    public String getApTgPropertyCode() {
        
        return AP_TG_PRPRTY_CD;
        
    }
    
    public void setApTgPropertyCode(String AP_TG_PRPRTY_CD) {
        
        this.AP_TG_PRPRTY_CD = AP_TG_PRPRTY_CD;
        
    }
    
    public String getApTgCustodian() {
        
        return AP_TG_CSTDN;
        
    }
    
    public void setApTgCustodian(String AP_TG_CSTDN) {
        
        this.AP_TG_CSTDN = AP_TG_CSTDN;
        
    }
    
    public String getApTgCustodianPosition() {
        
        return AP_TG_CSTDN_PSTN;
        
    }
    
    public void setApTgCustodianPosition(String AP_TG_CSTDN_PSTN) {
        
        this.AP_TG_CSTDN_PSTN = AP_TG_CSTDN_PSTN;
        
    }
    
   public String getApAdjType() {
        
        return AP_ADJ_TYP;
        
    }
    
    public void setApAdjType(String AP_ADJ_TYP) {
        
        this.AP_ADJ_TYP = AP_ADJ_TYP;
        
    }
    
  public String getApAdjNotedBy() {
        
        return AP_ADJ_NT_BY;
        
    }
    
    public void setApAdjNotedBy(String AP_ADJ_NT_BY) {
        
        this.AP_ADJ_NT_BY = AP_ADJ_NT_BY;
        
    }
    
    public Date getApAdjDate() {
        
        return AP_ADJ_DT;
        
    }
    
    public void setApAdjDate(Date AP_ADJ_DT) {
        
        this.AP_ADJ_DT = AP_ADJ_DT;
        
    }

public String getApAdjSupplierName() {
        
        return AP_ADJ_SPL_NM;
        
    }
    
    public void setApAdjSupplierName(String AP_ADJ_SPL_NM) {
        
        this.AP_ADJ_SPL_NM = AP_ADJ_SPL_NM;
        
    }
 
public String getApAdjSupplierAddress() {
        
        return AP_ADJ_SPL_ADDRSS;
        
    }
    
    public void setApAdjSupplierAddress(String AP_ADJ_SPL_ADDRSS) {
        
        this.AP_ADJ_SPL_ADDRSS = AP_ADJ_SPL_ADDRSS;
        
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
    
public String getApAdjCreatedByPosition() {
        
        return AP_ADJ_CRTD_BY_PSTN;
        
    }
    
    public void setApAdjCreatedByPosition(String AP_ADJ_CRTD_BY_PSTN) {
        
        this.AP_ADJ_CRTD_BY_PSTN = AP_ADJ_CRTD_BY_PSTN;
        
    }
    
    
 public String getApAdjPostedBy() {
        
        return AP_ADJ_PSTD_BY;
        
    }
    
    public void setApAdjPostedBy(String AP_ADJ_PSTD_BY) {
        
        this.AP_ADJ_PSTD_BY = AP_ADJ_PSTD_BY;
        
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
    
    
    public byte getApAdjPosted() {
        
        return AP_ADJ_PSTD;
        
    }
    
    public void setApAdjPosted(byte AP_ADJ_PSTD) {
        
        this.AP_ADJ_PSTD = AP_ADJ_PSTD;
        
    }
    
    
    
	   public static Comparator ItemComparator = (ADJ, anotherADJ) -> {

           String ADJ_II_NM1 = ((InvRepAdjustmentPrintDetails) ADJ).getApAlIiName();


           String ADJ_II_NM2 = ((InvRepAdjustmentPrintDetails) anotherADJ).getApAlIiName();




           if (!(ADJ_II_NM1.equals(ADJ_II_NM2))) {

               return ADJ_II_NM1.compareTo(ADJ_II_NM2);

           } else {

               return ADJ_II_NM1.compareTo(ADJ_II_NM2);

           }

       };
    
}  // InvRepAdjustmentPrintDetails
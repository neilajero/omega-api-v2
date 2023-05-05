/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ap;

import java.util.Comparator;
import java.util.Date;

public class ApRepPurchaseOrderDetails implements java.io.Serializable {

	private Date PO_DT;
	private String PO_BRNCH_CD;
	private String PO_BRNCH_NM;
	private String PO_II_NM;
	private String PO_II_DESC;
	private String PO_LOC_NM;
	private String PO_DCMNT_NMBR;
	private String PO_NMBR;
	private String PO_SPL_NM;
	private String PO_UNT;
	private double PO_QTY;
	private double PO_UNT_CST;
	private double PO_AMNT;
	private double PO_QTY_RCVD;
	
	private String PO_VD;
	private String PO_CRTD_BY;
	private String PO_APPRVD_RJCTD_BY;
	private String PO_DESC;
	private String PO_RFRNC_NMBR;
	private String PO_PYMNT_TRM_NM;
	private String PO_SPL_ADDRSS;
	private String PO_SPL_PHN_NMBR;
	private String PO_SPL_FX_NMBR;
	
	private String ORDER_BY;

	public ApRepPurchaseOrderDetails() {
    }
	
	public Date getPoDate() {
		
		return PO_DT;
		
	}
	
	public void setPoDate(Date PO_DT) {
		
		this.PO_DT = PO_DT;
		
	}
	
	public String getPoDocNo() {
		
		return PO_DCMNT_NMBR;
		
	}
	
	public void setPoDocNo(String PO_DCMNT_NMBR) {
		
		this.PO_DCMNT_NMBR = PO_DCMNT_NMBR;
		
	}
	
	
	public String getPoNumber() {
		
		return PO_NMBR;
		
	}
	
	public void setPoNumber(String PO_NMBR) {
		
		this.PO_NMBR = PO_NMBR;
		
	}
	
	public double getPoAmount() {
		
		return PO_AMNT;
	
	}
	
	public void setPoAmount(double PO_AMNT) {
	
		this.PO_AMNT = PO_AMNT;
	
	}
	
	public String getPoItemDescription() {
	
		return PO_II_DESC;
	
	}
	
	public void setPoItemDescription(String PO_II_DESC) {
	
		this.PO_II_DESC = PO_II_DESC;
	
	}
	
	public String getPoBranchCode() {
		
		return PO_BRNCH_CD;
	
	}
	
	public void setPoBranchCode(String PO_BRNCH_CD) {
	
		this.PO_BRNCH_CD = PO_BRNCH_CD;
	
	}
	
	   public String getPoBranchName() {
        
        return PO_BRNCH_NM;
    
    }
    
    public void setPoBranchName(String PO_BRNCH_NM) {
    
        this.PO_BRNCH_NM = PO_BRNCH_NM;
    
    }
	
	public String getPoItemName() {
	
		return PO_II_NM;
	
	}
	
	public void setPoItemName(String PO_II_NM) {
	
		this.PO_II_NM = PO_II_NM;
	
	}
	
	public double getPoQuantity() {
	
		return PO_QTY;
	
	}
	
	public void setPoQuantity(double PO_QTY) {
	
		this.PO_QTY = PO_QTY;
	
	}
	
	public String getPoLocation() {
	
		return PO_LOC_NM;
	
	}
	
	public void setPoLocation(String PO_LOC_NM) {
	
		this.PO_LOC_NM = PO_LOC_NM;
	
	}
	
	public double getPoUnitCost() {
	
		return PO_UNT_CST;
	
	}
	
	public void setPoUnitCost(double PO_UNT_CST) {
	
		this.PO_UNT_CST = PO_UNT_CST;
	
	}
	
	public String getPoSupplierName() {
		
		return PO_SPL_NM;

	}
	
	public void setPoSupplierName(String PO_SPL_NM) {
		
		this.PO_SPL_NM = PO_SPL_NM;
		
	}
	
	public String getPoUnit() {
		
		return PO_UNT;

	}
	
	public void setPoUnit(String PO_UNT) {
		
		this.PO_UNT = PO_UNT;
		
	}
	
	public double getPoQuantityReceived() {
		
			return PO_QTY_RCVD;
		
		}
		
	public void setPoQuantityReceived(double PO_QTY_RCVD) {
		
		this.PO_QTY_RCVD = PO_QTY_RCVD;
		
	}
	
	public String getPoVoid() {
		
		return PO_VD;
		
	}
	
	public void setPoVoid(String PO_VD) {
		
		this.PO_VD = PO_VD;
		
	}
	
	public String getPoCreatedBy() {
		
		return PO_CRTD_BY;
		
	}
	
	public void setPoCreatedBy(String PO_CRTD_BY) {
		
		this.PO_CRTD_BY = PO_CRTD_BY;
		
	}
	
	public String getPoApprovedRejectedBy() {
		
		return PO_APPRVD_RJCTD_BY;
		
	}
	
	public void setPoApprovedRejectedBy(String PO_APPRVD_RJCTD_BY) {
		
		this.PO_APPRVD_RJCTD_BY = PO_APPRVD_RJCTD_BY;
		
	}
	
	public String getPoDescription() {
		
		return PO_DESC;
		
	}
	
	public void setPoDescription(String PO_DESC) {
		
		this.PO_DESC = PO_DESC;
		
	}
	
	public String getPoReferenceNumber() {
		
		return PO_RFRNC_NMBR;
		
	}
	
	public void setPoReferenceNumber(String PO_RFRNC_NMBR) {
		
		this.PO_RFRNC_NMBR = PO_RFRNC_NMBR;
		
	}
	
	public String getPoPaymentTermName() {
		
		return PO_PYMNT_TRM_NM;
		
	}
	
	public void setPoPaymentTermName(String PO_PYMNT_TRM_NM) {
		
		this.PO_PYMNT_TRM_NM = PO_PYMNT_TRM_NM;
		
	}
	
	public String getPoSupplierAddress() {
		
		return PO_SPL_ADDRSS;
		
	}
	
	public void setPoSupplierAddress(String PO_SPL_ADDRSS) {
		
		this.PO_SPL_ADDRSS = PO_SPL_ADDRSS;
		
	}
	
	public String getPoSupplierPhoneNumber() {
		
		return PO_SPL_PHN_NMBR;
		
	}
	
	public void setPoSupplierPhoneNumber(String PO_SPL_PHN_NMBR) {
		
		this.PO_SPL_PHN_NMBR = PO_SPL_PHN_NMBR;
		
	}
	
	public String getPoSupplierFaxNumber() {
		
		return PO_SPL_FX_NMBR;
		
	}
	
	public void setPoSupplierFaxNumber(String PO_SPL_FX_NMBR) {
		
		this.PO_SPL_FX_NMBR = PO_SPL_FX_NMBR;
		
	}
	public String getOrderBy() {
	   	
	   	  return ORDER_BY;
	   	
	   }
	   
	   public void setOrderBy(String ORDER_BY) {
	   	
	   	  this.ORDER_BY = ORDER_BY;
	   	
	   }
	
public static Comparator ItemNameComparator = (PO, anotherPO) -> {

    String PO_II_NM1 = ((ApRepPurchaseOrderDetails) PO).getPoItemName();
    Date PO_DT1 = ((ApRepPurchaseOrderDetails) PO).getPoDate();
    String PO_DCMNT_NMBR1 = ((ApRepPurchaseOrderDetails) PO).getPoDocNo();

    String PO_II_NM2 = ((ApRepPurchaseOrderDetails) anotherPO).getPoItemName();
    Date PO_DT2 = ((ApRepPurchaseOrderDetails) anotherPO).getPoDate();
    String PO_DCMNT_NMBR2 = ((ApRepPurchaseOrderDetails) anotherPO).getPoDocNo();

    String ORDER_BY = ((ApRepPurchaseOrderDetails) PO).getOrderBy();

    if (!(PO_II_NM1.equals(PO_II_NM2))) {

        return PO_II_NM1.compareTo(PO_II_NM2);

    } else {

        if(ORDER_BY.equals("DATE") && !(PO_DT1.equals(PO_DT2))){

            return PO_DT1.compareTo(PO_DT2);

        } else {

            return PO_DCMNT_NMBR1.compareTo(PO_DCMNT_NMBR2);

        }
    }

};
	   
} // ApRepPurchaseOrderDetails class
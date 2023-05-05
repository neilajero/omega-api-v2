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

public class InvRepBuildUnbuildAssemblyOrderDetails implements java.io.Serializable {

	private Date BO_DT;
	private String BO_BRNCH_CD;
	private String BO_II_NM;
	private String BO_II_DESC;
	private String BO_LOC_NM;
	private String BO_DCMNT_NMBR;
	private String BO_NMBR;
	private String BO_CST_NM;
	private String BO_UNT;
	private double BO_QTY;
	private double BO_SPCFC_GRVTY;
	private double BO_STNDRD_FLL_SZ;
	private double BO_UNT_CST;
	private double BO_AMNT;
	private double BO_RCVD;
	private double BO_RMNNG;
	
	private String BO_VD;
	private String BO_CRTD_BY;
	private String BO_APPRVD_RJCTD_BY;
	private String BO_DESC;
	private String BO_RFRNC_NMBR;
	private String BO_PYMNT_TRM_NM;
	private String BO_CST_ADDRSS;
	private String BO_CST_PHN_NMBR;
	private String BO_CST_FX_NMBR;
	
	private double BO_BM_QTY_NDD;
    private double BO_BM_QTY_ON_HND;
    private double BO_BM_CST;
    private String BO_BM_II_NM;
    private String BO_BM_II_CD;
    private String BO_BM_UOM_NM;
    private String BO_BM_CTGRY;
	
	private String ORDER_BY;

	public InvRepBuildUnbuildAssemblyOrderDetails() {
    }
	
	public Date getBoDate() {
		
		return BO_DT;
		
	}
	
	public void setBoDate(Date BO_DT) {
		
		this.BO_DT = BO_DT;
		
	}
	
	public String getBoDocNo() {
		
		return BO_DCMNT_NMBR;
		
	}
	
	public void setBoDocNo(String BO_DCMNT_NMBR) {
		
		this.BO_DCMNT_NMBR = BO_DCMNT_NMBR;
		
	}
	
	
	public String getBoNumber() {
		
		return BO_NMBR;
		
	}
	
	public void setBoNumber(String BO_NMBR) {
		
		this.BO_NMBR = BO_NMBR;
		
	}
	
	public double getBoAmount() {
		
		return BO_AMNT;
	
	}
	
	public void setBoAmount(double BO_AMNT) {
	
		this.BO_AMNT = BO_AMNT;
	
	}
	
	public String getBoItemDescription() {
	
		return BO_II_DESC;
	
	}
	
	public void setBoItemDescription(String BO_II_DESC) {
	
		this.BO_II_DESC = BO_II_DESC;
	
	}
	
	public String getBoBranchCode() {
		
		return BO_BRNCH_CD;
	
	}
	
	public void setBoBranchCode(String BO_BRNCH_CD) {
	
		this.BO_BRNCH_CD = BO_BRNCH_CD;
	
	}
	
	public String getBoItemName() {
	
		return BO_II_NM;
	
	}
	
	public void setBoItemName(String BO_II_NM) {
	
		this.BO_II_NM = BO_II_NM;
	
	}
	
	public double getBoQuantity() {
	
		return BO_QTY;
	
	}
	
	public void setBoQuantity(double BO_QTY) {
	
		this.BO_QTY = BO_QTY;
	
	}
	public double getBoSpecificGravity() {
		
		return BO_SPCFC_GRVTY;
	
	}
	
	public void setBoSpecificGravity(double BO_SPCFC_GRVTY) {
	
		this.BO_SPCFC_GRVTY = BO_SPCFC_GRVTY;
	
	}

public double getBoStandardFillSize() {
		
		return BO_STNDRD_FLL_SZ;
	
	}
	
	public void setBoStandardFillSize(double BO_STNDRD_FLL_SZ) {
	
		this.BO_STNDRD_FLL_SZ = BO_STNDRD_FLL_SZ;
	
	}
	
	public String getBoLocation() {
	
		return BO_LOC_NM;
	
	}
	
	public void setBoLocation(String BO_LOC_NM) {
	
		this.BO_LOC_NM = BO_LOC_NM;
	
	}
	
	public double getBoUnitCost() {
	
		return BO_UNT_CST;
	
	}
	
	public void setBoUnitCost(double BO_UNT_CST) {
	
		this.BO_UNT_CST = BO_UNT_CST;
	
	}
	
	public String getBoCustomerName() {
		
		return BO_CST_NM;

	}
	
	public void setBoCustomerName(String BO_CST_NM) {
		
		this.BO_CST_NM = BO_CST_NM;
		
	}
	
	public String getBoUnit() {
		
		return BO_UNT;

	}
	
	public void setBoUnit(String BO_UNT) {
		
		this.BO_UNT = BO_UNT;
		
	}
	
	public double getBoReceived() {
		
			return BO_RCVD;
		
		}
		
	public void setBoReceived(double BO_RCVD) {
		
		this.BO_RCVD = BO_RCVD;
		
	}
	
	public double getBoRemaining() {
		
		return BO_RMNNG;
	
	}
	
	public void setBoRemaining(double BO_RMNNG) {
		
		this.BO_RMNNG = BO_RMNNG;
		
	}
	
	public String getBoVoid() {
		
		return BO_VD;
		
	}
	
	public void setBoVoid(String BO_VD) {
		
		this.BO_VD = BO_VD;
		
	}
	
	public String getBoCreatedBy() {
		
		return BO_CRTD_BY;
		
	}
	
	public void setBoCreatedBy(String BO_CRTD_BY) {
		
		this.BO_CRTD_BY = BO_CRTD_BY;
		
	}
	
	public String getBoApprovedRejectedBy() {
		
		return BO_APPRVD_RJCTD_BY;
		
	}
	
	public void setBoApprovedRejectedBy(String BO_APPRVD_RJCTD_BY) {
		
		this.BO_APPRVD_RJCTD_BY = BO_APPRVD_RJCTD_BY;
		
	}
	
	public String getBoDescription() {
		
		return BO_DESC;
		
	}
	
	public void setBoDescription(String BO_DESC) {
		
		this.BO_DESC = BO_DESC;
		
	}
	
	public String getBoReferenceNumber() {
		
		return BO_RFRNC_NMBR;
		
	}
	
	public void setBoReferenceNumber(String BO_RFRNC_NMBR) {
		
		this.BO_RFRNC_NMBR = BO_RFRNC_NMBR;
		
	}
	
	public String getBoPaymentTermName() {
		
		return BO_PYMNT_TRM_NM;
		
	}
	
	public void setBoPaymentTermName(String BO_PYMNT_TRM_NM) {
		
		this.BO_PYMNT_TRM_NM = BO_PYMNT_TRM_NM;
		
	}
	
	public String getBoCustomerAddress() {
		
		return BO_CST_ADDRSS;
		
	}
	
	public void setBoCustomerAddress(String BO_CST_ADDRSS) {
		
		this.BO_CST_ADDRSS = BO_CST_ADDRSS;
		
	}
	
	public String getBoCustomerPhoneNumber() {
		
		return BO_CST_PHN_NMBR;
		
	}
	
	public void setBoCustomerPhoneNumber(String BO_CST_PHN_NMBR) {
		
		this.BO_CST_PHN_NMBR = BO_CST_PHN_NMBR;
		
	}
	
	public String getBoCustomerFaxNumber() {
		
		return BO_CST_FX_NMBR;
		
	}
	
	public void setBoCustomerFaxNumber(String BO_CST_FX_NMBR) {
		
		this.BO_CST_FX_NMBR = BO_CST_FX_NMBR;
		
	}
	public String getOrderBy() {
	   	
	   	  return ORDER_BY;
	   	
	   }
	   
	   public void setOrderBy(String ORDER_BY) {
	   	
	   	  this.ORDER_BY = ORDER_BY;
	   	
	   }
	   
	   
	   
	   public double getBoBmQuantityNeeded() {
	        
	        return BO_BM_QTY_NDD;
	        
	    }
	    
	    public void setBoBmQuantityNeeded(double BO_BM_QTY_NDD) {
	        
	        this.BO_BM_QTY_NDD = BO_BM_QTY_NDD;
	        
	    }

	    public double getBoBmQuantityOnHand() {
	        
	        return BO_BM_QTY_ON_HND;
	        
	    }
	    
	    public void setBoBmQuantityOnHand(double BO_BM_QTY_ON_HND) {
	        
	        this.BO_BM_QTY_ON_HND = BO_BM_QTY_ON_HND;
	        
	    }
	    
	    
	    public double getBoBmCost() {
	        
	        return BO_BM_CST;
	        
	    }
	    
	    public void setBoBmCost(double BO_BM_CST) {
	        
	        this.BO_BM_CST = BO_BM_CST;
	        
	    }
	    
	    
	    public String getBoBmIiName() {
	        
	        return BO_BM_II_NM;
	        
	    }
	    
	    public void setBoBmIiName(String BO_BM_II_NM) {
	        
	        this.BO_BM_II_NM = BO_BM_II_NM;
	        
	    }
	    
	    public String getBoBmIiCode() {
	        
	        return BO_BM_II_CD;
	        
	    }
	    
	    public void setBoBmIiCode(String BO_BM_II_CD) {
	        
	        this.BO_BM_II_CD = BO_BM_II_CD;
	        
	    }
	    
	    public String getBoBmUomName() {
	        
	        return BO_BM_UOM_NM;
	        
	    }
	    
	    public void setBoBmUomName(String BO_BM_UOM_NM) {
	        
	        this.BO_BM_UOM_NM = BO_BM_UOM_NM;
	        
	    }
	    
	    public String getBoBmCategory() {
	        
	        return BO_BM_CTGRY;
	        
	    }
	    
	    public void setBoBmCategory(String BO_BM_CTGRY) {
	        
	        this.BO_BM_CTGRY = BO_BM_CTGRY;
	        
	    }
	   
	
	   public static Comparator DocumentNumberComparator = (BO, anotherBO) -> {

           String BO_DCMNT_NMBR1 = ((InvRepBuildUnbuildAssemblyOrderDetails) BO).getBoDocNo();
           String BO_BOM_CTGRY1 = ((InvRepBuildUnbuildAssemblyOrderDetails) BO).getBoBmCategory();
           String BO_BOM_II_NM1 = ((InvRepBuildUnbuildAssemblyOrderDetails) BO).getBoBmIiCode();

           String BO_DCMNT_NMBR2 = ((InvRepBuildUnbuildAssemblyOrderDetails) anotherBO).getBoDocNo();
           String BO_BOM_CTGRY2 = ((InvRepBuildUnbuildAssemblyOrderDetails) anotherBO).getBoBmCategory();
           String BO_BOM_II_NM2 = ((InvRepBuildUnbuildAssemblyOrderDetails) anotherBO).getBoBmIiCode();


           String ORDER_BY = ((InvRepBuildUnbuildAssemblyOrderDetails) anotherBO).getOrderBy();

           if(!(BO_DCMNT_NMBR1.equals(BO_DCMNT_NMBR2)) ){

               return BO_DCMNT_NMBR1.compareTo(BO_DCMNT_NMBR2);

           } else {

               if (!(BO_BOM_CTGRY1.equals(BO_BOM_CTGRY2))) {

                      return BO_BOM_CTGRY1.compareTo(BO_BOM_CTGRY2);

                  } else {

                      return BO_BOM_II_NM1.compareTo(BO_BOM_II_NM2);
                  }


           }



       };
	   
	   
	   public static Comparator ItemNameComparator = (PO, anotherPO) -> {

           String BO_DCMNT_NMBR1 = ((InvRepBuildUnbuildAssemblyOrderDetails) PO).getBoDocNo();
           Date BO_DT1 = ((InvRepBuildUnbuildAssemblyOrderDetails) PO).getBoDate();
           String BO_II_NM1 = ((InvRepBuildUnbuildAssemblyOrderDetails) PO).getBoItemName();

           String BO_DCMNT_NMBR2 = ((InvRepBuildUnbuildAssemblyOrderDetails) anotherPO).getBoDocNo();
           Date BO_DT2 = ((InvRepBuildUnbuildAssemblyOrderDetails) anotherPO).getBoDate();
           String BO_II_NM2 = ((InvRepBuildUnbuildAssemblyOrderDetails) anotherPO).getBoItemName();

           String ORDER_BY = ((InvRepBuildUnbuildAssemblyOrderDetails) PO).getOrderBy();

           if (!(BO_DCMNT_NMBR1.equals(BO_DCMNT_NMBR2))) {

               return BO_DCMNT_NMBR1.compareTo(BO_DCMNT_NMBR2);

           } else {

               if(ORDER_BY.equals("DATE") && !(BO_DT1.equals(BO_DT2))){

                   return BO_DT1.compareTo(BO_DT2);

               } else {

                   return BO_II_NM1.compareTo(BO_II_NM2);

               }
           }

       };
	   
} // ApRepPurchaseOrderDetails class
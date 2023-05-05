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

public class ApRepReceivingItemsDetails implements java.io.Serializable {

	private Date RR_DT;
	private String RR_II_NM;
	private String RR_II_DESC;
	private String RR_LOC_NM;
	private String RR_DCMNT_NMBR;
	private String RR_SPL_NM;
	private String RR_SPL_CNTRY;
	private String RR_UNT;
	private double RR_QTY;
	private double RR_UNT_CST;
	private double RR_AMNT;
	private String RR_QC_NUM;
	private Date RR_QC_EXPRY_DT;
	private String ORDER_BY;
	private String RR_RFRNC_NMBR;
	private String RR_DESC;
	private String RR_BRNCH_NM;
	private byte RR_PSTD;

	public ApRepReceivingItemsDetails() {
    }
	
	public Date getRrDate() {
		
		return RR_DT;
		
	}
	
	public void setRrDate(Date RR_DT) {
		
		this.RR_DT = RR_DT;
		
	}
	
	public String getRrDocNo() {
		
		return RR_DCMNT_NMBR;
		
	}
	
	public void setRrDocNo(String RR_DCMNT_NMBR) {
		
		this.RR_DCMNT_NMBR = RR_DCMNT_NMBR;
		
	}
	
	public double getRrAmount() {
		
		return RR_AMNT;
	
	}
	
	public void setRrAmount(double RR_AMNT) {
	
		this.RR_AMNT = RR_AMNT;
	
	}
	
	public String getRrQcNumber() {
	   	
	   	  return RR_QC_NUM;
	   	  
	   }
	   
	public void setRrQcNumber(String RR_QC_NUM) {
	   	
	   	  this.RR_QC_NUM = RR_QC_NUM;
	   	  	   
	}
	
	
	public Date getRrQcExpiryDate() {
		
		return RR_QC_EXPRY_DT;
		
	}
	
	public void setRrQcExpiryDate(Date RR_QC_EXPRY_DT) {
		
		this.RR_QC_EXPRY_DT = RR_QC_EXPRY_DT;
		
	}
	
	public String getRrItemDescription() {
	
		return RR_II_DESC;
	
	}
	
	public void setRrItemDescription(String RR_II_DESC) {
	
		this.RR_II_DESC = RR_II_DESC;
	
	}
	
	public String getRrItemName() {
	
		return RR_II_NM;
	
	}
	
	public void setRrItemName(String RR_II_NM) {
	
		this.RR_II_NM = RR_II_NM;
	
	}
	
	public double getRrQuantity() {
	
		return RR_QTY;
	
	}
	
	public void setRrQuantity(double RR_QTY) {
	
		this.RR_QTY = RR_QTY;
	
	}
	
	public String getRrLocation() {
	
		return RR_LOC_NM;
	
	}
	
	public void setRrLocation(String RR_LOC_NM) {
	
		this.RR_LOC_NM = RR_LOC_NM;
	
	}
	
	public double getRrUnitCost() {
	
		return RR_UNT_CST;
	
	}
	
	public void setRrUnitCost(double RR_UNT_CST) {
	
		this.RR_UNT_CST = RR_UNT_CST;
	
	}
	
	public String getRrSupplierName() {
		
		return RR_SPL_NM;

	}
	
	public void setRrSupplierName(String RR_SPL_NM) {
		
		this.RR_SPL_NM = RR_SPL_NM;
		
	}
	

	public String getRrSupplierCountry() {
		
		return RR_SPL_CNTRY;

	}
	
	public void setRrSupplierCountry(String RR_SPL_CNTRY) {
		
		this.RR_SPL_CNTRY = RR_SPL_CNTRY;
		
	}
	
	public String getRrUnit() {
		
		return RR_UNT;

	}
	
	public void setRrUnit(String RR_UNT) {
		
		this.RR_UNT = RR_UNT;
		
	}

	public String getOrderBy() {
	   	
	   	  return ORDER_BY;
	   	  
	   }
	   
	public void setOrderBy(String ORDER_BY) {
	   	
	   	  this.ORDER_BY = ORDER_BY;
	   	  	   
	}
	
	public String getRrRefNo() {
		
		return RR_RFRNC_NMBR;
		
	}
	
	public void setRrRefNo(String RR_RFRNC_NMBR) {
		
		this.RR_RFRNC_NMBR = RR_RFRNC_NMBR;
		
	}
	
	public String getRrDesc() {
		
		return RR_DESC;
		
	}
	
	public void setRrDesc(String RR_DESC) {
		
		this.RR_DESC = RR_DESC;
		
	}
		
	public String getRrBranchName() {
		
		return RR_BRNCH_NM;
		
	}
	
	public void setRrBranchName(String RR_BRNCH_NM) {
		
		this.RR_BRNCH_NM = RR_BRNCH_NM;
		
	}
	
	public byte getRrPosted() {
        
        return RR_PSTD;
        
    }
    
    public void setRrPosted(byte RR_PSTD) {
        
        this.RR_PSTD = RR_PSTD;
        
    }
    
	public static Comparator SupplierNameComparator = (RR, anotherRR) -> {

        String RR_SPL_SPPLR_NM1 = ((ApRepReceivingItemsDetails) RR).getRrSupplierName();
        Date RR_DT1 = ((ApRepReceivingItemsDetails) RR).getRrDate();
        String RR_DCMNT_NMBR1 = ((ApRepReceivingItemsDetails) RR).getRrDocNo();
        String RR_ITM_DESC1 = ((ApRepReceivingItemsDetails) RR).getRrItemDescription();
        String RR_ITM_NM1 = ((ApRepReceivingItemsDetails) RR).getRrItemName();
        String RR_LCTN1 = ((ApRepReceivingItemsDetails) RR).getRrLocation();

        String RR_SPL_SPPLR_NM2 = ((ApRepReceivingItemsDetails) anotherRR).getRrSupplierName();
        Date RR_DT2 = ((ApRepReceivingItemsDetails) anotherRR).getRrDate();
        String RR_DCMNT_NMBR2 = ((ApRepReceivingItemsDetails) anotherRR).getRrDocNo();
        String RR_ITM_DESC2 = ((ApRepReceivingItemsDetails) anotherRR).getRrItemDescription();
        String RR_ITM_NM2 = ((ApRepReceivingItemsDetails) anotherRR).getRrItemName();
        String RR_LCTN2 = ((ApRepReceivingItemsDetails) anotherRR).getRrLocation();

        String ORDER_BY = ((ApRepReceivingItemsDetails) RR).getOrderBy();

        if (!(RR_SPL_SPPLR_NM1.equals(RR_SPL_SPPLR_NM2))) {

            return RR_SPL_SPPLR_NM1.compareTo(RR_SPL_SPPLR_NM2);

        } else {

            if(ORDER_BY.equals("DATE") && !(RR_DT1.equals(RR_DT2))){

                return RR_DT1.compareTo(RR_DT2);

            } else if(ORDER_BY.equals("ITEM DESCRIPTION") && !(RR_ITM_DESC1.equals(RR_ITM_DESC2))){

                return RR_ITM_DESC1.compareTo(RR_ITM_DESC2);

            } else if(ORDER_BY.equals("ITEM NAME") && !(RR_ITM_NM1.equals(RR_ITM_NM2))){

                return RR_ITM_NM1.compareTo(RR_ITM_NM2);

            } else if(ORDER_BY.equals("LOCATION") && !(RR_LCTN1.equals(RR_LCTN2))){

                return RR_LCTN1.compareTo(RR_LCTN2);

            } else {

                return RR_DCMNT_NMBR1.compareTo(RR_DCMNT_NMBR2);

            }

        }

    };
	   
	   public static Comparator ItemNameComparator = (RR, anotherRR) -> {

           Date RR_DT1 = ((ApRepReceivingItemsDetails) RR).getRrDate();
           String RR_DCMNT_NMBR1 = ((ApRepReceivingItemsDetails) RR).getRrDocNo();
           String RR_ITM_DESC1 = ((ApRepReceivingItemsDetails) RR).getRrItemDescription();
           String RR_ITM_NM1 = ((ApRepReceivingItemsDetails) RR).getRrItemName();
           String RR_LCTN1 = ((ApRepReceivingItemsDetails) RR).getRrLocation();

           Date RR_DT2 = ((ApRepReceivingItemsDetails) anotherRR).getRrDate();
           String RR_DCMNT_NMBR2 = ((ApRepReceivingItemsDetails) anotherRR).getRrDocNo();
           String RR_ITM_DESC2 = ((ApRepReceivingItemsDetails) anotherRR).getRrItemDescription();
           String RR_ITM_NM2 = ((ApRepReceivingItemsDetails) anotherRR).getRrItemName();
           String RR_LCTN2 = ((ApRepReceivingItemsDetails) anotherRR).getRrLocation();

           String ORDER_BY = ((ApRepReceivingItemsDetails) RR).getOrderBy();

           if (!(RR_ITM_NM1.equals(RR_ITM_NM2))) {

               return RR_ITM_NM1.compareTo(RR_ITM_NM2);

           } else {

               if(ORDER_BY.equals("DATE") && !(RR_DT1.equals(RR_DT2))){

                   return RR_DT1.compareTo(RR_DT2);

               } else if(ORDER_BY.equals("ITEM DESCRIPTION") && !(RR_ITM_DESC1.equals(RR_ITM_DESC2))){

                   return RR_ITM_DESC1.compareTo(RR_ITM_DESC2);

               } else if(ORDER_BY.equals("LOCATION") && !(RR_LCTN1.equals(RR_LCTN2))){

                   return RR_LCTN1.compareTo(RR_LCTN2);

               } else {

                   return RR_DCMNT_NMBR1.compareTo(RR_DCMNT_NMBR2);

               }

           }

       };
	   
	   public static Comparator DateComparator = (RR, anotherRR) -> {

           Date RR_DT1 = ((ApRepReceivingItemsDetails) RR).getRrDate();
           String RR_DCMNT_NMBR1 = ((ApRepReceivingItemsDetails) RR).getRrDocNo();
           String RR_ITM_DESC1 = ((ApRepReceivingItemsDetails) RR).getRrItemDescription();
           String RR_ITM_NM1 = ((ApRepReceivingItemsDetails) RR).getRrItemName();
           String RR_LCTN1 = ((ApRepReceivingItemsDetails) RR).getRrLocation();

           Date RR_DT2 = ((ApRepReceivingItemsDetails) anotherRR).getRrDate();
           String RR_DCMNT_NMBR2 = ((ApRepReceivingItemsDetails) anotherRR).getRrDocNo();
           String RR_ITM_DESC2 = ((ApRepReceivingItemsDetails) anotherRR).getRrItemDescription();
           String RR_ITM_NM2 = ((ApRepReceivingItemsDetails) anotherRR).getRrItemName();
           String RR_LCTN2 = ((ApRepReceivingItemsDetails) anotherRR).getRrLocation();

           String ORDER_BY = ((ApRepReceivingItemsDetails) RR).getOrderBy();

           if (!(RR_DT1.equals(RR_DT2))) {

               return RR_DT1.compareTo(RR_DT2);

           } else {

               if(ORDER_BY.equals("ITEM NAME") && !(RR_ITM_NM1.equals(RR_ITM_NM2))){

                   return RR_ITM_NM1.compareTo(RR_ITM_NM2);

               } else if(ORDER_BY.equals("ITEM DESCRIPTION") && !(RR_ITM_DESC1.equals(RR_ITM_DESC2))){

                   return RR_ITM_DESC1.compareTo(RR_ITM_DESC2);

               } else if(ORDER_BY.equals("LOCATION") && !(RR_LCTN1.equals(RR_LCTN2))){

                   return RR_LCTN1.compareTo(RR_LCTN2);

               } else {

                   return RR_DCMNT_NMBR1.compareTo(RR_DCMNT_NMBR2);

               }

           }

       };
	  
	   public static Comparator NoGroupComparator = (RR, anotherRR) -> {

           Date RR_DT1 = ((ApRepReceivingItemsDetails) RR).getRrDate();
           String RR_DCMNT_NMBR1 = ((ApRepReceivingItemsDetails) RR).getRrDocNo();
           String RR_ITM_DESC1 = ((ApRepReceivingItemsDetails) RR).getRrItemDescription();
           String RR_ITM_NM1 = ((ApRepReceivingItemsDetails) RR).getRrItemName();
           String RR_LCTN1 = ((ApRepReceivingItemsDetails) RR).getRrLocation();

           Date RR_DT2 = ((ApRepReceivingItemsDetails) anotherRR).getRrDate();
           String RR_DCMNT_NMBR2 = ((ApRepReceivingItemsDetails) anotherRR).getRrDocNo();
           String RR_ITM_DESC2 = ((ApRepReceivingItemsDetails) anotherRR).getRrItemDescription();
           String RR_ITM_NM2 = ((ApRepReceivingItemsDetails) anotherRR).getRrItemName();
           String RR_LCTN2 = ((ApRepReceivingItemsDetails) anotherRR).getRrLocation();

           String ORDER_BY = ((ApRepReceivingItemsDetails) RR).getOrderBy();

           if(ORDER_BY.equals("DATE") && !(RR_DT1.equals(RR_DT2))){

               return RR_DT1.compareTo(RR_DT2);

           } else if(ORDER_BY.equals("ITEM DESCRIPTION") && !(RR_ITM_DESC1.equals(RR_ITM_DESC2))){

               return RR_ITM_DESC1.compareTo(RR_ITM_DESC2);

           } else if(ORDER_BY.equals("ITEM NAME") && !(RR_ITM_NM1.equals(RR_ITM_NM2))){

               return RR_ITM_NM1.compareTo(RR_ITM_NM2);

           } else if(ORDER_BY.equals("LOCATION") && !(RR_LCTN1.equals(RR_LCTN2))){

               return RR_LCTN1.compareTo(RR_LCTN2);

           } else {

               return RR_DCMNT_NMBR1.compareTo(RR_DCMNT_NMBR2);

           }

       };
	
} // ApRepReceivingItemsDetails class
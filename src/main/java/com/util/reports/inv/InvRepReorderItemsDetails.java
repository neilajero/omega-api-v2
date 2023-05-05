/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.inv;

import java.util.Comparator;


public class InvRepReorderItemsDetails implements java.io.Serializable {

	private String RI_II_NM;
	private String RI_II_DESC;
	private String RI_II_CLSS;
	private String RI_LOC_NM;
	private String RI_UOM_NM;
	private double RI_IL_RDR_PT;
	private double RI_IL_RDR_QTY;
	private double RI_QTY;
	private String RI_ORDER_BY;
	private String RI_GROUP_BY;
	private String RI_SPL_CODE;
	private String RI_BRNCH_NM;

	public InvRepReorderItemsDetails() {
    }

	public String getRiItemClass() {
		
		return RI_II_CLSS;
		
	}
	
	public void setRiItemClass(String RI_II_CLSS) {
		
		this.RI_II_CLSS = RI_II_CLSS;
		
	}
	
	public double getRiReorderQuantity() {
		
		return RI_IL_RDR_QTY;
		
	}
	
	public void setRiReorderQuantity(double RI_IL_RDR_QTY) {
		
		this.RI_IL_RDR_QTY = RI_IL_RDR_QTY;
		
	}	

	public double getRiReorderPoint() {
		
		return RI_IL_RDR_PT;
		
	}
	
	public void setRiReorderPoint(double RI_IL_RDR_PT) {
		
		this.RI_IL_RDR_PT = RI_IL_RDR_PT;
		
	}

	public String getRiItemDescription() {
	
		return RI_II_DESC;
	
	}
	
	public void setRiItemDescription(String RI_II_DESC) {
	
		this.RI_II_DESC = RI_II_DESC;
	
	}
	
	public String getRiItemName() {
	
		return RI_II_NM;
	
	}
	
	public void setRiItemName(String RI_II_NM) {
	
		this.RI_II_NM = RI_II_NM;
	
	}
	
	public double getRiQuantity() {
	
		return RI_QTY;
	
	}
	
	public void setRiQuantity(double RI_QTY) {
	
		this.RI_QTY = RI_QTY;
	
	}
	
	public String getRiLocation() {
	
		return RI_LOC_NM;
	
	}
	
	public void setRiLocation(String RI_LOC_NM) {
	
		this.RI_LOC_NM = RI_LOC_NM;
	
	}
	
	public String getRiUomName() {
		
		return RI_UOM_NM;
	
	}
	
	public void setRiUomName(String RI_UOM_NM) {
	
		this.RI_UOM_NM = RI_UOM_NM;
	
	}
	
	public String getOrderBy() {
	   	
   		return RI_ORDER_BY;
   		
   }
         
   public void setOrderBy(String RI_ORDER_BY) {
   	
   		this.RI_ORDER_BY = RI_ORDER_BY;
		
   }
   
   public String getGroupBy() {
   	
   		return RI_GROUP_BY;
   		
   }
   
   public void setGroupBy(String RI_GROUP_BY) {
   	
   		this.RI_GROUP_BY = RI_GROUP_BY;
   		
   }
   
   public String getRiSupplierCode() {
	   
	   return RI_SPL_CODE;
	   
   }
	
   public void setRiSupplierCode(String RI_SPL_CODE) {
	   
	   this.RI_SPL_CODE = RI_SPL_CODE;
	   
   }
   
   public String getRiBranch() {
	   
	   return RI_BRNCH_NM;
	   
   }
	
   public void setRiBranch(String RI_BRNCH_NM) {
	   
	   this.RI_BRNCH_NM = RI_BRNCH_NM;
	   
   }
	
	public static Comparator ItemNameComparator = (RI, anotherRI) -> {

        String RI_II_NM1 = ((InvRepReorderItemsDetails)RI).getRiItemName();
        String RI_II_DESC1 = ((InvRepReorderItemsDetails)RI).getRiItemDescription();
        String RI_II_CLSS1 = ((InvRepReorderItemsDetails)RI).getRiItemClass();

        String RI_II_NM2 = ((InvRepReorderItemsDetails)anotherRI).getRiItemName();
        String RI_II_DESC2 = ((InvRepReorderItemsDetails)anotherRI).getRiItemDescription();
        String RI_II_CLSS2 = ((InvRepReorderItemsDetails)anotherRI).getRiItemClass();

        String ORDER_BY = ((InvRepReorderItemsDetails)RI).getOrderBy();

        if(!(RI_II_NM1.equals(RI_II_NM2))) {

            return RI_II_NM1.compareTo(RI_II_NM2);

        } else {

            if(ORDER_BY.equals("ITEM DESCRIPTION") && !(RI_II_DESC1.equals(RI_II_DESC2))){

                   return RI_II_DESC1.compareTo(RI_II_DESC2);

               } else if(ORDER_BY.equals("ITEM CLASS") && !(RI_II_CLSS1.equals(RI_II_CLSS2))){

                   return RI_II_CLSS1.compareTo(RI_II_CLSS2);

               } else {

                   return RI_II_NM1.compareTo(RI_II_NM2);

               }

        }
    };
   
   public static Comparator NoClassComparator = (RI, anotherRI) -> {

       String RI_II_NM1 = ((InvRepReorderItemsDetails)RI).getRiItemName();
       String RI_II_DESC1 = ((InvRepReorderItemsDetails)RI).getRiItemDescription();
       String RI_II_CLSS1 = ((InvRepReorderItemsDetails)RI).getRiItemClass();

       String RI_II_NM2 = ((InvRepReorderItemsDetails)anotherRI).getRiItemName();
       String RI_II_DESC2 = ((InvRepReorderItemsDetails)anotherRI).getRiItemDescription();
       String RI_II_CLSS2 = ((InvRepReorderItemsDetails)anotherRI).getRiItemClass();

       String ORDER_BY = ((InvRepReorderItemsDetails)RI).getOrderBy();

       if(ORDER_BY.equals("ITEM DESCRIPTION") && !(RI_II_DESC1.equals(RI_II_DESC2))){

           return RI_II_DESC1.compareTo(RI_II_DESC2);

       } else if(ORDER_BY.equals("ITEM CLASS") && !(RI_II_CLSS1.equals(RI_II_CLSS2))){

           return RI_II_CLSS1.compareTo(RI_II_CLSS2);

       } else {

           return RI_II_NM1.compareTo(RI_II_NM2);

       }

   };

} // InvRepReoderItemsDetails class
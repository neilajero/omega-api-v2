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

public class InvRepAdjustmentRegisterSummaryOfIssuanceDetails implements java.io.Serializable {

	private String IL_NAME;
	private Double IL_QUANTITY;
	private Double IL_TOTAL_COST;
	private String IL_DESCRIPTION;
	private String IL_DEPARTMENT;
	private String ORDER_BY;
	private String IL_REFERENCE;
	private Date IL_DATE;


	
	

	
	public String getIL_REFERENCE() {
		return IL_REFERENCE;
	}

	public void setIL_REFERENCE(String iLREFERENCE) {
		IL_REFERENCE = iLREFERENCE;
	}

	public Date getDATE() {
		return IL_DATE;
	}

	public void setDATE(Date dATE) {
		IL_DATE = dATE;
	}

	public InvRepAdjustmentRegisterSummaryOfIssuanceDetails() {
    }

	public String getIlName() {

		return IL_NAME;

	}

	public void setIlName(String IL_NAME) {

		this.IL_NAME = IL_NAME;

	}


	   public String getOrderBy() {
		   	
		   	  return ORDER_BY;
		   	
		   }
		   
		   public void setOrderBy(String ORDER_BY) {
		   	
		   	  this.ORDER_BY = ORDER_BY;
		   	
		   }
		   
	public Double getIlQuantity() {

		return IL_QUANTITY;

	}

	public void setIlQuantity(Double IL_QUANTITY) {

		this.IL_QUANTITY = IL_QUANTITY;

	}
	
	public Double getIlTotalCost() {

		return IL_TOTAL_COST;

	}

	public void setIlTotalCost(Double IL_TOTAL_COST) {

		this.IL_TOTAL_COST = IL_TOTAL_COST;

	}

	public String getIlDescription() {

		return IL_DESCRIPTION;

	}
	
	public void setIlDescription(String IL_DESCRIPTION) {

		this.IL_DESCRIPTION = IL_DESCRIPTION;

	}
	

	
	public String getIlDepartment() {

		return IL_DEPARTMENT;

	}
	
	public void setIlDepartment(String IL_DEPARTMENT) {

		this.IL_DEPARTMENT = IL_DEPARTMENT;

	}
	
	  public static Comparator ItemNameComparator = (ADJ, anotherADJ) -> {

          String ADJ_II_NM1 = ((InvRepAdjustmentRegisterSummaryOfIssuanceDetails) ADJ).getIlName();

          String ADJ_DCMNT_NMBR1 = ((InvRepAdjustmentRegisterSummaryOfIssuanceDetails) ADJ).getIlDepartment();

          String ADJ_II_NM2 = ((InvRepAdjustmentRegisterSummaryOfIssuanceDetails) anotherADJ).getIlName();

          String ADJ_DCMNT_NMBR2 = ((InvRepAdjustmentRegisterSummaryOfIssuanceDetails) anotherADJ).getIlDepartment();

          String ORDER_BY = ((InvRepAdjustmentRegisterSummaryOfIssuanceDetails) ADJ).getOrderBy();

          if (!(ADJ_II_NM1.equals(ADJ_II_NM2))) {

              return ADJ_II_NM1.compareTo(ADJ_II_NM2);

          } else {

              if(ORDER_BY.equals(ADJ_II_NM1) && !(ADJ_DCMNT_NMBR1.equals(ADJ_DCMNT_NMBR2))){

                  return ADJ_DCMNT_NMBR1.compareTo(ADJ_DCMNT_NMBR2);

              } else {

                  return ADJ_DCMNT_NMBR1.compareTo(ADJ_DCMNT_NMBR2);

              }
          }

      };

	
	
	
	
	

} // InvRepItemListDetails class
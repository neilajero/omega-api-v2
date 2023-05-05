/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.inv;

import java.util.Comparator;

public class InvRepAdjustmentRegisterPostedDeliveriesDetails implements java.io.Serializable {

	private String IL_DATE;
	private String IL_DOC_NUM;
	private String IL_ITEM_DESC;
	private String IL_ITEM_UNIT;
	private String IL_ITEM_CODE;
	private Double IL_ITEM_QUANTITY;
	private Double IL_ITEM_COST;
	

	public String getIL_ITEM_CODE() {
		return IL_ITEM_CODE;
	}

	public void setIL_ITEM_CODE(String IL_ITEM_CODE) {
		this.IL_ITEM_CODE = IL_ITEM_CODE;
	}

	
	

	
	public String getIL_DATE() {
		return IL_DATE;
	}

	public void setIL_DATE(String IL_DATE) {
		this.IL_DATE = IL_DATE;
	}

	public String getIL_DOC_NUM() {
		return IL_DOC_NUM;
	}

	public void setIL_DOC_NUM(String IL_DOC_NUM) {
		this.IL_DOC_NUM = IL_DOC_NUM;
	}

	public InvRepAdjustmentRegisterPostedDeliveriesDetails() {
    }

	public String getIL_ITEM_DESC() {

		return IL_ITEM_DESC;

	}

	public void setIL_ITEM_DESC(String IL_ITEM_DESC) {

		this.IL_ITEM_DESC = IL_ITEM_DESC;

	}


	   public String getIL_ITEM_UNIT() {
		   	
		   	  return IL_ITEM_UNIT;
		   	
		   }
		   
		   public void setIL_ITEM_UNIT(String IL_ITEM_UNIT) {
		   	
		   	  this.IL_ITEM_UNIT = IL_ITEM_UNIT;
		   	
		   }
		   
	public Double getIL_ITEM_QUANTITY() {

		return IL_ITEM_QUANTITY;

	}

	public void setIL_ITEM_QUANTITY(Double IL_ITEM_QUANTITY) {

		this.IL_ITEM_QUANTITY = IL_ITEM_QUANTITY;

	}
	
	public Double getIL_ITEM_COST() {

		return IL_ITEM_COST;

	}

	public void setIL_ITEM_COST(Double IL_ITEM_COST) {

		this.IL_ITEM_COST = IL_ITEM_COST;

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
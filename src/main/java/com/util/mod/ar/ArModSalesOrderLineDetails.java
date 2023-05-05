/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ar;

import com.util.ar.ArSalesOrderLineDetails;

import java.io.Serializable;
import java.util.ArrayList;

public class ArModSalesOrderLineDetails extends ArSalesOrderLineDetails implements Serializable, Cloneable {

    private String SOL_UOM_NM = null;
    private String SOL_LOC_NM = null;
    private String SOL_II_NM = null;
    private String SOL_II_DESC = null;
    private String SOL_II_eDESC = null;
    private double SOL_QTY_DLVRD = 0d;
    private double SOL_RMNNG = 0d;
    private boolean SOL_ISSUE = false;
    byte SOL_TRC_MSC;
    ArrayList solTagList = new ArrayList();

	  public Object clone() throws CloneNotSupportedException {
  	 

 		try {
 		   return super.clone();
 		 }
 		  catch (CloneNotSupportedException e) {
 			  throw e;
 		
 		  }	
 	 }



    public byte getTraceMisc() {

 	   return SOL_TRC_MSC;

    }

    public void setTraceMisc(byte SOL_TRC_MSC) {

 	   this.SOL_TRC_MSC = SOL_TRC_MSC;

    }


    public ArrayList getSolTagList() {

 	   return solTagList;

    }

    public void setSolTagList(ArrayList solTagList) {

 	   this.solTagList = solTagList;

    }

    public ArModSalesOrderLineDetails() {
    }

    public String getSolUomName() {

        return SOL_UOM_NM;

    }

    public void setSolUomName(String SOL_UOM_NM) {

        this.SOL_UOM_NM = SOL_UOM_NM;

    }

    public String getSolLocName() {

        return SOL_LOC_NM;

    }

    public void setSolLocName(String SOL_LOC_NM) {

        this.SOL_LOC_NM = SOL_LOC_NM;

    }

    public String getSolIiName() {

        return SOL_II_NM;

    }

    public void setSolIiName(String SOL_II_NM) {

        this.SOL_II_NM = SOL_II_NM;

    }

    public String getSolIiDescription() {

        return SOL_II_DESC;

    }

    public void setSolIiDescription(String SOL_II_DESC) {

        this.SOL_II_DESC = SOL_II_DESC;

    }

    public String getSolEDesc() {

        return SOL_II_eDESC;

    }

    public void setSolEDesc(String SOL_II_eDESC) {

        this.SOL_II_eDESC = SOL_II_eDESC;

    }

    public double getSolRemaining() {

        return SOL_RMNNG;

    }

    public void setSolRemaining(double SOL_RMNNG) {

        this.SOL_RMNNG = SOL_RMNNG;

    }

    public boolean getSolIssue() {

        return SOL_ISSUE;

    }

    public void setSolIssue(boolean SOL_ISSUE) {

        this.SOL_ISSUE = SOL_ISSUE;

    }

    public double getSolQuantityDelivered() {

        return SOL_QTY_DLVRD;

    }

    public void setSolQuantityDelivered(double SOL_QTY_DLVRD) {

        this.SOL_QTY_DLVRD = SOL_QTY_DLVRD;

    }

} // ArModSalesOrderLineDetails
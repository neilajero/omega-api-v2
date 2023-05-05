/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ar;

import com.util.ar.ArSalesOrderDetails;

import java.util.ArrayList;

public class ArModSalesOrderDetails extends ArSalesOrderDetails implements java.io.Serializable {

    private String SO_FC_NM;
    private String SO_TC_NM;
    private String SO_CST_CSTMR_CODE;
    private String SO_CST_NM;
    private String SO_PYT_NM;
    private double SO_RMNNG;
    private double SO_ADVNC_AMNT;
    private String SO_SLP_SLSPRSN_CODE;
    private String SO_SLP_NM;
    private double SO_TC_RT;
    private String SO_TC_TYP;
    private ArrayList soSolList;
    private String SO_ORDR_STTS;
    private String SO_AMNT;
    private String SO_BRNCH_CODE;
    private String SO_STATUS;

    public ArModSalesOrderDetails() {
    }

    public String getSoFcName() {

        return SO_FC_NM;

    }

    public void setSoFcName(String SO_FC_NM) {

        this.SO_FC_NM = SO_FC_NM;

    }

    public String getSoOrderStatus() {

        return SO_ORDR_STTS;

    }

    public void setSoOrderStatus(String SO_ORDR_STTS) {

        this.SO_ORDR_STTS = SO_ORDR_STTS;

    }

    public String getSoTcName() {

        return SO_TC_NM;

    }

    public void setSoTcName(String SO_TC_NM) {

        this.SO_TC_NM = SO_TC_NM;

    }

    public String getSoCstCustomerCode() {

        return SO_CST_CSTMR_CODE;

    }

    public void setSoCstCustomerCode(String SO_CST_CSTMR_CODE) {

        this.SO_CST_CSTMR_CODE = SO_CST_CSTMR_CODE;

    }

    public String getSoCstName() {

        return SO_CST_NM;

    }

    public void setSoCstName(String SO_CST_NM) {

        this.SO_CST_NM = SO_CST_NM;

    }

    public String getSoPytName() {

        return SO_PYT_NM;

    }

    public void setSoPytName(String SO_PYT_NM) {

        this.SO_PYT_NM = SO_PYT_NM;

    }

    public double getSoRemaining() {

        return SO_RMNNG;

    }

    public void setSoRemaining(double SO_RMNNG) {

        this.SO_RMNNG = SO_RMNNG;

    }

    public double getSoAdvanceAmount() {

        return SO_ADVNC_AMNT;

    }

    public void setSoAdvanceAmount(double SO_ADVNC_AMNT) {

        this.SO_ADVNC_AMNT = SO_ADVNC_AMNT;

    }

    public ArrayList getSoSolList() {

        return soSolList;

    }

    public void setSoSolList(ArrayList soSolList) {

        this.soSolList = soSolList;

    }

    public String getSoSlpSalespersonCode() {

        return SO_SLP_SLSPRSN_CODE;

    }

    public void setSoSlpSalespersonCode(String SO_SLP_SLSPRSN_CODE) {

        this.SO_SLP_SLSPRSN_CODE = SO_SLP_SLSPRSN_CODE;

    }

    public String getSoSlpName() {

        return SO_SLP_NM;

    }

    public void setSoSlpName(String SO_SLP_NM) {

        this.SO_SLP_NM = SO_SLP_NM;

    }

    public double getSoTcRate() {

       	   return SO_TC_RT;

       }

    public void setSoTcRate(double SO_TC_RT) {

    	this.SO_TC_RT = SO_TC_RT;

    }

    public String getSoTcType() {

    	return SO_TC_TYP;

    }

    public void setSoTcType(String SO_TC_TYP) {

    	this.SO_TC_TYP = SO_TC_TYP;

    }
    
     public String getSoAmount() {

        return SO_AMNT;

    }

    public void setSoAmount(String SO_AMNT) {

        this.SO_AMNT = SO_AMNT;

    }
    
     public String getSoBranchCode() {

        return SO_BRNCH_CODE;

    }

    public void setSoBranchCode(String SO_BRNCH_CODE) {

        this.SO_BRNCH_CODE = SO_BRNCH_CODE;

    }

    public String getSoStatus() {

        return SO_STATUS;

    }

    public void setSoStatus(String SO_STATUS) {

        this.SO_STATUS = SO_STATUS;

    }

} // ArModSalesOrderDetails class
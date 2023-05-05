/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ar;

import com.util.ar.ArJobOrderDetails;

import java.util.ArrayList;

public class ArModJobOrderDetails extends ArJobOrderDetails implements java.io.Serializable {

	private String JO_TYPE;
	private String JO_FC_NM;
    private String JO_TC_NM;
    private String JO_CST_CSTMR_CODE;
    private String JO_CST_NM;
    private String JO_PYT_NM;
    private double JO_RMNNG;
    private double JO_ADVNC_AMNT;
    private String JO_SLP_SLSPRSN_CODE;
    private String JO_SLP_NM;
    private double JO_TC_RT;
    private String JO_TC_TYP;
    private ArrayList joJolList;
    private String JO_ORDR_STTS;

    public ArModJobOrderDetails() {
    }

    public String getJoType() {

        return JO_TYPE;

    }

    public void setJoType(String JO_TYPE) {

        this.JO_TYPE = JO_TYPE;

    }
    
    
    public String getJoFcName() {

        return JO_FC_NM;

    }

    public void setJoFcName(String JO_FC_NM) {

        this.JO_FC_NM = JO_FC_NM;

    }

    public String getJoOrderStatus() {

        return JO_ORDR_STTS;

    }

    public void setJoOrderStatus(String JO_ORDR_STTS) {

        this.JO_ORDR_STTS = JO_ORDR_STTS;

    }

    public String getJoTcName() {

        return JO_TC_NM;

    }

    public void setJoTcName(String JO_TC_NM) {

        this.JO_TC_NM = JO_TC_NM;

    }

    public String getJoCstCustomerCode() {

        return JO_CST_CSTMR_CODE;

    }

    public void setJoCstCustomerCode(String JO_CST_CSTMR_CODE) {

        this.JO_CST_CSTMR_CODE = JO_CST_CSTMR_CODE;

    }

    public String getJoCstName() {

        return JO_CST_NM;

    }

    public void setJoCstName(String JO_CST_NM) {

        this.JO_CST_NM = JO_CST_NM;

    }

    public String getJoPytName() {

        return JO_PYT_NM;

    }

    public void setJoPytName(String JO_PYT_NM) {

        this.JO_PYT_NM = JO_PYT_NM;

    }

    public double getJoRemaining() {

        return JO_RMNNG;

    }

    public void setJoRemaining(double JO_RMNNG) {

        this.JO_RMNNG = JO_RMNNG;

    }

    public double getJoAdvanceAmount() {

        return JO_ADVNC_AMNT;

    }

    public void setJoAdvanceAmount(double JO_ADVNC_AMNT) {

        this.JO_ADVNC_AMNT = JO_ADVNC_AMNT;

    }

    public ArrayList getJoJolList() {

        return joJolList;

    }

    public void setJoJolList(ArrayList joJolList) {

        this.joJolList = joJolList;

    }

    public String getJoSlpSalespersonCode() {

        return JO_SLP_SLSPRSN_CODE;

    }

    public void setJoSlpSalespersonCode(String JO_SLP_SLSPRSN_CODE) {

        this.JO_SLP_SLSPRSN_CODE = JO_SLP_SLSPRSN_CODE;

    }

    public String getJoSlpName() {

        return JO_SLP_NM;

    }

    public void setJoSlpName(String JO_SLP_NM) {

        this.JO_SLP_NM = JO_SLP_NM;

    }

    public double getJoTcRate() {

       	   return JO_TC_RT;

       }

    public void setJoTcRate(double JO_TC_RT) {

    	this.JO_TC_RT = JO_TC_RT;

    }

    public String getJoTcType() {

    	return JO_TC_TYP;

    }

    public void setJoTcType(String JO_TC_TYP) {

    	this.JO_TC_TYP = JO_TC_TYP;

    }

} // ArModJobOrderDetails class
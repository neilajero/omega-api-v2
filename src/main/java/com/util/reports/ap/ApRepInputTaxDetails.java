
/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ap;


public class ApRepInputTaxDetails implements java.io.Serializable{
	
	private String TI_TIN_OF_VNDR;
	private String TI_RGSTRD_NM;
	private String TI_LST_NM;
	private String TI_FRST_NM;
	private String TI_MDDL_NM;
	private String TI_ADDRSS1;
	private String TI_ADDRSS2;		
	private double TI_NET_AMNT;
	private double TI_INPT_TX;
	
	public ApRepInputTaxDetails() {
    }
	
	public String getTiTinOfVendor() {
		
		return TI_TIN_OF_VNDR;
		
	}
	
	public void setTiTinOfVendor(String TI_TIN_OF_VNDR) {
		
		this.TI_TIN_OF_VNDR = TI_TIN_OF_VNDR;
		
	}
	
	public String getTiRegisteredName() {
		
		return TI_RGSTRD_NM;
		
	}
	
	public  void setTiRegisteredName(String TI_RGSTRD_NM) {
		
		this.TI_RGSTRD_NM = TI_RGSTRD_NM;
		
	}
	
	public  String getTiLastName() {
		
		return TI_LST_NM;
		
	}
	
	public  void setTiLastName(String TI_LST_NM) {
		
		this.TI_LST_NM = TI_LST_NM;
		
	}
	
	public  String getTiFirstName() {
		
		return TI_FRST_NM;
		
	}
	
	public  void setTiFirstName(String TI_FRST_NM) {
		
		this.TI_FRST_NM = TI_FRST_NM;
		
	}
	
	public  String getTiMiddleName() {
		
		return TI_MDDL_NM;
		
	}
	
	public  void setTiMiddleName(String TI_MDDL_NM) {
		
		this.TI_MDDL_NM = TI_MDDL_NM;
		
	}
	
	public  String getTiAddress1() {
		
		return TI_ADDRSS1;
		
	}
	
	public  void setTiAddress1(String TI_ADDRSS1) {
		
		this.TI_ADDRSS1 = TI_ADDRSS1;
		
	}
	
	public String getTiAddress2() {
		
		return TI_ADDRSS1;
		
	}
	
	public void setTiAddress2(String TI_ADDRSS2) {
		
		this.TI_ADDRSS2 = TI_ADDRSS2;
		
	}		
	
	public double getTiNetAmount() {
		
		return TI_NET_AMNT;
		
	}
	
	public void setTiNetAmount(double TI_NET_AMNT) {
		
		this.TI_NET_AMNT = TI_NET_AMNT;
		
	}
	
	public double getTiInputTax() {
		
		return TI_INPT_TX;
		
	}
	
	public void setTiInputTax(double TI_INPT_TX) {
		
		this.TI_INPT_TX = TI_INPT_TX;
		
	}
	
}
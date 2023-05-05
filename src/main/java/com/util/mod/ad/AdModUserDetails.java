/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ad;

import com.util.ad.AdUserDetails;

import java.util.Comparator;


public class AdModUserDetails extends AdUserDetails implements java.io.Serializable {
   
 

   public AdModUserDetails() {
   }
   
   public String CMP_SHRT_NM = null;
   public String CMP_NM = null;
   public String CMP_WLCM_NT = null;
  

	 
	   
	public String getCmpShortName() {
		return CMP_SHRT_NM;
	}
	
	
	
	
	public String getCmpName() {
		return CMP_NM;
	}
	
	
	
	
	public String getCmpWelcomeNote() {
		return CMP_WLCM_NT;
	}
	
	
	
	
	public void setCmpShortName(String CMP_SHRT_NM) {
		this.CMP_SHRT_NM = CMP_SHRT_NM;
	}
	
	
	
	
	public void setCmpName(String CMP_NM) {
		this.CMP_NM = CMP_NM;
	}
	
	
	
	
	public void setCmpWelcomeNote(String CMP_WLCM_NT) {
		this.CMP_WLCM_NT = CMP_WLCM_NT;
	}

//AD_USER_DEPARTMENT
   public static Comparator DepartmentComparator = (SMT, anotherSMT) -> {

	   String AD_USR_DPT1 = ((AdModUserDetails) SMT).getUsrDept();
	   String AD_USR_CD1 = ((AdModUserDetails) SMT).getUsrName();

	   String AD_USR_DPT2 = ((AdModUserDetails) anotherSMT).getUsrDept();
	   String AD_USR_CD2 = ((AdModUserDetails) SMT).getUsrName();



	   if (!(AD_USR_DPT1.equals(AD_USR_DPT2))) {

		   return AD_USR_DPT1.compareTo(AD_USR_DPT2);

	   } else {

		   return AD_USR_CD1.compareTo(AD_USR_CD2);

	   }

   };


} // AdModCompanyDetails class
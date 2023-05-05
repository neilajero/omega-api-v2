/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ad;

import com.util.ad.AdUserResponsibilityDetails;

import java.util.Date;

public class AdModUserResponsibilityDetails extends AdUserResponsibilityDetails implements java.io.Serializable {

   private String UR_RS_NM; 
   private Integer UR_RS_CODE;
   
   private Date UR_RS_DT_FRM;
   private Date UR_RS_DT_TO;

    
   public AdModUserResponsibilityDetails () {
   }

   public Integer getUrResCode() {
   	
   	   return UR_RS_CODE;
   	
   }
   public void setUrResCode(Integer UR_RS_CODE) {
   	
   	   this.UR_RS_CODE = UR_RS_CODE;
   	
   }
   
   public String getUrResponsibilityName() {
   	
   	   return UR_RS_NM;
   	
   }
   
   public void setUrResponsibilityName(String UR_RS_NM) {
   	
   	   this.UR_RS_NM = UR_RS_NM;
   	
   }
   
   public Date getUrResDateFrom(){
   	return UR_RS_DT_FRM;
   }
   
   public void setUrResDateFrom(Date UR_RS_DT_FRM){
   	this.UR_RS_DT_FRM = UR_RS_DT_FRM;
   
   }
   
   public Date getUrResDateTo(){
   	return UR_RS_DT_TO;
   }
   
   public void setUrResDateTo(Date UR_RS_DT_TO){
   this.UR_RS_DT_TO  = UR_RS_DT_TO;
   
   }
   

   
   
} // AdModUserResponsibilityDetails class   
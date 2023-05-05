/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.gl;

import com.util.gl.GlRecurringJournalDetails;

import java.util.ArrayList;
import java.util.Date;

public class GlModRecurringJournalDetails extends GlRecurringJournalDetails implements java.io.Serializable {

   private String RJ_JC_NM;
   private double RJ_TTL_DBT;
   private double RJ_TTL_CRDT;
   private Date RJ_NW_NXT_RN_DT;   
   private ArrayList rjRjlList;
   
   private String RJ_JB_NM;
   
   public GlModRecurringJournalDetails() {
   }
   
   public String getRjJcName() {
   	
   	  return RJ_JC_NM;
   	
   }
   
   public void setRjJcName(String RJ_JC_NM) {
   	
   	  this.RJ_JC_NM = RJ_JC_NM;
   	
   }
   
   public double getRjTotalDebit() {
   	
   	   return RJ_TTL_DBT;
   	
   }
   
   public void setRjTotalDebit(double RJ_TTL_DBT) {
   
       this.RJ_TTL_DBT = RJ_TTL_DBT;
      
   }
   
   public double getRjTotalCredit() {
   	
   	   return RJ_TTL_CRDT;
   	
   }
   
   public void setRjTotalCredit(double RJ_TTL_CRDT) {
   	
   	    this.RJ_TTL_CRDT = RJ_TTL_CRDT;
   	
   }
   
   public Date getRjNewNextRunDate() {
   	
   	    return RJ_NW_NXT_RN_DT;
   	 
   }
   
   public void setRjNewNextRunDate(Date RJ_NW_NXT_RN_DT) {
   	
   	    this.RJ_NW_NXT_RN_DT = RJ_NW_NXT_RN_DT;
   	
   }
   	
   
   public ArrayList getRjRjlList() {
   	
   	  return rjRjlList;
   	
   }
   
   public void setRjRjlList(ArrayList rjRjlList) {
   	
   	  this.rjRjlList = rjRjlList;
   	
   }
   
   public String getRjJbName() {
   	
   	return RJ_JB_NM;
   	
   }
   
   public void setRjJbName(String RJ_JB_NM) {
   	
   	this.RJ_JB_NM = RJ_JB_NM;
   	
   }

} // GlModRecurringJournalDetails class   
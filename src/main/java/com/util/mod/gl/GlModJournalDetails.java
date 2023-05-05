/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.gl;

import com.util.gl.GlJournalDetails;

import java.util.ArrayList;

public class GlModJournalDetails extends GlJournalDetails implements java.io.Serializable {

   private String JR_JC_NM;
   private String JR_JS_NM;
   private String JR_FC_NM;
   private String JR_JB_NM;
   private ArrayList jrJlList;
   private double JR_TTL_DBT;
   private double JR_TTL_CRDT;
   private char JR_ACV_STATUS;
   private byte JR_FRZN;   

   public GlModJournalDetails() {
   }


   public String getJrJcName() {
   	
   	  return JR_JC_NM;
   	
   }
   
   public void setJrJcName(String JR_JC_NM) {
   	
   	  this.JR_JC_NM = JR_JC_NM;
   	
   }
   
   public String getJrJsName() {
   	
   	  return JR_JS_NM;
   	
   }
   
   public void setJrJsName(String JR_JS_NM) {
   	
   	  this.JR_JS_NM = JR_JS_NM;
   	
   }
   
   public String getJrFcName() {
   	
   	  return JR_FC_NM;
   	
   }
   
   public void setJrFcName(String JR_FC_NM) {
   	
   	  this.JR_FC_NM = JR_FC_NM;
   	
   }
   
   public String getJrJbName() {
   	
   	  return JR_JB_NM;
   	
   }
   
   public void setJrJbName(String JR_JB_NM) {
   	
   	  this.JR_JB_NM = JR_JB_NM;
   	
   }
   
   public ArrayList getJrJlList() {
   	
   	  return jrJlList;
   	
   }
   
   public void setJrJlList(ArrayList jrJlList) {
   	
   	  this.jrJlList = jrJlList;
   	
   }
   
   public double getJrTotalDebit() {
   	
   	  return JR_TTL_DBT;
   	
   }
   
   public void setJrTotalDebit(double JR_TTL_DBT) {
   	
   	  this.JR_TTL_DBT = JR_TTL_DBT;
   	
   }
   
   public double getJrTotalCredit() {
   	
   	  return JR_TTL_CRDT;
   	
   }
   
   public void setJrTotalCredit(double JR_TTL_CRDT) {
   	
   	  this.JR_TTL_CRDT = JR_TTL_CRDT;
   	
   }
   
   public char getJrAcvStatus() {
   	
   	  return JR_ACV_STATUS;
   	
   }
   
   public void setJrAcvStatus(char JR_ACV_STATUS) {
   	
   	  this.JR_ACV_STATUS = JR_ACV_STATUS;
   	
   }
   
   public byte getJrFrozen() {
   	
   	  return JR_FRZN;
   	
   }
   
   public void setJrFrozen(byte JR_FRZN) {
   	
   	  this.JR_FRZN = JR_FRZN;
   	
   }
   

} // GlModJournalDetails class
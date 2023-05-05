/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.gl;

import com.util.gl.GlBudgetAmountDetails;

import java.util.ArrayList;

public class GlModBudgetAmountDetails extends GlBudgetAmountDetails implements java.io.Serializable {

   private ArrayList bgaBudgetAmountCoaList;
   private String bgaPassword;
   private String bgaBoName;
   private String bgaBgtName;

   public GlModBudgetAmountDetails() {
   }

   
   public ArrayList getBgaBudgetAmountCoaList() {
   	
   	  return bgaBudgetAmountCoaList;
   	
   }
   
   public void setBgaBudgetAmountCoaList(ArrayList bgaBudgetAmountCoaList) {
   	
   	  this.bgaBudgetAmountCoaList = bgaBudgetAmountCoaList;
   	
   }
   
   public String getBgaPassword() {
   	  
   	  return bgaPassword;
   	
   }
   
   public void setBgaPassword(String bgaPassword) {
   	
   	  this.bgaPassword = bgaPassword;
   	
   }
   
   public String getBgaBoName() {
   	
   	  return bgaBoName;
   	 
   }
   
   public void setBgaBoName(String bgaBoName) {
   	
   	  this.bgaBoName = bgaBoName;  
   	
   }
   
   public String getBgaBgtName() {
   	
   	  return bgaBgtName;
   	 
   }
   
   public void setBgaBgtName(String bgaBgtName) {
   	
   	  this.bgaBgtName = bgaBgtName;  
   	
   }

} // GlModBudgetAmountDetails class
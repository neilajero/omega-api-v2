/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.gl;

import com.util.gl.GlJournalLineDetails;

public class GlModJournalLineDetails extends GlJournalLineDetails implements java.io.Serializable {

   private String JL_COA_ACCNT_NMBR;
   private String JL_COA_ACCNT_DESC;

   public GlModJournalLineDetails() {
   }

   public String getJlCoaAccountNumber() {
   	
   	  return JL_COA_ACCNT_NMBR;
   	
   }
   
   public void setJlCoaAccountNumber(String JL_COA_ACCNT_NMBR) {
   	
   	  this.JL_COA_ACCNT_NMBR = JL_COA_ACCNT_NMBR;
   	
   }
   
   public String getJlCoaAccountDescription() {
   	
   	  return JL_COA_ACCNT_DESC;
   	
   }
   
   public void setJlCoaAccountDescription(String JL_COA_ACCNT_DESC) {
   	
   	  this.JL_COA_ACCNT_DESC = JL_COA_ACCNT_DESC;
   	
   }

   
} // GlModJournalLineDetails class
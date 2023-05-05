/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.gl;

import com.util.gl.GlRecurringJournalLineDetails;

public class GlModRecurringJournalLineDetails extends GlRecurringJournalLineDetails implements java.io.Serializable {

   private String RJL_COA_ACCNT_NMBR;
   private String RJL_COA_ACCNT_DESC;

   public GlModRecurringJournalLineDetails() {
   }

   public String getRjlCoaAccountNumber() {
   	
   	  return RJL_COA_ACCNT_NMBR;
   	
   }
   
   public void setRjlCoaAccountNumber(String RJL_COA_ACCNT_NMBR) {
   	
   	  this.RJL_COA_ACCNT_NMBR = RJL_COA_ACCNT_NMBR;
   	
   }
   
   public String getRjlCoaAccountDescription() {
   	
   	  return RJL_COA_ACCNT_DESC;
   	
   }
   
   public void setRjlCoaAccountDescription(String RJL_COA_ACCNT_DESC) {
   	
   	  this.RJL_COA_ACCNT_DESC = RJL_COA_ACCNT_DESC;
   	
   }

   
} // GlModRecurringJournalLineDetails class
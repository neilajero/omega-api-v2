/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ar;


import com.util.ar.ArInvoiceLineDetails;

public class ArModInvoiceLineDetails extends ArInvoiceLineDetails implements java.io.Serializable {

   private String IL_SML_NM;
   private String IL_RCPT_NMBR;
   
   public ArModInvoiceLineDetails () {
   }

   public String getIlSmlName() {
   	
   	  return IL_SML_NM;
   	
   }
   
   public void setIlSmlName(String IL_SML_NM) {
   	
   	  this.IL_SML_NM = IL_SML_NM;
   	
   }
   
   public String getIlReceiptNumber() {
   	
   	  return IL_RCPT_NMBR;
   	
   }
   
   public void setIlReceiptNumber(String IL_RCPT_NMBR) {
   	
   	  this.IL_RCPT_NMBR = IL_RCPT_NMBR;
   	
   }

} // ArModInvoiceLineDetails class   
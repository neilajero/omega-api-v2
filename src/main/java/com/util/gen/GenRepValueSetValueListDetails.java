/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gen;


public class GenRepValueSetValueListDetails implements java.io.Serializable {

   private String VSL_VS_NM; 
   private String VSL_VL; 
   private String VSL_DESC;
   private String VSL_ACCNT_TYP; 
   private byte VSL_ENBL;
   
   public GenRepValueSetValueListDetails() {
   }

   public String getVslValueSetName() {
   	return VSL_VS_NM;
   }
   
   public void setVslValueSetName(String VSL_VS_NM) {
   	this.VSL_VS_NM = VSL_VS_NM;
   }
   
   public String getVslValue() {
      return VSL_VL;
   }

   public void setVslValue(String VSL_VL) {
      this.VSL_VL = VSL_VL;
   }

   public String getVslDescription() {
      return VSL_DESC;
   }

   public void setVslDescription(String VSL_DESC) {
      this.VSL_DESC = VSL_DESC;
   }

   public byte getVslEnable() {
      return VSL_ENBL;
   }

   public void setVslEnable(byte VSL_ENBL) {
      this.VSL_ENBL = VSL_ENBL;
   }

   public String getVslAccountType() {
   	  return VSL_ACCNT_TYP;
   }
   public void setVslAccountType(String VSL_ACCNT_TYP) {
   	  this.VSL_ACCNT_TYP = VSL_ACCNT_TYP;
   }
   
} // GenRepValueSetValueListDetails
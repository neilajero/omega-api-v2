/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gen;


public class GenModValueSetValueDetails extends GenValueSetValueDetails implements java.io.Serializable {

   private String VSV_QL_ACCNT_TYP;
   private String VSV_VS_NM;

   public GenModValueSetValueDetails() {
   }

   public GenModValueSetValueDetails (Integer VSV_CODE, String VSV_VL, String VSV_DESC,
      byte VSV_PRNT, short VSV_LVL, byte VSV_ENBL, String VSV_QL_ACCNT_TYP, String VSV_VS_NM) {

      super(VSV_CODE, VSV_VL, VSV_DESC,
      VSV_PRNT, VSV_LVL, VSV_ENBL);  

      this.VSV_QL_ACCNT_TYP = VSV_QL_ACCNT_TYP;
      this.VSV_VS_NM = VSV_VS_NM;

   }

   public String getVsvQlAccountType() {
   	
      return VSV_QL_ACCNT_TYP;
      
   }

   public void setVsvQlAccountType(String VSV_QL_ACCNT_TYP) {
   	
      this.VSV_QL_ACCNT_TYP = VSV_QL_ACCNT_TYP;
   
   }
   
   public String getVsvVsName() {
   	
      return VSV_VS_NM;
      
   }

   public void setVsvVsName(String VSV_VS_NM) {
   	
      this.VSV_VS_NM = VSV_VS_NM;
   
   }   

} // GenModValueSetValueDetails
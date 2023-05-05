/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gen;


public class GenValueSetDetails implements java.io.Serializable {

   private Integer VS_CODE;
   private String VS_NM;
   private String VS_DESC;
   private byte VS_ENBL;

   public GenValueSetDetails() {
   }

   public GenValueSetDetails (Integer VS_CODE, String VS_NM, String VS_DESC,
      byte VS_ENBL) {

      this.VS_CODE = VS_CODE;
      this.VS_NM = VS_NM;
      this.VS_DESC = VS_DESC;
      this.VS_ENBL = VS_ENBL;

   }

   public Integer getVsCode() {
      return VS_CODE;
   }
   
   public void setVsCode(Integer VS_CODE){
   	  this.VS_CODE = VS_CODE;
   }

   public String getVsName() {
      return VS_NM;
   }
   
   public void setVsName(String VS_NM){
   	  this.VS_NM = VS_NM;
   }

   public String getVsDescription() {
      return VS_DESC;
   }
   
   public void setVsDescription(String VS_DESC){
   	  this.VS_DESC = VS_DESC;
   }

   public byte getVsEnable() {
      return VS_ENBL;
   }
   
   public void setVsEnable(byte VS_ENBL){
   	  this.VS_ENBL = VS_ENBL;
   }

   public String toString() {
       return "VS_CODE = " + VS_CODE + " VS_NM = " + VS_NM +
          " VS_DESC = " + VS_DESC + " VS_ENBL = " + VS_ENBL;
   }

} // GenValueSetDetails
/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gl;


public class GlResponsibilityDetails implements java.io.Serializable {

   private Integer RES_CODE;
   private byte RES_ENBL;

   public GlResponsibilityDetails() {
   }
   
   public GlResponsibilityDetails (Integer RES_CODE, byte RES_ENBL) {
   
      this.RES_CODE = RES_CODE;
      this.RES_ENBL = RES_ENBL;
   
   } 
   
   public Integer getResCode() {
      return RES_CODE;
   }
   
   public byte getResEnable() {
      return RES_ENBL;
   }
   
   public String toString() {
       return "RES_CODE = " + RES_CODE + " RES_ENBL = " + RES_ENBL;
   }
      
} //GlSummaryAccountDetails class
/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gl;


public class GlUserDetails implements java.io.Serializable {

   private Integer USR_CODE;
   private double USR_AUTHRZTN_LMT;
   private Integer USR_PR1_USR_CODE;
   private Integer USR_PR2_USR_CODE;
   private Integer USR_PR3_USR_CODE;
   private byte USR_ENBL;

   public GlUserDetails() {
   }

   public GlUserDetails (Integer USR_CODE, double USR_AUTHRZTN_LMT,
      Integer USR_PR1_USR_CODE, Integer USR_PR2_USR_CODE,
      Integer USR_PR3_USR_CODE, byte USR_ENBL) {

      this.USR_CODE = USR_CODE;
      this.USR_AUTHRZTN_LMT = USR_AUTHRZTN_LMT;
      this.USR_PR1_USR_CODE = USR_PR1_USR_CODE;
      this.USR_PR2_USR_CODE = USR_PR2_USR_CODE;
      this.USR_PR3_USR_CODE = USR_PR3_USR_CODE;
      this.USR_ENBL = USR_ENBL;

   }

   public Integer getUsrCode() {
      return USR_CODE;
   }

   public double getUsrAuthorizationLimit() {
      return USR_AUTHRZTN_LMT;
   }

   public Integer getUsrPriority1UserCode() {
      return USR_PR1_USR_CODE;
   }

   public Integer getUsrPriority2UserCode() {
      return USR_PR2_USR_CODE;
   }

   public Integer getUsrPriority3UserCode() {
      return USR_PR3_USR_CODE;
   }

   public byte getUsrEnable() {
      return USR_ENBL;
   }

   public String toString() {
       return " USR_CODE = " + USR_CODE +
          " USR_AUTHRZTN_LMT = " + USR_AUTHRZTN_LMT +
      " USR_PR1_USR_CODE = " + USR_PR1_USR_CODE +
      " USR_PR2_USR_CODE = " + USR_PR2_USR_CODE +
      " USR_PR3_USR_CODE = " + USR_PR3_USR_CODE +
      " USR_ENBL = " + USR_ENBL;
   }

 }  // GlUserDetails
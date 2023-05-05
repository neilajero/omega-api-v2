/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gl;

import java.util.Date;

public class GlRegisteredAccountDetails implements java.io.Serializable {

   private Integer RA_CODE;
   private Date RA_DT_RGSTRD;

   public GlRegisteredAccountDetails() {
   }
   
   public GlRegisteredAccountDetails (Integer RA_CODE, Date RA_DT_RGSTRD) {

      this.RA_CODE = RA_CODE;
      this.RA_DT_RGSTRD = RA_DT_RGSTRD;

   }

   public Integer getRaCode() {
      return RA_CODE;
   }

   public Date getRaDateRegistered() {
      return RA_DT_RGSTRD;
   }

   public String toString() {
       return "RA_CODE = " + RA_CODE + " RA_DT_RGSTRD = " + RA_DT_RGSTRD;
   }

} // GlRegisteredAccountDetails class   
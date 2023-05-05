/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.gl;


import com.util.gl.GlFunctionalCurrencyDetails;

public class GlModFunctionalCurrencyDetails extends GlFunctionalCurrencyDetails implements java.io.Serializable {

   private Integer FC_CODE;
   private String FC_NM;
   private byte FC_SOB;

   public GlModFunctionalCurrencyDetails() { }

   public GlModFunctionalCurrencyDetails (Integer FC_CODE, String FC_NM, byte FC_SOB) {
      this.FC_CODE = FC_CODE;
      this.FC_NM = FC_NM;
      this.FC_SOB = FC_SOB;
   }

   public Integer getFcCode() {
      return FC_CODE;
   }

   public String getFcName() {
      return FC_NM;
   }

   public byte getFcSob() { return FC_SOB; }

   public String toString() {
       return "FC_CODE = " + FC_CODE + " FC_NM = " + FC_NM +
      " FC_SOB = " + FC_SOB;
   }

} // GlFunctionalCurrencyDetails class
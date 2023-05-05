/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gen;


public class GenChildRangeDetails implements java.io.Serializable {

   private Integer CR_CODE;
   private short CR_LW;
   private short CR_HGH;
   private char CR_RNG_TYP;

   public GenChildRangeDetails () {
   }

   public GenChildRangeDetails (Integer CR_CODE, short CR_LW, short CR_HGH,
      char CR_RNG_TYP) {

      this.CR_CODE = CR_CODE;
      this.CR_LW = CR_LW;
      this.CR_HGH = CR_HGH;
      this.CR_RNG_TYP = CR_RNG_TYP;

   }

   public Integer getCrCode() {
      return CR_CODE;
   }

   public short getCrLow() {
      return CR_LW;
   }

   public short getCrHigh() {
      return CR_HGH;
   }

   public char getCrRangeType() {
      return CR_RNG_TYP;
   }

   public String toString() {
       return "CR_CODE = " + CR_CODE + " CR_LW = " + CR_LW + " CR_HGH = " + CR_HGH +
          " CR_RNG_TYP = " + CR_RNG_TYP;
   }

} // GenChildRangeDetails
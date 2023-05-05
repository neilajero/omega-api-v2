/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gen;

public class GenStructureDetails implements java.io.Serializable {

   private Integer ST_CODE;
   private String ST_NM;
   private String ST_DESC;
   private byte ST_ENBL;

   public GenStructureDetails() {
   }

   public GenStructureDetails (Integer ST_CODE, String ST_NM,
      String ST_DESC, byte ST_ENBL) {

      this.ST_CODE = ST_CODE;
      this.ST_NM = ST_NM;
      this.ST_DESC = ST_DESC;
      this.ST_ENBL = ST_ENBL;

   }

   public Integer getStCode() {
      return ST_CODE;
   }

   public String getStName() {
      return ST_NM;
   }

   public String getStDescription() {
      return ST_DESC;
   }

   public byte getStEnable() {
      return ST_ENBL;
   }

   public String toString() {
       return "ST_CODE = " + ST_CODE + " ST_NM = " + ST_NM +
          " ST_DESC = " + ST_DESC + " ST_ENBL = " + ST_ENBL;
   }

} // GenStructureDetails
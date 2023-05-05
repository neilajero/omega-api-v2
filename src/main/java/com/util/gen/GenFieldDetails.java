/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gen;


public class GenFieldDetails implements java.io.Serializable {

   private Integer FL_CODE;
   private String FL_NM;
   private String FL_DESC;
   private char FL_SGMNT_SPRTR;
   private short FL_NMBR_OF_SGMNT;
   private byte FL_FRZ_RLLP;
   private byte FL_ENBL;

   public GenFieldDetails () {
   }

   public GenFieldDetails (Integer FL_CODE, String FL_NM, String FL_DESC,
      char FL_SGMNT_SPRTR, short FL_NMBR_OF_SGMNT,
      byte FL_FRZ_RLLP, byte FL_ENBL) {

      this.FL_CODE = FL_CODE;
      this.FL_NM = FL_NM;
      this.FL_DESC = FL_DESC;
      this.FL_SGMNT_SPRTR = FL_SGMNT_SPRTR;
      this.FL_NMBR_OF_SGMNT = FL_NMBR_OF_SGMNT;
      this.FL_FRZ_RLLP = FL_FRZ_RLLP;
      this.FL_ENBL = FL_ENBL;

   }

   public GenFieldDetails (String FL_NM) {

      this.FL_NM = FL_NM;

   }

   public Integer getFlCode() {
      return FL_CODE;
   }

   public String getFlName() {
      return FL_NM;
   }

   public String getFlDescription() {
      return FL_DESC;
   }

   public char getFlSegmentSeparator() {
      return FL_SGMNT_SPRTR;
   }

   public short getFlNumberOfSegment() {
      return FL_NMBR_OF_SGMNT;
   }

   public byte getFlFreezeRollup() {
      return FL_FRZ_RLLP;
   }

   public byte getFlEnable() {
      return FL_ENBL;
   }

   public String toString() {
       return "FL_CODE = " + FL_CODE + " FL_NM = " + FL_NM +
          " FL_DESC = " +  FL_DESC + " FL_SGMNT_SPRTR = " + FL_SGMNT_SPRTR +
      " FL_NMBR_OF_SGMNT = " + FL_NMBR_OF_SGMNT +
      " FL_FRZ_RLLP = " + FL_FRZ_RLLP +
      " FL_ENBL = " + FL_ENBL;
   }

} // GenFieldDetails class
/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gen;


public class GenValueSetValueDetails implements java.io.Serializable {

   private Integer VSV_CODE;
   private String VSV_VL;
   private String VSV_DESC;
   private byte VSV_PRNT;
   private short VSV_LVL;
   private byte VSV_ENBL;
   private short VSV_SGMNT_NMBR;
   private String VSV_ACCNT_TYP;
   
   public GenValueSetValueDetails() {
   }

   public GenValueSetValueDetails (Integer VSV_CODE, String VSV_VL, String VSV_DESC,
      byte VSV_PRNT, short VSV_LVL, byte VSV_ENBL) {

      this.VSV_CODE = VSV_CODE;
      this.VSV_VL = VSV_VL;
      this.VSV_DESC = VSV_DESC;
      this.VSV_PRNT = VSV_PRNT;
      this.VSV_LVL = VSV_LVL;
      this.VSV_ENBL = VSV_ENBL;
   }

   public GenValueSetValueDetails (String VSV_VL, String VSV_DESC,
      byte VSV_PRNT, short VSV_LVL, byte VSV_ENBL) {

      this.VSV_VL = VSV_VL;
      this.VSV_DESC = VSV_DESC;
      this.VSV_PRNT = VSV_PRNT;
      this.VSV_LVL = VSV_LVL;
      this.VSV_ENBL = VSV_ENBL;
   }
   
   public GenValueSetValueDetails (String VSV_VL, String VSV_DESC,
        short VSV_SGMNT_NMBR, String VSV_ACCNT_TYP) {

        this.VSV_VL = VSV_VL;
        this.VSV_DESC = VSV_DESC;
        this.VSV_SGMNT_NMBR = VSV_SGMNT_NMBR;
        this.VSV_ACCNT_TYP = VSV_ACCNT_TYP;
        
     }

   public Integer getVsvCode() {
      return VSV_CODE;
   }

   public void setVsvCode(Integer VSV_CODE) {
      this.VSV_CODE = VSV_CODE;
   }

   public String getVsvValue() {
      return VSV_VL;
   }

   public void setVsvValue(String VSV_VL) {
      this.VSV_VL = VSV_VL;
   }

   public String getVsvDescription() {
      return VSV_DESC;
   }

   public void setVsvDescription(String VSV_DESC) {
      this.VSV_DESC = VSV_DESC;
   }

   public byte getVsvParent() {
      return VSV_PRNT;
   }

   public void setVsvParent(byte VSV_PRNT) {
      this.VSV_PRNT = VSV_PRNT;
   }
   
   public short getVsvLevel() {
      return VSV_LVL;
   }

   public void setVsvLevel(short VSV_LVL) {
      this.VSV_LVL = VSV_LVL;
   }

   public byte getVsvEnable() {
      return VSV_ENBL;
   }

   public void setVsvEnable(byte VSV_ENBL) {
      this.VSV_ENBL = VSV_ENBL;
   }
   
   public short getVsvSegmentNumber() {
   	
   	  return VSV_SGMNT_NMBR;
   	
   }
   
   public void setVsvSegmentNumber(short VSV_SGMNT_NMBR) {
   	
   	  this.VSV_SGMNT_NMBR = VSV_SGMNT_NMBR;
   	
   }
   
   public String getVsvAccountType() {
   	
   	   return VSV_ACCNT_TYP;
   	
   }

   public String toString() {
       return "VSV_CODE = " + VSV_CODE + " VSV_VL = " + VSV_VL +
          " VSV_DESC = " + VSV_DESC + " VSV_PRNT = " + VSV_PRNT +
      " VSV_LVL = " + VSV_LVL + " VSV_ENBL = " + VSV_ENBL;
   }

} // GenValueSetValueDetails
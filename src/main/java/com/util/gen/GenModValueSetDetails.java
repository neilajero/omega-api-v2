/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gen;


public class GenModValueSetDetails extends GenValueSetDetails implements java.io.Serializable {

   private byte VS_SG_NTRL;
   private short VS_SGMNT_NMBR;
   
   public GenModValueSetDetails() {
   }

   public GenModValueSetDetails (byte VS_SG_NTRL, short VS_SGMNT_NMBR) {

      this.VS_SG_NTRL = VS_SG_NTRL;
      this.VS_SGMNT_NMBR = VS_SGMNT_NMBR;
      
   }

   public byte getVsSgNatural() {
   	
      return VS_SG_NTRL;
      
   }
   
   public void setVsSgNatural(byte VS_SG_NTRL) {
   	
   	  this.VS_SG_NTRL = VS_SG_NTRL;
    
   }
   
   public short getVsSegmentNumber() {
   	
   	  return VS_SGMNT_NMBR;
   	  
   }
   
   public void setVsSegmentNumber(short VS_SGMNT_NMBR) {
   	
   	  this.VS_SGMNT_NMBR = VS_SGMNT_NMBR;
   	  
   }
   
} // GenModValueSetDetails
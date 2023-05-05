/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.gl;


public class GlModAccountingCalendarDetails implements java.io.Serializable {

   private Integer MAC_CODE;
   private String MAC_NM;
   private String MAC_DESC;
   private String MAC_PT_NM;

   public GlModAccountingCalendarDetails() {
   }

   public GlModAccountingCalendarDetails (Integer MAC_CODE,
      String MAC_NM, String MAC_DESC, String MAC_PT_NM) {

      this.MAC_CODE = MAC_CODE;
      this.MAC_NM = MAC_NM;
      this.MAC_DESC = MAC_DESC;
      this.MAC_PT_NM = MAC_PT_NM;

   }

   public GlModAccountingCalendarDetails (
      String MAC_DESC, String MAC_PT_NM) {

      this.MAC_DESC = MAC_DESC;
      this.MAC_PT_NM = MAC_PT_NM;
   }

   public Integer getAcCode() {
      return MAC_CODE;
   }

   public String getAcName() {
      return MAC_NM;
   }

   public String getAcDescription() {
      return MAC_DESC;
   }

   public String getAcPtName() {
      return MAC_PT_NM;
   }

   public String toString() {
       return MAC_CODE + "&nbsp;&nbsp;&nbsp;&nbsp;" +
                  MAC_NM + "&nbsp;&nbsp;&nbsp;&nbsp;" +
          MAC_DESC + "&nbsp;&nbsp;&nbsp;&nbsp;" +
          MAC_PT_NM;
   }

} // GlAccountingCalendarDetails class
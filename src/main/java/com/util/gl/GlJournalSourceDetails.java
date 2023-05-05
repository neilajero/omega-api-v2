/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gl;


public class GlJournalSourceDetails implements java.io.Serializable {

   private Integer JS_CODE;
   private String JS_NM;
   private String JS_DESC;
   private byte JS_FRZ_JRNL;
   private byte JS_JRNL_APPRVL;
   private char JS_EFFCTV_DT_RL;

   public GlJournalSourceDetails() {
   }

   public GlJournalSourceDetails (Integer JS_CODE, String JS_NM,
      String JS_DESC, byte JS_FRZ_JRNL,
      byte JS_JRNL_APPRVL, char JS_EFFCTV_DT_RL) {

      this.JS_CODE = JS_CODE;
      this.JS_NM = JS_NM;
      this.JS_DESC = JS_DESC;
      this.JS_FRZ_JRNL = JS_FRZ_JRNL;
      this.JS_JRNL_APPRVL = JS_JRNL_APPRVL;
      this.JS_EFFCTV_DT_RL = JS_EFFCTV_DT_RL;

   }

   public GlJournalSourceDetails (String JS_NM,
         String JS_DESC, byte JS_FRZ_JRNL,
	 byte JS_JRNL_APPRVL, char JS_EFFCTV_DT_RL) {

	 this.JS_NM = JS_NM;
	 this.JS_DESC = JS_DESC;
	 this.JS_FRZ_JRNL = JS_FRZ_JRNL;
	 this.JS_JRNL_APPRVL = JS_JRNL_APPRVL;
	 this.JS_EFFCTV_DT_RL = JS_EFFCTV_DT_RL;
   }
   
   public Integer getJsCode() {
      return JS_CODE;
   }

   public String getJsName() {
      return JS_NM;
   }

   public String getJsDescription() {
      return JS_DESC;
   }

   public byte getJsFreezeJournal() {
      return JS_FRZ_JRNL;
   }

   public byte getJsJournalApproval() {
      return JS_JRNL_APPRVL;
   }

   public char getJsEffectiveDateRule() {
      return JS_EFFCTV_DT_RL;
   }

   public String toString() {
       return "JS_CODE = " + JS_CODE + " JS_NM = " + JS_NM +
          " JS_DESC = " + JS_DESC +
      " JS_FRZ_JRNL = " + JS_FRZ_JRNL +
      " JS_JRNL_APPRVL = " + JS_JRNL_APPRVL + " JS_EFFCTV_DT_RL = " +
      JS_EFFCTV_DT_RL;
   }

} // GlJournalSourceDetails class
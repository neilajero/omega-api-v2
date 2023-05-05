/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gl;


public class GlOrganizationDetails implements java.io.Serializable {

   private Integer ORG_CODE;
   private String ORG_NM;
   private String ORG_DESC;
   private Integer ORG_MSTR_CODE;

   public GlOrganizationDetails() {
   }

   public GlOrganizationDetails (Integer ORG_CODE, String ORG_NM,
      String ORG_DESC, Integer ORG_MSTR_CODE) {

      this.ORG_CODE = ORG_CODE;
      this.ORG_NM = ORG_NM;
      this.ORG_DESC = ORG_DESC;
      this.ORG_MSTR_CODE = ORG_MSTR_CODE;

   }

 public GlOrganizationDetails (String ORG_NM,
    String ORG_DESC, Integer ORG_MSTR_CODE) {

    this.ORG_NM = ORG_NM;
    this.ORG_DESC = ORG_DESC;
    this.ORG_MSTR_CODE = ORG_MSTR_CODE;
    
 }

   public Integer getOrgCode() {
      return ORG_CODE;
   }

   public String getOrgName() {
      return ORG_NM;
   }

   public String getOrgDescription() {
      return ORG_DESC;
   }

   public Integer getOrgMasterCode() {
      return ORG_MSTR_CODE;
   }

   public String toString() {
       return "ORG_CODE = " + ORG_CODE + " ORG_NM = " + ORG_NM +
          " ORG_DESC = " + ORG_DESC + " ORG_MSTR = " + ORG_MSTR_CODE;
   }

} // GlOrganizationDetails class   
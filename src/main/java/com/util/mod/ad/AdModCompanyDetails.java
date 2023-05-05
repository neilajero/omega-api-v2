/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ad;


import com.util.ad.AdCompanyDetails;

public class AdModCompanyDetails extends AdCompanyDetails implements java.io.Serializable {
   
   private String CMP_GL_FC_NM;
   private String CMP_GEN_FL_NM;

   public AdModCompanyDetails() {
   }

   public String getCmpGlFcName() {
   	
   	   return CMP_GL_FC_NM;
   	
   }  
   
   public void setCmpGlFcName(String CMP_GL_FC_NM) {
   	
   	  this.CMP_GL_FC_NM = CMP_GL_FC_NM;
   	
   }
   
   public String getCmpGenFlName() {
   	
   	   return CMP_GEN_FL_NM;
   	
   }  
   
   public void setCmpGenFlName(String CMP_GEN_FL_NM) {
   	
   	  this.CMP_GEN_FL_NM = CMP_GEN_FL_NM;
   	
   }


} // AdModCompanyDetails class
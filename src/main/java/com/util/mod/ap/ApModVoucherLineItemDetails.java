/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ap;

import com.util.ap.ApVoucherLineItemDetails;
import com.util.Debug;

import java.util.ArrayList;

public class ApModVoucherLineItemDetails extends ApVoucherLineItemDetails implements java.io.Serializable {

   String VLI_UOM_NM = null;
   String VLI_LOC_NM = null;
   String VLI_II_NM = null;
   String VLI_II_DESC = null;
   String VLI_VOU_DCMNT_NMBR = null;
   String VLI_II_PRT_NMBR = null;
   String VLI_MISC = null;
   String VLI_PRJCT_CODE = null;
   String VLI_PRJCT_TYP_CODE = null;
   String VLI_PRJCT_PHS_NM = null;

   boolean VLI_IS_VT_RLF = false;
   boolean VLI_IS_PRJCT = false;

   boolean IS_TRACE_MISC = false;
   ArrayList tagList = new ArrayList();

   public ApModVoucherLineItemDetails() {
   }



   public boolean getVliIsVatRelief() {

   	  return VLI_IS_VT_RLF;

   }

   public void setVliIsVatRelief(boolean VLI_IS_VT_RLF) {

   	  this.VLI_IS_VT_RLF = VLI_IS_VT_RLF;

   }

   public boolean getVliIsProject() {

   	  return VLI_IS_PRJCT;

   }

   public void setVliIsProject(boolean VLI_IS_PRJCT) {

   	  this.VLI_IS_PRJCT = VLI_IS_PRJCT;

   }

   public boolean getIsTraceMic() {

   	  return IS_TRACE_MISC;

   }

   public void setIsTraceMisc(boolean IS_TRACE_MISC) {

   	  this.IS_TRACE_MISC = IS_TRACE_MISC;

   }


   public String getVliUomName() {

   	  return VLI_UOM_NM;

   }

   public void setVliUomName(String VLI_UOM_NM) {

   	  this.VLI_UOM_NM = VLI_UOM_NM;

   }
   public String getVliLocName() {

   	  return VLI_LOC_NM;

   }

   public void setVliLocName(String VLI_LOC_NM) {

   	  this.VLI_LOC_NM = VLI_LOC_NM;

   }

   public String getVliIiName() {

   	  return VLI_II_NM;

   }

   public void setVliIiName(String VLI_II_NM) {

   	  this.VLI_II_NM = VLI_II_NM;

   }

   public String getVliIiDescription() {

   	  return VLI_II_DESC;

   }

   public void setVliIiDescription(String VLI_II_DESC) {

   	  this.VLI_II_DESC = VLI_II_DESC;

   }

   public String getVliVouDocumentNumber() {

   	  return VLI_VOU_DCMNT_NMBR;

   }

   public void setVliVouDocumentNumber(String VLI_VOU_DCMNT_NMBR) {

   	  this.VLI_VOU_DCMNT_NMBR = VLI_VOU_DCMNT_NMBR;

   }

   public String getVliMisc() {

	   return VLI_MISC;

   }

   public void setVliMisc(String VLI_MISC) {

	   this.VLI_MISC = VLI_MISC;
	   Debug.print("VLI_MISC : " + VLI_MISC);

   }


   public String getVliProjectCode() {

	   return VLI_PRJCT_CODE;

   }

   public void setVliProjectCode(String VLI_PRJCT_CODE) {

	   this.VLI_PRJCT_CODE = VLI_PRJCT_CODE;


   }


   public String getVliProjectTypeCode() {

	   return VLI_PRJCT_TYP_CODE;

   }

   public void setVliProjectTypeCode(String VLI_PRJCT_TYP_CODE) {

	   this.VLI_PRJCT_TYP_CODE = VLI_PRJCT_TYP_CODE;


   }


   public String getVliProjecPhaseName() {

	   return VLI_PRJCT_PHS_NM;

   }

   public void setVliProjectPhaseName(String VLI_PRJCT_PHS_NM) {

	   this.VLI_PRJCT_PHS_NM = VLI_PRJCT_PHS_NM;


   }

   public String getVliIiPartNumber() {

	   return VLI_II_PRT_NMBR;

   }

   public void setVliIiPartNumber(String VLI_II_PRT_NMBR) {

	   this.VLI_II_PRT_NMBR = VLI_II_PRT_NMBR;

   }


   public ArrayList getTagList() {

	   return tagList;

   }

   public void setTagList(ArrayList tagList) {

	   this.tagList = tagList;

   }

} // ApModVoucherLineItemDetails class
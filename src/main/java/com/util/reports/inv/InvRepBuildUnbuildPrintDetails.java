/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.inv;

import java.util.Comparator;
import java.util.Date;


public class InvRepBuildUnbuildPrintDetails implements java.io.Serializable {
    
    private Date BUP_BUA_DT;
    private String BUP_BUA_DCMNT_NMBR;
    private String BUP_BUA_CST_NM;
    private String BUP_BUA_RFRNC_NMBR;
    private String BUP_BUA_DESC;
    private String BUP_BUA_CRTD_BY;
    
    private String BUP_BL_II_NM;
    private String BUP_BL_II_DESC;
    private String BUP_BL_II_CTGRY;
    
    private String BUP_BL_II_NM_ARRAY;
    private String BUP_BL_II_DESC_ARRAY;
    
    private String BUP_BL_LOC_NM;
    private String BUP_BL_UOM_NM;
    private double BUP_BL_BLD_QNTTY;
    private String BUP_BL_BLD_QNTTY_ARRAY;
    private double BUP_BL_BLD_RMNNG;
    private double BUP_BL_BLD_RCVD;
    private double BUP_BL_BLD_SPCFC_GRVTY;
    private double BUP_BL_BLD_STNDRD_FLL_SZ;
    private double BUP_BL_QNTTY_ON_HND;
    
    private double BUP_BL_BM_QTY_NDD;
    private double BUP_BL_BM_QTY_ON_HND;
    private double BUP_BL_BM_UNPSTD_QTY;
    private double BUP_BL_BM_CST;
    private String BUP_BL_BM_II_NM;
    private String BUP_BL_BM_II_CD;
    private String BUP_BL_BM_UOM_NM;
    private String BUP_BL_BM_CTGRY;
    
    private String BUP_BL_BM_QC1;
    private String BUP_BL_BM_QC2;
    private String BUP_BL_BM_QC3;
    private String BUP_BL_BM_QC4;
    private String BUP_BL_BM_QC5;
    private String BUP_BL_BM_QC6;
    private String BUP_BL_BM_QC7;
    private String BUP_BL_BM_QC8;
    private String BUP_BL_BM_QC9;
    private String BUP_BL_BM_QC10;
    private String BUP_BL_BM_QC_OTHRS;
    
    private double BUP_BL_BM_QC1_RMNNG;
    private double BUP_BL_BM_QC2_RMNNG;
    private double BUP_BL_BM_QC3_RMNNG;
    private double BUP_BL_BM_QC4_RMNNG;
    private double BUP_BL_BM_QC5_RMNNG;
    private double BUP_BL_BM_QC6_RMNNG;
    private double BUP_BL_BM_QC7_RMNNG;
    private double BUP_BL_BM_QC8_RMNNG;
    private double BUP_BL_BM_QC9_RMNNG;
    private double BUP_BL_BM_QC10_RMNNG;
    private double BUP_BL_BM_QC_OTHRS_RMNNG;
    
    
    public InvRepBuildUnbuildPrintDetails() {
    }
    
    public Date getBupBuaDate() {
        
        return BUP_BUA_DT;
        
    }
    
    public void setBupBuaDate(Date BUP_BUA_DT) {
        
        this.BUP_BUA_DT = BUP_BUA_DT;
        
    }
    
    public String getBupBuaDocumentNumber() {
        
        return BUP_BUA_DCMNT_NMBR;
        
    }
    
    public void setBupBuaDocumentNumber(String BUP_BUA_DCMNT_NMBR) {
        
        this.BUP_BUA_DCMNT_NMBR = BUP_BUA_DCMNT_NMBR;
        
    }
    
    public String getBupBuaCustomerName() {
        
        return BUP_BUA_CST_NM;
        
    }
    
    public void setBupBuaCustomerName(String BUP_BUA_CST_NM) {
        
        this.BUP_BUA_CST_NM = BUP_BUA_CST_NM;
        
    }
    
    public String getBupBuaReferenceNumber() {
        
        return BUP_BUA_RFRNC_NMBR;
        
    }
    
    public void setBupBuaReferenceNumber(String BUP_BUA_RFRNC_NMBR) {
        
        this.BUP_BUA_RFRNC_NMBR = BUP_BUA_RFRNC_NMBR;
        
    }
    
    public String getBupBuaDescription() {
        
        return BUP_BUA_DESC;
        
    }
    
    public void setBupBuaDescription(String BUP_BUA_DESC) {
        
        this.BUP_BUA_DESC = BUP_BUA_DESC;
        
    }
    
    public String getBupBuaCreatedBy() {
        
        return BUP_BUA_CRTD_BY;
        
    }
    
    public void setBupBuaCreatedBy(String BUP_BUA_CRTD_BY) {
        
        this.BUP_BUA_CRTD_BY = BUP_BUA_CRTD_BY;
        
    }
    
    public String getBupBlIiName() {
        
        return BUP_BL_II_NM;
        
    }
    
    public void setBupBlIiName(String BUP_BL_II_NM) {
        
        this.BUP_BL_II_NM = BUP_BL_II_NM;
        
    }
    
    
    
    public String getBupBlIiDesc() {
        
        return BUP_BL_II_DESC;
        
    }
    
    public void setBupBlIiDesc(String BUP_BL_II_DESC) {
        
        this.BUP_BL_II_DESC = BUP_BL_II_DESC;
        
    }
    
    public String getBupBlIiCategory() {
        
        return BUP_BL_II_CTGRY;
        
    }
    
    public void setBupBlIiCategory(String BUP_BL_II_CTGRY) {
        
        this.BUP_BL_II_CTGRY = BUP_BL_II_CTGRY;
        
    }
    

    public String getBupBlIiNameArray() {
        
        return BUP_BL_II_NM_ARRAY;
        
    }
    
    public void setBupBlIiNameArray(String BUP_BL_II_NM_ARRAY) {
        
        this.BUP_BL_II_NM_ARRAY = BUP_BL_II_NM_ARRAY;
        
    }
    
    
    
    public String getBupBlIiDescArray() {
        
        return BUP_BL_II_DESC_ARRAY;
        
    }
    
    public void setBupBlIiDescArray(String BUP_BL_II_DESC_ARRAY) {
        
        this.BUP_BL_II_DESC_ARRAY = BUP_BL_II_DESC_ARRAY;
        
    }
    
        
    public String getBupBlLocName() {
        
        return BUP_BL_LOC_NM;
        
    }
    
    public void setBupBlLocName(String BUP_BL_LOC_NM) {
        
        this.BUP_BL_LOC_NM = BUP_BL_LOC_NM;
        
    }
    
    public String getBupBlUomName() {
        
        return BUP_BL_UOM_NM;
        
    }
    
    public void setBupBlUomName(String BUP_BL_UOM_NM) {
        
        this.BUP_BL_UOM_NM = BUP_BL_UOM_NM;
        
    }

    public double getBupBlBuildQuantity() {
        
        return BUP_BL_BLD_QNTTY;
        
    }
    
    public void setBupBlBuildQuantity(double BUP_BL_BLD_QNTTY) {
        
        this.BUP_BL_BLD_QNTTY = BUP_BL_BLD_QNTTY;
        
    }
    
public String getBupBlBuildQuantityArray() {
        
        return BUP_BL_BLD_QNTTY_ARRAY;
        
    }
    
    public void setBupBlBuildQuantityArray(String BUP_BL_BLD_QNTTY_ARRAY) {
        
        this.BUP_BL_BLD_QNTTY_ARRAY = BUP_BL_BLD_QNTTY_ARRAY;
        
    }

    
public double getBupBlRemaining() {
        
        return BUP_BL_BLD_RMNNG;
        
    }
    
    public void setBupBlRemaining(double BUP_BL_BLD_RMNNG) {
        
        this.BUP_BL_BLD_RMNNG = BUP_BL_BLD_RMNNG;
        
    }
    
 

    public double getBupBlReceived() {
        
        return BUP_BL_BLD_RCVD;
        
    }
    
    public void setBupBlReceived(double BUP_BL_BLD_RCVD) {
        
        this.BUP_BL_BLD_RCVD = BUP_BL_BLD_RCVD;
        
    }
    
public double getBupBlBuildSpecificGravity() {
        
        return BUP_BL_BLD_SPCFC_GRVTY;
        
    }
    
    public void setBupBlBuildSpecificGravity(double BUP_BL_BLD_SPCFC_GRVTY) {
        
        this.BUP_BL_BLD_SPCFC_GRVTY = BUP_BL_BLD_SPCFC_GRVTY;
        
    }

    public double getBupBlBuildStandardFillSize() {
        
        return BUP_BL_BLD_STNDRD_FLL_SZ;
        
    }
    
    public void setBupBlBuildStandardFillSize(double BUP_BL_BLD_STNDRD_FLL_SZ) {
        
        this.BUP_BL_BLD_STNDRD_FLL_SZ = BUP_BL_BLD_STNDRD_FLL_SZ;
        
    }
    
    public double getBupBlQuantityOnHand() {
        
        return BUP_BL_QNTTY_ON_HND;
        
    }
    
    public void setBupBlQuantityOnHand(double BUP_BL_QNTTY_ON_HND) {
        
        this.BUP_BL_QNTTY_ON_HND = BUP_BL_QNTTY_ON_HND;
        
    }
    
    
    
    public double getBupBlBmQuantityNeeded() {
        
        return BUP_BL_BM_QTY_NDD;
        
    }
    
    public void setBupBlBmQuantityNeeded(double BUP_BL_BM_QTY_NDD) {
        
        this.BUP_BL_BM_QTY_NDD = BUP_BL_BM_QTY_NDD;
        
    }
    
    
    public double getBupBlBmQuantityOnHand() {
        
        return BUP_BL_BM_QTY_ON_HND;
        
    }
    
    public void setBupBlBmQuantityOnHand(double BUP_BL_BM_QTY_ON_HND) {
        
        this.BUP_BL_BM_QTY_ON_HND = BUP_BL_BM_QTY_ON_HND;
        
    }
    
public double getBupBlBmUnpostedQuantity() {
        
        return BUP_BL_BM_UNPSTD_QTY;
        
    }
    
    public void setBupBlBmUnpostedQuantity(double BUP_BL_BM_UNPSTD_QTY) {
        
        this.BUP_BL_BM_UNPSTD_QTY = BUP_BL_BM_UNPSTD_QTY;
        
    }
    
    public double getBupBlBmCost() {
        
        return BUP_BL_BM_CST;
        
    }
    
    public void setBupBlBmCost(double BUP_BL_BM_CST) {
        
        this.BUP_BL_BM_CST = BUP_BL_BM_CST;
        
    }
    
    
    public String getBupBlBmIiName() {
        
        return BUP_BL_BM_II_NM;
        
    }
    
    public void setBupBlBmIiName(String BUP_BL_BM_II_NM) {
        
        this.BUP_BL_BM_II_NM = BUP_BL_BM_II_NM;
        
    }
    
    public String getBupBlBmIiCode() {
        
        return BUP_BL_BM_II_CD;
        
    }
    
    public void setBupBlBmIiCode(String BUP_BL_BM_II_CD) {
        
        this.BUP_BL_BM_II_CD = BUP_BL_BM_II_CD;
        
    }
    
    public String getBupBlBmUomName() {
        
        return BUP_BL_BM_UOM_NM;
        
    }
    
    public void setBupBlBmUomName(String BUP_BL_BM_UOM_NM) {
        
        this.BUP_BL_BM_UOM_NM = BUP_BL_BM_UOM_NM;
        
    }
    
    public String getBupBlBmCategory() {
        
        return BUP_BL_BM_CTGRY;
        
    }
    
    public void setBupBlBmCategory(String BUP_BL_BM_CTGRY) {
        
        this.BUP_BL_BM_CTGRY = BUP_BL_BM_CTGRY;
        
    }
    
    
    public String getBupBlBmQualityControl1() {
        
        return BUP_BL_BM_QC1;
        
    }
    
    public void setBupBlBmQualityControl1(String BUP_BL_BM_QC1) {
        
        this.BUP_BL_BM_QC1 = BUP_BL_BM_QC1;
        
    }
    
    public String getBupBlBmQualityControl2() {
        
        return BUP_BL_BM_QC2;
        
    }
    
    public void setBupBlBmQualityControl2(String BUP_BL_BM_QC2) {
        
        this.BUP_BL_BM_QC2 = BUP_BL_BM_QC2;
        
    }
    
public String getBupBlBmQualityControl3() {
        
        return BUP_BL_BM_QC3;
        
    }
    
    public void setBupBlBmQualityControl3(String BUP_BL_BM_QC3) {
        
        this.BUP_BL_BM_QC3 = BUP_BL_BM_QC3;
        
    }
    
    
public String getBupBlBmQualityControl4() {
        
        return BUP_BL_BM_QC4;
        
    }
    
    public void setBupBlBmQualityControl4(String BUP_BL_BM_QC4) {
        
        this.BUP_BL_BM_QC4 = BUP_BL_BM_QC4;
        
    }
    
public String getBupBlBmQualityControl5() {
        
        return BUP_BL_BM_QC5;
        
    }
    
    public void setBupBlBmQualityControl5(String BUP_BL_BM_QC5) {
        
        this.BUP_BL_BM_QC5 = BUP_BL_BM_QC5;
        
    }
    
public String getBupBlBmQualityControl6() {
        
        return BUP_BL_BM_QC6;
        
    }
    
    public void setBupBlBmQualityControl6(String BUP_BL_BM_QC6) {
        
        this.BUP_BL_BM_QC6 = BUP_BL_BM_QC6;
        
    }
    
public String getBupBlBmQualityControl7() {
        
        return BUP_BL_BM_QC7;
        
    }
    
    public void setBupBlBmQualityControl7(String BUP_BL_BM_QC7) {
        
        this.BUP_BL_BM_QC7 = BUP_BL_BM_QC7;
        
    }
    
public String getBupBlBmQualityControl8() {
        
        return BUP_BL_BM_QC8;
        
    }
    
    public void setBupBlBmQualityControl8(String BUP_BL_BM_QC8) {
        
        this.BUP_BL_BM_QC8 = BUP_BL_BM_QC8;
        
    }
    
public String getBupBlBmQualityControl9() {
        
        return BUP_BL_BM_QC9;
        
    }
    
    public void setBupBlBmQualityControl9(String BUP_BL_BM_QC9) {
        
        this.BUP_BL_BM_QC9 = BUP_BL_BM_QC9;
        
    }
    
public String getBupBlBmQualityControl10() {
        
        return BUP_BL_BM_QC10;
        
    }
    
    public void setBupBlBmQualityControl10(String BUP_BL_BM_QC10) {
        
        this.BUP_BL_BM_QC10 = BUP_BL_BM_QC10;
        
    }
    
public String getBupBlBmQualityControlOthers() {
        
        return BUP_BL_BM_QC_OTHRS;
        
    }
    
    public void setBupBlBmQualityControlOthers(String BUP_BL_BM_QC_OTHRS) {
        
        this.BUP_BL_BM_QC_OTHRS = BUP_BL_BM_QC_OTHRS;
        
    }
    
    
    public double getBupBlBmQualityControl1Remaining() {
            
            return BUP_BL_BM_QC1_RMNNG;
            
        }
        
        public void setBupBlBmQualityControl1Remaining(double BUP_BL_BM_QC1_RMNNG) {
            
            this.BUP_BL_BM_QC1_RMNNG = BUP_BL_BM_QC1_RMNNG;
            
        }
        
public double getBupBlBmQualityControl2Remaining() {
            
            return BUP_BL_BM_QC2_RMNNG;
            
        }
        
        public void setBupBlBmQualityControl2Remaining(double BUP_BL_BM_QC2_RMNNG) {
            
            this.BUP_BL_BM_QC2_RMNNG = BUP_BL_BM_QC2_RMNNG;
            
        }
        
public double getBupBlBmQualityControl3Remaining() {
            
            return BUP_BL_BM_QC3_RMNNG;
            
        }
        
        public void setBupBlBmQualityControl3Remaining(double BUP_BL_BM_QC3_RMNNG) {
            
            this.BUP_BL_BM_QC3_RMNNG = BUP_BL_BM_QC3_RMNNG;
            
        }
        
public double getBupBlBmQualityControl4Remaining() {
            
            return BUP_BL_BM_QC4_RMNNG;
            
        }
        
        public void setBupBlBmQualityControl4Remaining(double BUP_BL_BM_QC4_RMNNG) {
            
            this.BUP_BL_BM_QC4_RMNNG = BUP_BL_BM_QC4_RMNNG;
            
        }
        
public double getBupBlBmQualityControl5Remaining() {
            
            return BUP_BL_BM_QC5_RMNNG;
            
        }
        
        public void setBupBlBmQualityControl5Remaining(double BUP_BL_BM_QC5_RMNNG) {
            
            this.BUP_BL_BM_QC5_RMNNG = BUP_BL_BM_QC5_RMNNG;
            
        }
        
public double getBupBlBmQualityControl6Remaining() {
            
            return BUP_BL_BM_QC6_RMNNG;
            
        }
        
        public void setBupBlBmQualityControl6Remaining(double BUP_BL_BM_QC6_RMNNG) {
            
            this.BUP_BL_BM_QC6_RMNNG = BUP_BL_BM_QC6_RMNNG;
            
        }
        
public double getBupBlBmQualityControl7Remaining() {
            
            return BUP_BL_BM_QC7_RMNNG;
            
        }
        
        public void setBupBlBmQualityControl7Remaining(double BUP_BL_BM_QC7_RMNNG) {
            
            this.BUP_BL_BM_QC7_RMNNG = BUP_BL_BM_QC7_RMNNG;
            
        }
        
public double getBupBlBmQualityControl8Remaining() {
            
            return BUP_BL_BM_QC8_RMNNG;
            
        }
        
        public void setBupBlBmQualityControl8Remaining(double BUP_BL_BM_QC8_RMNNG) {
            
            this.BUP_BL_BM_QC8_RMNNG = BUP_BL_BM_QC8_RMNNG;
            
        }
        
public double getBupBlBmQualityControl9Remaining() {
            
            return BUP_BL_BM_QC9_RMNNG;
            
        }
        
        public void setBupBlBmQualityControl9Remaining(double BUP_BL_BM_QC9_RMNNG) {
            
            this.BUP_BL_BM_QC9_RMNNG = BUP_BL_BM_QC9_RMNNG;
            
        }
        
public double getBupBlBmQualityControl10Remaining() {
            
            return BUP_BL_BM_QC10_RMNNG;
            
        }
        
        public void setBupBlBmQualityControl10Remaining(double BUP_BL_BM_QC10_RMNNG) {
            
            this.BUP_BL_BM_QC10_RMNNG = BUP_BL_BM_QC10_RMNNG;
            
        }
        
public double getBupBlBmQualityControlOthersRemaining() {
            
            return BUP_BL_BM_QC_OTHRS_RMNNG;
            
        }
        
        public void setBupBlBmQualityControlOthersRemaining(double BUP_BL_BM_QC_OTHRS_RMNNG) {
            
            this.BUP_BL_BM_QC_OTHRS_RMNNG = BUP_BL_BM_QC_OTHRS_RMNNG;
            
        }
    
    
    public static Comparator ItemComparator = (r1, r2) -> {

        String BUA_BOM_ITM_CD1 = ((InvRepBuildUnbuildPrintDetails) r1).getBupBlBmIiCode();
        String BUA_BOM_CTGRY1 = ((InvRepBuildUnbuildPrintDetails) r1).getBupBlBmCategory();

        String BUA_BOM_ITM_CD2 = ((InvRepBuildUnbuildPrintDetails) r2).getBupBlBmIiCode();
        String BUA_BOM_CTGRY2 = ((InvRepBuildUnbuildPrintDetails) r2).getBupBlBmCategory();


        if(!(BUA_BOM_CTGRY1.equals(BUA_BOM_CTGRY2))){
            return BUA_BOM_CTGRY1.compareTo(BUA_BOM_CTGRY2);

        } else {
            return BUA_BOM_ITM_CD1.compareTo(BUA_BOM_ITM_CD2);

        }



    };
    
    
    
}  // InvRepAdjustmentPrintDetails
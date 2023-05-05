/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.gl;

import java.util.ArrayList;
import java.util.Date;

public class GlRepFinancialReportDetails implements java.io.Serializable {

   private String FR_TTL;
   private Date FR_DT;
   private String FR_PRD;
   private int FR_YR;
   private String FR_FC_NM;
   private int FR_WDTH;
   private String FR_COL_NM;
   private int FR_COL_PSTN;
   private String FR_COL_FRMT_MSK;
   private String FR_COL_FCTR;
   private Double FR_COL_AMNT;
   private String FR_ROW_NM;
   private int FR_ROW_INDNT;
   private int FR_ROW_LN_TO_SKP_BFR;
   private int FR_ROW_LN_TO_SKP_AFTR;
   private int FR_ROW_UNDRLN_CHAR_BFR;
   private int FR_ROW_UNDRLN_CHAR_AFTR;
   private int FR_ROW_PG_BRK_BFR;
   private int FR_ROW_PG_BRK_AFTR;
   private String FR_ROW_FNT_STYL;
   private ArrayList FR_CLMN_LST;
   private int FR_FNT_SZ;
   private String FR_FNT_STYL;
   private String FR_HRZNTL_ALGN;
   private String FR_ROW_HRZNTL_ALGN;
   private byte FR_ROW_HD_RW;
   private int FR_FRST_COL_PSTN;
   private int FR_LST_COL_PSTN;
   
   public GlRepFinancialReportDetails() {
   }

   public GlRepFinancialReportDetails(String FR_TTL, Date FR_DT, String FR_PRD, int FR_YR, 
   		String FR_FC_NM, int FR_WDTH, int FR_FNT_SZ, String FR_FNT_STYL, String FR_HRZNTL_ALGN) {
       	
       
       this.FR_TTL = FR_TTL;
       this.FR_DT = FR_DT;
       this.FR_PRD = FR_PRD;
       this.FR_YR = FR_YR;
       this.FR_FC_NM = FR_FC_NM;
       this.FR_WDTH = FR_WDTH;
       this.FR_FNT_SZ = FR_FNT_SZ;
       this.FR_FNT_STYL = FR_FNT_STYL;
       this.FR_HRZNTL_ALGN = FR_HRZNTL_ALGN;

   }
   
   public GlRepFinancialReportDetails(String FR_COL_NM, int FR_COL_PSTN) {
   	
   	   this.FR_COL_NM = FR_COL_NM;
   	   this.FR_COL_PSTN = FR_COL_PSTN;
   	
   }
   
   public GlRepFinancialReportDetails(int FR_COL_PSTN, String FR_COL_FRMT_MSK,
       String FR_COL_FCTR, Double FR_COL_AMNT) {
       	
       this.FR_COL_PSTN = FR_COL_PSTN;
       this.FR_COL_FRMT_MSK = FR_COL_FRMT_MSK;
       this.FR_COL_FCTR = FR_COL_FCTR;
       this.FR_COL_AMNT = FR_COL_AMNT;
       	
   }
   
   public GlRepFinancialReportDetails(String FR_ROW_NM, int FR_ROW_INDNT, int FR_ROW_LN_TO_SKP_BFR, 
   	   int FR_ROW_LN_TO_SKP_AFTR, int FR_ROW_UNDRLN_CHAR_BFR, int FR_ROW_UNDRLN_CHAR_AFTR, 
	   int FR_ROW_PG_BRK_BFR, int FR_ROW_PG_BRK_AFTR, String FR_ROW_FNT_STYL, 
	   String FR_ROW_HRZNTL_ALGN, byte FR_ROW_HD_RW, int FR_FRST_COL_PSTN, int FR_LST_COL_PSTN, ArrayList FR_CLMN_LST) {
       	
       this.FR_ROW_NM = FR_ROW_NM;
       this.FR_ROW_INDNT = FR_ROW_INDNT;
       this.FR_ROW_LN_TO_SKP_BFR = FR_ROW_LN_TO_SKP_BFR;
       this.FR_ROW_LN_TO_SKP_AFTR = FR_ROW_LN_TO_SKP_AFTR;
       this.FR_ROW_UNDRLN_CHAR_BFR = FR_ROW_UNDRLN_CHAR_BFR;
       this.FR_ROW_UNDRLN_CHAR_AFTR = FR_ROW_UNDRLN_CHAR_AFTR;
       this.FR_ROW_PG_BRK_BFR = FR_ROW_PG_BRK_BFR;
       this.FR_ROW_PG_BRK_AFTR = FR_ROW_PG_BRK_AFTR;
       this.FR_ROW_FNT_STYL = FR_ROW_FNT_STYL;
       this.FR_ROW_HRZNTL_ALGN = FR_ROW_HRZNTL_ALGN;
       this.FR_ROW_HD_RW = FR_ROW_HD_RW; 
       this.FR_FRST_COL_PSTN = FR_FRST_COL_PSTN;
       this.FR_LST_COL_PSTN = FR_LST_COL_PSTN;
       this.FR_CLMN_LST = FR_CLMN_LST;
       	
	}

    public String getFrTitle() {
    	
    	return FR_TTL;
    	
    }
    
    public Date getFrDate() {
    	
    	return FR_DT;
    }
    
    public String getFrPeriod() {
    	
    	return FR_PRD;
    	
    }
    
    public int getFrYear() {
    	
    	return FR_YR;
    	
    }
    
    public String getFrFcName() {
    	
    	return FR_FC_NM;
    	
    }
    
    public int getFrWidth() {
    	
    	return FR_WDTH;
    	
    }
    
    public String getFrColName() {
    	
    	return FR_COL_NM;
    	
    }
    
    public int getFrColPosition() {
    	
    	return FR_COL_PSTN;
    	
    }
    
    public String getFrColFormatMask() {
    	
    	return FR_COL_FRMT_MSK;
    	
    }
    
    public String getFrColFactor() {
    	
    	return FR_COL_FCTR;
    	
    }
    
    public Double getFrColAmount() {
    	
    	return FR_COL_AMNT;
    	
    }
    
  public void setFrColAmount(double FR_COL_AMNT) {
    	
    	this.FR_COL_AMNT = FR_COL_AMNT;
    	
    }
    
    public String getFrRowName() {
    	
    	return FR_ROW_NM;
    	
    }
    
    public int getFrRowIndent() {
    	
    	return FR_ROW_INDNT;
    	
    }
    
    public int getFrRowLineToSkipBefore() {
    	
    	return FR_ROW_LN_TO_SKP_BFR;
    	
    }
    
    public int getFrRowLineToSkipAfter() {
    	
    	return FR_ROW_LN_TO_SKP_AFTR;
    	
    }
    
    public int getFrRowUnderlineCharacterBefore() {
    	
    	return FR_ROW_UNDRLN_CHAR_BFR;
    	
    }
    
    public int getFrRowUnderlineCharacterAfter() {
    	
    	return FR_ROW_UNDRLN_CHAR_AFTR;
    	
    }
    
    public int getFrRowPageBreakBefore() {
    	
    	return FR_ROW_PG_BRK_BFR;
    	
    }
    
    public int getFrRowPageBreakAfter() {
    	
    	return FR_ROW_PG_BRK_AFTR;
    	
    }
    
    public String getFrRowFontStyle() {
    	
    	return FR_ROW_FNT_STYL;
    	
    }
    
    public ArrayList getFrColumnList() {
    	
    	return FR_CLMN_LST;
    	
    }

    public int getFrFontSize() {
    	
    	return FR_FNT_SZ;
    	
    }
    
    public String getFrFontStyle() {
    	
    	return FR_FNT_STYL;
    	
    }
    
    public String getFrHorizontalAlign() {
    	
    	return FR_HRZNTL_ALGN;
    	
    }
    
    public String getFrRowHorizontalAlign() {
    	
    	return FR_ROW_HRZNTL_ALGN;
    	
    }
    
    public byte getFrRowHideRow() {
    	
    	return FR_ROW_HD_RW;
    	
    }
    
    public int getFrFirstColPosition() {
    	
    	return FR_FRST_COL_PSTN;
    	
    }
    
    public int getFrLastColPosition() {
    	
    	return FR_LST_COL_PSTN;
    	
    }
   
} // GlRepFinancialReportDetails class   
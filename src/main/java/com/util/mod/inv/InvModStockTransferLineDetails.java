/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.inv;

import com.util.Debug;
import com.util.inv.InvStockTransferLineDetails;

public class InvModStockTransferLineDetails extends InvStockTransferLineDetails implements java.io.Serializable {
    
    private String STL_II_NM;
    private String STL_II_DSC;
    private String STL_UOM_NM;
    private String STL_LOC_NM_FRM;
    private String STL_LOC_NM_TO;
    private short STL_LN_NMBR;
    String STL_MISC = null;
    
    public InvModStockTransferLineDetails() {
    }
    
    public String getStlIiName() {        
        return STL_II_NM;        
    }
    
    public void setStlIiName(String STL_II_NM) {        
        this.STL_II_NM = STL_II_NM;        
    }
    
    public String getStlIiDescription() {
        return STL_II_DSC;
    }
    
    public void setStlIiDescription(String STL_II_DSC) {
        this.STL_II_DSC = STL_II_DSC;
    }
    
    public String getStlUomName() {
        return STL_UOM_NM;        
    }
    
    public void setStlUomName(String STL_UOM_NM) {
        this.STL_UOM_NM = STL_UOM_NM;
    }
    
    public String getStlLocationNameFrom() {
        return STL_LOC_NM_FRM;        
    }
    
    public void setStlLocationNameFrom(String STL_LOC_NM_FRM) {
        this.STL_LOC_NM_FRM = STL_LOC_NM_FRM;
    }
    
    public String getStlLocationNameTo() {
        return STL_LOC_NM_TO;        
    }
    
    public void setStlLocationNameTo(String STL_LOC_NM_TO) {
        this.STL_LOC_NM_TO = STL_LOC_NM_TO;
    }
    
    public short getStlLineNumber() {
        return STL_LN_NMBR;
    }
    
    public void setStlLineNumber(short STL_LN_NMBR) {
        this.STL_LN_NMBR = STL_LN_NMBR;
    }

    public String getStlMisc() {

    	return STL_MISC;

    }

    public void setStlMisc(String STL_MISC) {

    	this.STL_MISC = STL_MISC;
    	Debug.print("STL_MISC : " + STL_MISC);

    }
	
            
} // InvModStockTransferLineDetails class
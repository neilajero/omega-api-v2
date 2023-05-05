/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.inv;

import com.util.inv.InvBranchStockTransferDetails;

import java.util.ArrayList;

public class InvModBranchStockTransferDetails extends InvBranchStockTransferDetails implements java.io.Serializable {
    
    private String BST_BRNCH_TO;
    private String BST_BRNCH_FROM;
    private String BST_TRNST_LOC;
    private ArrayList BST_BTL_LST;
    private String BST_TYP;
    
    public InvModBranchStockTransferDetails() {
    }
    
    public ArrayList getBstBtlList() {        
        return BST_BTL_LST;        
    }
    
    public void setBstBtlList(ArrayList BST_BTL_LST) {        
        this.BST_BTL_LST = BST_BTL_LST;        
    }
    
    public String getBstBranchTo(){
        return BST_BRNCH_TO;
    }
    
    public void setBstBranchFrom(String BST_BRNCH_FROM) {
        this.BST_BRNCH_FROM = BST_BRNCH_FROM;
    }
    
    public String getBstBranchFrom(){
        return BST_BRNCH_FROM;
    }
    
    public void setBstBranchTo(String BST_BRNCH_TO) {
        this.BST_BRNCH_TO = BST_BRNCH_TO;
    }
    
     
    public String getBstTransitLocation(){
        return BST_TRNST_LOC;
    }
    
    public void setBstTransitLocation(String BST_TRNST_LOC) {
        this.BST_TRNST_LOC = BST_TRNST_LOC;
    }
    
     
    public String getBstType(){
        return BST_TYP;
    }
    
    public void setBstType(String BST_TYP) {
        this.BST_TYP = BST_TYP;
    }

}
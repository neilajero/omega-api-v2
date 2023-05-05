
/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.inv;

import com.util.inv.InvStockTransferDetails;

import java.util.ArrayList;

public class InvModStockTransferDetails extends InvStockTransferDetails implements java.io.Serializable {
    
    private ArrayList ST_STL_LST;
    
    public InvModStockTransferDetails() {
    }
    
    public ArrayList getStStlList() {        
        return ST_STL_LST;        
    }
    
    public void setStStlList(ArrayList ST_STL_LST) {        
        this.ST_STL_LST = ST_STL_LST;        
    }

} // InvModStockTransferDetails class
/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ar;

import com.util.ar.ArReceiptImportPreferenceDetails;
import com.util.ar.ArReceiptImportPreferenceLineDetails;

import java.util.ArrayList;

public class ArModReceiptImportPreferenceDetails extends ArReceiptImportPreferenceDetails implements java.io.Serializable {
	
	private ArrayList ripRilList = new ArrayList();
	
	public ArModReceiptImportPreferenceDetails() {
    }
	
	public ArReceiptImportPreferenceLineDetails getRipRilListByIndex(int index){
		
		return ((ArReceiptImportPreferenceLineDetails)ripRilList.get(index));
		
	}
	
	public int getRipRilListSize(){
		
		return(ripRilList.size());
		
	}
	
	public void saveRipRilList(Object newRipRilList){
		
		ripRilList.add(newRipRilList);   	  
		
	}
	
	public ArrayList getRipRilList() {
		
		return ripRilList;
		
	}
	
	public void setRipRilList(ArrayList ripRilList) {
		
		this.ripRilList = ripRilList;
		
	}
	
}
/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.inv;

import com.util.inv.InvUnitOfMeasureDetails;

public class InvModUnitOfMeasureDetails extends InvUnitOfMeasureDetails implements java.io.Serializable {

    private boolean isDefault = false;

    public InvModUnitOfMeasureDetails() {
    }

	public boolean isDefault() {
		
		return isDefault;
		
	}
	
	public void setDefault(boolean isDefault) {
		
		this.isDefault = isDefault;
		
	}
	
}
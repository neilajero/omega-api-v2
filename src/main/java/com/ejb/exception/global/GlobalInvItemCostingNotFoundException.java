package com.ejb.exception.global;

import com.util.Debug;

public class GlobalInvItemCostingNotFoundException extends Exception {

	public GlobalInvItemCostingNotFoundException() {
	      Debug.print("GlobalInvItemCostingNotFoundException Constructor");
	   }

	public GlobalInvItemCostingNotFoundException(String msg) {
	      super(msg);
	      Debug.print("GlobalInvItemCostingNotFoundException Constructor");
	   }
}
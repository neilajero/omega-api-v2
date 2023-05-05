/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ad;


import com.util.ad.AdApprovalUserDetails;

public class AdModApprovalUserDetails extends AdApprovalUserDetails implements java.io.Serializable {

    private String AU_USR_NM;

    public AdModApprovalUserDetails() {
    }

    public String getAuUsrName() {
   	
   	    return AU_USR_NM;
   	
    }
   
    public void setAuUsrName(String AU_USR_NM) {
   	
   	    this.AU_USR_NM = AU_USR_NM;
   	
    }
       	    	      
} // AdModApprovalUserDetails class
/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.mod.ar;

import com.util.ar.ArStandardMemoLineClassDetails;

/**
 *
 * @author ASUS-OMEGA
 */
public class ArModStandardMemoLineClassDetails extends ArStandardMemoLineClassDetails implements java.io.Serializable{
    
    private String SMC_LN = null;
    private String SMC_AD_BRANCH_NAME = null;
    private String SMC_SML_NM = null;
    private String SMC_CC_NM = null;
    private String SMC_CST_CSTMR_CODE = null;
    private String SMC_CST_NM = null;
    
    
 
    public String getSmcLine(){
        return SMC_LN;
    }
    
    public void setSmcLine(String SMC_LN){
        this.SMC_LN = SMC_LN;
    }
    
    
    public String getSmcAdBranchName(){
        return SMC_AD_BRANCH_NAME;
    }
    
    public void setSmcAdBranchName(String SMC_AD_BRANCH_NAME){
        this.SMC_AD_BRANCH_NAME = SMC_AD_BRANCH_NAME;
    }
    
    public String getSmcStandardMemoLineName(){
        return SMC_SML_NM;
    }
    
    public void setSmcStandardMemoLineName(String SMC_SML_NM){
        this.SMC_SML_NM = SMC_SML_NM;
    }
    
    public String getSmcCustomerClassName(){
        return SMC_CC_NM;
    }
    
    public void setSmcCustomerClassName(String SMC_SML_NM){
        this.SMC_CC_NM = SMC_CC_NM;
    }
    
    public String getSmcCustomerCode(){
        return SMC_CST_CSTMR_CODE;
    }
    
    public void setSmcCustomerCode(String SMC_CST_CSTMR_CODE){
        this.SMC_CST_CSTMR_CODE = SMC_CST_CSTMR_CODE;
    }
   
    public String getSmcCustomerName(){
        return SMC_CST_NM;
    }
    
    public void setSmcCustomerName(String SMC_CST_NM){
        this.SMC_CST_NM = SMC_CST_NM;
    }
    
}
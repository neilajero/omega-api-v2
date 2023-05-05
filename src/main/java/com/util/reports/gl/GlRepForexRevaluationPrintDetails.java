/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.gl;


public class GlRepForexRevaluationPrintDetails implements java.io.Serializable {

   private String FXP_FRL_COA_ACCNT_NMBR;
   private String FXP_FRL_COA_ACCNT_DESC;
   private String FXP_FRL_COA_ACCNT_TYP;
   private double FXP_FRL_RT;
   private double FXP_FRL_FRX_AMNT;
   private double FXP_FRX_GN_LSS;
   private double FXP_FRL_FRX_RMNG_AMNT;

   public GlRepForexRevaluationPrintDetails() {
   }

   public String getFxpFrlCoaAccountNumber() {
   	
   	  return FXP_FRL_COA_ACCNT_NMBR;
   	
   }
   
   public void setFxpFrlCoaAccountNumber(String FXP_FRL_COA_ACCNT_NMBR) {
   	
   	  this.FXP_FRL_COA_ACCNT_NMBR = FXP_FRL_COA_ACCNT_NMBR;
   	
   }
   
   public String getFxpFrlCoaAccountDescription() {
   	
   	  return FXP_FRL_COA_ACCNT_DESC;
   	
   }
   
   public void setFxpFrlCoaAccountDescription(String FXP_FRL_COA_ACCNT_DESC) {
   	
   	  this.FXP_FRL_COA_ACCNT_DESC = FXP_FRL_COA_ACCNT_DESC;
   	
   }
   
   public String getFxpFrlCoaAccountType() {
   	
   	  return FXP_FRL_COA_ACCNT_TYP;
   	
   }
   
   public void setFxpFrlCoaAccountType(String FXP_FRL_COA_ACCNT_TYP) {
   	
   	  this.FXP_FRL_COA_ACCNT_TYP = FXP_FRL_COA_ACCNT_TYP;
   	
   }
   
   public double getFxpFrlRate() {
   	
   	  return FXP_FRL_RT;
   	
   }
   
   public void setFxpFrlRate(double FXP_FRL_RT) {
   	
   	  this.FXP_FRL_RT = FXP_FRL_RT;
   	
   }
   
   public double getFxpFrlForexAmount() {
   	
   	  return FXP_FRL_FRX_AMNT;
   	
   }
   
   public void setFxpFrlForexAmount(double FXP_FRL_FRX_AMNT) {
   	
   	  this.FXP_FRL_FRX_AMNT = FXP_FRL_FRX_AMNT;
   	
   }
   
   public double getFxpForexGainLoss() {
   	
   	  return FXP_FRX_GN_LSS;
   	
   }
   
   public void setFxpForexGainLoss(double FXP_FRX_GN_LSS) {
   	
   	  this.FXP_FRX_GN_LSS = FXP_FRX_GN_LSS;
   	
   }
   
   public double getFxpForexRemainingAmount() {
   	
   	  return FXP_FRL_FRX_RMNG_AMNT;
   	
   }
   
   public void setFxpForexRemainingAmount(double FXP_FRL_FRX_RMNG_AMNT) {
   	
   	  this.FXP_FRL_FRX_RMNG_AMNT = FXP_FRL_FRX_RMNG_AMNT;
   	
   }

 } // GlRepForexRevaluationPrintDetails
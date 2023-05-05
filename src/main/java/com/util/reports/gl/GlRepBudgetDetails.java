/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.gl;

import java.util.Comparator;
import java.util.Date;

public class GlRepBudgetDetails implements java.io.Serializable {

   private String BGT_ACCNT_NMBR;
   private String BGT_ACCNT_DESC;
   private String BGT_DESC;
   private String BGT_DCMNT_NMBR;
   private Date BDG_DATE;
   private String BGT_MISC1;
   private String BGT_MISC2;
   private String BGT_MISC3;
   private String BGT_MISC4;
   private String BGT_MISC5;
   private String BGT_MISC6;
   private double BGT_AMNT;
   private double BGT_RL_PRCNTG1;
   private double BGT_RL_PRCNTG2;
   private double BGT_ACCNT_BLNC;
   
   private String BGT_BRNCH;
   private String BGT_BAU_NM;
   private String BGT_IS_BAU;
   private String BGT_RGN;
   private String BGT_SGMNT_ACCNT;
   private String BGT_MNTH;
   

   public GlRepBudgetDetails() {
   }

   public String getBgtAccountNumber() {
   	
   	  return BGT_ACCNT_NMBR;
   	 
   }
   
   public void setBgtAccountNumber(String BGT_ACCNT_NMBR) {
   	
   	  this.BGT_ACCNT_NMBR = BGT_ACCNT_NMBR;  
   	
   }
   
   public String getBgtAccountDescription() {
   	
   	  return BGT_ACCNT_DESC;
   	 
   }
   
   public void setBgtAccountDescription(String BGT_ACCNT_DESC) {
   	
   	  this.BGT_ACCNT_DESC = BGT_ACCNT_DESC;  
   	
   }
   
   public String getBgtDescription() {
	   	
   	  return BGT_DESC;
   	 
   }
   
   public void setBgtDescription(String BGT_DESC) {
   	
   	  this.BGT_DESC = BGT_DESC;  
   	
   }
   
   public String getBgtDocumentNumber() {
	   	
   	  return BGT_DCMNT_NMBR;
   	 
   }
   
   public void setBgtDocumentNumber(String BGT_DCMNT_NMBR) {
   	
   	  this.BGT_DCMNT_NMBR = BGT_DCMNT_NMBR;  
   	
   }
   
   public Date getBgtDate() {
	   	
   	  return BDG_DATE;
   	 
   }
   
   public void setBgtDate(Date BDG_DATE) {
   	
   	  this.BDG_DATE = BDG_DATE;  
   	
   }
   
   public String getBgtMisc1() {
	   	
   	  return BGT_MISC1;
   	 
   }
   
   public void setBgtMisc1(String BGT_MISC1) {
   	
   	  this.BGT_MISC1 = BGT_MISC1;  
   	
   }
   
   public String getBgtMisc2() {
	   	
   	  return BGT_MISC2;
   	 
   }
   
   public void setBgtMisc2(String BGT_MISC2) {
   	
   	  this.BGT_MISC2 = BGT_MISC2;  
   	
   }
	   
   public String getBgtMisc3() {
	   	
   	  return BGT_MISC3;
   	 
   }
   
   public void setBgtMisc3(String BGT_MISC3) {
   	
   	  this.BGT_MISC3 = BGT_MISC3;  
   	
   }
		   
   public String getBgtMisc4() {
	   	
   	  return BGT_MISC4;
   	 
   }
   
   public void setBgtMisc4(String BGT_MISC4) {
   	
   	  this.BGT_MISC4 = BGT_MISC4;  
   	
   }
			   
   public String getBgtMisc5() {
	   	
   	  return BGT_MISC5;
   	 
   }
   
   public void setBgtMisc5(String BGT_MISC5) {
   	
   	  this.BGT_MISC5 = BGT_MISC5;  
   	
   }
				   
   public String getBgtMisc6() {
	   	
   	  return BGT_MISC6;
   	 
   }
   
   public void setBgtMisc6(String BGT_MISC6) {
   	
   	  this.BGT_MISC6 = BGT_MISC6;  
   	
   }
	   
   
   public double getBgtAmount() {
   	
   	  return BGT_AMNT;
   	  
   }
   
   public void setBgtAmount(double BGT_AMNT){
   	
   	  this.BGT_AMNT = BGT_AMNT;
   	
   }
   
   public double getBgtRulePercentage1() {
	   	
   	  return BGT_RL_PRCNTG1;
   	  
   }
   
   public void setBgtRulePercentage1(double BGT_RL_PRCNTG1){
   	
   	  this.BGT_RL_PRCNTG1 = BGT_RL_PRCNTG1;
   	
   }
   
   public double getBgtRulePercentage2() {
	   	
	   	  return BGT_RL_PRCNTG2;
	   	  
	   }
	   
	   public void setBgtRulePercentage2(double BGT_RL_PRCNTG2){
	   	
	   	  this.BGT_RL_PRCNTG2 = BGT_RL_PRCNTG2;
	   	
	   }
   
   public double getBgtAccountBalance() {
	   	
   	  return BGT_ACCNT_BLNC;
   	  
   }
   
   public void setBgtAccountBalance(double BGT_ACCNT_BLNC){
   	
   	  this.BGT_ACCNT_BLNC = BGT_ACCNT_BLNC;
   	
   }
   
    public String getBgtBranch() {
	   	
   	  return BGT_BRNCH;
   	  
   }
   
   public void setBgtBranch(String BGT_BRNCH){
   	
   	  this.BGT_BRNCH = BGT_BRNCH;
   	
   }
   
    public String getBgtBauName() {
	   	
   	  return BGT_BAU_NM;
   	  
   }
   
   public void setBgtBauName(String BGT_BAU_NM){
   	
   	  this.BGT_BAU_NM = BGT_BAU_NM;
   	
   }
   
   
   public String getBgtIsBau() {
	   	
   	  return BGT_IS_BAU;
   	  
   }
   
   public void setBgtIsBau(String BGT_IS_BAU){
   	
   	  this.BGT_IS_BAU = BGT_IS_BAU;
   	
   }
   
   
   public String getBgtRegion() {
	   	
   	  return BGT_RGN;
   	  
   }
   
   public void setBgtRegion(String BGT_RGN){
   	
   	  this.BGT_RGN = BGT_RGN;
   	
   }
   
   
    public String getBgtSegmentAccount() {
	   	
   	  return BGT_SGMNT_ACCNT;
   	  
   }
   
   public void setBgtSegmentAccount(String BGT_SGMNT_ACCNT){
   	
   	  this.BGT_SGMNT_ACCNT = BGT_SGMNT_ACCNT;
   	
   }
   
   
    public String getBgtMonth() {
	   	
   	  return BGT_MNTH;
   	  
   }
   
   public void setBgtMonth(String BGT_MNTH){
   	
   	  this.BGT_MNTH = BGT_MNTH;
   	
   }


   
   
   public static Comparator AccountNumberComparator = (r1, r2) -> {

       String GL_ACCNT_NMBR1 = ((GlRepBudgetDetails) r1).getBgtAccountNumber() != null ? ((GlRepBudgetDetails) r1).getBgtAccountNumber() : "";
       String GL_ACCNT_NMBR2 = ((GlRepBudgetDetails) r2).getBgtAccountNumber() != null ? ((GlRepBudgetDetails) r2).getBgtAccountNumber() : "";

       String GL_MISC4_1 = ((GlRepBudgetDetails) r1).getBgtMisc4() != null ? ((GlRepBudgetDetails) r1).getBgtMisc4() : "";
       String GL_MISC4_2 = ((GlRepBudgetDetails) r2).getBgtMisc4() != null ? ((GlRepBudgetDetails) r2).getBgtMisc4() : "";

       String GL_MISC5_1 = ((GlRepBudgetDetails) r1).getBgtMisc5() != null ? ((GlRepBudgetDetails) r1).getBgtMisc5() : "";
       String GL_MISC5_2 = ((GlRepBudgetDetails) r2).getBgtMisc5() != null ? ((GlRepBudgetDetails) r2).getBgtMisc5() : "";

       String GL_MISC6_1 = ((GlRepBudgetDetails) r1).getBgtMisc6() != null ? ((GlRepBudgetDetails) r1).getBgtMisc6() : "";
       String GL_MISC6_2 = ((GlRepBudgetDetails) r2).getBgtMisc6() != null ? ((GlRepBudgetDetails) r2).getBgtMisc6() : "";

       Date GL_DATE1 = ((GlRepBudgetDetails) r1).getBgtDate();
       Date GL_DATE2 = ((GlRepBudgetDetails) r2).getBgtDate();



       if (!(GL_ACCNT_NMBR1.equals(GL_ACCNT_NMBR2))) {

           return GL_ACCNT_NMBR1.compareTo(GL_ACCNT_NMBR2);

       } else if(!(GL_MISC4_1.equals(GL_MISC4_2))) {

           return GL_MISC4_1.compareTo(GL_MISC4_2);

       } else if(!(GL_MISC5_1.equals(GL_MISC5_2))) {

           return GL_MISC5_1.compareTo(GL_MISC5_2);

       } else if(!(GL_MISC6_1.equals(GL_MISC6_2))) {

           return GL_MISC6_1.compareTo(GL_MISC6_2);


       } else {

           return GL_DATE1.compareTo(GL_DATE2);

       }



   };

} // GlModBudgetAmountDetails class
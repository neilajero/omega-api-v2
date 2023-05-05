/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.gl;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public class GlRepJournalRegisterDetails implements Serializable {
	
	private String JR_JL_COA_ACCNT_NMBR;
	private String JR_JL_COA_ACCNT_DESC;
	private Date JR_JR_DT;
	private String  JR_JR_DCMNT_NMBR;
	private double JR_JL_DBT;
	private double JR_JL_CRDT;
	private String JR_JR_DESC;
	private String JR_JR_SRC;
	private String JR_JR_CTGRY;
	private String JR_JR_BTCH;
	private String JR_JR_RFRNC_NMBR;
	private double JR_JR_AMNT;
	
	private String ORDER_BY;
	
	public String getJrJlCoaAccountNumber() {
		
		return JR_JL_COA_ACCNT_NMBR;
		
	}
	
	public void setJrJlCoaAccountNumber(String JR_JL_COA_ACCNT_NMBR) {
		
		this.JR_JL_COA_ACCNT_NMBR = JR_JL_COA_ACCNT_NMBR;
		
	}
	
	public String getJrJlCoaAccountDescription() {
		
		return JR_JL_COA_ACCNT_DESC;
		
	}
	
	public void setJrJlCoaAccountDescription(String JR_JL_COA_ACCNT_DESC) {
		
		this.JR_JL_COA_ACCNT_DESC = JR_JL_COA_ACCNT_DESC;
		
	}
	
	public Date getJrJrDate() {
		
		return JR_JR_DT;
		
	}
	
	public void setJrJrDate(Date JR_JR_DT) {
		
		this.JR_JR_DT = JR_JR_DT;
		
	}
	
	public String getJrJrDocumentNumber() {
		
		return JR_JR_DCMNT_NMBR;
		
	}
	
	public void setJrJrDocumentNumber(String JR_JR_DCMNT_NMBR) {
		
		this.JR_JR_DCMNT_NMBR = JR_JR_DCMNT_NMBR;
		
	}
	
	public double getJrJlDebit() {
		
		return JR_JL_DBT;
		
	}
	
	public void setJrJlDebit(double JR_JL_DBT) {
		
		this.JR_JL_DBT = JR_JL_DBT;
		
	}
	
	public double getJrJlCredit() {
		
		return JR_JL_CRDT;
		
	}
	
	public void setJrJlCredit(double JR_JL_CRDT) {
		
		this.JR_JL_CRDT = JR_JL_CRDT;
		
	}
	
	public String getJrJrDescription() {
		
		return JR_JR_DESC;
		
	}
	
	public void setJrJrDescription(String JR_JR_DESC) {
		
		this.JR_JR_DESC = JR_JR_DESC;
		
	}
	
	public String getJrJrSource() {
		
		return JR_JR_SRC;
		
	}
	
	public void setJrJrSource(String JR_JR_SRC) {
		
		this.JR_JR_SRC = JR_JR_SRC;
		
	}
	
	public String getJrJrCategory() {
		
		return JR_JR_CTGRY;
		
	}
	
	public void setJrJrCategory(String JR_JR_CTGRY) {
		
		this.JR_JR_CTGRY = JR_JR_CTGRY;
		
	}
	
	public String getJrJrBatch() {
		
		return JR_JR_BTCH;
		
	}
	
	public void setJrJrBatch(String JR_JR_BTCH) {
		
		this.JR_JR_BTCH = JR_JR_BTCH;
		
	}
	
	public String getOrderBy() {
		
		return ORDER_BY;
		
	}
	
	public void setOrderBy(String ORDER_BY) {
		
		this.ORDER_BY = ORDER_BY;
		
	}
	
	public String getJrJrReferenceNumber() {
		
		return JR_JR_RFRNC_NMBR;
		
	}
	
	public void setJrJrReferenceNumber(String JR_JR_RFRNC_NMBR) {
		
		this.JR_JR_RFRNC_NMBR = JR_JR_RFRNC_NMBR;
		
	}
	
	public double getJrJrAmount() {
		
		return JR_JR_AMNT;
		
	}
	
	public void setJrJrAmount(double JR_JR_AMNT) {
		
		this.JR_JR_AMNT = JR_JR_AMNT;
		
	}
	
	public static Comparator JournalSourceComparator = (JR, anotherJR) -> {

        String JR_JR_SRC1 = ((GlRepJournalRegisterDetails)JR).getJrJrSource();
        Date JR_JR_DT1 = ((GlRepJournalRegisterDetails) JR).getJrJrDate();
        String JR_JR_DCMNT_NMBR1 = ((GlRepJournalRegisterDetails) JR).getJrJrDocumentNumber();

        String JR_JR_SRC2 = ((GlRepJournalRegisterDetails)anotherJR).getJrJrSource();
        Date JR_JR_DT2 = ((GlRepJournalRegisterDetails) anotherJR).getJrJrDate();
        String JR_JR_DCMNT_NMBR2 = ((GlRepJournalRegisterDetails) anotherJR).getJrJrDocumentNumber();

        String ORDER_BY = ((GlRepJournalRegisterDetails) JR).getOrderBy();

        if (!(JR_JR_SRC1.equals(JR_JR_SRC2))) {

            return JR_JR_SRC1.compareTo(JR_JR_SRC2);

        } else {

            if(ORDER_BY.equals("DATE") && !(JR_JR_DT1.equals(JR_JR_DT2))){

                return JR_JR_DT1.compareTo(JR_JR_DT2);

            } else {

                return JR_JR_DCMNT_NMBR1.compareTo(JR_JR_DCMNT_NMBR2);

            }

        }

    };
	
	public static Comparator JournalCategoryComparator = (JR, anotherJR) -> {

        String JR_JR_CTGRY1 = ((GlRepJournalRegisterDetails)JR).getJrJrCategory();
        Date JR_JR_DT1 = ((GlRepJournalRegisterDetails) JR).getJrJrDate();
        String JR_JR_DCMNT_NMBR1 = ((GlRepJournalRegisterDetails) JR).getJrJrDocumentNumber();

        String JR_JR_CTGRY2 = ((GlRepJournalRegisterDetails)anotherJR).getJrJrCategory();
        Date JR_JR_DT2 = ((GlRepJournalRegisterDetails) anotherJR).getJrJrDate();
        String JR_JR_DCMNT_NMBR2 = ((GlRepJournalRegisterDetails) anotherJR).getJrJrDocumentNumber();

        String ORDER_BY = ((GlRepJournalRegisterDetails) JR).getOrderBy();

        if (!(JR_JR_CTGRY1.equals(JR_JR_CTGRY2))) {

            return JR_JR_CTGRY1.compareTo(JR_JR_CTGRY2);

        } else {

            if(ORDER_BY.equals("DATE") && !(JR_JR_DT1.equals(JR_JR_DT2))){

                return JR_JR_DT1.compareTo(JR_JR_DT2);

            } else {

                return JR_JR_DCMNT_NMBR1.compareTo(JR_JR_DCMNT_NMBR2);

            }

        }

    };
	
	public static Comparator JournalBatchComparator = (JR, anotherJR) -> {

        String JR_JR_BTCH1 = ((GlRepJournalRegisterDetails)JR).getJrJrBatch();
        Date JR_JR_DT1 = ((GlRepJournalRegisterDetails) JR).getJrJrDate();
        String JR_JR_DCMNT_NMBR1 = ((GlRepJournalRegisterDetails) JR).getJrJrDocumentNumber();

        String JR_JR_BTCH2 = ((GlRepJournalRegisterDetails)anotherJR).getJrJrBatch();
        Date JR_JR_DT2 = ((GlRepJournalRegisterDetails) anotherJR).getJrJrDate();
        String JR_JR_DCMNT_NMBR2 = ((GlRepJournalRegisterDetails) anotherJR).getJrJrDocumentNumber();

        String ORDER_BY = ((GlRepJournalRegisterDetails) JR).getOrderBy();

        if (!(JR_JR_BTCH1.equals(JR_JR_BTCH2))) {

            return JR_JR_BTCH1.compareTo(JR_JR_BTCH2);

        } else {

            if(ORDER_BY.equals("DATE") && !(JR_JR_DT1.equals(JR_JR_DT2))){

                return JR_JR_DT1.compareTo(JR_JR_DT2);

            } else {

                return JR_JR_DCMNT_NMBR1.compareTo(JR_JR_DCMNT_NMBR2);

            }

        }

    };
	
	public static Comparator NoGroupComparator = (JR, anotherJR) -> {

        Date JR_JR_DT1 = ((GlRepJournalRegisterDetails) JR).getJrJrDate();
        String JR_JR_DCMNT_NMBR1 = ((GlRepJournalRegisterDetails) JR).getJrJrDocumentNumber();

        Date JR_JR_DT2 = ((GlRepJournalRegisterDetails) anotherJR).getJrJrDate();
        String JR_JR_DCMNT_NMBR2 = ((GlRepJournalRegisterDetails) anotherJR).getJrJrDocumentNumber();

        String ORDER_BY = ((GlRepJournalRegisterDetails) JR).getOrderBy();

        if(ORDER_BY.equals("DATE") && !(JR_JR_DT1.equals(JR_JR_DT2))){

            return JR_JR_DT1.compareTo(JR_JR_DT2);

        } else {

            return JR_JR_DCMNT_NMBR1.compareTo(JR_JR_DCMNT_NMBR2);

        }

    };
	
	public static Comparator CoaAccountNumberComparator = (JR, anotherJR) -> {

        Date JR_JR_DT1 = ((GlRepJournalRegisterDetails) JR).getJrJrDate();
        String JR_JR_DCMNT_NMBR1 = ((GlRepJournalRegisterDetails) JR).getJrJrDocumentNumber();
        String JR_DR_GL_COA_ACCNT_NMBR1 = ((GlRepJournalRegisterDetails) JR).getJrJlCoaAccountNumber();

        Date JR_JR_DT2 = ((GlRepJournalRegisterDetails) anotherJR).getJrJrDate();
        String JR_JR_DCMNT_NMBR2 = ((GlRepJournalRegisterDetails) anotherJR).getJrJrDocumentNumber();
        String JR_DR_GL_COA_ACCNT_NMBR2 = ((GlRepJournalRegisterDetails) anotherJR).getJrJlCoaAccountNumber();

        String ORDER_BY = ((GlRepJournalRegisterDetails) JR).getOrderBy();

        if (!(JR_DR_GL_COA_ACCNT_NMBR1.equals(JR_DR_GL_COA_ACCNT_NMBR2))) {

            return JR_DR_GL_COA_ACCNT_NMBR1.compareTo(JR_DR_GL_COA_ACCNT_NMBR2);

        }  else {

            if(ORDER_BY.equals("DATE") && !(JR_JR_DT1.equals(JR_JR_DT2))){
                return JR_JR_DT1.compareTo(JR_JR_DT2);

            } else {

                return JR_JR_DCMNT_NMBR1.compareTo(JR_JR_DCMNT_NMBR2);

            }

           }

    };
	
}
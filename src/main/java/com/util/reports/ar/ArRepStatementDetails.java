/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ar;

import java.util.Date;

public class ArRepStatementDetails implements Cloneable, java.io.Serializable {

	private String SMT_CSTMR_BTCH;
	private String SMT_CSTMR_DPRTMNT;
	private String SMT_CSTMR_CODE;
	private String SMT_CSTMR_NM;
	private String SMT_CSTMR_ADDRSS;


	private Date SMT_TRNSCTN_DT;
	private String SMT_TRNSCTN_NMBR;
	private String SMT_TRNSCTN_RFRNC_NMBR;
	private String SMT_TRNSCTN_DSCRPTN;
	private Double SMT_TRNSCTN_AMNT;

	private String ORDER_BY;

	public ArRepStatementDetails() {

	}

   public Object clone() throws CloneNotSupportedException {


 		try {
 		   return super.clone();
 		 }
 		  catch (CloneNotSupportedException e) {
 			  throw e;

 		  }
 	 }

   public String getSmtCustomerBatch() {

   	   return SMT_CSTMR_BTCH;

   }
   public void setSmtCustomerBatch(String SMT_CSTMR_BTCH) {

   	   this.SMT_CSTMR_BTCH = SMT_CSTMR_BTCH;

   }

   public String getSmtCustomerDepartment() {

   	   return SMT_CSTMR_DPRTMNT;

   }
   public void setSmtCustomerDepartment(String SMT_CSTMR_DPRTMNT) {

   	   this.SMT_CSTMR_DPRTMNT = SMT_CSTMR_DPRTMNT;

   }

   public String getSmtCustomerCode() {

		return SMT_CSTMR_CODE;

	}
	public void setSmtCustomerCode(String SMT_CSTMR_CODE) {

		this.SMT_CSTMR_CODE = SMT_CSTMR_CODE;

	}

	public String getSmtCustomerName() {

   	   return SMT_CSTMR_NM;

   }
   public void setSmtCustomerName(String SMT_CSTMR_NM) {

   	   this.SMT_CSTMR_NM = SMT_CSTMR_NM;

   }

   public String getSmtCustomerAddress() {

   	   return SMT_CSTMR_ADDRSS;

   }
   public void setSmtCustomerAddress(String SMT_CSTMR_ADDRSS) {

   	   this.SMT_CSTMR_ADDRSS = SMT_CSTMR_ADDRSS;

   }


	public Date getSmtTransactionDate() {

		return SMT_TRNSCTN_DT;

	}
	public void setSmtTransactionDate(Date SMT_TRNSCTN_DT) {

		this.SMT_TRNSCTN_DT = SMT_TRNSCTN_DT;

	}

	public String getSmtTransactionNumber() {

		return SMT_TRNSCTN_NMBR;

	}
	public void setSmtTransactionNumber(String SMT_TRNSCTN_NMBR) {

		this.SMT_TRNSCTN_NMBR = SMT_TRNSCTN_NMBR;

	}

	public String getSmtTransactionReferenceNumber() {

		return SMT_TRNSCTN_RFRNC_NMBR;

	}
	public void setSmtTransactionReferenceNumber(String SMT_TRNSCTN_RFRNC_NMBR) {

		this.SMT_TRNSCTN_RFRNC_NMBR = SMT_TRNSCTN_RFRNC_NMBR;

	}

	public String getSmtTransactionDescription() {

		return SMT_TRNSCTN_DSCRPTN;

	}
	public void setSmtTransactionDescription(String SMT_TRNSCTN_DSCRPTN) {

		this.SMT_TRNSCTN_DSCRPTN = SMT_TRNSCTN_DSCRPTN;

	}

	public Double getSmtTransactionAmount() {

		return SMT_TRNSCTN_AMNT;

	}
	public void setSmtTransactionAmount(Double SMT_TRNSCTN_AMNT) {

		this.SMT_TRNSCTN_AMNT = SMT_TRNSCTN_AMNT;

	}







   //SMT_TRNSCTN
   /*public static Comparator TransactionComparator = (SMT, anotherSMT) -> {

       String SMT_TRNSCTN1 = ((ArRepStatementDetails) SMT).getSmtTransaction();

       String SMT_TRNSCTN2 = ((ArRepStatementDetails) anotherSMT).getSmtTransaction();

       String ORDER_BY = ((ArRepStatementDetails) SMT).getOrderBy();

       if (!(SMT_TRNSCTN1.equals(SMT_TRNSCTN2))) {

           return SMT_TRNSCTN1.compareTo(SMT_TRNSCTN2);

       } else {

           return SMT_TRNSCTN1.compareTo(SMT_TRNSCTN2);

       }

   };

 //SMT_TRNSCTN
   public static Comparator CustomerCodeComparator = (SMT, anotherSMT) -> {

       String SMT_CSTMR_CD1 = ((ArRepStatementDetails) SMT).getSmtCustomerCode() == null ? "" :  ((ArRepStatementDetails) SMT).getSmtCustomerCode();

       String SMT_CSTMR_CD2 = ((ArRepStatementDetails) anotherSMT).getSmtCustomerCode() == null ? "" : ((ArRepStatementDetails) anotherSMT).getSmtCustomerCode();

       String ORDER_BY = ((ArRepStatementDetails) SMT).getOrderBy();
       Debug.print("TRANSACTION="+((ArRepStatementDetails) anotherSMT).getSmtTransaction());
       Debug.print("SMT_CSTMR_CD1="+SMT_CSTMR_CD1);
       Debug.print("SMT_CSTMR_CD2="+SMT_CSTMR_CD2);

       if (!(SMT_CSTMR_CD1.equals(SMT_CSTMR_CD2))) {

           return SMT_CSTMR_CD1.compareTo(SMT_CSTMR_CD2);

       } else {

           return SMT_CSTMR_CD1.compareTo(SMT_CSTMR_CD2);

       }

   };


	public static Comparator DueDateComparator = (SMT, anotherSMT) -> {

        String SMT_CST_BTCH1 = ((ArRepStatementDetails) SMT).getSmtCustomerBatch() == null ? "" :  ((ArRepStatementDetails) SMT).getSmtCustomerBatch();

        String SMT_CST_BTCH2 = ((ArRepStatementDetails) anotherSMT).getSmtCustomerBatch() == null ? "" : ((ArRepStatementDetails) anotherSMT).getSmtCustomerBatch();


        String SMT_TRANS1 = ((ArRepStatementDetails) SMT).getSmtTransaction() == null ? "" :  ((ArRepStatementDetails) SMT).getSmtTransaction();

        String SMT_TRANS2 = ((ArRepStatementDetails) anotherSMT).getSmtTransaction() == null ? "" : ((ArRepStatementDetails) anotherSMT).getSmtTransaction();



        Date SM_DT1 = ((ArRepStatementDetails) SMT).getSmtDate();

        Date SM_DT2 = ((ArRepStatementDetails) anotherSMT).getSmtDate();

        Date SM_DUE_DT1 = ((ArRepStatementDetails) SMT).getSmtDueDate();

        Date SM_DUE_DT2 = ((ArRepStatementDetails) anotherSMT).getSmtDueDate();

        String SMT_CSTMR_CD1 = ((ArRepStatementDetails) SMT).getSmtCustomerCode() == null ? "" :  ((ArRepStatementDetails) SMT).getSmtCustomerCode();

        String SMT_CSTMR_CD2 = ((ArRepStatementDetails) anotherSMT).getSmtCustomerCode() == null ? "" : ((ArRepStatementDetails) anotherSMT).getSmtCustomerCode();

        String SMT_ITM1 = ((ArRepStatementDetails) SMT).getSmtItemDescription() == null ? "" :  ((ArRepStatementDetails) SMT).getSmtItemDescription();

        String SMT_ITM2 = ((ArRepStatementDetails) anotherSMT).getSmtItemDescription() == null ? "" : ((ArRepStatementDetails) anotherSMT).getSmtItemDescription();


        String ORDER_BY = ((ArRepStatementDetails) SMT).getOrderBy();

        if (!(SMT_CSTMR_CD1.equals(SMT_CSTMR_CD2))) {

            return SMT_CSTMR_CD1.compareTo(SMT_CSTMR_CD2);

        } else if (!(SM_DT1.equals(SM_DT2))) {

            return SM_DT1.compareTo(SM_DT2);

        } else if (!(SMT_TRANS1.equals(SMT_TRANS2))) {

            return SMT_TRANS1.compareTo(SMT_TRANS2);

        } else {

            return SM_DUE_DT1.compareTo(SM_DUE_DT2);

        }

    };

   public static Comparator DateComparator = (SMT, anotherSMT) -> {

       String SMT_CST_BTCH1 = ((ArRepStatementDetails) SMT).getSmtCustomerBatch() == null ? "" :  ((ArRepStatementDetails) SMT).getSmtCustomerBatch();

       String SMT_CST_BTCH2 = ((ArRepStatementDetails) anotherSMT).getSmtCustomerBatch() == null ? "" : ((ArRepStatementDetails) anotherSMT).getSmtCustomerBatch();

       Date SM_DT1 = ((ArRepStatementDetails) SMT).getSmtDate();

       Date SM_DT2 = ((ArRepStatementDetails) anotherSMT).getSmtDate();

       String SMT_CSTMR_CD1 = ((ArRepStatementDetails) SMT).getSmtCustomerCode() == null ? "" :  ((ArRepStatementDetails) SMT).getSmtCustomerCode();

       String SMT_CSTMR_CD2 = ((ArRepStatementDetails) anotherSMT).getSmtCustomerCode() == null ? "" : ((ArRepStatementDetails) anotherSMT).getSmtCustomerCode();

       String SMT_ITM1 = ((ArRepStatementDetails) SMT).getSmtItemDescription() == null ? "" :  ((ArRepStatementDetails) SMT).getSmtItemDescription();

       String SMT_ITM2 = ((ArRepStatementDetails) anotherSMT).getSmtItemDescription() == null ? "" : ((ArRepStatementDetails) anotherSMT).getSmtItemDescription();


       String SMT_CST_DEP1 = ((ArRepStatementDetails) SMT).getSmtCustomerDepartment() == null ? "" :  ((ArRepStatementDetails) SMT).getSmtCustomerDepartment();

       String SMT_CST_DEP2 = ((ArRepStatementDetails) anotherSMT).getSmtCustomerDepartment() == null ? "" : ((ArRepStatementDetails) anotherSMT).getSmtCustomerDepartment();


       String ORDER_BY = ((ArRepStatementDetails) SMT).getOrderBy();

       if (!(SMT_CST_BTCH1.equals(SMT_CST_BTCH2))) {

           return SMT_CST_BTCH1.compareTo(SMT_CST_BTCH2);

       } else if (!(SMT_CST_DEP1.equals(SMT_CST_DEP2))) {

           return SMT_CST_DEP1.compareTo(SMT_CST_DEP2);

       } else if (!(SMT_CSTMR_CD1.equals(SMT_CSTMR_CD2))) {

           return SMT_CSTMR_CD1.compareTo(SMT_CSTMR_CD2);

       } else {

           return SM_DT1.compareTo(SM_DT2);

       }

   };


 //SMT_TRNSCTN
   public static Comparator CustomerLedgerComparator = (SMT, anotherSMT) -> {

       String SMT_CST_BTCH1 = ((ArRepStatementDetails) SMT).getSmtCustomerBatch() == null ? "" :  ((ArRepStatementDetails) SMT).getSmtCustomerBatch();

       String SMT_CST_BTCH2 = ((ArRepStatementDetails) anotherSMT).getSmtCustomerBatch() == null ? "" : ((ArRepStatementDetails) anotherSMT).getSmtCustomerBatch();

       Date SM_DT1 = ((ArRepStatementDetails) SMT).getSmtDate();

       Date SM_DT2 = ((ArRepStatementDetails) anotherSMT).getSmtDate();

       String SMT_CSTMR_CD1 = ((ArRepStatementDetails) SMT).getSmtCustomerCode() == null ? "" :  ((ArRepStatementDetails) SMT).getSmtCustomerCode();

       String SMT_CSTMR_CD2 = ((ArRepStatementDetails) anotherSMT).getSmtCustomerCode() == null ? "" : ((ArRepStatementDetails) anotherSMT).getSmtCustomerCode();

       String SMT_ITM1 = ((ArRepStatementDetails) SMT).getSmtItemDescription() == null ? "" :  ((ArRepStatementDetails) SMT).getSmtItemDescription();

       String SMT_ITM2 = ((ArRepStatementDetails) anotherSMT).getSmtItemDescription() == null ? "" : ((ArRepStatementDetails) anotherSMT).getSmtItemDescription();


       String ORDER_BY = ((ArRepStatementDetails) SMT).getOrderBy();

       if (!(SMT_CSTMR_CD1.equals(SMT_CSTMR_CD2))) {

           return SMT_CSTMR_CD1.compareTo(SMT_CSTMR_CD2);

       } else if (!(SMT_ITM1.equals(SMT_ITM2))) {

           return SMT_ITM1.compareTo(SMT_ITM2);

       } else {

           return SM_DT1.compareTo(SM_DT2);

       }

   };



   public static Comparator AccountSummaryComparator = (SMT, anotherSMT) -> {


       Date SM_DT1 = ((ArRepStatementDetails) SMT).getSmtDate();

       Date SM_DT2 = ((ArRepStatementDetails) anotherSMT).getSmtDate();

       String SMT_CSTMR_CD1 = ((ArRepStatementDetails) SMT).getSmtCustomerCode() == null ? "" :  ((ArRepStatementDetails) SMT).getSmtCustomerCode();
       String SMT_CSTMR_CD2 = ((ArRepStatementDetails) anotherSMT).getSmtCustomerCode() == null ? "" : ((ArRepStatementDetails) anotherSMT).getSmtCustomerCode();

       String SMT_PM_PRJCT_CODE1 = ((ArRepStatementDetails) SMT).getSmtPmProjectCode() == null ? "" :  ((ArRepStatementDetails) SMT).getSmtPmProjectCode();
       String SMT_PM_PRJCT_CODE2 = ((ArRepStatementDetails) anotherSMT).getSmtPmProjectCode() == null ? "" : ((ArRepStatementDetails) anotherSMT).getSmtPmProjectCode();

       String SMT_PM_PRJCT_TYP1 = ((ArRepStatementDetails) SMT).getSmtPmProjectType() == null ? "" :  ((ArRepStatementDetails) SMT).getSmtPmProjectType();
       String SMT_PM_PRJCT_TYP2 = ((ArRepStatementDetails) anotherSMT).getSmtPmProjectType() == null ? "" : ((ArRepStatementDetails) anotherSMT).getSmtPmProjectType();

       String SMT_PM_CNTRCT_TRM_NM1 = ((ArRepStatementDetails) SMT).getSmtPmContractTermName() == null ? "" :  ((ArRepStatementDetails) SMT).getSmtPmContractTermName();
       String SMT_PM_CNTRCT_TRM_NM2 = ((ArRepStatementDetails) anotherSMT).getSmtPmContractTermName() == null ? "" : ((ArRepStatementDetails) anotherSMT).getSmtPmContractTermName();



       if (!(SMT_CSTMR_CD1.equals(SMT_CSTMR_CD2))) {

           return SMT_CSTMR_CD1.compareTo(SMT_CSTMR_CD2);

       } else if (!(SMT_PM_PRJCT_CODE1.equals(SMT_PM_PRJCT_CODE2))) {

           return SMT_PM_PRJCT_CODE1.compareTo(SMT_PM_PRJCT_CODE2);

       } else if (!(SMT_PM_PRJCT_TYP1.equals(SMT_PM_PRJCT_TYP2))) {

           return SMT_PM_PRJCT_TYP1.compareTo(SMT_PM_PRJCT_TYP2);

       } else if (!(SMT_PM_CNTRCT_TRM_NM1.equals(SMT_PM_CNTRCT_TRM_NM2))) {

           return SMT_PM_CNTRCT_TRM_NM1.compareTo(SMT_PM_CNTRCT_TRM_NM2);

       } else {

           return SM_DT1.compareTo(SM_DT2);

       }

   };*/

} // ArRepStatementDetails class
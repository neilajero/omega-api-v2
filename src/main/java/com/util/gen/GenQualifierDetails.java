/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gen;

public class GenQualifierDetails implements java.io.Serializable {

   private Integer QL_CODE;
   private String QL_ACCNT_TYP;
   private byte QL_BDGTNG_ALLWD;
   private byte QL_PSTNG_ALLWD;

   public GenQualifierDetails () {
   }

   public GenQualifierDetails (Integer QL_CODE, String QL_ACCNT_TYP,
      byte QL_BDGTNG_ALLWD, byte QL_PSTNG_ALLWD) {

      this.QL_CODE = QL_CODE;
      this.QL_ACCNT_TYP = QL_ACCNT_TYP;
      this.QL_BDGTNG_ALLWD = QL_BDGTNG_ALLWD;
      this.QL_PSTNG_ALLWD = QL_PSTNG_ALLWD;
   }

   public Integer getQlCode() {
      return QL_CODE;
   }

   public String getQlAccountType() {
      return QL_ACCNT_TYP;
   }

   public byte getQlBudgetingAllowed() {
      return QL_BDGTNG_ALLWD;
   }

   public byte getQlPostingAllowed() {
      return QL_PSTNG_ALLWD;
   }

   public String toString() {
       return "QL_CODE = " + QL_CODE + " QL_ACCNT_TYP = " + QL_ACCNT_TYP +
          " QL_BDGTNG_ALLWD = " + QL_BDGTNG_ALLWD + " QL_PSTNG_ALLWD = " + QL_PSTNG_ALLWD;
   }

} // GenQualifierDetails
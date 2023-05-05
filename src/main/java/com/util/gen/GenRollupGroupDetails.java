/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gen;


public class GenRollupGroupDetails implements java.io.Serializable {

   private Integer RLG_CODE;
   private String RLG_NM;
   private String RLG_DESC;

   public GenRollupGroupDetails() {
   }

   public GenRollupGroupDetails (Integer RLG_CODE, String RLG_NM, String RLG_DESC) {
   
      this.RLG_CODE = RLG_CODE;
      this.RLG_NM = RLG_NM;
      this.RLG_DESC = RLG_DESC;
   }

   public Integer getRlgCode() {
      return RLG_CODE;
   }

   public String getRlgName() {
      return RLG_NM;
   }

   public String getRlgDescription() {
      return  RLG_DESC;
   }

   public String toString() {
       return "RLG_CODE = " + RLG_CODE + " RLG_NM = " + RLG_NM +
          " RLG_DESC = " + RLG_DESC;
   }

} // GenRollupGroupDetails
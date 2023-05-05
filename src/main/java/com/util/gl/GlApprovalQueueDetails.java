/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gl;

public class GlApprovalQueueDetails implements java.io.Serializable {

   private Integer AQ_CODE;

   public GlApprovalQueueDetails() {
   }
   
   public GlApprovalQueueDetails (Integer AQ_CODE) {

      this.AQ_CODE = AQ_CODE;

   }

   public Integer getAqCode() {
      return AQ_CODE;
   }

   public String toString() {
       return "AQ_CODE = " + AQ_CODE;
   }

} // GlApprovalQueueDetails
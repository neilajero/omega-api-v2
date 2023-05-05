/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.gen;


public class GenModSegmentDetails extends GenSegmentDetails implements java.io.Serializable {

   private char SG_FL_SGMNT_SPRTR;
   
   public GenModSegmentDetails() {
   }

   public char getSgFlSegmentSeparator() {
   	
   	   return SG_FL_SGMNT_SPRTR;
   	
   }
   
   public void setSgFlSegmentSeparator(char SG_FL_SGMNT_SPRTR) {
   	
   	   this.SG_FL_SGMNT_SPRTR = SG_FL_SGMNT_SPRTR;
   	
   }

} // GenModSegmentDetails
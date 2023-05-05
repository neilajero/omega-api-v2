/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejb.exception.ap;

import com.util.Debug;

/**
 *
 * @author ASUS-OMEGA
 */
public class ApPONoBatchNameException extends Exception {
    
    public ApPONoBatchNameException(){
        Debug.print("ApPONoBatchNameException Constructor");
    }
    public ApPONoBatchNameException(String msg){
        super(msg);
        Debug.print("ApPONoBatchNameException Constructor");
    }
}

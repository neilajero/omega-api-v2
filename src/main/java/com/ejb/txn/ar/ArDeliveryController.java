package com.ejb.txn.ar;

import com.util.mod.ar.ArModSalesOrderDetails;

import jakarta.ejb.FinderException;
import jakarta.ejb.Local;

@Local
public interface ArDeliveryController {

    void saveArDelivery(java.util.ArrayList list, java.lang.String SO_DCMNT_NMBR, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    void deleteDeliveryByDvCode(java.lang.Integer DV_CODE) throws FinderException;

    ArModSalesOrderDetails getArSalesOrderByCode(java.lang.Integer SO_CODE) throws FinderException;

    java.util.ArrayList getDeliveryBySoCode(java.lang.Integer SO_CODE, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws FinderException;

    java.util.ArrayList getDeliveryBySoDocumentNumber(java.lang.String SO_DCMNT_NMBR, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws FinderException;

    java.util.ArrayList getAdLvTransactionStatusAll(java.lang.Integer AD_CMPNY);

}
package com.ejb.txnreports.ar;

import com.util.ad.AdCompanyDetails;
import com.util.mod.ar.ArModSalesOrderDetails;

import jakarta.ejb.Local;

@Local
public interface ArRepDeliveryPrintController {

    java.util.ArrayList executeArRepDeliveryPrintAll(java.lang.Integer SO_CODE, java.lang.Integer AD_CMPNY);

    java.util.ArrayList executeArRepDeliveryByDvCode(java.lang.Integer DV_CODE, java.lang.Integer AD_CMPNY);

    ArModSalesOrderDetails getArSalesOrderBySoCode(java.lang.Integer SO_CODE);

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

}
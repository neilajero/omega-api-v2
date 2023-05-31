package com.ejb.txnsync.ar;

import com.ejb.restfulapi.sync.ar.models.ArInvoiceSyncRequest;
import com.ejb.restfulapi.sync.ar.models.ArInvoiceSyncResponse;
import jakarta.ejb.Local;

@Local
public interface ArInvoiceSyncController {

    int setArInvoiceAllNewAndVoid(String[] invoices, Integer branchCode, Integer companyCode, String companyShortName);

    int setArSoNew(String[] newSO, Integer branchCode, Integer companyCode, String companyShortName);

    ArInvoiceSyncResponse setArInvoiceAllNewAndVoid(ArInvoiceSyncRequest request);

    ArInvoiceSyncResponse setArSoNew(ArInvoiceSyncRequest request);

}
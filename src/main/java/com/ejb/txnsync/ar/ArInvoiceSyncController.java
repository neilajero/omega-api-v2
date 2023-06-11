package com.ejb.txnsync.ar;

import com.ejb.restfulapi.sync.ar.models.ArInvoiceSyncRequest;
import com.ejb.restfulapi.sync.ar.models.ArInvoiceSyncResponse;
import jakarta.ejb.Local;

@Local
public interface ArInvoiceSyncController {

    ArInvoiceSyncResponse setArInvoiceAllNewAndVoid(ArInvoiceSyncRequest request);

    ArInvoiceSyncResponse setArSoNew(ArInvoiceSyncRequest request);

}
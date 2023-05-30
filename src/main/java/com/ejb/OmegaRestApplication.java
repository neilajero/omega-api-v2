package com.ejb;

import com.ejb.restfulapi.ad.*;
import com.ejb.restfulapi.ap.ApSupplierRestApi;
import com.ejb.restfulapi.ar.ArCustomerRestApi;
import com.ejb.restfulapi.ar.ArInvoiceRestApi;
import com.ejb.restfulapi.ar.ArReceiptRestApi;
import com.ejb.restfulapi.ar.ArStandardMemoLineRestApi;
import com.ejb.restfulapi.filters.CORSFilter;
import com.ejb.restfulapi.filters.OmegaJWTTokenFilter;
import com.ejb.restfulapi.inv.InvItemRestApi;
import com.ejb.restfulapi.reports.ar.ArRepStatementRestApi;
import com.ejb.restfulapi.sync.ad.AdBankAccountSyncApi;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class OmegaRestApplication extends Application {

    public OmegaRestApplication() {

    }

    @Override
    public Set<Class<?>> getClasses() {

        Set<Class<?>> classes = new HashSet<>();

        // Filters
        classes.add(CORSFilter.class);
        classes.add(OmegaJWTTokenFilter.class);

        // Admin
        classes.add(AdAmountLimitRestApi.class);
        classes.add(AdBranchRestApi.class);
        classes.add(AdDocumentSequenceAssignmentRestApi.class);
        classes.add(AdAuthenticationRestApi.class);

        // Accounts Payables
        classes.add(ApSupplierRestApi.class);

        // Accounts Receivables
        classes.add(ArInvoiceRestApi.class);
        classes.add(ArReceiptRestApi.class);
        classes.add(ArCustomerRestApi.class);
        classes.add(ArStandardMemoLineRestApi.class);

        // Inventory
        classes.add(InvItemRestApi.class);

        // Reports
        classes.add(ArRepStatementRestApi.class);

        // Synchronizer
        classes.add(AdBankAccountSyncApi.class);

        return classes;
    }

}
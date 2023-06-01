package com.ejb.txnsync.gl;

import com.ejb.restfulapi.sync.gl.models.GlCurrencySyncRequest;
import com.ejb.restfulapi.sync.gl.models.GlCurrencySyncResponse;
import jakarta.ejb.Local;

@Local
public interface GlCurrencySyncController {

    String[] getGlFcAll(Integer companyCode, String companyShortName);

    String[] getGlCurrentFcRates(Integer companyCode, String companyShortName);

    GlCurrencySyncResponse getGlFcAll(GlCurrencySyncRequest request);

    GlCurrencySyncResponse getGlCurrentFcRates(GlCurrencySyncRequest request);
}
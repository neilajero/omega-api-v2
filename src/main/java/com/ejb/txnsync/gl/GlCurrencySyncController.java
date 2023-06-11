package com.ejb.txnsync.gl;

import com.ejb.restfulapi.sync.gl.models.GlCurrencySyncRequest;
import com.ejb.restfulapi.sync.gl.models.GlCurrencySyncResponse;
import jakarta.ejb.Local;

@Local
public interface GlCurrencySyncController {

    GlCurrencySyncResponse getGlFcAll(GlCurrencySyncRequest request);

    GlCurrencySyncResponse getGlCurrentFcRates(GlCurrencySyncRequest request);
}
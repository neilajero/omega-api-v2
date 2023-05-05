package com.ejb.txnapi.ad;

import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.restfulapi.ad.models.UserDetailsRequest;

import jakarta.ejb.Local;

@Local
public interface AdAuthenticationApiController {

    OfsApiResponse validateUserDetails(UserDetailsRequest request);
}
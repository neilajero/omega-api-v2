package com.ejb.txnapi.ar;

import com.ejb.exception.ar.ArCCCoaGlReceivableAccountNotFoundException;
import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.restfulapi.ar.models.CustomerListResponse;
import com.ejb.restfulapi.ar.models.CustomerRequest;
import com.util.mod.ar.ArModCustomerDetails;

import jakarta.ejb.Local;

@Local
public interface ArCustomerEntryApiController {

	/**
	 * This method saves customer information into Omega ERP.
	 */
    Integer saveArCstEntry(ArModCustomerDetails modCustomerDetails, String customerType, String dealPrice, String paymentTerm, String customerClass,
						   String bankAccount, Integer branchCode, Integer companyCode)
			throws ArCCCoaGlReceivableAccountNotFoundException;
	
	/**
	 * This method creates a customer object.
	 */
    OfsApiResponse createCustomer(CustomerRequest customerRequest);

	CustomerListResponse getAllCustomersPerCompany(String companyshortname);

}
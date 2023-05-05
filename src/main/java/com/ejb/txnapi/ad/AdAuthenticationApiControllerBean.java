package com.ejb.txnapi.ad;

import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.restfulapi.OfsApiResponse;
import com.ejb.restfulapi.ad.models.UserDetailsRequest;
import com.util.Debug;
import com.util.EJBCommonAPIErrCodes;
import com.util.EJBContextClass;

import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

@Stateless(name = "AdAuthenticationApiControllerEJB")
public class AdAuthenticationApiControllerBean extends EJBContextClass implements AdAuthenticationApiController {

    @EJB
    private LocalAdUserHome adUserHome;

    @EJB
    private ILocalAdCompanyHome adCompanyHome;

    @Override
    public OfsApiResponse validateUserDetails(UserDetailsRequest request) {

        Debug.print("AdAuthenticationApiControllerBean validateUserDetails");
        OfsApiResponse response = new OfsApiResponse();
        Boolean isValidateUser;
        try {
            LocalAdCompany company = adCompanyHome.findByCmpShortName(request.getCompany());
            isValidateUser = adUserHome.validateUserDetails(request.getUsername(), request.getPassword(),
                    company.getCmpCode(), request.getCompany());
            if (!isValidateUser) {
                response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
                response.setStatus(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            }
            response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setStatus(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
        }
        catch (FinderException ex) {
            response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

}
package com.ejb.txnapi.ad;

import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdDocumentSequenceAssignmentHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdDocumentSequenceAssignment;
import com.ejb.restfulapi.OfsApiResponse;
import com.util.Debug;
import com.util.EJBCommonAPIErrCodes;
import com.util.EJBContextClass;

import jakarta.ejb.EJB;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

@Stateless(name = "AdDocumentSequenceAssignmentApiControllerEJB")
public class AdDocumentSequenceAssignmentApiControllerBean extends EJBContextClass
        implements AdDocumentSequenceAssignmentApiController {

    @EJB
    private LocalAdDocumentSequenceAssignmentHome adDocumentSequenceAssignmentHome;

    @EJB
    private ILocalAdCompanyHome adCompanyHome;

    @Override
    public OfsApiResponse findByDcName(String documentName, Integer companyCode) throws FinderException {

        Debug.print("AdDocumentSequenceAssignmentApiControllerBean findByDcName");
        OfsApiResponse response = new OfsApiResponse();
        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment;
        String documentNumber;
        try {
            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDsName(documentName, companyCode);
            documentNumber = adDocumentSequenceAssignment.getDsaNextSequence();
            response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setStatus(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setDocumentNumber(documentNumber);
        }
        catch (FinderException ex) {
            response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        //adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
        return response;
    }

    @Override
    public OfsApiResponse findByDcName(String documentName, String companyShortName) throws FinderException {

        Debug.print("AdDocumentSequenceAssignmentApiControllerBean findByDcName");
        OfsApiResponse response = new OfsApiResponse();
        LocalAdDocumentSequenceAssignment adDocumentSequenceAssignment;
        String documentNumber;
        try {
            LocalAdCompany adCompany = adCompanyHome.findByCmpShortName(companyShortName);
            adDocumentSequenceAssignment = adDocumentSequenceAssignmentHome.findByDsName(documentName, adCompany.getCmpCode(), adCompany.getCmpShortName());
            documentNumber = adDocumentSequenceAssignment.getDsaNextSequence();
            response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setStatus(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setDocumentNumber(documentNumber);
        }
        catch (FinderException ex) {
            response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        //adDocumentSequenceAssignment.setDsaNextSequence(EJBCommon.incrementStringNumber(adDocumentSequenceAssignment.getDsaNextSequence()));
        return response;
    }

}
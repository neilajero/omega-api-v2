package com.ejb.txnsync.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.ad.LocalAdBranchStandardMemoLineHome;
import com.ejb.dao.ar.LocalArStandardMemoLineHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchStandardMemoLine;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ar.LocalArStandardMemoLine;
import com.ejb.restfulapi.sync.ar.models.ArStandardMemoLineSyncRequest;
import com.ejb.restfulapi.sync.ar.models.ArStandardMemoLineSyncResponse;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBCommonAPIErrCodes;
import com.util.EJBContextClass;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import java.util.Collection;
import java.util.Iterator;

@Stateless(name = "ArStandardMemoLineSyncControllerBeanEJB")
public class ArStandardMemoLineSyncControllerBean extends EJBContextClass implements ArStandardMemoLineSyncController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalArStandardMemoLineHome arStandardMemoLineHome;
    @EJB
    private LocalAdBranchStandardMemoLineHome adBranchStandardMemoLineHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdBranchHome adBranchHome;

    @Override
    public ArStandardMemoLineSyncResponse getArStandardMemoLineAllNewLength(ArStandardMemoLineSyncRequest request) {

        Debug.print("ArStandardMemoLineSyncControllerBean getArStandardMemoLineAllNewLength");

        ArStandardMemoLineSyncResponse response = new ArStandardMemoLineSyncResponse();

        LocalAdCompany adCompany;
        LocalAdBranch adBranch;
        Integer companyCode = null;
        Integer branchCode = null;
        String companyShortName = null;
        char[] downloadNewStatus = {'N', 'N', 'N'};

        try {
            // Company Code
            try {
                if (request.getCompanyCode() == null) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return response;
                }
                adCompany = adCompanyHome.findByCmpShortName(request.getCompanyCode());
                if (adCompany != null) {
                    companyCode = adCompany.getCmpCode();
                    companyShortName = adCompany.getCmpShortName();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return response;
            }

            // Branch Code
            try {
                if (request.getBranchCode() == null || request.getBranchCode().equals("")) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return response;
                }
                assert adCompany != null;
                adBranch = adBranchHome.findByBrBranchCode(request.getBranchCode(), adCompany.getCmpCode(), companyShortName);
                if (adBranch != null) {
                    branchCode = adBranch.getBrCode();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                response.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, request.getBranchCode()));
                return response;
            }

            int count = this.getStandardMemoLineStatusCount(branchCode, companyCode, companyShortName, downloadNewStatus);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setCount(count);
            response.setStatus("Get new standard memo lines count.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public ArStandardMemoLineSyncResponse getArStandardMemoLineHomeAllUpdatedLength(ArStandardMemoLineSyncRequest request) {

        Debug.print("ArStandardMemoLineSyncControllerBean getArStandardMemoLineHomeAllUpdatedLength");

        ArStandardMemoLineSyncResponse response = new ArStandardMemoLineSyncResponse();

        LocalAdCompany adCompany;
        LocalAdBranch adBranch;
        Integer companyCode = null;
        Integer branchCode = null;
        String companyShortName = null;
        char[] downloadNewStatus = {'U', 'U', 'X'};

        try {
            // Company Code
            try {
                if (request.getCompanyCode() == null) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return response;
                }
                adCompany = adCompanyHome.findByCmpShortName(request.getCompanyCode());
                if (adCompany != null) {
                    companyCode = adCompany.getCmpCode();
                    companyShortName = adCompany.getCmpShortName();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return response;
            }

            // Branch Code
            try {
                if (request.getBranchCode() == null || request.getBranchCode().equals("")) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return response;
                }
                assert adCompany != null;
                adBranch = adBranchHome.findByBrBranchCode(request.getBranchCode(), adCompany.getCmpCode(), companyShortName);
                if (adBranch != null) {
                    branchCode = adBranch.getBrCode();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                response.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, request.getBranchCode()));
                return response;
            }

            int count = this.getStandardMemoLineStatusCount(branchCode, companyCode, companyShortName, downloadNewStatus);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setCount(count);
            response.setStatus("Get updated standard memo lines count.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public ArStandardMemoLineSyncResponse getArStandardMemoLineAllNewAndUpdated(ArStandardMemoLineSyncRequest request) {

        Debug.print("ArStandardMemoLineSyncControllerBean getArStandardMemoLineHomeAllUpdatedLength");

        ArStandardMemoLineSyncResponse response = new ArStandardMemoLineSyncResponse();

        LocalAdCompany adCompany;
        LocalAdBranch adBranch;
        Integer companyCode = null;
        Integer branchCode = null;
        String companyShortName = null;

        try {
            // Company Code
            try {
                if (request.getCompanyCode() == null) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return response;
                }
                adCompany = adCompanyHome.findByCmpShortName(request.getCompanyCode());
                if (adCompany != null) {
                    companyCode = adCompany.getCmpCode();
                    companyShortName = adCompany.getCmpShortName();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return response;
            }

            // Branch Code
            try {
                if (request.getBranchCode() == null || request.getBranchCode().equals("")) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return response;
                }
                assert adCompany != null;
                adBranch = adBranchHome.findByBrBranchCode(request.getBranchCode(), adCompany.getCmpCode(), companyShortName);
                if (adBranch != null) {
                    branchCode = adBranch.getBrCode();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                response.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, request.getBranchCode()));
                return response;
            }

            String[] result = this.getArStandardMemoLineAllNewAndUpdated(branchCode, companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setResults(result);
            response.setStatus("Get new and updated standard memo lines count.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    @Override
    public ArStandardMemoLineSyncResponse setArStandardMemoLineAllNewAndUpdatedSuccessConfirmation(ArStandardMemoLineSyncRequest request) {

        Debug.print("ArStandardMemoLineSyncControllerBean setArStandardMemoLineAllNewAndUpdatedSuccessConfirmation");

        ArStandardMemoLineSyncResponse response = new ArStandardMemoLineSyncResponse();

        LocalAdCompany adCompany;
        LocalAdBranch adBranch;
        Integer companyCode = null;
        Integer branchCode = null;
        String companyShortName = null;

        try {
            // Company Code
            try {
                if (request.getCompanyCode() == null) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_041);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_041_MSG);
                    return response;
                }
                adCompany = adCompanyHome.findByCmpShortName(request.getCompanyCode());
                if (adCompany != null) {
                    companyCode = adCompany.getCmpCode();
                    companyShortName = adCompany.getCmpShortName();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_001);
                response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_001_MSG);
                return response;
            }

            // Branch Code
            try {
                if (request.getBranchCode() == null || request.getBranchCode().equals("")) {
                    response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_047);
                    response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_047_MSG);
                    return response;
                }
                assert adCompany != null;
                adBranch = adBranchHome.findByBrBranchCode(request.getBranchCode(), adCompany.getCmpCode(), companyShortName);
                if (adBranch != null) {
                    branchCode = adBranch.getBrCode();
                }
            }
            catch (FinderException ex) {
                response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_065);
                response.setMessage(String.format(EJBCommonAPIErrCodes.OAPI_ERR_065_MSG, request.getBranchCode()));
                return response;
            }

            this.setArStandardMemoLineAllNewAndUpdatedSuccessConfirmation(branchCode, companyCode, companyShortName);

            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
            response.setStatus("Set all standard memo line data successfully.");
        }
        catch (Exception ex) {
            response.setStatusCode(EJBCommonAPIErrCodes.OAPI_ERR_007);
            response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_007_MSG);
            return response;
        }
        return response;
    }

    private int getStandardMemoLineStatusCount(Integer branchCode, Integer companyCode, String companyShortName, char[] downloadStatus) {

        Debug.print("ArStandardMemoLineSyncControllerBean getStandardMemoLineStatusCount");

        try {
            Collection arStandardMemoLines = arStandardMemoLineHome
                    .findSmlBySmlNewAndUpdated(branchCode, companyCode,
                            downloadStatus[0], downloadStatus[1], downloadStatus[2], companyShortName);
            return arStandardMemoLines.size();
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private String[] getArStandardMemoLineAllNewAndUpdated(Integer branchCode, Integer companyCode, String companyShortName) {

        Debug.print(" ArStandardMemoLineSyncControllerBean getArStandardMemoLineAllNewAndUpdated");

        char[] downloadNewStatus = {'N', 'N', 'N'};
        char[] downloadUpdatedStatus = {'U', 'U', 'X'};

        try {
            Collection arStandardMemoLines = arStandardMemoLineHome
                    .findSmlBySmlNewAndUpdated(branchCode, companyCode,
                            downloadNewStatus[0], downloadNewStatus[1], downloadNewStatus[2], companyShortName);

            Collection arUpdatedStandardMemoLines = arStandardMemoLineHome
                    .findSmlBySmlNewAndUpdated(branchCode, companyCode,
                            downloadUpdatedStatus[0], downloadUpdatedStatus[1], downloadUpdatedStatus[2], companyShortName);

            String[] results = new String[arStandardMemoLines.size() + arUpdatedStandardMemoLines.size()];

            Iterator i = arStandardMemoLines.iterator();
            int ctr = 0;
            while (i.hasNext()) {
                LocalArStandardMemoLine arStandardMemoLine = (LocalArStandardMemoLine) i.next();
                results[ctr] = standardMemoLineRowEncode(arStandardMemoLine);
                ctr++;
            }

            i = arUpdatedStandardMemoLines.iterator();
            while (i.hasNext()) {
                LocalArStandardMemoLine arStandardMemoLine = (LocalArStandardMemoLine) i.next();
                results[ctr] = standardMemoLineRowEncode(arStandardMemoLine);
                ctr++;

            }
            return results;
        }
        catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private void setArStandardMemoLineAllNewAndUpdatedSuccessConfirmation(Integer branchCode, Integer companyCode, String companyShortName) {

        Debug.print(" ArStandardMemoLineSyncControllerBean setArStandardMemoLineAllNewAndUpdatedSuccessConfirmation");

        char[] downloadNewUpdatedStatus = {'N', 'U', 'X'};

        try {
            Collection adBranchStandardMemoLines = adBranchStandardMemoLineHome
                    .findBSMLByBSMLNewAndUpdated(branchCode, companyCode,
                            downloadNewUpdatedStatus[0], downloadNewUpdatedStatus[1], downloadNewUpdatedStatus[2], companyShortName);
            for (Object adBranchStandardMemoLine : adBranchStandardMemoLines) {
                LocalAdBranchStandardMemoLine retObj = (LocalAdBranchStandardMemoLine) adBranchStandardMemoLine;
                retObj.setBsmlStandardMemoLineDownloadStatus('D');
            }
        }
        catch (Exception ex) {
            ctx.setRollbackOnly();
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private String standardMemoLineRowEncode(LocalArStandardMemoLine arStandardMemoLine) {

        char separator = EJBCommon.SEPARATOR;
        StringBuilder tempResult = new StringBuilder();
        String encodedResult;

        // Start separator
        tempResult.append(separator);

        // Primary Key
        Debug.print("Get Primary Key " + arStandardMemoLine.getSmlCode());
        tempResult.append(arStandardMemoLine.getSmlCode());
        tempResult.append(separator);

        // SML Name
        Debug.print("Get Name " + arStandardMemoLine.getSmlName());
        tempResult.append(arStandardMemoLine.getSmlName());
        tempResult.append(separator);


        // SML Description
        Debug.print("Get Description " + arStandardMemoLine.getSmlDescription());
        tempResult.append(arStandardMemoLine.getSmlDescription());
        tempResult.append(separator);


        // SML Unit Price
        Debug.print("Get Unit Price " + arStandardMemoLine.getSmlUnitPrice());
        tempResult.append(arStandardMemoLine.getSmlUnitPrice());
        tempResult.append(separator);

        // SML Tax
        Debug.print("Get Tax " + arStandardMemoLine.getSmlTax());
        tempResult.append(Double.toString(arStandardMemoLine.getSmlTax()));
        tempResult.append(separator);


        //  SML Enable
        Debug.print("Get Enable " + arStandardMemoLine.getSmlEnable());
        tempResult.append(arStandardMemoLine.getSmlEnable());
        tempResult.append(separator);


        //  SML Subject To Commission
        Debug.print("Get Commission " + arStandardMemoLine.getSmlSubjectToCommission());
        tempResult.append(arStandardMemoLine.getSmlSubjectToCommission());
        tempResult.append(separator);

        if (arStandardMemoLine.getSmlUnitOfMeasure() != null) {
            //  SML Unit of Measure
            Debug.print("Get Unit of Measure " + arStandardMemoLine.getSmlUnitOfMeasure());
            tempResult.append(arStandardMemoLine.getSmlUnitOfMeasure());
            tempResult.append(separator);
        } else {
            //  SML Unit of Measure
            Debug.print("Get Unit of Measure " + arStandardMemoLine.getSmlUnitOfMeasure());
            tempResult.append("0");
            tempResult.append(separator);
        }


        //  SML GL Chart of Account
        Debug.print("Get Chart of Account " + arStandardMemoLine.getGlChartOfAccount().getCoaAccountNumber());
        tempResult.append(arStandardMemoLine.getGlChartOfAccount().getCoaAccountNumber());
        tempResult.append(separator);

        // remove unwanted chars from encodedResult;
        encodedResult = tempResult.toString();
        encodedResult = encodedResult.replace("\"", " ");
        encodedResult = encodedResult.replace("'", " ");
        encodedResult = encodedResult.replace(";", " ");
        encodedResult = encodedResult.replace("\\", " ");
        encodedResult = encodedResult.replace("|", " ");

        return encodedResult;
    }

}
package com.ejb.txnapi.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.restfulapi.OfsApiResponse;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBCommonAPIErrCodes;
import com.util.EJBContextClass;
import com.util.mod.ad.AdModBranchDetails;

import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collection;

@Stateless(name = "AdBranchApiControllerEJB")
public class AdBranchApiControllerBean extends EJBContextClass implements AdBranchApiController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdBranchHome adBranchHome;

    @Override
    public OfsApiResponse getAdBrAll(String companyShortName) throws GlobalNoRecordFoundException {

        Debug.print("AdBranchControllerBean getAdBrAll");

        LocalAdBranch adBranch;
        Collection adBranches = null;
        ArrayList<String> list = new ArrayList<>();
        OfsApiResponse response = new OfsApiResponse();
        try {
            LocalAdCompany adCompany = adCompanyHome.findByCmpShortName(companyShortName);
            adBranches = adBranchHome.findBrAll(adCompany.getCmpCode(), adCompany.getCmpShortName());
        } catch (FinderException ex) {
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        if (adBranches.isEmpty()) {
            throw new GlobalNoRecordFoundException();
        }
        for (Object branch : adBranches) {
            adBranch = (LocalAdBranch) branch;
            list.add(adBranch.getBrName());
        }

        response.setCode(EJBCommonAPIErrCodes.OAPI_ERR_000);
        response.setMessage(EJBCommonAPIErrCodes.OAPI_ERR_000_MSG);
        response.setBranchNames(list);
        return response;
    }

}
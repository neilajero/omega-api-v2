package com.ejb.txn.ad;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.LocalAdFormFunctionHome;
import com.ejb.dao.ad.LocalAdFormFunctionResponsibilityHome;
import com.ejb.dao.ad.LocalAdResponsibilityHome;
import com.ejb.entities.ad.LocalAdFormFunction;
import com.ejb.entities.ad.LocalAdFormFunctionResponsibility;
import com.ejb.entities.ad.LocalAdResponsibility;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.ad.AdResponsibilityDetails;
import com.util.mod.ad.AdModFormFunctionResponsibilityDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

@Stateless(name = "AdFormFunctionResponsibilityControllerEJB")
public class AdFormFunctionResponsibilityControllerBean extends EJBContextClass implements AdFormFunctionResponsibilityController {

    @EJB
    public PersistenceBeanClass em;

    @EJB
    LocalAdFormFunctionResponsibilityHome adFormFunctionResponsibilityHome;

    @EJB
    LocalAdResponsibilityHome adResponsibilityHome;

    @EJB
    LocalAdFormFunctionHome adFormFunctionHome;

    public AdModFormFunctionResponsibilityDetails getAdFrByRsCodeAndFfCode(Integer responsibilityCode, Integer formFunctionCode, Integer companyCode) {

        Debug.print("AdFormFunctionResponsibilityControllerBean getAdFrByRsCodeAndFfCode");

        LocalAdFormFunctionResponsibility adFormFunctionResponsibility;
        try {
            adFormFunctionResponsibility = adFormFunctionResponsibilityHome.findByRsCodeAndFfCode(responsibilityCode, formFunctionCode, companyCode);
            AdModFormFunctionResponsibilityDetails mdetails = new AdModFormFunctionResponsibilityDetails();
            mdetails.setFrCode(adFormFunctionResponsibility.getFrCode());
            mdetails.setFrParameter(adFormFunctionResponsibility.getFrParameter());
            mdetails.setFrFfCode(adFormFunctionResponsibility.getAdFormFunction().getFfCode());
            mdetails.setFrFfFormFunctionName(adFormFunctionResponsibility.getAdFormFunction().getFfName());
            return mdetails;
        } catch (FinderException ex) {
            return null;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdFrByRsCode(Integer responsibilityCode, Integer companyCode) {

        Debug.print("AdFormFunctionResponsibilityControllerBean getAdFrByRsCode");

        ArrayList list = new ArrayList();
        try {
            Collection adFormFunctionResponsibilities = adFormFunctionResponsibilityHome.findByRsCode(responsibilityCode, companyCode);
            for (Object formFunctionResponsibility : adFormFunctionResponsibilities) {
                LocalAdFormFunctionResponsibility adFormFunctionResponsibility = (LocalAdFormFunctionResponsibility) formFunctionResponsibility;

                AdModFormFunctionResponsibilityDetails mdetails = new AdModFormFunctionResponsibilityDetails();

                mdetails.setFrCode(adFormFunctionResponsibility.getFrCode());
                mdetails.setFrParameter(adFormFunctionResponsibility.getFrParameter());
                mdetails.setFrFfCode(adFormFunctionResponsibility.getAdFormFunction().getFfCode());
                mdetails.setFrFfFormFunctionName(adFormFunctionResponsibility.getAdFormFunction().getFfName());
                mdetails.setFrRsCode(adFormFunctionResponsibility.getAdResponsibility().getRsCode());

                list.add(mdetails);
            }
            return list;
        } catch (FinderException ex) {
            return null;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public AdResponsibilityDetails getAdRsByRsCode(Integer responsibilityCode, Integer companyCode) {

        Debug.print("AdFormFunctionResponsibilityControllerBean getAdRsByRsCode");

        try {
            LocalAdResponsibility adResponsibility = adResponsibilityHome.findByPrimaryKey(responsibilityCode);
            AdResponsibilityDetails details = new AdResponsibilityDetails();
            details.setRsCode(adResponsibility.getRsCode());
            details.setRsName(adResponsibility.getRsName());
            details.setRsDescription(adResponsibility.getRsDescription());
            return details;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public void saveAdFrEntry(ArrayList list, Integer responsibilityCode, Integer companyCode) {

        Debug.print("AdFormFunctionResponsibilityControllerBean saveAdFrEntry");

        try {
            LocalAdFormFunctionResponsibility adFormFunctionResponsibility;
            // delete all existing form function responsibility
            Collection formFunctions = adFormFunctionResponsibilityHome.findByRsCode(responsibilityCode, companyCode);
            Iterator i = formFunctions.iterator();
            while (i.hasNext()) {
                adFormFunctionResponsibility = (LocalAdFormFunctionResponsibility) i.next();
                i.remove();
                em.remove(adFormFunctionResponsibility);
            }
            // create new form function responsibility
            i = list.iterator();
            while (i.hasNext()) {
                AdModFormFunctionResponsibilityDetails mdetails = (AdModFormFunctionResponsibilityDetails) i.next();
                adFormFunctionResponsibility = adFormFunctionResponsibilityHome.create(mdetails.getFrParameter(), companyCode);
                // add form function
                LocalAdFormFunction adFormFunction = adFormFunctionHome.findByPrimaryKey(mdetails.getFrFfCode());
                adFormFunction.addAdFormFunctionResponsibility(adFormFunctionResponsibility);
                // add responsibility
                LocalAdResponsibility adResposibility = adResponsibilityHome.findByPrimaryKey(responsibilityCode);
                adResposibility.addAdFormFunctionResponsibility(adFormFunctionResponsibility);
            }
        } catch (Exception ex) {
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("AdFormFunctionResponsibilityControllerBean ejbCreate");
    }

}
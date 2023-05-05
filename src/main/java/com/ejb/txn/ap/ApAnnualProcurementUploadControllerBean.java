/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class ApAnnualProcurementUploadControllerBean
 * @created Jun 14, 2022 18:33
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.ap;

import java.util.*;

import jakarta.ejb.*;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.inv.*;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.entities.ad.LocalAdUser;
import com.ejb.exception.global.GlobalRecordInvalidException;
import com.ejb.entities.inv.*;
import com.util.*;
import com.util.mod.inv.InvModTransactionalBudgetDetails;

@Stateless(name = "ApAnnualProcurementUploadControllerEJB")
public class ApAnnualProcurementUploadControllerBean extends EJBContextClass implements ApAnnualProcurementUploadController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalInvTransactionalBudgetHome invTransactionalBudgetHome;
    @EJB
    private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalInvItemHome invItemHome;
    @EJB
    private LocalAdUserHome adUserHome;

    public void uploadAnnualProcurement(ArrayList list, String userDepartment, Integer branchCode, Integer companyCode) throws GlobalRecordInvalidException {
        Debug.print("ApAnnualProcurementUploadControllerBean uploadAnnualProcurement");
        LocalInvTransactionalBudget invTransactionalBudget;
        try {
            Iterator itr = list.iterator();
            int ctr = 0;
            while (itr.hasNext()) {
                ctr++;
                InvModTransactionalBudgetDetails mdetails = (InvModTransactionalBudgetDetails) itr.next();
                // create transactional budget
                LocalInvItem invItem;
                try {
                    invItem = invItemHome.findByIiName(mdetails.getTbItemName(), companyCode);
                } catch (FinderException ex) {
                    throw new GlobalRecordInvalidException("" + ctr);
                }
                LocalAdLookUpValue adLookUpValue;
                try {
                    adLookUpValue = adLookUpValueHome.findByLvName(userDepartment, companyCode);
                } catch (FinderException ex) {
                    throw new GlobalRecordInvalidException("" + ctr);
                }
                LocalInvUnitOfMeasure invUOM;
                try {
                    invUOM = invUnitOfMeasureHome.findByUomName(mdetails.getTbUnit(), companyCode);
                } catch (FinderException ex) {
                    throw new GlobalRecordInvalidException("" + ctr);
                }
                int adLvDepartmentCode = adLookUpValue.getLvCode();
                try {
                    // search for existing transactional budget
                    invTransactionalBudget = invTransactionalBudgetHome.findByTbItemNameAndDepartment(mdetails.getTbItemName(), adLvDepartmentCode, companyCode);
                    // update for same department
                    invTransactionalBudget.setTbQuantityJan(mdetails.getTbQtyJan() + invTransactionalBudget.getTbQuantityJan());
                    invTransactionalBudget.setTbQuantityFeb(mdetails.getTbQtyFeb() + invTransactionalBudget.getTbQuantityFeb());
                    invTransactionalBudget.setTbQuantityMrch(mdetails.getTbQtyMrch() + invTransactionalBudget.getTbQuantityMrch());
                    invTransactionalBudget.setTbQuantityAprl(mdetails.getTbQtyAprl() + invTransactionalBudget.getTbQuantityAprl());
                    invTransactionalBudget.setTbQuantityMay(mdetails.getTbQtyMay() + invTransactionalBudget.getTbQuantityMay());
                    invTransactionalBudget.setTbQuantityJun(mdetails.getTbQtyJun() + invTransactionalBudget.getTbQuantityJun());
                    invTransactionalBudget.setTbQuantityJul(mdetails.getTbQtyJul() + invTransactionalBudget.getTbQuantityJul());
                    invTransactionalBudget.setTbQuantityAug(mdetails.getTbQtyAug() + invTransactionalBudget.getTbQuantityAug());
                    invTransactionalBudget.setTbQuantitySep(mdetails.getTbQtySep() + invTransactionalBudget.getTbQuantitySep());
                    invTransactionalBudget.setTbQuantityOct(mdetails.getTbQtyOct() + invTransactionalBudget.getTbQuantityOct());
                    invTransactionalBudget.setTbQuantityNov(mdetails.getTbQtyNov() + invTransactionalBudget.getTbQuantityNov());
                    invTransactionalBudget.setTbQuantityDec(mdetails.getTbQtyDec() + invTransactionalBudget.getTbQuantityDec());
                    invTransactionalBudget.setTbYear(mdetails.getTbYear());
                } catch (FinderException ex) {
                    // create if it does not exist
                    invTransactionalBudget = invTransactionalBudgetHome.create(mdetails.getTbItemName(), invItem.getIiDescription(), mdetails.getTbQtyJan(), mdetails.getTbQtyFeb(), mdetails.getTbQtyMrch(), mdetails.getTbQtyAprl(), mdetails.getTbQtyMay(), mdetails.getTbQtyJun(), mdetails.getTbQtyJul(), mdetails.getTbQtyAug(), mdetails.getTbQtySep(), mdetails.getTbQtyOct(), mdetails.getTbQtyNov(), mdetails.getTbQtyDec(), mdetails.getTbUnitCost(), mdetails.getTbTotalCost(), mdetails.getTbYear(), companyCode);

                    invTransactionalBudget.setInvUnitOfMeasure(invUOM);
                    invTransactionalBudget.setInvItem(invItem);
                    invTransactionalBudget.setTbAdLookupValue(adLookUpValue.getLvCode());
                }
            }
        } catch (GlobalRecordInvalidException ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw ex;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public String getAdUsrDepartment(String username, Integer companyCode) {
        Debug.print("ApAnnualProcurementUploadControllerBean getAdUsrAll");
        LocalAdUser adUser;
        try {
            adUser = adUserHome.findByUsrName(username, companyCode);
            return adUser.getUsrDept();
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ApVoucherImportControllerBean ejbCreate");
    }
}
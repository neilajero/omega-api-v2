/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ApFindRecurringVoucherControllerBean
 * @created February 20, 2004, 5:43 PM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.ap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.entities.ap.LocalApRecurringVoucher;
import com.ejb.dao.ap.LocalApRecurringVoucherHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.mod.ap.ApModRecurringVoucherDetails;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ApFindRecurringVoucherControllerEJB")
public class ApFindRecurringVoucherControllerBean extends EJBContextClass implements ApFindRecurringVoucherController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private LocalApRecurringVoucherHome apRecurringJournalHome;

    public ArrayList getApRvByCriteria(HashMap criteria, String ORDER_BY, Integer OFFSET, Integer LIMIT, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApFindRecurringVoucherControllerBean getApRvByCriteria");

        ArrayList rvList = new ArrayList();

        StringBuilder jbossQl = new StringBuilder();
        jbossQl.append("SELECT OBJECT(rv) FROM ApRecurringVoucher rv ");

        boolean firstArgument = true;
        short ctr = 0;
        int criteriaSize = criteria.size();
        Object[] obj;

        // Allocate the size of the object parameter

        if (criteria.containsKey("name")) {

            criteriaSize--;
        }

        obj = new Object[criteriaSize];

        if (criteria.containsKey("name")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("rv.rvName LIKE '%").append(criteria.get("name")).append("%' ");
        }

        if (criteria.containsKey("supplierCode")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("rv.apSupplier.splSupplierCode=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("supplierCode");
            ctr++;
        }

        if (criteria.containsKey("nextRunDateFrom")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("rv.rvNextRunDate>=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("nextRunDateFrom");
            ctr++;
        }

        if (criteria.containsKey("nextRunDateTo")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("rv.rvNextRunDate<=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("nextRunDateTo");
            ctr++;
        }

        if (!firstArgument) {
            jbossQl.append("AND ");
        } else {
            firstArgument = false;
            jbossQl.append("WHERE ");
        }
        jbossQl.append("rv.rvAdBranch=").append(AD_BRNCH).append(" ");

        if (!firstArgument) {
            jbossQl.append("AND ");
        } else {
            firstArgument = false;
            jbossQl.append("WHERE ");
        }
        jbossQl.append("rv.rvAdCompany=").append(AD_CMPNY).append(" ");

        String orderBy = null;

        switch (ORDER_BY) {
            case "NAME":

                orderBy = "rv.rvName";

                break;
            case "NEXT RUN DATE":

                orderBy = "rv.rvNextRunDate";

                break;
            case "SUPPLIER CODE":

                orderBy = "rv.apSupplier.splSupplierCode";
                break;
        }

        if (orderBy != null) {

            jbossQl.append("ORDER BY ").append(orderBy);
        }

        Debug.print("QL + " + jbossQl);

        Collection apRecurringJournals = null;

        try {

            apRecurringJournals = apRecurringJournalHome.getRvByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (apRecurringJournals.isEmpty()) throw new GlobalNoRecordFoundException();

        for (Object apRecurringJournal : apRecurringJournals) {

            LocalApRecurringVoucher apRecurringVoucher = (LocalApRecurringVoucher) apRecurringJournal;

            ApModRecurringVoucherDetails mdetails = new ApModRecurringVoucherDetails();
            mdetails.setRvCode(apRecurringVoucher.getRvCode());
            mdetails.setRvName(apRecurringVoucher.getRvName());
            mdetails.setRvDescription(apRecurringVoucher.getRvDescription());
            mdetails.setRvSchedule(apRecurringVoucher.getRvSchedule());
            mdetails.setRvSplSupplierCode(apRecurringVoucher.getApSupplier().getSplSupplierCode());
            mdetails.setRvNextRunDate(apRecurringVoucher.getRvNextRunDate());
            mdetails.setRvLastRunDate(apRecurringVoucher.getRvLastRunDate());

            rvList.add(mdetails);
        }

        return rvList;
    }

    public Integer getApRvSizeByCriteria(HashMap criteria, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApFindRecurringVoucherControllerBean getApRvSizeByCriteria");

        StringBuilder jbossQl = new StringBuilder();
        jbossQl.append("SELECT OBJECT(rv) FROM ApRecurringVoucher rv ");

        boolean firstArgument = true;
        short ctr = 0;
        int criteriaSize = criteria.size();
        Object[] obj;

        // Allocate the size of the object parameter

        if (criteria.containsKey("name")) {

            criteriaSize--;
        }

        obj = new Object[criteriaSize];

        if (criteria.containsKey("name")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("rv.rvName LIKE '%").append(criteria.get("name")).append("%' ");
        }

        if (criteria.containsKey("supplierCode")) {

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("rv.apSupplier.splSupplierCode=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("supplierCode");
            ctr++;
        }

        if (criteria.containsKey("nextRunDateFrom")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("rv.rvNextRunDate>=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("nextRunDateFrom");
            ctr++;
        }

        if (criteria.containsKey("nextRunDateTo")) {

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("rv.rvNextRunDate<=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("nextRunDateTo");
            ctr++;
        }

        if (!firstArgument) {
            jbossQl.append("AND ");
        } else {
            firstArgument = false;
            jbossQl.append("WHERE ");
        }
        jbossQl.append("rv.rvAdBranch=").append(AD_BRNCH).append(" ");

        if (!firstArgument) {
            jbossQl.append("AND ");
        } else {
            firstArgument = false;
            jbossQl.append("WHERE ");
        }
        jbossQl.append("rv.rvAdCompany=").append(AD_CMPNY).append(" ");

        Collection apRecurringJournals = null;

        try {

            apRecurringJournals = apRecurringJournalHome.getRvByCriteria(jbossQl.toString(), obj, 0, 0);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (apRecurringJournals.isEmpty()) throw new GlobalNoRecordFoundException();

        return apRecurringJournals.size();
    }

    public byte getAdPrfApUseSupplierPulldown(Integer AD_CMPNY) {

        Debug.print("ApFindRecurringVoucherControllerBean getAdPrfApUseSupplierPulldown");

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            return adPreference.getPrfApUseSupplierPulldown();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApFindRecurringVoucherControllerBean ejbCreate");
    }

    // private methods

}
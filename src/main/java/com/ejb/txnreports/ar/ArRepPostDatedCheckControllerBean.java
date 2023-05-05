/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArRepPostDatedCheckControllerBean
 * @created July 1, 2005, 02:40 PM
 * @author Arnel Masikip
 */
package com.ejb.txnreports.ar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ar.LocalArPdc;
import com.ejb.dao.ar.LocalArPdcHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ar.ArRepPostDatedCheckDetails;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ArRepPostDatedCheckControllerEJB")
public class ArRepPostDatedCheckControllerBean extends EJBContextClass implements ArRepPostDatedCheckController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalArPdcHome arPdcHome;

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepPostDatedCheckControllerBean getAdBrResAll");

        LocalAdBranchResponsibility adBranchResponsibility = null;
        LocalAdBranch adBranch = null;

        Collection adBranchResponsibilities = null;

        ArrayList list = new ArrayList();

        try {

            adBranchResponsibilities = adBranchResponsibilityHome.findByAdResponsibility(RS_CODE, AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (adBranchResponsibilities.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }

        try {

            for (Object branchResponsibility : adBranchResponsibilities) {

                adBranchResponsibility = (LocalAdBranchResponsibility) branchResponsibility;

                adBranch = adBranchHome.findByPrimaryKey(adBranchResponsibility.getAdBranch().getBrCode());

                AdBranchDetails details = new AdBranchDetails();
                details.setBrCode(adBranch.getBrCode());
                details.setBrBranchCode(adBranch.getBrBranchCode());
                details.setBrName(adBranch.getBrName());
                details.setBrHeadQuarter(adBranch.getBrHeadQuarter());

                list.add(details);
            }

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    public ArrayList executeArRepPostDatedCheckList(HashMap criteria, ArrayList branchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepPostDatedCheckControllerBean executeArRepPostDatedCheckList");

        ArrayList list = new ArrayList();

        try {

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(pdc) FROM ArPdc pdc WHERE (");

            if (branchList.isEmpty()) throw new GlobalNoRecordFoundException();

            Iterator brIter = branchList.iterator();

            AdBranchDetails brDetails = (AdBranchDetails) brIter.next();
            jbossQl.append("pdc.pdcAdBranch=").append(brDetails.getBrCode());

            while (brIter.hasNext()) {

                brDetails = (AdBranchDetails) brIter.next();

                jbossQl.append(" OR pdc.pdcAdBranch=").append(brDetails.getBrCode());
            }

            jbossQl.append(") ");
            firstArgument = false;

            Object[] obj;

            // Allocate the size of the object parameter

            obj = new Object[criteriaSize];

            if (criteria.containsKey("maturityDateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("pdc.pdcMaturityDate>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("maturityDateFrom");
                ctr++;
            }

            if (criteria.containsKey("maturityDateTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("pdc.pdcMaturityDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("maturityDateTo");
                ctr++;
            }

            if (criteria.containsKey("status")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pdc.pdcStatus=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("status");
                ctr++;
            }

            if (criteria.containsKey("isCancelled")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pdc.pdcCancelled=?").append(ctr + 1).append(" ");

                obj[ctr] = criteria.get("isCancelled");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("pdc.pdcAdCompany = ").append(AD_CMPNY).append(" ORDER BY pdc.arCustomer.cstCode");

            Collection arPostDatedChecks = arPdcHome.getPdcByCriteria(jbossQl.toString(), obj, 0, 0);

            if (arPostDatedChecks.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object postDatedCheck : arPostDatedChecks) {

                LocalArPdc arPostDatedCheck = (LocalArPdc) postDatedCheck;

                ArRepPostDatedCheckDetails details = new ArRepPostDatedCheckDetails();

                details.setPdcCustomerCode(arPostDatedCheck.getArCustomer().getCstCustomerCode());
                details.setPdcCustomerName(arPostDatedCheck.getArCustomer().getCstName());
                details.setPdcDescription(arPostDatedCheck.getPdcDescription());
                details.setPdcDateReceived(arPostDatedCheck.getPdcDateReceived());
                details.setPdcMaturityDate(arPostDatedCheck.getPdcMaturityDate());
                details.setPdcCheckNumber(arPostDatedCheck.getPdcCheckNumber());
                details.setPdcAmount(arPostDatedCheck.getPdcAmount());
                list.add(details);
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("ArRepAgingControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ArRepAgingControllerBean ejbCreate");
    }
}
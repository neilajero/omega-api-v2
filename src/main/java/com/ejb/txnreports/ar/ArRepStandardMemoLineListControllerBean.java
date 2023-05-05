/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArRepStandardMemoLineListControllerBean
 * @created March 02, 2005, 04:06 PM
 * @author Neil Andrew M. Ajero
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
import com.ejb.entities.ar.LocalArStandardMemoLine;
import com.ejb.dao.ar.LocalArStandardMemoLineHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ar.ArRepStandardMemoLineListDetails;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ArRepStandardMemoLineListControllerEJB")
public class ArRepStandardMemoLineListControllerBean extends EJBContextClass implements ArRepStandardMemoLineListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalArStandardMemoLineHome arStandardMemoLineHome;

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepStandardMemoLineListControllerBean getAdBrResAll");

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

    public ArrayList executeArRepStandardMemoLineList(HashMap criteria, ArrayList branchList, String ORDER_BY, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepStandardMemoLineListControllerBean executeArRepStandardMemoLineList");

        ArrayList list = new ArrayList();

        try {

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT DISTINCT OBJECT(sml) FROM ArStandardMemoLine sml, IN(sml.adBranchStandardMemoLines)bsml WHERE(");

            if (branchList.isEmpty()) throw new GlobalNoRecordFoundException();

            Iterator brIter = branchList.iterator();

            AdBranchDetails brDetails = (AdBranchDetails) brIter.next();
            jbossQl.append("bsml.adBranch.brCode=").append(brDetails.getBrCode());

            while (brIter.hasNext()) {

                brDetails = (AdBranchDetails) brIter.next();

                jbossQl.append(" OR bsml.adBranch.brCode=").append(brDetails.getBrCode());
            }

            jbossQl.append(") ");
            firstArgument = false;

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("standardMemoLineName")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("standardMemoLineName")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("sml.smlName LIKE '%").append(criteria.get("standardMemoLineName")).append("%' ");
            }

            if (criteria.containsKey("type")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("sml.smlType=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("type");
                ctr++;
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("sml.smlAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            if (ORDER_BY.equals("MEMO LINE NAME")) {

                orderBy = "sml.smlName";

            } else if (ORDER_BY.equals("MEMO LINE TYPE")) {

                orderBy = "sml.smlType";
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy);
            }

            Collection arStandardMemoLines = arStandardMemoLineHome.getSmlByCriteria(jbossQl.toString(), obj);

            if (arStandardMemoLines.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object standardMemoLine : arStandardMemoLines) {

                LocalArStandardMemoLine arStandardMemoLine = (LocalArStandardMemoLine) standardMemoLine;

                ArRepStandardMemoLineListDetails details = new ArRepStandardMemoLineListDetails();
                details.setSllSmlName(arStandardMemoLine.getSmlName());
                details.setSllSmlDescription(arStandardMemoLine.getSmlDescription());
                details.setSllSmlType(arStandardMemoLine.getSmlType());
                details.setSllSmlUnitPrice(arStandardMemoLine.getSmlUnitPrice());

                if (arStandardMemoLine.getGlChartOfAccount() != null) {

                    details.setSllCoaGlAccountNumber(arStandardMemoLine.getGlChartOfAccount().getCoaAccountNumber());
                    details.setSllCoaGlAccountDescription(arStandardMemoLine.getGlChartOfAccount().getCoaAccountDescription());
                }

                details.setSllTaxable(arStandardMemoLine.getSmlTax());
                details.setSllEnable(arStandardMemoLine.getSmlEnable());

                list.add(details);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("ArRepStandardMemoLineListControllerBean getAdCompany");

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

        Debug.print("ArRepStandardMemoLineListControllerBean ejbCreate");
    }
}
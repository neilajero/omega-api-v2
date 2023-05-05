package com.ejb.txn.ad;

import com.ejb.PersistenceBeanClass;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.*;

import jakarta.ejb.*;

import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.dao.ad.LocalAdResponsibilityHome;
import com.ejb.entities.ad.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.*;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.ad.AdRepResponsibilityListDetails;

@Stateless(name = "AdRepResponsibilityListControllerEJB")
public class AdRepResponsibilityListControllerBean extends EJBContextClass implements AdRepResponsibilityListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdResponsibilityHome adResponsibilityHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;

    public ArrayList executeAdRepResponsibilityList(HashMap criteria, ArrayList branchList, String orderBy, boolean showFormFunction, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdRepResponsibilityListControllerBean executeAdRepResponsibilityList");

        ArrayList list = new ArrayList();

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(rs) FROM AdResponsibility rs ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            if (branchList.isEmpty()) {

                throw new GlobalNoRecordFoundException();

            } else {

                jbossQl.append(", in (rs.adBranchResponsibilities) brs WHERE brs.adBranch.brCode in (");

                boolean firstLoop = true;

                for (Object o : branchList) {

                    if (!firstLoop) {
                        jbossQl.append(", ");
                    } else {
                        firstLoop = false;
                    }

                    AdBranchDetails mdetails = (AdBranchDetails) o;

                    jbossQl.append(mdetails.getBrCode());
                }

                jbossQl.append(") ");

                firstArgument = false;
            }

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("responsibilityName")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("responsibilityName")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rs.rsName LIKE '%").append(criteria.get("responsibilityName")).append("%' ");
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rs.rsDateFrom=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateFrom");
                ctr++;
            }

            if (criteria.containsKey("dateTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("rs.rsDateTo=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("rs.rsAdCompany=").append(companyCode).append(" ");

            if (orderBy.equals("RESPONSIBILITY NAME")) {

                orderBy = "rs.rsName";

            } else if (orderBy.equals("RESPONSIBILITY DESC")) {

                orderBy = "rs.rsDescription";
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy);
            }

            // responsibility

            Collection adResponsibilities = adResponsibilityHome.getRsByCriteria(jbossQl.toString(), obj);

            if (adResponsibilities.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object responsibility : adResponsibilities) {

                LocalAdResponsibility adResponsibility = (LocalAdResponsibility) responsibility;

                if (showFormFunction) {

                    Collection adFormFunctions = adResponsibility.getAdFormFunctionResponsibilities();

                    for (Object formFunction : adFormFunctions) {

                        LocalAdFormFunctionResponsibility adFormFunction = (LocalAdFormFunctionResponsibility) formFunction;

                        list = this.getResponsibilityList(list, adResponsibility, adFormFunction.getAdFormFunction().getFfName());
                    }

                } else {

                    list = this.getResponsibilityList(list, adResponsibility, "");
                }
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer companyCode) {

        Debug.print("AdRepResponsibilityListControllerBean getAdCompany");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(companyCode);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());
            details.setCmpAddress(adCompany.getCmpAddress());
            details.setCmpCity(adCompany.getCmpCity());
            details.setCmpContact(adCompany.getCmpContact());
            details.setCmpCountry(adCompany.getCmpCountry());
            details.setCmpEmail(adCompany.getCmpEmail());
            details.setCmpTin(adCompany.getCmpTin());
            details.setCmpDescription(adCompany.getCmpDescription());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBrResAll(Integer responsibilityCode, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdRepBankAccountListControllerBean getAdBrResAll");

        LocalAdBranchResponsibility adBranchResponsibility;
        LocalAdBranch adBranch;
        Collection adBranchResponsibilities = null;
        ArrayList list = new ArrayList();
        try {

            adBranchResponsibilities = adBranchResponsibilityHome.findByAdResponsibility(responsibilityCode, companyCode);

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

                adBranch = adBranchResponsibility.getAdBranch();

                AdBranchDetails details = new AdBranchDetails();

                details.setBrCode(adBranch.getBrCode());
                details.setBrBranchCode(adBranch.getBrBranchCode());
                details.setBrName(adBranch.getBrName());
                details.setBrHeadQuarter(adBranch.getBrHeadQuarter());

                list.add(details);
            }

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
        return list;
    }

    // private method
    private ArrayList getResponsibilityList(ArrayList list, LocalAdResponsibility adResponsibility, String formFunction) {

        AdRepResponsibilityListDetails details = new AdRepResponsibilityListDetails();
        details.setRlResponsiblityName(adResponsibility.getRsName());
        details.setRlResponsibilityDescription(adResponsibility.getRsDescription());
        details.setRlDateFrom(adResponsibility.getRsDateFrom());
        details.setRlDateTo(adResponsibility.getRsDateTo());
        details.setRlFormFunction(formFunction);
        list.add(details);
        return list;
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("AdRepResponsibilityListControllerBean ejbCreate");
    }
}
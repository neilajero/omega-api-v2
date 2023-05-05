package com.ejb.txn.ad;

import java.util.*;

import jakarta.ejb.*;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdUser;
import com.ejb.entities.ad.LocalAdUserResponsibility;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.*;
import com.util.ad.AdCompanyDetails;
import com.util.ad.AdRepUserListDetails;

@Stateless(name = "AdRepUserListControllerEJB")
public class AdRepUserListControllerBean extends EJBContextClass implements AdRepUserListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdUserHome adUserHome;

    public ArrayList executeAdRepUserList(HashMap criteria, String orderBy, Integer companyCode) throws GlobalNoRecordFoundException {

        Debug.print("AdRepUserListControllerBean executeAdRepUserList");

        ArrayList list = new ArrayList();
        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(usr) FROM AdUser usr ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("userName")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("userName")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("usr.usrName LIKE '%").append(criteria.get("userName")).append("%' ");
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("usr.usrDateFrom=?").append(ctr + 1).append(" ");
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

                jbossQl.append("usr.usrDateTo=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("usr.usrAdCompany=").append(companyCode).append(" ");

            if (orderBy.equals("USER NAME")) {

                orderBy = "usr.usrName";

            } else if (orderBy.equals("USER DESC")) {

                orderBy = "usr.usrDescription";
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy);
            }

            Collection adUsers = adUserHome.getUsrByCriteria(jbossQl.toString(), obj);

            if (adUsers.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object user : adUsers) {

                LocalAdUser adUser = (LocalAdUser) user;

                Collection usrResponsibilities = adUser.getAdUserResponsibilities();

                for (Object responsibility : usrResponsibilities) {

                    AdRepUserListDetails details = new AdRepUserListDetails();

                    LocalAdUserResponsibility usrResponsibility = (LocalAdUserResponsibility) responsibility;

                    details.setUlUserName(adUser.getUsrName());
                    details.setUlUserDescription(adUser.getUsrDescription());
                    details.setUlPasswordExpirationCode(adUser.getUsrPasswordExpirationCode());
                    details.setUlPasswordExpirationDays(adUser.getUsrPasswordExpirationDays());
                    details.setUlPasswordExpirationAccess(adUser.getUsrPasswordExpirationAccess());
                    details.setUlDateFrom(adUser.getUsrDateFrom());
                    details.setUlDateTo(adUser.getUsrDateTo());
                    details.setUlResponsibilityName(usrResponsibility.getAdResponsibility().getRsName());
                    details.setUlResponsibilityDescription(usrResponsibility.getAdResponsibility().getRsDescription());
                    details.setUlResponsibilityDateFrom(usrResponsibility.getUrDateFrom());
                    details.setUlResponsibilityDateTo(usrResponsibility.getUrDateTo());

                    list.add(details);
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

        Debug.print("AdRepUserListControllerBean getAdCompany");

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

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("AdRepUserListControllerBean ejbCreate");
    }
}
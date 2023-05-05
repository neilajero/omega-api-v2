/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvRepStockTransferControllerBean
 * @created
 * @author
 */
package com.ejb.txnreports.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.inv.LocalInvLocationHome;
import com.ejb.dao.inv.LocalInvStockTransferLineHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.inv.LocalInvLocation;
import com.ejb.entities.inv.LocalInvStockTransferLine;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.inv.InvRepStockTransferDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Stateless(name = "InvRepStockTransferControllerEJB")
public class InvRepStockTransferControllerBean extends EJBContextClass implements InvRepStockTransferController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalInvStockTransferLineHome invStockTransferLineHome;
    @EJB
    private LocalInvLocationHome invLocationHome;

    public ArrayList executeInvRepStockTransfer(HashMap criteria, String ORDER_BY, ArrayList branchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvRepStockTransferControllerBean executeInvRepStockTransfer");
        ArrayList invStockTransferList = new ArrayList();
        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalInvLocation invLocation = null;

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT (stl) FROM InvStockTransferLine stl ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            if (branchList.isEmpty()) {

                throw new GlobalNoRecordFoundException();

            } else {

                jbossQl.append("WHERE stl.invStockTransfer.stAdBranch in (");

                boolean firstLoop = true;

                for (Object o : branchList) {

                    if (firstLoop == false) {
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

            // Allocate the size of the object parameter

            if (criteria.containsKey("itemName")) {

                criteriaSize--;
            }

            if (criteria.containsKey("includeUnposted")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("itemName")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("stl.invItem.iiName  LIKE '%").append(criteria.get("itemName")).append("%' ");
            }

            if (criteria.get("includeUnposted").equals("no")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("stl.invStockTransfer.stPosted=1 ");
            }

            if (criteria.containsKey("itemClass")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("stl.invItem.iiClass=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("itemClass");
                ctr++;
            }

            if (criteria.containsKey("category")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("stl.invItem.iiAdLvCategory=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("category");
                ctr++;
            }

            if (criteria.containsKey("locationTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                try {

                    invLocation = invLocationHome.findByLocName((String) criteria.get("locationTo"), AD_CMPNY);

                } catch (FinderException ex) {

                    throw new GlobalNoRecordFoundException();
                }

                jbossQl.append("stl.stlLocationTo=?").append(ctr + 1).append(" ");
                obj[ctr] = invLocation.getLocCode();
                ctr++;
            }

            if (criteria.containsKey("locationFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                try {

                    invLocation = invLocationHome.findByLocName((String) criteria.get("locationFrom"), AD_CMPNY);

                } catch (FinderException ex) {

                    throw new GlobalNoRecordFoundException();
                }

                jbossQl.append("stl.stlLocationFrom=?").append(ctr + 1).append(" ");
                obj[ctr] = invLocation.getLocCode();
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("stl.invStockTransfer.stDate>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("stl.invStockTransfer.stDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("stl.stlAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            switch (ORDER_BY) {
                case "DATE":

                    orderBy = "stl.invStockTransfer.stDate";

                    break;
                case "ITEM NAME":

                    orderBy = "stl.invItem.iiName";

                    break;
                case "ITEM DESCRIPTION":

                    orderBy = "stl.invItem.iiDescription";

                    break;
                case "DOCUMENT NUMBER":

                    orderBy = "stl.invStockTransfer.stDocumentNumber";
                    break;
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy);
            }

            Collection invStockTransfers = null;

            try {

                invStockTransfers = invStockTransferLineHome.getStlByCriteria(jbossQl.toString(), obj);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            if (invStockTransfers.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            for (Object invStockTransfer : invStockTransfers) {

                LocalInvStockTransferLine invStockTransferLine = (LocalInvStockTransferLine) invStockTransfer;

                InvRepStockTransferDetails details = new InvRepStockTransferDetails();

                details.setStDate(invStockTransferLine.getInvStockTransfer().getStDate());
                details.setStItemName(invStockTransferLine.getInvItem().getIiName());
                details.setStItemDescription(invStockTransferLine.getInvItem().getIiDescription());
                details.setStItemCategory(invStockTransferLine.getInvItem().getIiAdLvCategory());
                details.setStUnit(invStockTransferLine.getInvUnitOfMeasure().getUomName());

                try {

                    invLocation = invLocationHome.findByPrimaryKey(invStockTransferLine.getStlLocationFrom());

                } catch (FinderException ex) {

                    throw new GlobalNoRecordFoundException();
                }

                details.setStLocationFrom(invLocation.getLocName());

                try {

                    invLocation = invLocationHome.findByPrimaryKey(invStockTransferLine.getStlLocationTo());

                } catch (FinderException ex) {

                    throw new GlobalNoRecordFoundException();
                }
                System.out.print(invStockTransferLine.getStlUnitCost() + " item cost");

                details.setStLocationTo(invLocation.getLocName());

                details.setStDocNumber(invStockTransferLine.getInvStockTransfer().getStDocumentNumber());
                details.setStQuantity(invStockTransferLine.getStlQuantityDelivered());
                details.setStUnitCost(invStockTransferLine.getStlUnitCost());
                details.setStAmount(invStockTransferLine.getStlAmount());

                invStockTransferList.add(details);
            }

            if (ORDER_BY.equals("LOCATION FROM")) {

                invStockTransferList.sort(InvRepStockTransferDetails.LocationFromComparator);

            } else if (ORDER_BY.equals("LOCATION TO")) {

                invStockTransferList.sort(InvRepStockTransferDetails.LocationToComparator);
            }

            return invStockTransferList;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("InvRepStockTransferControllerBean getAdCompany");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            AdCompanyDetails details = new AdCompanyDetails();
            details.setCmpName(adCompany.getCmpName());
            details.setCmpAddress(adCompany.getCmpAddress());
            details.setCmpCity(adCompany.getCmpCity());
            details.setCmpContact(adCompany.getCmpContact());
            details.setCmpCountry(adCompany.getCmpCountry());
            details.setCmpEmail(adCompany.getCmpEmail());
            details.setCmpTin(adCompany.getCmpTin());
            details.setCmpDescription(adCompany.getCmpDescription());
            details.setCmpPhone(adCompany.getCmpPhone());

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("InvRepStockTransferControllerBean ejbCreate");
    }

}
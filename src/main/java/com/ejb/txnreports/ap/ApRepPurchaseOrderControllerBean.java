package com.ejb.txnreports.ap;

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
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ap.LocalApPurchaseOrderLine;
import com.ejb.dao.ap.LocalApPurchaseOrderLineHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.inv.LocalInvLocation;
import com.ejb.dao.inv.LocalInvLocationHome;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ap.ApRepPurchaseOrderDetails;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ApRepPurchaseOrderControllerEJB")
public class ApRepPurchaseOrderControllerBean extends EJBContextClass implements ApRepPurchaseOrderController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalApPurchaseOrderLineHome apPurchaseOrderLineHome;
    @EJB
    private LocalInvLocationHome invLocationHome;


    public ArrayList getAdLvInvItemCategoryAll(Integer AD_CMPNY) {

        Debug.print("ApRepPurchaseOrderControllerBean getAdLvInvItemCategoryAll");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("INV ITEM CATEGORY", AD_CMPNY);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getInvLocAll(Integer AD_CMPNY) {

        Debug.print("ApRepPurchaseOrderControllerBean getInvLocAll");

        Collection invLocations = null;
        ArrayList list = new ArrayList();

        try {

            invLocations = invLocationHome.findLocAll(AD_CMPNY);

            if (invLocations.isEmpty()) {

                return null;
            }

            for (Object location : invLocations) {

                LocalInvLocation invLocation = (LocalInvLocation) location;
                String details = invLocation.getLocName();

                list.add(details);
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList executeApRepPurchaseOrder(HashMap criteria, String ORDER_BY, boolean SUMMARIZE, ArrayList adBrnchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepPurchaseOrderControllerBean executeApRepPurchaseOrder");

        ArrayList apPurchaseOrderList = new ArrayList();

        try {

            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT (pl) FROM ApPurchaseOrderLine pl ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("itemName")) {

                criteriaSize--;
            }

            if (criteria.containsKey("supplierCode")) {

                criteriaSize--;
            }

            if (criteria.containsKey("includeUnposted")) {

                criteriaSize--;
            }

            if (criteria.containsKey("includeCancelled")) {

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

                jbossQl.append("pl.invItemLocation.invItem.iiName LIKE '%").append(criteria.get("itemName")).append("%' ");
            }

            if (criteria.get("includeUnposted").equals("no")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pl.apPurchaseOrder.poPosted=1 ");
            }

            if (criteria.get("includeCancelled").equals("no")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pl.apPurchaseOrder.poVoid=0 ");
            }

            if (criteria.containsKey("supplierCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pl.apPurchaseOrder.apSupplier.splSupplierCode LIKE '%").append(criteria.get("supplierCode")).append("%' ");
            }

            if (criteria.containsKey("category")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pl.invItemLocation.invItem.iiAdLvCategory=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("category");
                ctr++;
            }

            if (criteria.containsKey("location")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pl.invItemLocation.invLocation.locName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("location");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pl.apPurchaseOrder.poDate>=?").append(ctr + 1).append(" ");
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

                jbossQl.append("pl.apPurchaseOrder.poDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateTo");
                ctr++;
            }

            if (adBrnchList.isEmpty()) {

                throw new GlobalNoRecordFoundException();

            } else {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pl.apPurchaseOrder.poAdBranch in (");

                boolean firstLoop = true;

                for (Object o : adBrnchList) {

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

            jbossQl.append("AND pl.apPurchaseOrder.poReceiving = 0 AND pl.plAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            switch (ORDER_BY) {
                case "DATE":

                    orderBy = "pl.apPurchaseOrder.poDate";

                    break;
                case "ITEM NAME":

                    orderBy = "pl.invItemLocation.invItem.iiName";

                    break;
                case "ITEM DESCRIPTION":

                    orderBy = "pl.invItemLocation.invItem.iiDescription";

                    break;
                case "DOCUMENT NUMBER":

                    orderBy = "pl.apPurchaseOrder.poDocumentNumber";

                    break;
                case "LOCATION":

                    orderBy = "pl.invItemLocation.invLocation.locName";

                    break;
                case "PO NUMBER":

                    orderBy = "pl.apPurchaseOrder.poCode";
                    break;
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy);
            }

            Collection apPurchaseOrderLines = null;

            try {

                apPurchaseOrderLines = apPurchaseOrderLineHome.getPolByCriteria(jbossQl.toString(), obj);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            if (apPurchaseOrderLines.isEmpty()) throw new GlobalNoRecordFoundException();

            for (Object purchaseOrderLine : apPurchaseOrderLines) {

                LocalApPurchaseOrderLine apPurchaseOrderLine = (LocalApPurchaseOrderLine) purchaseOrderLine;

                ApRepPurchaseOrderDetails details = new ApRepPurchaseOrderDetails();

                details.setPoDate(apPurchaseOrderLine.getApPurchaseOrder().getPoDate());
                LocalAdBranch adBranchCode = adBranchHome.findByPrimaryKey(apPurchaseOrderLine.getApPurchaseOrder().getPoAdBranch());

                details.setPoBranchCode(adBranchCode.getBrBranchCode());
                details.setPoBranchName(adBranchCode.getBrName());
                details.setPoItemName(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName());
                details.setPoLocation(apPurchaseOrderLine.getInvItemLocation().getInvLocation().getLocName());
                details.setPoUnit(apPurchaseOrderLine.getInvUnitOfMeasure().getUomName());
                details.setPoSupplierName(apPurchaseOrderLine.getApPurchaseOrder().getApSupplier().getSplName());
                details.setPoDocNo(apPurchaseOrderLine.getApPurchaseOrder().getPoDocumentNumber());
                details.setPoNumber(apPurchaseOrderLine.getApPurchaseOrder().getPoCode().toString());
                details.setPoQuantity(apPurchaseOrderLine.getPlQuantity());
                details.setPoUnitCost(apPurchaseOrderLine.getPlUnitCost());
                details.setPoDescription(apPurchaseOrderLine.getApPurchaseOrder().getPoDescription());
                details.setPoAmount(apPurchaseOrderLine.getPlAmount());
                details.setPoReferenceNumber(apPurchaseOrderLine.getApPurchaseOrder().getPoReferenceNumber());
                details.setPoItemDescription(apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiDescription());

                details.setPoCreatedBy(apPurchaseOrderLine.getApPurchaseOrder().getPoCreatedBy());
                details.setOrderBy(ORDER_BY);
                if (apPurchaseOrderLine.getApPurchaseOrder().getPoVoid() == 1) details.setPoVoid("YES");
                else details.setPoVoid("NO");

                obj = new Object[2];

                obj[0] = apPurchaseOrderLine.getApPurchaseOrder().getPoDocumentNumber();
                obj[1] = apPurchaseOrderLine.getInvItemLocation().getInvItem().getIiName();

                Collection apReceivingItemsLines = null;

                try {

                    apReceivingItemsLines = apPurchaseOrderLineHome.getPolByCriteria("SELECT OBJECT (pl) FROM ApPurchaseOrderLine pl WHERE pl.apPurchaseOrder.poRcvPoNumber =?1" + " and pl.invItemLocation.invItem.iiName=?2", obj);

                } catch (FinderException ex) {

                }

                if (!apPurchaseOrderLines.isEmpty()) {

                    Iterator j = apReceivingItemsLines.iterator();

                    double TTL_RCV_QTY = 0d;

                    while (j.hasNext()) {

                        LocalApPurchaseOrderLine receivingItem = (LocalApPurchaseOrderLine) j.next();
                        Debug.print("receivingItem=" + receivingItem.getApPurchaseOrder().getPoDocumentNumber());
                        TTL_RCV_QTY += receivingItem.getPlQuantity();
                    }

                    Debug.print("TTL_RCV_QTY=" + TTL_RCV_QTY);
                    details.setPoQuantityReceived(TTL_RCV_QTY);

                } else {

                    details.setPoQuantityReceived(0);
                }

                if (apPurchaseOrderLine.getApPurchaseOrder().getPoApprovedRejectedBy() != null)
                    details.setPoApprovedRejectedBy(apPurchaseOrderLine.getApPurchaseOrder().getPoApprovedRejectedBy());

                apPurchaseOrderList.add(details);
            }

            if (SUMMARIZE) {

                apPurchaseOrderList.sort(ApRepPurchaseOrderDetails.ItemNameComparator);
            }

            return apPurchaseOrderList;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("ApRepPurchaseOrderControllerBean getAdCompany");

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

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepPurchaseOrderControllerBean getAdBrResAll");

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

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApRepPurchaseOrderControllerBean ejbCreate");
    }
}
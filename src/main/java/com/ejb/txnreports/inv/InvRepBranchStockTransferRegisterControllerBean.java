/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class InvRepBranchStockTransferRegisterControllerBean
 */
package com.ejb.txnreports.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.inv.LocalInvBranchStockTransferLineHome;
import com.ejb.dao.inv.LocalInvItemLocationHome;
import com.ejb.dao.inv.LocalInvUnitOfMeasureConversionHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.inv.*;
import com.ejb.exception.global.GlobalInvItemLocationNotFoundException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ad.AdCompanyDetails;
import com.util.reports.inv.InvRepBranchStockTransferRegisterDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

@Stateless(name = "InvRepBranchStockTransferRegisterControllerEJB")
public class InvRepBranchStockTransferRegisterControllerBean extends EJBContextClass implements InvRepBranchStockTransferRegisterController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalInvBranchStockTransferLineHome invBranchStockTransferLineHome;
    @EJB
    private LocalInvItemLocationHome invItemLocationHome;
    @EJB
    private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
    @EJB
    private LocalAdBranchHome adBranchHome;

    public ArrayList executeInvRepBranchStockTransferRegister(HashMap criteria, String ORDER_BY, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvRepBranchStockTransferRegisterControllerBean executeInvRepBranchStockTransferRegister");
        ArrayList list = new ArrayList();
        try {

            String type = "";
            StringBuffer jbossQl = new StringBuffer();

            // BST OUT

            if (criteria.containsKey("includeTransferOut") && criteria.get("includeTransferOut").equals("YES")) {

                type = "OUT";

                jbossQl = new StringBuffer();
                jbossQl.append("SELECT OBJECT(bstl) FROM InvBranchStockTransferLine bstl ");

                boolean firstArgument = true;
                short ctr = 0;
                int criteriaSize = 0;

                Object[] obj;

                // Allocate the size of the object parameter

                if (criteria.containsKey("dateFrom")) {

                    criteriaSize++;
                }

                if (criteria.containsKey("dateTo")) {

                    criteriaSize++;
                }

                if (criteria.containsKey("transferOutNumberFrom")) {

                    criteriaSize++;
                }

                if (criteria.containsKey("transferOutNumberTo")) {

                    criteriaSize++;
                }

                if (criteria.containsKey("branchTo")) {

                    criteriaSize++;
                }
                if (criteria.containsKey("itemCategory")) {
                    criteriaSize++;
                }

                obj = new Object[criteriaSize];

                if (criteria.containsKey("dateFrom")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("bstl.invBranchStockTransfer.bstDate>=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateFrom");
                    ctr++;
                }

                if (criteria.containsKey("itemCategory")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("bstl.invItemLocation.invItem.iiAdLvCategory=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("itemCategory");
                    ctr++;
                }

                if (criteria.containsKey("dateTo")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("bstl.invBranchStockTransfer.bstDate<=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateTo");
                    ctr++;
                }

                if (criteria.containsKey("transferOutNumberFrom")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("bstl.invBranchStockTransfer.bstNumber>=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("transferOutNumberFrom");
                    ctr++;
                }

                if (criteria.containsKey("transferOutNumberTo")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("bstl.invBranchStockTransfer.bstNumber<=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("transferOutNumberTo");
                    ctr++;
                }

                if (criteria.containsKey("branchTo")) {

                    Integer brCode = this.getAdBrCodeByBrName((String) criteria.get("branchTo"), AD_CMPNY);

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("bstl.invBranchStockTransfer.adBranch.brCode=?").append(ctr + 1).append(" ");
                    obj[ctr] = brCode;
                    ctr++;
                }

                if (criteria.get("includeUnposted").equals("NO")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("bstl.invBranchStockTransfer.bstPosted=1 ");
                }

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("bstl.invBranchStockTransfer.bstVoid=0 AND bstl.invBranchStockTransfer.bstType='").append(type).append("' AND bstl.invBranchStockTransfer.bstAdBranch=").append(AD_BRNCH).append(" AND bstl.invBranchStockTransfer.bstAdCompany=").append(AD_CMPNY).append(" ");

                String orderBy = null;

                if (ORDER_BY.equals("DOCUMENT NUMBER")) {

                    jbossQl.append("ORDER BY bstl.invBranchStockTransfer.bstNumber");

                } else {

                    jbossQl.append("ORDER BY bstl.invBranchStockTransfer.bstDate");
                }

                Collection invBranchStockTransferLines = invBranchStockTransferLineHome.getBstlByCriteria(jbossQl.toString(), obj);

                Iterator i = invBranchStockTransferLines.iterator();
                Debug.print("Size=" + invBranchStockTransferLines.size());
                while (i.hasNext()) {

                    LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) i.next();

                    InvRepBranchStockTransferRegisterDetails mdetails = new InvRepBranchStockTransferRegisterDetails();
                    Debug.print("BSL--" + invBranchStockTransferLine.getBslCode());
                    mdetails.setBstrBstType(invBranchStockTransferLine.getInvBranchStockTransfer().getBstType());
                    mdetails.setBstrBstStatus("TRANSFER OUT");
                    mdetails.setBstrBstDocumentNumber(invBranchStockTransferLine.getInvBranchStockTransfer().getBstNumber());
                    mdetails.setBstrBstDate(invBranchStockTransferLine.getInvBranchStockTransfer().getBstDate());
                    mdetails.setBstrBstTransferOutNumber(invBranchStockTransferLine.getInvBranchStockTransfer().getBstTransferOutNumber());
                    mdetails.setBstrBstBranch(invBranchStockTransferLine.getInvBranchStockTransfer().getAdBranch().getBrName());
                    mdetails.setBstrBstTransitLocation(invBranchStockTransferLine.getInvBranchStockTransfer().getInvLocation().getLocName());
                    mdetails.setBstrBstCreatedBy(invBranchStockTransferLine.getInvBranchStockTransfer().getBstCreatedBy());

                    mdetails.setBstrBstlItemName(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName());
                    mdetails.setBstrBstlItemDescription(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiDescription());
                    mdetails.setBstrBstlLocation(invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName());
                    mdetails.setBstrBstlUnit(invBranchStockTransferLine.getInvUnitOfMeasure().getUomShortName());
                    mdetails.setBstrBstlUnitCost(invBranchStockTransferLine.getBslUnitCost());
                    mdetails.setBstrBstlAmount(invBranchStockTransferLine.getBslAmount());

                    mdetails.setBstrBstlQuantity(invBranchStockTransferLine.getBslQuantity());
                    mdetails.setOrderBy(ORDER_BY);

                    double quantityReceived = 0d;

                    Debug.print("Doc Num=" + mdetails.getBstrBstDocumentNumber());
                    Debug.print("Item Name=" + mdetails.getBstrBstlItemName());
                    Debug.print("Location=" + mdetails.getBstrBstlLocation());
                    Collection invReceivedQuantities = invBranchStockTransferLineHome.findQtyTransferedByBstTranferOutNumberAndIiCode(mdetails.getBstrBstDocumentNumber(), mdetails.getBstrBstlItemName(), mdetails.getBstrBstlLocation(), AD_CMPNY);

                    for (Object receivedQuantity : invReceivedQuantities) {

                        LocalInvBranchStockTransferLine invReceivedQuantity = (LocalInvBranchStockTransferLine) receivedQuantity;

                        if (mdetails.getBstrBstlUnit().equals(invReceivedQuantity.getInvUnitOfMeasure().getUomShortName())) {
                            quantityReceived += invReceivedQuantity.getBslQuantityReceived();
                        }
                    }

                    mdetails.setBstrBstlQuantityTransfered(quantityReceived);

                    list.add(mdetails);
                }
            }

            // BST IN

            if (criteria.containsKey("includeTransferIn") && criteria.get("includeTransferIn").equals("YES")) {

                type = "IN";

                jbossQl = new StringBuffer();
                jbossQl.append("SELECT OBJECT(bstl) FROM InvBranchStockTransferLine bstl ");

                boolean firstArgument = true;
                short ctr = 0;
                int criteriaSize = 0;

                Object[] obj;

                // Allocate the size of the object parameter

                if (criteria.containsKey("dateFrom")) {

                    criteriaSize++;
                }

                if (criteria.containsKey("dateTo")) {

                    criteriaSize++;
                }

                if (criteria.containsKey("transferInNumberFrom")) {

                    criteriaSize++;
                }

                if (criteria.containsKey("transferInNumberTo")) {

                    criteriaSize++;
                }

                if (criteria.containsKey("branchFrom")) {

                    criteriaSize++;
                }

                if (criteria.containsKey("itemCategory")) {
                    criteriaSize++;
                }

                obj = new Object[criteriaSize];

                if (criteria.containsKey("dateFrom")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("bstl.invBranchStockTransfer.bstDate>=?").append(ctr + 1).append(" ");
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
                    jbossQl.append("bstl.invBranchStockTransfer.bstDate<=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateTo");
                    ctr++;
                }

                if (criteria.containsKey("itemCategory")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("bstl.invItemLocation.invItem.iiAdLvCategory=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("itemCategory");
                    ctr++;
                }

                if (criteria.containsKey("transferInNumberFrom")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("bstl.invBranchStockTransfer.bstNumber>=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("transferInNumberFrom");
                    ctr++;
                }

                if (criteria.containsKey("transferInNumberTo")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("bstl.invBranchStockTransfer.bstNumber<=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("transferInNumberTo");
                    ctr++;
                }

                if (criteria.containsKey("branchFrom")) {

                    Integer brCode = this.getAdBrCodeByBrName((String) criteria.get("branchFrom"), AD_CMPNY);

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("bstl.invBranchStockTransfer.adBranch.brCode=?").append(ctr + 1).append(" ");
                    obj[ctr] = brCode;
                    ctr++;
                }

                if (criteria.containsKey("referenceNumber")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("bstl.invBranchStockTransfer.bstTransferOutNumber LIKE '%").append(criteria.get("referenceNumber")).append("%' ");
                }

                if (criteria.get("includeUnposted").equals("NO")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("bstl.invBranchStockTransfer.bstPosted=1 ");
                }

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("bstl.invBranchStockTransfer.bstType='").append(type).append("' AND bstl.invBranchStockTransfer.bstAdBranch=").append(AD_BRNCH).append(" AND bstl.invBranchStockTransfer.bstAdCompany=").append(AD_CMPNY).append(" ");

                String orderBy = null;

                if (ORDER_BY.equals("DOCUMENT NUMBER")) {

                    jbossQl.append("ORDER BY bstl.invBranchStockTransfer.bstNumber");

                } else {

                    jbossQl.append("ORDER BY bstl.invBranchStockTransfer.bstDate");
                }

                Collection invBranchStockTransferLines = invBranchStockTransferLineHome.getBstlByCriteria(jbossQl.toString(), obj);

                for (Object branchStockTransferLine : invBranchStockTransferLines) {

                    LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) branchStockTransferLine;

                    InvRepBranchStockTransferRegisterDetails mdetails = new InvRepBranchStockTransferRegisterDetails();
                    mdetails.setBstrBstType(invBranchStockTransferLine.getInvBranchStockTransfer().getBstType());
                    mdetails.setBstrBstStatus("TRANSFER IN");
                    mdetails.setBstrBstDocumentNumber(invBranchStockTransferLine.getInvBranchStockTransfer().getBstNumber());
                    mdetails.setBstrBstDate(invBranchStockTransferLine.getInvBranchStockTransfer().getBstDate());
                    mdetails.setBstrBstTransferOutNumber(invBranchStockTransferLine.getInvBranchStockTransfer().getBstTransferOutNumber());
                    mdetails.setBstrBstBranch(invBranchStockTransferLine.getInvBranchStockTransfer().getAdBranch().getBrName());
                    mdetails.setBstrBstTransitLocation(invBranchStockTransferLine.getInvBranchStockTransfer().getInvLocation().getLocName());
                    mdetails.setBstrBstCreatedBy(invBranchStockTransferLine.getInvBranchStockTransfer().getBstCreatedBy());

                    mdetails.setBstrBstlItemName(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName());
                    mdetails.setBstrBstlItemDescription(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiDescription());
                    mdetails.setBstrBstlLocation(invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName());
                    mdetails.setBstrBstlUnit(invBranchStockTransferLine.getInvUnitOfMeasure().getUomShortName());
                    mdetails.setBstrBstlUnitCost(invBranchStockTransferLine.getBslUnitCost());
                    mdetails.setBstrBstlAmount(invBranchStockTransferLine.getBslAmount());

                    mdetails.setBstrBstlQuantity(invBranchStockTransferLine.getBslQuantityReceived());

                    mdetails.setOrderBy(ORDER_BY);

                    list.add(mdetails);
                }
            }
            // BST ORDER

            if (criteria.containsKey("includeTransferOrder") && criteria.get("includeTransferOrder").equals("YES")) {

                type = "ORDER";

                jbossQl = new StringBuffer();
                jbossQl.append("SELECT OBJECT(bstl) FROM InvBranchStockTransferLine bstl ");

                boolean firstArgument = true;
                short ctr = 0;
                int criteriaSize = 0;

                Object[] obj;

                // Allocate the size of the object parameter

                if (criteria.containsKey("dateFrom")) {

                    criteriaSize++;
                }

                if (criteria.containsKey("dateTo")) {

                    criteriaSize++;
                }

                if (criteria.containsKey("itemCategory")) {

                    criteriaSize++;
                }

                if (criteria.containsKey("branchFrom")) {

                    criteriaSize++;
                }

                obj = new Object[criteriaSize];

                if (criteria.containsKey("dateFrom")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("bstl.invBranchStockTransfer.bstDate>=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateFrom");
                    ctr++;
                }

                if (criteria.containsKey("itemCategory")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("bstl.invItemLocation.invItem.iiAdLvCategory=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("itemCategory");
                    ctr++;
                }

                if (criteria.containsKey("dateTo")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("bstl.invBranchStockTransfer.bstDate<=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateTo");
                    ctr++;
                }

                if (criteria.containsKey("branchFrom")) {

                    Integer brCode = this.getAdBrCodeByBrName((String) criteria.get("branchFrom"), AD_CMPNY);

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("bstl.invBranchStockTransfer.bstAdBranch=?").append(ctr + 1).append(" ");
                    obj[ctr] = brCode;
                    ctr++;
                }

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("bstl.invBranchStockTransfer.bstPosted=1 AND bstl.invBranchStockTransfer.bstLock=0 " + "AND bstl.invBranchStockTransfer.bstType='").append(type).append("' ").append("AND bstl.invBranchStockTransfer.adBranch.brCode=").append(AD_BRNCH).append(" AND bstl.invBranchStockTransfer.bstAdCompany=").append(AD_CMPNY).append(" ");

                String orderBy = null;

                if (ORDER_BY.equals("DOCUMENT NUMBER")) {

                    jbossQl.append("ORDER BY bstl.invBranchStockTransfer.bstNumber");

                } else {

                    jbossQl.append("ORDER BY bstl.invBranchStockTransfer.bstDate");
                }

                Collection invBranchStockTransferLines = invBranchStockTransferLineHome.getBstlByCriteria(jbossQl.toString(), obj);

                Iterator i = invBranchStockTransferLines.iterator();
                LocalInvItemLocation invItemLocation = null;
                short LINE_NUMBER = 0;
                while (i.hasNext()) {
                    LINE_NUMBER++;
                    LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) i.next();

                    try {
                        // search the item from item location
                        invItemLocation = invItemLocationHome.findByLocNameAndIiName(invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName(), invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName(), AD_CMPNY);

                    } catch (FinderException ex) {

                        throw new GlobalInvItemLocationNotFoundException("Line " + String.valueOf(LINE_NUMBER) + " - " + invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName());
                    }
                    Debug.print(invBranchStockTransferLine.getBslQuantity() + "<== before conversion");

                    double convertedQuantity = this.convertByUomAndQuantity(invBranchStockTransferLine.getInvUnitOfMeasure(), invItemLocation.getInvItem(), invBranchStockTransferLine.getBslQuantity(), AD_CMPNY);
                    Debug.print(convertedQuantity + "<== after conversion");

                    Debug.print(invBranchStockTransferLine.getInvUnitOfMeasure().getUomShortName() + "<== Unit of Conversion edited in transfer order");
                    String defaultUOM = invItemLocation.getInvItem().getInvUnitOfMeasure().getUomShortName();
                    Debug.print(defaultUOM + "<== default Unit of Conversion in item entry");

                    InvRepBranchStockTransferRegisterDetails mdetails = new InvRepBranchStockTransferRegisterDetails();
                    mdetails.setBstrBstType(invBranchStockTransferLine.getInvBranchStockTransfer().getBstType());
                    mdetails.setBstrBstStatus("TRANSFER ORDER");
                    mdetails.setBstrBstDocumentNumber(invBranchStockTransferLine.getInvBranchStockTransfer().getBstNumber());
                    mdetails.setBstrBstDate(invBranchStockTransferLine.getInvBranchStockTransfer().getBstDate());
                    mdetails.setBstrBstTransferOutNumber(invBranchStockTransferLine.getInvBranchStockTransfer().getBstTransferOutNumber());
                    mdetails.setBstrBstBranch(this.getAdBrNameByBrCode(invBranchStockTransferLine.getInvBranchStockTransfer().getBstAdBranch()));
                    mdetails.setBstrBstTransitLocation(invBranchStockTransferLine.getInvBranchStockTransfer().getInvLocation().getLocName());
                    mdetails.setBstrBstCreatedBy(invBranchStockTransferLine.getInvBranchStockTransfer().getBstCreatedBy());

                    mdetails.setBstrBstlItemName(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName());
                    mdetails.setBstrBstlItemDescription(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiDescription());
                    mdetails.setBstrBstlLocation(invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName());
                    mdetails.setBstrBstlUnit(defaultUOM);
                    mdetails.setBstrBstlUnitCost(invBranchStockTransferLine.getBslUnitCost());
                    mdetails.setBstrBstlAmount(invBranchStockTransferLine.getBslAmount());

                    mdetails.setBstrBstlQuantity(convertedQuantity);

                    mdetails.setOrderBy(ORDER_BY);

                    list.add(mdetails);
                }
                list.sort(InvRepBranchStockTransferRegisterDetails.ItemNameComparator);
            }

            // BST Incoming

            if (criteria.containsKey("includeIncoming") && criteria.get("includeIncoming").equals("YES")) {

                type = "OUT";

                jbossQl = new StringBuffer();
                jbossQl.append("SELECT OBJECT(bstl) FROM InvBranchStockTransferLine bstl ");

                boolean firstArgument = true;
                short ctr = 0;
                int criteriaSize = 0;

                Object[] obj;

                // Allocate the size of the object parameter

                if (criteria.containsKey("dateFrom")) {

                    criteriaSize++;
                }

                if (criteria.containsKey("dateTo")) {

                    criteriaSize++;
                }

                if (criteria.containsKey("branchFrom")) {

                    criteriaSize++;
                }

                if (criteria.containsKey("itemCategory")) {
                    criteriaSize++;
                }

                obj = new Object[criteriaSize];

                if (criteria.containsKey("dateFrom")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("bstl.invBranchStockTransfer.bstDate>=?").append(ctr + 1).append(" ");
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
                    jbossQl.append("bstl.invBranchStockTransfer.bstDate<=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("dateTo");
                    ctr++;
                }

                if (criteria.containsKey("itemCategory")) {

                    if (!firstArgument) {

                        jbossQl.append("AND ");

                    } else {

                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("bstl.invItemLocation.invItem.iiAdLvCategory=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("itemCategory");
                    ctr++;
                }

                if (criteria.containsKey("branchFrom")) {

                    Integer brCode = this.getAdBrCodeByBrName((String) criteria.get("branchFrom"), AD_CMPNY);

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("bstl.invBranchStockTransfer.bstAdBranch=?").append(ctr + 1).append(" ");
                    obj[ctr] = brCode;
                    ctr++;
                }

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("bstl.invBranchStockTransfer.bstPosted=1 AND bstl.invBranchStockTransfer.bstLock=0 " + "AND bstl.invBranchStockTransfer.bstType='").append(type).append("' ").append("AND bstl.invBranchStockTransfer.adBranch.brCode=").append(AD_BRNCH).append(" AND bstl.invBranchStockTransfer.bstAdCompany=").append(AD_CMPNY).append(" ");

                String orderBy = null;

                if (ORDER_BY.equals("DOCUMENT NUMBER")) {

                    jbossQl.append("ORDER BY bstl.invBranchStockTransfer.bstNumber");

                } else {

                    jbossQl.append("ORDER BY bstl.invBranchStockTransfer.bstDate");
                }

                Collection invBranchStockTransferLines = invBranchStockTransferLineHome.getBstlByCriteria(jbossQl.toString(), obj);

                for (Object branchStockTransferLine : invBranchStockTransferLines) {

                    LocalInvBranchStockTransferLine invBranchStockTransferLine = (LocalInvBranchStockTransferLine) branchStockTransferLine;

                    InvRepBranchStockTransferRegisterDetails mdetails = new InvRepBranchStockTransferRegisterDetails();
                    mdetails.setBstrBstType(invBranchStockTransferLine.getInvBranchStockTransfer().getBstType());
                    mdetails.setBstrBstStatus("INCOMING TRANSFER");
                    mdetails.setBstrBstDocumentNumber(invBranchStockTransferLine.getInvBranchStockTransfer().getBstNumber());
                    mdetails.setBstrBstDate(invBranchStockTransferLine.getInvBranchStockTransfer().getBstDate());
                    mdetails.setBstrBstTransferOutNumber(invBranchStockTransferLine.getInvBranchStockTransfer().getBstTransferOutNumber());
                    mdetails.setBstrBstBranch(this.getAdBrNameByBrCode(invBranchStockTransferLine.getInvBranchStockTransfer().getBstAdBranch()));
                    mdetails.setBstrBstTransitLocation(invBranchStockTransferLine.getInvBranchStockTransfer().getInvLocation().getLocName());
                    mdetails.setBstrBstCreatedBy(invBranchStockTransferLine.getInvBranchStockTransfer().getBstCreatedBy());

                    mdetails.setBstrBstlItemName(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiName());
                    mdetails.setBstrBstlItemDescription(invBranchStockTransferLine.getInvItemLocation().getInvItem().getIiDescription());
                    mdetails.setBstrBstlLocation(invBranchStockTransferLine.getInvItemLocation().getInvLocation().getLocName());
                    mdetails.setBstrBstlUnit(invBranchStockTransferLine.getInvUnitOfMeasure().getUomShortName());
                    mdetails.setBstrBstlUnitCost(invBranchStockTransferLine.getBslUnitCost());
                    mdetails.setBstrBstlAmount(invBranchStockTransferLine.getBslAmount());

                    mdetails.setBstrBstlQuantity(invBranchStockTransferLine.getBslQuantity());

                    mdetails.setOrderBy(ORDER_BY);

                    list.add(mdetails);
                }
            }

            if (list.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            // sort
            list.sort(InvRepBranchStockTransferRegisterDetails.StatusComparator);

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("InvRepBranchStockTransferRegisterControllerBean getAdCompany");
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

    private double convertByUomAndQuantity(LocalInvUnitOfMeasure invFromUnitOfMeasure, LocalInvItem invItem, double ADJST_QTY, Integer AD_CMPNY) {

        Debug.print("InvBranchStockTransferOrderEntryControllerBean convertByUomFromAndUomToAndQuantity");
        try {
            Debug.print("InvBranchStockTransferOrderEntryControllerBean convertByUomFromAndUomToAndQuantity A");
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invFromUnitOfMeasure.getUomName(), AD_CMPNY);
            LocalInvUnitOfMeasureConversion invDefaultUomConversion = invUnitOfMeasureConversionHome.findUmcByIiNameAndUomName(invItem.getIiName(), invItem.getInvUnitOfMeasure().getUomName(), AD_CMPNY);
            Debug.print("InvBranchStockTransferOrderEntryControllerBean convertByUomFromAndUomToAndQuantity B");
            return EJBCommon.roundIt(ADJST_QTY * invDefaultUomConversion.getUmcConversionFactor() / invUnitOfMeasureConversion.getUmcConversionFactor(), adPreference.getPrfInvQuantityPrecisionUnit());

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // private methods
    private Integer getAdBrCodeByBrName(String BR_BRNCH_NM, Integer AD_CMPNY) {

        Debug.print("InvRepBranchStockTransferRegisterControllerBean getAdBrCodeByBrName");
        try {

            LocalAdBranch adBranch = adBranchHome.findByBrBranchCode(BR_BRNCH_NM, AD_CMPNY);
            return adBranch.getBrCode();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    private String getAdBrNameByBrCode(Integer BR_CODE) {

        Debug.print("InvRepBranchStockTransferRegisterControllerBean getAdBrNameByBrCode");
        try {

            LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(BR_CODE);
            return adBranch.getBrName();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("InvRepBranchStockTransferRegisterControllerBean ejbCreate");
    }

}
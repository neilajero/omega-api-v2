/**
 * @copyright 2022 Omega Business Consulting, Inc.
 * @class InvRepStockCardControllerBean
 */
package com.ejb.txnreports.inv;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.gen.LocalGenValueSetValueHome;
import com.ejb.dao.inv.LocalInvCostingHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.gen.LocalGenValueSetValue;
import com.ejb.entities.inv.LocalInvCosting;
import com.ejb.entities.inv.LocalInvItemLocation;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.inv.InvRepStockCardDetails;

import jakarta.ejb.*;
import java.util.*;

@Stateless(name = "InvRepStockCardControllerEJB")
public class InvRepStockCardControllerBean extends EJBContextClass implements InvRepStockCardController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    LocalInvCostingHome invCostingHome;
    @EJB
    LocalGenValueSetValueHome genValueSetValueHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;

    public ArrayList executeInvRepStockCard(HashMap criteria, ArrayList branchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("InvRepStockCardControllerBean executeInvRepStockCard");
        
        ArrayList list = new ArrayList();
        try {

            StringBuffer jbossQl = new StringBuffer();
            jbossQl.append("SELECT OBJECT(cst) FROM InvCosting cst ");

            boolean firstArgument = true;

            Object[] obj = new Object[0];

            if (branchList.isEmpty()) {

                throw new GlobalNoRecordFoundException();

            } else {

                jbossQl.append("WHERE cst.cstAdBranch in (");

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

            if (criteria.containsKey("itemName")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.invItemLocation.invItem.iiName >= '").append(criteria.get("itemName")).append("' ");
            }

            if (criteria.containsKey("itemNameTo")) {
                Debug.print("itemNameTo");
                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.invItemLocation.invItem.iiName <= '").append(criteria.get("itemNameTo")).append("' ");
            }

            if (criteria.containsKey("category")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.invItemLocation.invItem.iiAdLvCategory='").append(criteria.get("category")).append("' ");
            }

            if (criteria.containsKey("location")) {

                String loc = (String) criteria.get("location");

                if (!loc.equalsIgnoreCase("ALL")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("cst.invItemLocation.invLocation.locName= '").append(criteria.get("location")).append("' ");
                }
            }

            List<Date> dateQuery = new ArrayList<>();
            int index = 1;

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                dateQuery.add((Date) criteria.get("dateFrom"));
                jbossQl.append("cst.cstDate>=?").append(++index).append(" ");
            }

            if (criteria.containsKey("dateTo")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                dateQuery.add((Date) criteria.get("dateTo"));
                jbossQl.append("cst.cstDate<=?").append(index).append(" ");
            }

            if (dateQuery.size() > 0) {
                obj = new Object[index];

                for (int x = 0; x < dateQuery.size(); x++) {
                    obj[x] = dateQuery.get(x);
                }
            }

            if (criteria.containsKey("itemClass")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("cst.invItemLocation.invItem.iiClass= '").append(criteria.get("itemClass")).append("' ");
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("cst.invItemLocation.invItem.iiNonInventoriable=0 AND cst.invItemLocation.invItem.iiEnable=1 AND cst.cstAdCompany=").append(AD_CMPNY).append(" ");

            jbossQl.append("ORDER BY cst.invItemLocation.invItem.iiName, cst.cstDate, cst.cstDateToLong, cst.cstLineNumber");

            Debug.print("InvRepStockCard QUERY : " + jbossQl);

            Collection invCostings = invCostingHome.getCstByCriteria(jbossQl.toString(), obj, 0, 0);

            for (Object costing : invCostings) {

                LocalInvCosting invCosting = (LocalInvCosting) costing;

                InvRepStockCardDetails details = new InvRepStockCardDetails();

                LocalInvCosting invBeginningCosting = this.getCstIlBeginningBalanceByItemLocationAndDate(invCosting.getInvItemLocation(), (Date) criteria.get("dateFrom"), invCosting.getCstAdBranch(), AD_CMPNY);

                details.setRscBeginningQuantity(invBeginningCosting != null ? invBeginningCosting.getCstRemainingQuantity() : 0);

                details.setRscItemName(invCosting.getInvItemLocation().getInvItem().getIiName() + "-" + invCosting.getInvItemLocation().getInvItem().getIiDescription());

                details.setRscUnit(invCosting.getInvItemLocation().getInvItem().getInvUnitOfMeasure().getUomName());

                details.setRscDate(invCosting.getCstDate());

                if (invCosting.getInvAdjustmentLine() != null) {

                    details.setRscAccount(invCosting.getInvAdjustmentLine().getInvAdjustment().getGlChartOfAccount().getCoaAccountDescription());

                    LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

                    // Specific to LBP company only
                    if (adCompany.getCmpShortName().equals("LBP")) {

                        Collection BrList = genValueSetValueHome.findByVsName("BRANCH", AD_CMPNY);

                        String sample = invCosting.getInvAdjustmentLine().getInvAdjustment().getGlChartOfAccount().getCoaAccountDescription();

                        for (Object o : BrList) {
                            LocalGenValueSetValue glvsv = (LocalGenValueSetValue) o;
                            if (sample.contains(glvsv.getVsvDescription() + "-")) {
                                Debug.print(glvsv.getVsvDescription());
                                sample = glvsv.getVsvDescription();
                                break;
                            }
                        }
                        details.setRscAccount(sample);
                    }
                }

                if (invCosting.getInvAdjustmentLine() != null) {

                    details.setRscDocumentNumber(invCosting.getInvAdjustmentLine().getInvAdjustment().getAdjDocumentNumber());
                    details.setRscReferenceNumber(invCosting.getInvAdjustmentLine().getInvAdjustment().getAdjReferenceNumber());

                } else if (invCosting.getInvStockTransferLine() != null) {

                    details.setRscDocumentNumber(invCosting.getInvStockTransferLine().getInvStockTransfer().getStDocumentNumber());
                    details.setRscReferenceNumber(invCosting.getInvStockTransferLine().getInvStockTransfer().getStReferenceNumber());
                    details.setRscSource("INV");

                } else if (invCosting.getInvBranchStockTransferLine() != null) {

                    details.setRscDocumentNumber(invCosting.getInvBranchStockTransferLine().getInvBranchStockTransfer().getBstNumber());
                    details.setRscReferenceNumber(invCosting.getInvBranchStockTransferLine().getInvBranchStockTransfer().getBstTransferOutNumber());
                    details.setRscSource("INV");

                } else if (invCosting.getApVoucherLineItem() != null) {

                    if (invCosting.getApVoucherLineItem().getApVoucher() != null) {

                        details.setRscDocumentNumber(invCosting.getApVoucherLineItem().getApVoucher().getVouDocumentNumber());
                        details.setRscReferenceNumber(invCosting.getApVoucherLineItem().getApVoucher().getVouReferenceNumber());
                        details.setRscSource("AP");

                    } else if (invCosting.getApVoucherLineItem().getApCheck() != null) {

                        details.setRscDocumentNumber(invCosting.getApVoucherLineItem().getApCheck().getChkDocumentNumber());
                        details.setRscReferenceNumber(invCosting.getApVoucherLineItem().getApCheck().getChkNumber());
                        details.setRscSource("AP");
                    }

                } else if (invCosting.getArInvoiceLineItem() != null) {

                    if (invCosting.getArInvoiceLineItem().getArInvoice() != null) {

                        details.setRscDocumentNumber(invCosting.getArInvoiceLineItem().getArInvoice().getInvNumber());
                        details.setRscReferenceNumber(invCosting.getArInvoiceLineItem().getArInvoice().getInvReferenceNumber());
                        details.setRscSource("AR");

                    } else if (invCosting.getArInvoiceLineItem().getArReceipt() != null) {

                        details.setRscDocumentNumber(invCosting.getArInvoiceLineItem().getArReceipt().getRctNumber());
                        details.setRscReferenceNumber(invCosting.getArInvoiceLineItem().getArReceipt().getRctReferenceNumber());
                        details.setRscSource("AR");
                    }

                } else if (invCosting.getApPurchaseOrderLine() != null) {

                    details.setRscDocumentNumber(invCosting.getApPurchaseOrderLine().getApPurchaseOrder().getPoDocumentNumber());
                    details.setRscReferenceNumber(invCosting.getApPurchaseOrderLine().getApPurchaseOrder().getPoReferenceNumber());
                    details.setRscSource("AP");

                } else if (invCosting.getArSalesOrderInvoiceLine() != null) {

                    details.setRscDocumentNumber(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getInvNumber());
                    details.setRscReferenceNumber(invCosting.getArSalesOrderInvoiceLine().getArInvoice().getInvReferenceNumber());
                    details.setRscSource("AR");
                }

                details.setRscLocation(invCosting.getInvItemLocation().getInvLocation().getLocDescription());
                details.setRscInQuantity(0d);
                details.setRscOutQuantity(0d);

                if (invCosting.getCstQuantityReceived() != 0d) {

                    if (invCosting.getCstQuantityReceived() > 0) {

                        details.setRscInQuantity(invCosting.getCstQuantityReceived());

                    } else if (invCosting.getCstQuantityReceived() < 0) {

                        details.setRscOutQuantity(invCosting.getCstQuantityReceived() * -1);
                    }

                } else if (invCosting.getCstAdjustQuantity() != 0d) {

                    if (invCosting.getCstAdjustQuantity() > 0) {

                        details.setRscInQuantity(invCosting.getCstAdjustQuantity());

                    } else if (invCosting.getCstAdjustQuantity() < 0) {

                        details.setRscOutQuantity(invCosting.getCstAdjustQuantity() * -1);
                    }

                } else if (invCosting.getCstQuantitySold() != 0d) {

                    if (invCosting.getCstQuantitySold() > 0) {

                        details.setRscOutQuantity(invCosting.getCstQuantitySold());

                    } else if (invCosting.getCstQuantitySold() < 0) {

                        details.setRscInQuantity(invCosting.getCstQuantitySold() * -1);
                    }
                }

                details.setRscRemainingQuantity(invCosting.getCstRemainingQuantity());

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

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("InvRepStockCardControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("InvRepStockCardControllerBean getAdCompany");
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

    // private method
    private LocalInvCosting getCstIlBeginningBalanceByItemLocationAndDate(LocalInvItemLocation invItemLocation, Date date, Integer AD_BRNCH, Integer AD_CMPNY) {

        Debug.print("InvRepStockCardControllerBean getCstIlBeginningBalanceByItemLocationAndDate");
        LocalInvCosting invCosting = null;
        try {

            GregorianCalendar calendar = new GregorianCalendar();

            if (date != null) {

                calendar.setTime(date);
                calendar.add(GregorianCalendar.DATE, -1);

                Date CST_DT = calendar.getTime();

                try {

                    invCosting = invCostingHome.getByMaxCstDateToLongAndMaxCstLineNumberAndLessThanEqualCstDateAndIiNameAndLocName(CST_DT, invItemLocation.getInvItem().getIiName(), invItemLocation.getInvLocation().getLocName(), AD_BRNCH, AD_CMPNY);

                } catch (FinderException ex) {

                }

                Debug.print("Date1 :" + date);
                Debug.print("Date2 :" + CST_DT);
            }

            return invCosting;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("InvRepStockCardControllerBean ejbCreate");
    }

}
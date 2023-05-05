package com.ejb.txnreports.ap;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.dao.ap.LocalApPurchaseOrderHome;
import com.ejb.dao.ap.LocalApPurchaseRequisitionHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.entities.ap.LocalApCanvass;
import com.ejb.entities.ap.LocalApPurchaseOrder;
import com.ejb.entities.ap.LocalApPurchaseRequisition;
import com.ejb.entities.ap.LocalApPurchaseRequisitionLine;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ap.ApRepPurchaseRequisitionRegisterDetails;

import jakarta.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

@Stateless(name = "ApRepPurchaseRequisitionRegisterControllerEJB")
public class ApRepPurchaseRequisitionRegisterControllerBean extends EJBContextClass
        implements ApRepPurchaseRequisitionRegisterController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalAdBranchHome adBranchHome;
    @EJB
    private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
    @EJB
    private LocalAdLookUpValueHome adLookUpValueHome;
    @EJB
    private LocalApPurchaseOrderHome apPurchaseOrderHome;
    @EJB
    private LocalApPurchaseRequisitionHome apPurchaseRequisitionHome;

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("ApRepPurchaseRequisitionRegisterControllerBean getAdCompany");

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

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepPurchaseRequisitionRegisterControllerBean getAdBrResAll");

        LocalAdBranchResponsibility adBranchResponsibility = null;
        LocalAdBranch adBranch = null;

        Collection adBranchResponsibilities = null;

        ArrayList list = new ArrayList();

        try {

            adBranchResponsibilities = adBranchResponsibilityHome.findByAdResponsibility(RS_CODE, AD_CMPNY);

        }
        catch (FinderException ex) {

        }
        catch (Exception ex) {

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

        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApRepPurchaseRequisitionRegisterControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList executeApRepPurchaseRequisitionRegister(HashMap criteria, String ORDER_BY, String GROUP_BY, boolean SHOW_LN_ITEMS, ArrayList adBrnchList, String REPORT_TYPE, Integer AD_CMPNY) throws GlobalNoRecordFoundException, FinderException {

        Debug.print("ApRepPurchaseRequisitionRegisterControllerBean executeApRepPurchaseRequisitionRegister");

        ArrayList list = new ArrayList();
        ArrayList listAdj = new ArrayList();


        switch (REPORT_TYPE) {
            case "RM_USED":

                return list;
            case "PROD_LIST":
                return list;

            case "RM_VAR":
                return list;
            default:
                try {
                    LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
                    StringBuilder jbossQl = new StringBuilder();
                    jbossQl.append("SELECT OBJECT(pr) FROM ApPurchaseRequisition pr ");
                    boolean firstArgument = true;
                    short ctr = 0;
                    int criteriaSize = criteria.size();
                    Object[] obj;

                    // Allocate the size of the object parameter
                    if (criteria.containsKey("includedUnposted")) {
                        criteriaSize--;
                    }
                    obj = new Object[criteriaSize];
                    if (criteria.containsKey("type")) {
                        if (!firstArgument) {
                            jbossQl.append("AND ");
                        } else {
                            firstArgument = false;
                            jbossQl.append("WHERE ");
                        }
                        jbossQl.append("pr.prType=?").append(ctr + 1).append(" ");
                        Debug.print("TYPE=" + criteria.get("type"));
                        obj[ctr] = criteria.get("type");
                        ctr++;
                    }
                    if (criteria.containsKey("accountDescription")) {
                        if (!firstArgument) {
                            jbossQl.append("AND ");
                        } else {
                            firstArgument = false;
                            jbossQl.append("WHERE ");
                        }
                        System.out.print("hhhhhererhere" + criteria.get("accountDescription"));
                        jbossQl.append("pr.glChartOfAccount.coaAccountNumber=?").append(ctr + 1).append(" ");
                        obj[ctr] = criteria.get("accountDescription");
                        ctr++;
                    }
                    if (criteria.containsKey("dateFrom")) {
                        if (!firstArgument) {
                            jbossQl.append("AND ");
                        } else {
                            firstArgument = false;
                            jbossQl.append("WHERE ");
                        }
                        jbossQl.append("pr.prDate>=?").append(ctr + 1).append(" ");
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
                        jbossQl.append("pr.prDate<=?").append(ctr + 1).append(" ");
                        obj[ctr] = criteria.get("dateTo");
                        ctr++;
                    }
                    if (criteria.containsKey("documentNumberFrom")) {
                        if (!firstArgument) {
                            jbossQl.append("AND ");
                        } else {
                            firstArgument = false;
                            jbossQl.append("WHERE ");
                        }
                        jbossQl.append("pr.prDocumentNumber>=?").append(ctr + 1).append(" ");
                        obj[ctr] = criteria.get("documentNumberFrom");
                        ctr++;
                    }
                    if (criteria.containsKey("documentNumberTo")) {
                        if (!firstArgument) {
                            jbossQl.append("AND ");
                        } else {
                            firstArgument = false;
                            jbossQl.append("WHERE ");
                        }
                        jbossQl.append("pr.prDocumentNumber<=?").append(ctr + 1).append(" ");
                        obj[ctr] = criteria.get("documentNumberTo");
                        ctr++;
                    }
                    if (criteria.containsKey("referenceNumber")) {
                        if (!firstArgument) {
                            jbossQl.append("AND ");
                        } else {
                            firstArgument = false;
                            jbossQl.append("WHERE ");
                        }
                        jbossQl.append("pr.prReferenceNumber>=?").append(ctr + 1).append(" ");
                        obj[ctr] = criteria.get("referenceNumber");
                        ctr++;
                    }
                    if (criteria.get("includedUnposted").equals("NO")) {
                        if (!firstArgument) {
                            jbossQl.append("AND ");
                        } else {
                            firstArgument = false;
                            jbossQl.append("WHERE ");
                        }
                        jbossQl.append("pr.prPosted = 1 ");
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
                        jbossQl.append("pr.prAdBranch in (");
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
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("pr.prAdCompany = ").append(AD_CMPNY).append(" ");
                    String orderBy = null;
                    if (ORDER_BY.equals("DATE")) {
                        orderBy = "pr.prDate";
                    } else if (ORDER_BY.equals("DOCUMENT NUMBER")) {
                        orderBy = "pr.prDocumentNumber";
                    }
                    if (orderBy != null) {
                        jbossQl.append("ORDER BY ").append(orderBy);
                    }
                    Collection apPurchaseRequisitions = null;
                    try {
                        Debug.print("jbossQl.toString()" + jbossQl);
                        apPurchaseRequisitions = apPurchaseRequisitionHome.getPrByCriteria(jbossQl.toString(), obj, 0, 0);
                        Debug.print("Purchase Requisition=" + apPurchaseRequisitions.size());
                    }
                    catch (FinderException ex) {
                    }
                    if (apPurchaseRequisitions.size() == 0) {
                        throw new GlobalNoRecordFoundException();
                    }
                    for (Object purchaseRequisition : apPurchaseRequisitions) {
                        LocalApPurchaseRequisition apPurchaseRequisition = (LocalApPurchaseRequisition) purchaseRequisition;
                        if (SHOW_LN_ITEMS) {
                            double AMOUNT = 0d;
                            Collection apPurchaseRequisitionLines = apPurchaseRequisition.getApPurchaseRequisitionLines();
                            for (Object purchaseRequisitionLine : apPurchaseRequisitionLines) {
                                LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = (LocalApPurchaseRequisitionLine) purchaseRequisitionLine;
                                ApRepPurchaseRequisitionRegisterDetails details = new ApRepPurchaseRequisitionRegisterDetails();
                                details.setPrDate(apPurchaseRequisition.getPrDate());
                                details.setPrDocumentNumber(apPurchaseRequisition.getPrNumber());
                                details.setPrReferenceNumber(apPurchaseRequisition.getPrReferenceNumber());
                                details.setPrType(apPurchaseRequisition.getPrType());
                                details.setPrDescription(apPurchaseRequisition.getPrDescription());
                                details.setPrItemName(apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiName());
                                details.setPrItemDesc(apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiDescription());

                                details.setPrDepartment(apPurchaseRequisition.getPrDepartment());

                                LocalApPurchaseOrder apPurchaseOrder;
                                try {
                                    apPurchaseOrder = apPurchaseOrderHome.findByPoReferenceNumber(apPurchaseRequisition.getPrCode().toString(), AD_CMPNY);
                                    details.setPrApSupplier(apPurchaseOrder.getApSupplier().getSplName());
                                    details.setPrApPoNumber(apPurchaseOrder.getPoDocumentNumber());
                                    details.setPrPoDate(apPurchaseOrder.getPoDate());
                                }
                                catch (FinderException ex) {
                                    details.setPrApSupplier("");
                                    details.setPrApPoNumber("");
                                }

                                details.setPrLocation(apPurchaseRequisitionLine.getInvItemLocation().getInvLocation().getLocName());
                                details.setPrQuantity(apPurchaseRequisitionLine.getPrlQuantity());
                                details.setPrUnit(apPurchaseRequisitionLine.getInvUnitOfMeasure().getUomName());
                                details.setPrUnitCost(apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiUnitCost());
                                details.setPrAccountDescription(null);


                                //get amount
                                AMOUNT += EJBCommon.roundIt(apPurchaseRequisitionLine.getPrlAmount(), this.getGlFcPrecisionUnit(AD_CMPNY));
                              /*
                               * removed due to getting amount from the unit cost of the item
                              AMOUNT = EJBCommon.roundIt(
                                  apPurchaseRequisitionLine.getPrlAmount()
                                      * apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiUnitCost(),
                                  this.getGlFcPrecisionUnit(AD_CMPNY));
                                  */
                                details.setPrAmount(AMOUNT);
                                details.setOrderBy(ORDER_BY);

                                // get branch information
                                LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(apPurchaseRequisition.getPrAdBranch());
                                details.setPrBranchCode(adBranch.getBrBranchCode());
                                details.setPrBranchName(adBranch.getBrName());

                                // get Status
                                byte isPosted = apPurchaseRequisition.getPrPosted();
                                String status = apPurchaseRequisition.getPrApprovalStatus();


                                if (status == null) {
                                    status = "DRAFT";
                                } else if (status.equals("N/A") || status.equals("APPROVED")) {
                                    status = "POSTED";
                                }

                                if (isPosted == EJBCommon.FALSE) {
                                    status = "DRAFT";
                                }
                                details.setPrStatus(status);


                                // get Supplier
                                String supplier = this.getSupplierFronApCanvasses(apPurchaseRequisitionLine.getApCanvasses());
                                details.setPrApSupplier(supplier);

                                list.add(details);
                            }
                        } else {

                            ApRepPurchaseRequisitionRegisterDetails details = new ApRepPurchaseRequisitionRegisterDetails();
                            details.setPrDate(apPurchaseRequisition.getPrDate());
                            details.setPrDocumentNumber(apPurchaseRequisition.getPrNumber());
                            details.setPrReferenceNumber(apPurchaseRequisition.getPrReferenceNumber());
                            details.setPrType(apPurchaseRequisition.getPrType());
                            details.setPrDescription(apPurchaseRequisition.getPrDescription());
                            details.setPrAccountDescription(null);
                            details.setPrDepartment(apPurchaseRequisition.getPrDepartment());
                            double AMOUNT = 0d;


                            Collection apPurchaseRequisitionLines = apPurchaseRequisition.getApPurchaseRequisitionLines();
                            for (Object purchaseRequisitionLine : apPurchaseRequisitionLines) {
                                LocalApPurchaseRequisitionLine apPurchaseRequisitionLine = (LocalApPurchaseRequisitionLine) purchaseRequisitionLine;
                  
                              /*
                               * remove due to gettting amount from unit cost of the item
                              AMOUNT += EJBCommon.roundIt(
                                  apPurchaseRequisitionLine.getPrlQuantity()
                                      * apPurchaseRequisitionLine.getInvItemLocation().getInvItem().getIiUnitCost(),
                                  this.getGlFcPrecisionUnit(AD_CMPNY));
                             */

                                //get amount
                                AMOUNT += EJBCommon.roundIt(apPurchaseRequisitionLine.getPrlAmount(), this.getGlFcPrecisionUnit(AD_CMPNY));

                                // get Supplier
                                String supplier = this.getSupplierFronApCanvasses(apPurchaseRequisitionLine.getApCanvasses());
                                details.setPrApSupplier(supplier);


                            }
                            details.setPrAmount(AMOUNT);
                            details.setOrderBy(ORDER_BY);

                            // get branch information
                            LocalAdBranch adBranch = adBranchHome.findByPrimaryKey(apPurchaseRequisition.getPrAdBranch());
                            details.setPrBranchCode(adBranch.getBrBranchCode());
                            details.setPrBranchName(adBranch.getBrName());

                            // get Status
                            byte isPosted = apPurchaseRequisition.getPrPosted();
                            String status = apPurchaseRequisition.getPrApprovalStatus();

                            if (status == null) {
                                status = "DRAFT";
                            } else if (status.equals("N/A") || status.equals("APPROVED")) {
                                status = "POSTED";
                            }

                            if (isPosted == EJBCommon.FALSE) {
                                status = "DRAFT";
                            }

                            details.setPrStatus(status);


                            list.add(details);
                        }
                    }

                    // sort

                    if (GROUP_BY.equals("ITEM NAME")) {
                        list.sort(ApRepPurchaseRequisitionRegisterDetails.ItemNameComparator);
                    } else if (GROUP_BY.equals("DATE")) {
                        list.sort(ApRepPurchaseRequisitionRegisterDetails.DateComparator);
                    } else {
                        list.sort(ApRepPurchaseRequisitionRegisterDetails.NoGroupComparator);
                    }
                    Debug.print("3");
                    return list;
                }
                catch (GlobalNoRecordFoundException ex) {
                    throw ex;
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    throw new EJBException(ex.getMessage());
                }
        }
    }

    // private methods
    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

        Debug.print("ApRepPurchaseRequisitionRegisterControllerBean convertForeignToFunctionalCurrency");

        LocalAdCompany adCompany = null;

        // get company and extended precision

        try {

            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

        }
        catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Convert to functional currency if necessary

        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {

            AMOUNT = AMOUNT * CONVERSION_RATE;
        }

        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    public ArrayList getAdLvPurchaseRequisitionType(Integer AD_CMPNY) {

        Debug.print("ApRepPurchaseRequisitionRegisterControllerBean getAdLvPurchaseRequisitionType");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("PR TYPE", AD_CMPNY);

            for (Object lookUpValue : adLookUpValues) {

                LocalAdLookUpValue adLookUpValue = (LocalAdLookUpValue) lookUpValue;

                list.add(adLookUpValue.getLvName());
            }

            return list;

        }
        catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public String getSupplierFronApCanvasses(Collection apCanvasses) {

        String supplier = "";

        for (Object canvass : apCanvasses) {
            LocalApCanvass apCanvass = (LocalApCanvass) canvass;

            if (apCanvass.getCnvPo() == EJBCommon.FALSE) {
                continue;
            }

            if (apCanvass.getApSupplier() != null) {

                supplier = apCanvass.getApSupplier().getSplName();

            }

        }

        return supplier;

    }

    public void ejbCreate() throws CreateException {

        Debug.print("ApRepPurchaseRequisitionControllerBean ejbCreate");
    }

}
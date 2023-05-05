package com.ejb.txnreports.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.*;
import com.ejb.dao.ar.*;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.entities.ar.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ar.ArRepJobStatusReportDetails;

import jakarta.ejb.*;

import java.util.*;

@Stateless(name = "ArRepJobStatusReportControllerEJB")
public class ArRepJobStatusReportControllerBean extends EJBContextClass implements ArRepJobStatusReportController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalArJobOrderHome arJobOrderHome;
    @EJB
    private LocalArJobOrderTypeHome arJobOrderTypeHome;
    @EJB
    private LocalArPersonelHome arPersonelHome;
    @EJB
    private LocalArPersonelTypeHome arPersonelTypeHome;

    public ArrayList executeArRepJobStatusReport(HashMap criteria, ArrayList branchList, String personelName, String invoiceStatus, String ORDER_BY, String GROUP_BY, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArRepJobStatusReportControllerBean executeArRepJobStatusReport");

        ArrayList list = new ArrayList();

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            StringBuilder jbossQl = new StringBuilder();
            Iterator brIter = null;
            AdBranchDetails details = null;
            Object[] obj;
            Collection arJobOrderLines = null;

            if (criteria.containsKey("jobOrderType")) {

                criteriaSize--;
            }

            if (criteria.containsKey("jobOrderStatus")) {

                criteriaSize--;
            }

            if (criteria.containsKey("personelType")) {

                criteriaSize--;
            }

            if (criteria.containsKey("customerCode")) {

                criteriaSize--;
            }

            if (criteria.get("approvalStatus").equals("")) {

                criteriaSize--;
            }

            // if (((String)criteria.get("orderStatus")).equals("")) {

            criteriaSize--;
            criteriaSize--;

            // }

            jbossQl.append("SELECT OBJECT(jol) FROM ArJobOrderLine jol ");

            // Allocate the size of the object parameter

            obj = new Object[criteriaSize];

            if (criteria.containsKey("jobOrderType")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jol.arJobOrder.arJobOrderType.jotName LIKE '%").append(criteria.get("jobOrderType")).append("%' ");
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("jobOrderStatus")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jol.arJobOrder.joJobOrderStatus = '").append(criteria.get("jobOrderStatus")).append("' ");
            }

            if (criteria.containsKey("customerCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jol.arJobOrder.arCustomer.cstCustomerCode LIKE '%").append(criteria.get("customerCode")).append("%' ");
            }

            if (criteria.containsKey("customerType")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jol.arJobOrder.arCustomer.arCustomerType.ctName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerType");
                ctr++;
            }

            if (criteria.containsKey("customerBatch")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jol.arJobOrder.arCustomer.cstCustomerBatch=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerBatch");
                ctr++;
            }

            if (criteria.containsKey("customerClass")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jol.arJobOrder.arCustomer.arCustomerClass.ccName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerClass");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("jol.arJobOrder.joDate>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("jol.arJobOrder.joDate<=?").append(ctr + 1).append(" ");
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
                jbossQl.append("jol.arJobOrder.joDocumentNumber>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("jol.arJobOrder.joDocumentNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberTo");
                ctr++;
            }

            if (criteria.containsKey("referenceNumberFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("jol.arJobOrder.joReferenceNumber>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("referenceNumberFrom");
                ctr++;
            }

            if (criteria.containsKey("referenceNumberTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("jol.arJobOrder.joReferenceNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("referenceNumberTo");
                ctr++;
            }

            if (criteria.containsKey("approvalStatus")) {

                if (criteria.get("approvalStatus").equals("N/A") || criteria.get("approvalStatus").equals("PENDING") || criteria.get("approvalStatus").equals("APPROVED")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    jbossQl.append("jol.arJobOrder.joApprovalStatus=?").append(ctr + 1).append(" ");
                    obj[ctr] = criteria.get("approvalStatus");
                    ctr++;
                }
            }

            if (criteria.containsKey("includedUnposted")) {

                String unposted = (String) criteria.get("includedUnposted");

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                if (unposted.equals("NO")) {

                    jbossQl.append("jol.arJobOrder.joPosted = 1 ");

                } else {

                    jbossQl.append("jol.arJobOrder.joVoid = 0 ");
                }
            }

            if (criteria.containsKey("orderStatus")) {
                Debug.print("ORDER STATUS : " + criteria.get("orderStatus"));

                if (!criteria.get("orderStatus").equals("")) {
                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }

                    if (criteria.get("orderStatus").equals("Good")) {
                        jbossQl.append("jol.arJobOrder.joOrderStatus = 'Good' ");
                    } else {
                        // Debug.print("ORDER STATUS : " + (String)criteria.get("orderStatus"));
                        jbossQl.append("jol.arJobOrder.joOrderStatus LIKE 'Bad%' ");
                    }
                }
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("jol.arJobOrder.joAdCompany=").append(AD_CMPNY).append(" ");

            Debug.print("jbossQl-" + jbossQl);
            Debug.print("obj size :" + obj.length);
            arJobOrderLines = arJobOrderHome.getJOByCriteria(jbossQl.toString(), obj, 0, 0);

            double ORDER_AMOUNT = 0d;
            double ORDER_TAX_AMOUNT = 0d;
            double ORDER_QTY = 0d;
            double INVOICE_QTY = 0d;
            Integer JO_CODE = 0;

            if (!arJobOrderLines.isEmpty()) {

                for (Object jobOrderLine : arJobOrderLines) {

                    LocalArJobOrderLine arJobOrderLine = (LocalArJobOrderLine) jobOrderLine;

                    ArRepJobStatusReportDetails mdetails = new ArRepJobStatusReportDetails();

                    LocalArJobOrder arJobOrder = arJobOrderLine.getArJobOrder();

                    if (!Objects.equals(JO_CODE, arJobOrder.getJoCode())) {

                        ORDER_AMOUNT = 0d;
                        ORDER_TAX_AMOUNT = 0d;
                        ORDER_QTY = 0d;
                        INVOICE_QTY = 0d;

                        JO_CODE = arJobOrder.getJoCode();
                    }

                    mdetails.setJoDate(arJobOrder.getJoDate());
                    mdetails.setJoType(arJobOrder.getArJobOrderType() == null ? null : arJobOrder.getArJobOrderType().getJotName());
                    mdetails.setJoDocumentNumber(arJobOrder.getJoDocumentNumber());
                    mdetails.setJoReferenceNumber(arJobOrder.getJoReferenceNumber());
                    mdetails.setJoDescription(arJobOrder.getJoDescription());
                    mdetails.setJoCstCustomerCode(arJobOrder.getArCustomer().getCstCustomerCode() + "-" + arJobOrder.getArCustomer().getCstName());
                    mdetails.setJoCstCustomerClass(arJobOrder.getArCustomer().getArCustomerClass().getCcName());
                    mdetails.setJoCstCustomerCode2(arJobOrder.getArCustomer().getCstCustomerCode());
                    mdetails.setJoJobOrderStatus(arJobOrder.getJoJobOrderStatus());
                    mdetails.setJoSlsSalespersonCode(arJobOrder.getArSalesperson() != null ? arJobOrder.getArSalesperson().getSlpSalespersonCode() : null);

                    mdetails.setJoSlsName(arJobOrder.getArSalesperson() != null ? arJobOrder.getArSalesperson().getSlpName() : null);

                    // select customer type
                    if (arJobOrder.getArCustomer().getArCustomerType() == null) {

                        mdetails.setJoCstCustomerType("UNDEFINED");

                    } else {

                        mdetails.setJoCstCustomerType(arJobOrder.getArCustomer().getArCustomerType().getCtName());
                    }

                    ORDER_QTY += arJobOrderLine.getJolQuantity();

                    if (arJobOrderLine.getArJobOrder().getArTaxCode().getTcType().equals("INCLUSIVE")) {

                        ORDER_TAX_AMOUNT += arJobOrderLine.getJolAmount() - EJBCommon.roundIt(arJobOrderLine.getJolAmount() / (1 + (arJobOrderLine.getArJobOrder().getArTaxCode().getTcRate() / 100)), adCompany.getGlFunctionalCurrency().getFcPrecision());
                        ORDER_AMOUNT += arJobOrderLine.getJolAmount();

                    } else if (arJobOrderLine.getArJobOrder().getArTaxCode().getTcType().equals("arJobOrderLine")) {

                        // tax exclusive, none, zero rated or exempt

                        ORDER_TAX_AMOUNT += EJBCommon.roundIt(arJobOrderLine.getJolAmount() * arJobOrderLine.getArJobOrder().getArTaxCode().getTcRate() / 100, adCompany.getGlFunctionalCurrency().getFcPrecision());
                        ORDER_AMOUNT += arJobOrderLine.getJolAmount() + ORDER_TAX_AMOUNT;

                    } else {

                        ORDER_AMOUNT += arJobOrderLine.getJolAmount();
                    }

                    mdetails.setJoOrderQty(ORDER_QTY);
                    mdetails.setJoAmount(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arJobOrder.getGlFunctionalCurrency().getFcCode(), arJobOrder.getGlFunctionalCurrency().getFcName(), arJobOrder.getJoConversionDate(), arJobOrder.getJoConversionRate(), ORDER_AMOUNT, AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));

                    mdetails.setJoTaxAmount(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arJobOrder.getGlFunctionalCurrency().getFcCode(), arJobOrder.getGlFunctionalCurrency().getFcName(), arJobOrder.getJoConversionDate(), arJobOrder.getJoConversionRate(), ORDER_TAX_AMOUNT, AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                    mdetails.setOrderBy(ORDER_BY);
                    mdetails.setJoOrderStatus(arJobOrder.getJoOrderStatus());
                    mdetails.setJoApprovalStatus(arJobOrder.getJoApprovalStatus());
                    mdetails.setJoApprovedRejectedBy(arJobOrder.getJoApprovedRejectedBy());

                    mdetails.setJolIIName(arJobOrderLine.getInvItemLocation().getInvItem().getIiName());
                    mdetails.setJolDescription(arJobOrderLine.getInvItemLocation().getInvItem().getIiDescription());
                    mdetails.setJolQuantity(arJobOrderLine.getJolQuantity());
                    mdetails.setJolUnitPrice(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arJobOrder.getGlFunctionalCurrency().getFcCode(), arJobOrder.getGlFunctionalCurrency().getFcName(), arJobOrder.getJoConversionDate(), arJobOrder.getJoConversionRate(), arJobOrderLine.getJolUnitPrice(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                    mdetails.setJolAmount(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arJobOrder.getGlFunctionalCurrency().getFcCode(), arJobOrder.getGlFunctionalCurrency().getFcName(), arJobOrder.getJoConversionDate(), arJobOrder.getJoConversionRate(), arJobOrderLine.getJolAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                    mdetails.setJolUnit(arJobOrderLine.getInvUnitOfMeasure().getUomName());

                    if (arJobOrderLine.getArJobOrderAssignments().size() > 0) {
                        Collection arJobOrderAssignments = arJobOrderLine.getArJobOrderAssignments();

                        Iterator jai = arJobOrderAssignments.iterator();

                        boolean isFirst = true;
                        while (jai.hasNext()) {

                            LocalArJobOrderAssignment arJobOrderAssignment = (LocalArJobOrderAssignment) jai.next();

                            ArRepJobStatusReportDetails mdetails2 = (ArRepJobStatusReportDetails) mdetails.clone();
                            isFirst = false;
                            if (!isFirst) {
                                mdetails.setJolIIName(null);
                                mdetails.setJolDescription(null);
                                mdetails.setJolQuantity(0);
                                mdetails.setJolUnitPrice(0);
                                mdetails.setJolAmount(0);
                                mdetails.setJolUnit(null);
                            }

                            if (arJobOrderAssignment.getJaSo() == EJBCommon.FALSE) {
                                continue;
                            }

                            if (!personelName.equals("") && !personelName.equals(arJobOrderAssignment.getArPersonel().getPeName())) {
                                continue;
                            }
                            mdetails2.setJaPersonelCode(arJobOrderAssignment.getArPersonel().getPeIdNumber());
                            mdetails2.setJaPersonelName(arJobOrderAssignment.getArPersonel().getPeName());
                            mdetails2.setJaQuantity(arJobOrderAssignment.getJaQuantity());
                            mdetails2.setJaUnitCost(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arJobOrder.getGlFunctionalCurrency().getFcCode(), arJobOrder.getGlFunctionalCurrency().getFcName(), arJobOrder.getJoConversionDate(), arJobOrder.getJoConversionRate(), arJobOrderAssignment.getJaUnitCost(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                            mdetails2.setJaAmount(EJBCommon.roundIt(this.convertForeignToFunctionalCurrency(arJobOrder.getGlFunctionalCurrency().getFcCode(), arJobOrder.getGlFunctionalCurrency().getFcName(), arJobOrder.getJoConversionDate(), arJobOrder.getJoConversionRate(), arJobOrderAssignment.getJaAmount(), AD_CMPNY), adCompany.getGlFunctionalCurrency().getFcPrecision()));
                            mdetails2.setJaRemarks(arJobOrderAssignment.getJaRemarks());

                            list.add(mdetails2);
                        }

                    } else {

                        if (!personelName.equals("")) {
                            continue;
                        }
                        mdetails.setJaPersonelCode(null);
                        mdetails.setJaPersonelName(null);
                        mdetails.setJaQuantity(0);
                        mdetails.setJaUnitCost(0);
                        mdetails.setJaAmount(0);
                        mdetails.setJaRemarks(null);
                        list.add(mdetails);
                    }
                }
            }

            if (list.isEmpty() || list.size() == 0) {

                throw new GlobalNoRecordFoundException();
            }

            // sort

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {
        Debug.print("ArRepJobStatusReportControllerBean getAdCompany");
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
            throw new EJBException(ex.getMessage());
        }
    }

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {
        Debug.print("ArRepJobStatusReportControllerBean convertForeignToFunctionalCurrency");
        LocalAdCompany adCompany = null;
        // get company and extended precision
        try {
            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        // Convert to functional currency if necessary
        if (CONVERSION_RATE != 1 && CONVERSION_RATE != 0) {
            AMOUNT = AMOUNT / CONVERSION_RATE;
        }
        return EJBCommon.roundIt(AMOUNT, adCompany.getGlFunctionalCurrency().getFcPrecision());
    }

    public ArrayList getAllJobOrderTypeName(Integer AD_CMPNY) {
        Debug.print("ArRepPersonelSummaryControllerBean getAllJobOrderType");
        try {
            Collection arJobOrderTypes = arJobOrderTypeHome.findJotAll(AD_CMPNY);
            ArrayList list = new ArrayList();
            for (Object jobOrderType : arJobOrderTypes) {
                LocalArJobOrderType arJobOrderType = (LocalArJobOrderType) jobOrderType;
                list.add(arJobOrderType.getJotName());
            }
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArPeAll(Integer AD_BRNCH, Integer AD_CMPNY) {
        Debug.print("ArRepPersonelSummaryControllerBean getApSplAll");
        ArrayList list = new ArrayList();
        try {
            Collection arPersonels = arPersonelHome.findPeAll(AD_CMPNY);
            for (Object personel : arPersonels) {
                LocalArPersonel arPersonel = (LocalArPersonel) personel;
                list.add(arPersonel.getPeName());
            }
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArPtAll(Integer AD_CMPNY) {
        Debug.print("ArRepJobStatusReportControllerBean getArPtAll");
        ArrayList list = new ArrayList();
        try {
            Collection arPersonelTypes = arPersonelTypeHome.findPtAll(AD_CMPNY);
            for (Object personelType : arPersonelTypes) {
                LocalArPersonelType arPersonelType = (LocalArPersonelType) personelType;
                list.add(arPersonelType.getPtName());
            }
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {
        Debug.print("ArRepJobStatusReportControllerBean ejbCreate");
    }

}
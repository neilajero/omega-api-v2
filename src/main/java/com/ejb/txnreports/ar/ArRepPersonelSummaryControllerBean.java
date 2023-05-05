package com.ejb.txnreports.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.dao.ar.*;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ar.*;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ar.ArRepPersonelSummaryDetails;

import jakarta.ejb.*;

import java.util.*;

@Stateless(name = "ArRepPersonelSummaryControllerEJB")
public class ArRepPersonelSummaryControllerBean extends EJBContextClass implements ArRepPersonelSummaryController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalArJobOrderTypeHome arJobOrderTypeHome;
    @EJB
    private LocalArPersonelHome arPersonelHome;
    @EJB
    private LocalArJobOrderHome arJobOrderHome;

    public ArrayList executeArRepPersonelSummary(HashMap criteria, ArrayList branchList, String personelName, String GROUP_BY, boolean includeUnposted, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArRepPersonelSummaryControllerBean executeArRepSalesperson");

        ArrayList list = new ArrayList();
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();
            String PYMNT_STTS = null;

            StringBuffer jbossQl = new StringBuffer();
            jbossQl.append("SELECT OBJECT(jo) FROM ArJobOrder jo WHERE (");

            if (branchList.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }

            Iterator brIter = branchList.iterator();

            AdBranchDetails details = (AdBranchDetails) brIter.next();
            jbossQl.append(" jo.joAdBranch=").append(details.getBrCode());

            while (brIter.hasNext()) {

                details = (AdBranchDetails) brIter.next();

                jbossQl.append(" OR jo.joAdBranch=").append(details.getBrCode());
            }

            //  jbossQl.append(") AND inv.arSalesperson.slpSalespersonCode IS NOT NULL ");
            jbossQl.append(") ");

            firstArgument = false;

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("jobOrderType")) {

                criteriaSize--;
            }

            if (criteria.containsKey("customerCode")) {

                criteriaSize--;
            }

            if (criteria.containsKey("paymentStatus")) {

                criteriaSize--;
                PYMNT_STTS = (String) criteria.get("paymentStatus");
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("jobOrderType")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jo.arJobOrderType.jotName = '").append(criteria.get("jobOrderType")).append("' ");
            }

            if (criteria.containsKey("customerCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jo.arCustomer.cstCustomerCode LIKE '%").append(criteria.get("customerCode")).append("%' ");
            }

            if (criteria.containsKey("customerClass")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jo.arCustomer.arCustomerClass.ccName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerClass");
                ctr++;
            }

            if (criteria.containsKey("customerType")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("jo.arCustomer.arCustomerType.ctName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerType");
                ctr++;
            }

            if (criteria.containsKey("dateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("jo.joDate>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("jo.joDate<=?").append(ctr + 1).append(" ");
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
                jbossQl.append("jo.joDocumentNumber>=?").append(ctr + 1).append(" ");
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
                jbossQl.append("jo.joDocumentNumber<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("documentNumberTo");
                ctr++;
            }

            if (!includeUnposted) {
                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append(" jo.joPosted = 1 ");
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append(" jo.joVoid = 0 AND jo.joAdCompany=").append(AD_CMPNY).append(" ORDER BY jo.joDocumentNumber");
            Collection arJobOrders = arJobOrderHome.getJOByCriteria(jbossQl.toString(), obj, 0, 0);

            double JO_AMOUNT = 0;
            double JO_QTY = 0;
            double JA_AMOUNT = 0;
            double JA_QTY = 0;
            Integer JO_CODE = 0;

            if (!arJobOrders.isEmpty()) {

                for (Object jobOrder : arJobOrders) {

                    LocalArJobOrder arJobOrder = (LocalArJobOrder) jobOrder;

                    ArRepPersonelSummaryDetails mdetails = new ArRepPersonelSummaryDetails();

                    JO_AMOUNT = 0;
                    JO_QTY = 0;

                    mdetails.setJoCode(arJobOrder.getJoCode());
                    mdetails.setJoDate(arJobOrder.getJoDate());
                    mdetails.setJoType(arJobOrder.getArJobOrderType().getJotName());
                    mdetails.setJoDocumentNumber(arJobOrder.getJoDocumentNumber());
                    mdetails.setJoCstCustomerCode(arJobOrder.getArCustomer().getCstCustomerCode());
                    mdetails.setJoCstCustomerCode(arJobOrder.getArCustomer().getCstName());
                    mdetails.setJoCstCustomerClass(arJobOrder.getArCustomer().getArCustomerClass() != null ? arJobOrder.getArCustomer().getArCustomerClass().getCcName() : null);
                    mdetails.setJoCstCustomerClass(arJobOrder.getArCustomer().getArCustomerType() != null ? arJobOrder.getArCustomer().getArCustomerType().getCtName() : null);
                    // amount

                    for (Object o : arJobOrder.getArJobOrderLines()) {
                        LocalArJobOrderLine arJobOrderLine = (LocalArJobOrderLine) o;

                        if (arJobOrderLine.getArJobOrderAssignments().size() <= 0) {
                            continue;
                        }

                        JO_QTY += arJobOrderLine.getJolQuantity();
                        JO_AMOUNT += arJobOrderLine.getJolAmount();

                        Iterator jai = arJobOrderLine.getArJobOrderAssignments().iterator();
                        JA_AMOUNT = 0;
                        JA_QTY = 0;
                        while (jai.hasNext()) {
                            LocalArJobOrderAssignment arJobOrderAssignment = (LocalArJobOrderAssignment) jai.next();

                            Debug.print("personel name: " + personelName);
                            if (!personelName.equals("") && !personelName.equals(arJobOrderAssignment.getArPersonel().getPeName())) {
                                continue;
                            }

                            ArRepPersonelSummaryDetails mdetails2 = (ArRepPersonelSummaryDetails) mdetails.clone();

                            mdetails2.setJaCode(arJobOrderAssignment.getJaCode());
                            mdetails2.setJaPersonelCode(arJobOrderAssignment.getArPersonel().getPeIdNumber());
                            mdetails2.setJaPersonelName(arJobOrderAssignment.getArPersonel().getPeName());
                            mdetails2.setJaQuantity(arJobOrderAssignment.getJaQuantity());
                            mdetails2.setJaUnitCost(arJobOrderAssignment.getJaUnitCost());
                            mdetails2.setJaAmount(arJobOrderAssignment.getJaAmount());

                            mdetails2.setJoAmount(JO_AMOUNT);
                            mdetails2.setJoQuantity(JO_QTY);

                            list.add(mdetails2);
                        }
                    }
                }
            }

            Debug.print("size is: " + list.size());
            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {
        Debug.print("ArRepSalespersonControllerBean getAdCompany");
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

            Debug.printStackTrace(ex);
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

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ArRepPersonelSummaryControllerBean ejbCreate");
    }

}
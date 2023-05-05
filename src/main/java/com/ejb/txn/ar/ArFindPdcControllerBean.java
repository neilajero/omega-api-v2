/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class ArFindPdcControllerBean
 * @created June 30, 2005 11:58 AM
 * @author Enrico C. Yap
 */
package com.ejb.txn.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.dao.ar.LocalArPdcHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.entities.ar.LocalArPdc;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.*;
import com.util.mod.ar.ArModPdcDetails;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Stateless(name = "ArFindPdcControllerEJB")
public class ArFindPdcControllerBean extends EJBContextClass implements ArFindPdcController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalAdPreferenceHome adPreferenceHome;
    @EJB
    private LocalArPdcHome arPdcHome;

    public byte getAdPrfEnableInvShift(Integer AD_CMPNY) {

        Debug.print("ArFindPdcControllerBean getAdPrfEnableInvShift");

        try {
            LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
            return adPreference.getPrfInvEnableShift();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getArPdcByCriteria(HashMap criteria, Integer OFFSET, Integer LIMIT, String ORDER_BY, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ArFindPdcControllerBean getArPdcByCriteria");
        
        ArrayList list = new ArrayList();
        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(pdc) FROM ArPdc pdc ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("referenceNumber")) {

                criteriaSize--;
            }

            if (criteria.containsKey("checkNumber")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("referenceNumber")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pdc.pdcReferenceNumber LIKE '%").append(criteria.get("referenceNumber")).append("%' ");
            }

            if (criteria.containsKey("checkNumber")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pdc.pdcCheckNumber LIKE '%").append(criteria.get("checkNumber")).append("%' ");
            }

            if (criteria.containsKey("shift")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pdc.pdcLvShift=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("shift");
                ctr++;
            }

            if (criteria.containsKey("customerCode")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pdc.arCustomer.cstCustomerCode=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerCode");
                ctr++;
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("pdc.pdcCancelled=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("cancelled");
            ctr++;

            if (criteria.containsKey("dateReceivedFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("pdc.pdcDateReceived>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateReceivedFrom");
                ctr++;
            }

            if (criteria.containsKey("dateReceivedTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("pdc.pdcDateReceived<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateReceivedTo");
                ctr++;
            }

            if (criteria.containsKey("maturityDateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("pdc.pdcMaturityDate>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("maturityDateFrom");
                ctr++;
            }

            if (criteria.containsKey("maturityDateTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("pdc.pdcMaturityDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("maturityDateFrom");
                ctr++;
            }

            if (criteria.containsKey("posted")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pdc.pdcPosted=?").append(ctr + 1).append(" ");

                String posted = (String) criteria.get("posted");

                if (posted.equals("YES")) {

                    obj[ctr] = EJBCommon.TRUE;

                } else {

                    obj[ctr] = EJBCommon.FALSE;
                }

                ctr++;
            }

            if (criteria.containsKey("status")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pdc.pdcStatus=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("status");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("pdc.pdcAdBranch=").append(AD_BRNCH).append(" AND pdc.pdcAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            if (ORDER_BY.equals("CUSTOMER CODE")) {

                orderBy = "pdc.arCustomer.cstCustomerCode";

            } else if (ORDER_BY.equals("CHECK NUMBER")) {

                orderBy = "pdc.pdcCheckNumber";
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy).append(", pdc.pdcDateReceived");

            } else {

                jbossQl.append("ORDER BY pdc.pdcDateReceived");
            }

            Collection arPdcs = arPdcHome.getPdcByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            if (arPdcs.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            for (Object pdc : arPdcs) {

                LocalArPdc arPdc = (LocalArPdc) pdc;

                ArModPdcDetails mdetails = new ArModPdcDetails();
                mdetails.setPdcCode(arPdc.getPdcCode());
                mdetails.setPdcCstCustomerCode(arPdc.getArCustomer().getCstCustomerCode());
                mdetails.setPdcDateReceived(arPdc.getPdcDateReceived());
                mdetails.setPdcMaturityDate(arPdc.getPdcMaturityDate());
                mdetails.setPdcCheckNumber(arPdc.getPdcCheckNumber());
                mdetails.setPdcReferenceNumber(arPdc.getPdcReferenceNumber());
                mdetails.setPdcAmount(arPdc.getPdcAmount());

                list.add(mdetails);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer getArPdcSizeByCriteria(HashMap criteria, Integer AD_BRNCH, Integer AD_CMPNY) throws GlobalNoRecordFoundException {
        Debug.print("ArFindPdcControllerBean getArPdcSizeByCriteria");
        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(pdc) FROM ArPdc pdc ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("referenceNumber")) {

                criteriaSize--;
            }

            if (criteria.containsKey("checkNumber")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("referenceNumber")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pdc.pdcReferenceNumber LIKE '%").append(criteria.get("referenceNumber")).append("%' ");
            }

            if (criteria.containsKey("checkNumber")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pdc.pdcCheckNumber LIKE '%").append(criteria.get("checkNumber")).append("%' ");
            }

            if (criteria.containsKey("shift")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pdc.pdcLvShift=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("shift");
                ctr++;
            }

            if (criteria.containsKey("customerCode")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pdc.arCustomer.cstCustomerCode=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("customerCode");
                ctr++;
            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("pdc.pdcCancelled=?").append(ctr + 1).append(" ");
            obj[ctr] = criteria.get("cancelled");
            ctr++;

            if (criteria.containsKey("dateReceivedFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("pdc.pdcDateReceived>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateReceivedFrom");
                ctr++;
            }

            if (criteria.containsKey("dateReceivedTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("pdc.pdcDateReceived<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("dateReceivedTo");
                ctr++;
            }

            if (criteria.containsKey("maturityDateFrom")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("pdc.pdcMaturityDate>=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("maturityDateFrom");
                ctr++;
            }

            if (criteria.containsKey("maturityDateTo")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("pdc.pdcMaturityDate<=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("maturityDateFrom");
                ctr++;
            }

            if (criteria.containsKey("posted")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pdc.pdcPosted=?").append(ctr + 1).append(" ");

                String posted = (String) criteria.get("posted");

                if (posted.equals("YES")) {

                    obj[ctr] = EJBCommon.TRUE;

                } else {

                    obj[ctr] = EJBCommon.FALSE;
                }

                ctr++;
            }

            if (criteria.containsKey("status")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("pdc.pdcStatus=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("status");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("pdc.pdcAdBranch=").append(AD_BRNCH).append(" AND pdc.pdcAdCompany=").append(AD_CMPNY).append(" ");

            Collection arPdcs = arPdcHome.getPdcByCriteria(jbossQl.toString(), obj, 0, 0);

            if (arPdcs.size() == 0) {
                throw new GlobalNoRecordFoundException();
            }

            return arPdcs.size();

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {
        Debug.print("ArFindPdcControllerBean getGlFcPrecisionUnit");
        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods
    public void ejbCreate() throws CreateException {

        Debug.print("ArFindPdcControllerBean ejbCreate");
    }

}
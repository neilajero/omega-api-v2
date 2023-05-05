package com.ejb.txn.ap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ap.ILocalApSupplierHome;
import com.ejb.entities.ap.LocalApSupplier;
import com.ejb.entities.ap.LocalApSupplierClass;
import com.ejb.dao.ap.LocalApSupplierClassHome;
import com.ejb.entities.ap.LocalApSupplierType;
import com.ejb.dao.ap.LocalApSupplierTypeHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdBranchDetails;
import com.util.mod.ap.ApModSupplierDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;

@Stateless(name = "ApFindSupplierControllerEJB")
public class ApFindSupplierControllerBean extends EJBContextClass implements ApFindSupplierController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalApSupplierHome apSupplierHome;
    @EJB
    private LocalApSupplierTypeHome apSupplierTypeHome;
    @EJB
    private LocalApSupplierClassHome apSupplierClassHome;

    public ArrayList getApStAll(Integer AD_CMPNY) {

        Debug.print("ApSupplierEntryControllerBean getApStAll");

        LocalApSupplierType apSupplierType = null;

        ArrayList list = new ArrayList();

        try {

            Collection apSupplierTypes = apSupplierTypeHome.findEnabledStAll(AD_CMPNY);

            for (Object supplierType : apSupplierTypes) {

                apSupplierType = (LocalApSupplierType) supplierType;

                list.add(apSupplierType.getStName());

            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }

    public ArrayList getApScAll(Integer AD_CMPNY) {

        Debug.print("ApSupplierEntryControllerBean getApScAll");

        LocalApSupplierClass apSupplierClass = null;

        ArrayList list = new ArrayList();

        try {

            Collection apSupplierClasses = apSupplierClassHome.findEnabledScAll(AD_CMPNY);

            for (Object supplierClass : apSupplierClasses) {

                apSupplierClass = (LocalApSupplierClass) supplierClass;

                list.add(apSupplierClass.getScName());

            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());

        }

    }

    //TODO: This function contains StringBuffer that needs to remove and replace. Code review.
    public ArrayList getApSplByCriteria(HashMap criteria, Integer OFFSET, Integer LIMIT, String ORDER_BY, ArrayList adBrnchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApFindSupplierControllerBean getApSplByCriteria");

        ArrayList list = new ArrayList();

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT DISTINCT OBJECT(spl) FROM ApSupplier spl ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            if (adBrnchList.isEmpty()) {

            } else {

                jbossQl.append(", in (spl.adBranchSuppliers) bspl WHERE bspl.adBranch.brCode in (");

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

            Object[] obj = null;

            // Allocate the size of the object parameter

            if (criteria.containsKey("supplierCode")) {

                criteriaSize--;

            }

            if (criteria.containsKey("name")) {

                criteriaSize--;

            }

            if (criteria.containsKey("email")) {

                criteriaSize--;

            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("supplierCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("spl.splSupplierCode LIKE '%").append(criteria.get("supplierCode")).append("%' ");

            }

            if (criteria.containsKey("name")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("spl.splName LIKE '%").append(criteria.get("name")).append("%' ");

            }

            if (criteria.containsKey("email")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("spl.splEmail LIKE '%").append(criteria.get("email")).append("%' ");

            }

            if (criteria.containsKey("supplierType")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("spl.apSupplierType.stName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("supplierType");
                ctr++;

            }

            if (criteria.containsKey("supplierClass")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("spl.apSupplierClass.scName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("supplierClass");
                ctr++;

            }

            if (criteria.containsKey("enable") && criteria.containsKey("disable")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("(spl.splEnable=?").append(ctr + 1).append(" OR ");
                obj[ctr] = EJBCommon.TRUE;
                ctr++;

                jbossQl.append("spl.splEnable=?").append(ctr + 1).append(") ");
                obj[ctr] = EJBCommon.FALSE;
                ctr++;

            } else {

                if (criteria.containsKey("enable")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("spl.splEnable=?").append(ctr + 1).append(" ");
                    obj[ctr] = EJBCommon.TRUE;
                    ctr++;

                }

                if (criteria.containsKey("disable")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("spl.splEnable=?").append(ctr + 1).append(" ");
                    obj[ctr] = EJBCommon.FALSE;
                    ctr++;

                }

            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("spl.splAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            if (ORDER_BY.equals("SUPPLIER CODE")) {

                orderBy = "spl.splSupplierCode";

            } else {

                orderBy = "spl.splName";

            }

            jbossQl.append("ORDER BY ").append(orderBy);

            Debug.print("jbossQL-" + jbossQl);
            Collection apSuppliers = apSupplierHome.getSplByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            if (apSuppliers.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object supplier : apSuppliers) {

                LocalApSupplier apSupplier = (LocalApSupplier) supplier;

                ApModSupplierDetails mdetails = new ApModSupplierDetails();
                mdetails.setSplCode(apSupplier.getSplCode());
                mdetails.setSplSupplierCode(apSupplier.getSplSupplierCode());
                mdetails.setSplName(apSupplier.getSplName());
                mdetails.setSplTin(apSupplier.getSplTin());
                mdetails.setSplEmail(apSupplier.getSplEmail());
                mdetails.setSplStName(apSupplier.getApSupplierType() != null ? apSupplier.getApSupplierType().getStName() : null);
                mdetails.setSplScName(apSupplier.getApSupplierClass().getScName());
                mdetails.setSplPytName(apSupplier.getAdPaymentTerm().getPytName());
                mdetails.setSplEnable(apSupplier.getSplEnable());

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

    //TODO: This function contains StringBuffer that needs to remove and replace. Code review.
    public Integer getApSplSizeByCriteria(HashMap criteria, ArrayList adBrnchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApFindSupplierControllerBean getApSplSizeByCriteria");

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT DISTINCT OBJECT(spl) FROM ApSupplier spl ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            if (adBrnchList.isEmpty()) {

            } else {

                jbossQl.append(", in (spl.adBranchSuppliers) bspl WHERE bspl.adBranch.brCode in (0");

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

            Object[] obj = null;

            // Allocate the size of the object parameter

            if (criteria.containsKey("supplierCode")) {

                criteriaSize--;

            }

            if (criteria.containsKey("name")) {

                criteriaSize--;

            }

            if (criteria.containsKey("email")) {

                criteriaSize--;

            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("supplierCode")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");

                }

                jbossQl.append("spl.splSupplierCode LIKE '%").append(criteria.get("supplierCode")).append("%' ");

            }

            if (criteria.containsKey("name")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("spl.splName LIKE '%").append(criteria.get("name")).append("%' ");

            }

            if (criteria.containsKey("email")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("spl.splEmail LIKE '%").append(criteria.get("email")).append("%' ");

            }

            if (criteria.containsKey("supplierType")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("spl.apSupplierType.stName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("supplierType");
                ctr++;

            }

            if (criteria.containsKey("supplierClass")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("spl.apSupplierClass.scName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("supplierClass");
                ctr++;

            }

            if (criteria.containsKey("enable") && criteria.containsKey("disable")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("(spl.splEnable=?").append(ctr + 1).append(" OR ");
                obj[ctr] = EJBCommon.TRUE;
                ctr++;

                jbossQl.append("spl.splEnable=?").append(ctr + 1).append(") ");
                obj[ctr] = EJBCommon.FALSE;
                ctr++;

            } else {

                if (criteria.containsKey("enable")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("spl.splEnable=?").append(ctr + 1).append(" ");
                    obj[ctr] = EJBCommon.TRUE;
                    ctr++;

                }

                if (criteria.containsKey("disable")) {

                    if (!firstArgument) {
                        jbossQl.append("AND ");
                    } else {
                        firstArgument = false;
                        jbossQl.append("WHERE ");
                    }
                    jbossQl.append("spl.splEnable=?").append(ctr + 1).append(" ");
                    obj[ctr] = EJBCommon.FALSE;
                    ctr++;

                }

            }

            if (!firstArgument) {
                jbossQl.append("AND ");
            } else {
                firstArgument = false;
                jbossQl.append("WHERE ");
            }
            jbossQl.append("spl.splAdCompany=").append(AD_CMPNY).append(" ");

            Collection apSuppliers = apSupplierHome.getSplByCriteria(jbossQl.toString(), obj, 0, 0);

            if (apSuppliers.size() == 0) throw new GlobalNoRecordFoundException();

            return apSuppliers.size();

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());

        }

    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApFindSupplierControllerBean ejbCreate");

    }
}
package com.ejb.txn.genfld;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.gen.LocalGenValueSet;
import com.ejb.dao.gen.LocalGenValueSetHome;
import com.ejb.entities.gen.LocalGenValueSetValue;
import com.ejb.dao.gen.LocalGenValueSetValueHome;
import com.util.ad.AdCompanyDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.gen.GenRepValueSetValueListDetails;

@Stateless(name = "GenRepValueSetValueListControllerEJB")
public class GenRepValueSetValueListControllerBean extends EJBContextClass implements GenRepValueSetValueListController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalGenValueSetHome genValueSetHome;
    @EJB
    private LocalGenValueSetValueHome genValueSetValueHome;


    public ArrayList getGenVsAll(Integer AD_CMPNY) {

        Debug.print("GenValueSetValueControllerBean getGenVsAll");

        ArrayList list = new ArrayList();

        try {

            Collection genValueSets = genValueSetHome.findVsAll(AD_CMPNY);

            for (Object valueSet : genValueSets) {

                LocalGenValueSet genValueSet = (LocalGenValueSet) valueSet;

                list.add(genValueSet.getVsName());
            }

            return list;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList executeGenRepValueSetValueList(HashMap criteria, String ORDER_BY, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GenRepValueSetValueListControllerBean executeGenRepValueSetValueList");

        ArrayList list = new ArrayList();

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(vsv) FROM GenValueSetValue vsv ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();

            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("valueSetName")) {

                criteriaSize--;
            }

            obj = new Object[criteriaSize];

            if (criteria.containsKey("valueSetName")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("vsv.genValueSet.vsName LIKE '%").append(criteria.get("valueSetName")).append("%' ");
            }

            if (criteria.containsKey("enable") && criteria.containsKey("disable")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("(vsv.vsvEnable=?").append(ctr + 1).append(" OR ");
                obj[ctr] = EJBCommon.TRUE;
                ctr++;

                jbossQl.append("vsv.vsvEnable=?").append(ctr + 1).append(") ");
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
                    jbossQl.append("vsv.vsvEnable=?").append(ctr + 1).append(" ");
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
                    jbossQl.append("vsv.vsvEnable=?").append(ctr + 1).append(" ");
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

            jbossQl.append("vsv.vsvAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            if (ORDER_BY.equals("VALUE NAME")) {

                orderBy = "vsv.genValueSet.vsName";
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy);
            }

            Collection genValueSetValues = genValueSetValueHome.getVsvByCriteria(jbossQl.toString(), obj);

            if (genValueSetValues.size() == 0) throw new GlobalNoRecordFoundException();

            for (Object valueSetValue : genValueSetValues) {

                LocalGenValueSetValue genValueSetValue = (LocalGenValueSetValue) valueSetValue;

                GenRepValueSetValueListDetails details = new GenRepValueSetValueListDetails();

                details.setVslValueSetName(genValueSetValue.getGenValueSet().getVsName());
                details.setVslValue(genValueSetValue.getVsvValue());
                details.setVslDescription(genValueSetValue.getVsvDescription());

                if (genValueSetValue.getGenQualifier() != null) {

                    details.setVslAccountType(genValueSetValue.getGenQualifier().getQlAccountType());
                }

                details.setVslEnable(genValueSetValue.getVsvEnable());

                list.add(details);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("GenRepValueSetValueListControllerBean getAdCompany");

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

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GenRepValueSetValueListControllerBean ejbCreate");
    }
}
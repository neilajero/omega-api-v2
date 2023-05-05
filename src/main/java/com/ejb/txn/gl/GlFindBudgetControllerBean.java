/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class GlFindBudgetControllerBean
 * @created July 23, 2004, 11:00 AM
 * @author Neil Andrew M. Ajero
 */
package com.ejb.txn.gl;

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
import com.ejb.entities.gl.LocalGlBudget;
import com.ejb.entities.gl.LocalGlBudgetAmount;
import com.ejb.entities.gl.LocalGlBudgetAmountCoa;
import com.ejb.dao.gl.LocalGlBudgetAmountHome;
import com.ejb.dao.gl.LocalGlBudgetHome;
import com.ejb.entities.gl.LocalGlBudgetOrganization;
import com.ejb.dao.gl.LocalGlBudgetOrganizationHome;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.mod.gl.GlModBudgetAmountCoaDetails;
import com.util.mod.gl.GlModBudgetAmountDetails;

@Stateless(name = "GlFindBudgetControllerEJB")
public class GlFindBudgetControllerBean extends EJBContextClass implements GlFindBudgetController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalGlBudgetHome glBudgetHome;
    @EJB
    private LocalGlBudgetAmountHome glBudgetAmountHome;
    @EJB
    private LocalGlBudgetOrganizationHome glBudgetOrganizationHome;


    public ArrayList getGlBoAll(Integer AD_CMPNY) {

        Debug.print("GlBudgetEntryControllerBean getGlBoAll");

        ArrayList list = new ArrayList();


        try {

            Collection glBudgetOrganizations = glBudgetOrganizationHome.findBoAll(AD_CMPNY);

            for (Object budgetOrganization : glBudgetOrganizations) {

                LocalGlBudgetOrganization glBudgetOrganization = (LocalGlBudgetOrganization) budgetOrganization;

                list.add(glBudgetOrganization.getBoName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGlBgtAll(Integer AD_CMPNY) {

        Debug.print("GlBudgetEntryControllerBean getGlBgtAll");

        ArrayList list = new ArrayList();

        try {

            Collection glBudgets = glBudgetHome.findBgtAll(AD_CMPNY);

            for (Object budget : glBudgets) {

                LocalGlBudget glBudget = (LocalGlBudget) budget;

                list.add(glBudget.getBgtName());
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGlBgaByCriteria(HashMap criteria, String ORDER_BY, Integer OFFSET, Integer LIMIT, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFindBudgetControllerBean getGlBgaByCriteria");

        try {

            ArrayList bgaList = new ArrayList();

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(bga) FROM GlBudgetAmount bga ");

            boolean firstArgument = true;
            short ctr = 0;
            Object[] obj = new Object[criteria.size()];

            if (criteria.containsKey("accountNumber")) {

                firstArgument = false;

                jbossQl.append(", In(bga.glBudgetAmountCoas) bc WHERE bc.glChartOfAccount.coaAccountNumber = ?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("accountNumber");
                ctr++;
            }

            if (criteria.containsKey("budgetOrganization")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("bga.glBudgetOrganization.boName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("budgetOrganization");
                ctr++;
            }

            if (criteria.containsKey("budgetName")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("bga.glBudget.bgtName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("budgetName");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("bga.bgaAdCompany=").append(AD_CMPNY).append(" ");

            String orderBy = null;

            if (ORDER_BY.equals("BUDGET ORGANIZATION")) {

                orderBy = "bga.glBudgetOrganization.boName";

            } else if (ORDER_BY.equals("BUDGET NAME")) {

                orderBy = "bga.glBudget.bgtName";
            }

            if (orderBy != null) {

                jbossQl.append("ORDER BY ").append(orderBy);
            }

            Debug.print("QL + " + jbossQl);

            Collection glBudgetAmounts = null;

            try {

                glBudgetAmounts = glBudgetAmountHome.getBgaByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }

            if (glBudgetAmounts.isEmpty()) throw new GlobalNoRecordFoundException();

            for (Object budgetAmount : glBudgetAmounts) {

                LocalGlBudgetAmount glBudgetAmount = (LocalGlBudgetAmount) budgetAmount;

                GlModBudgetAmountDetails mdetails = new GlModBudgetAmountDetails();
                mdetails.setBgaCode(glBudgetAmount.getBgaCode());
                mdetails.setBgaBoName(glBudgetAmount.getGlBudgetOrganization().getBoName());
                mdetails.setBgaBgtName(glBudgetAmount.getGlBudget().getBgtName());

                ArrayList glBgaBudgetAmountCoaList = new ArrayList();
                Collection glBudgetAmountCoas = glBudgetAmount.getGlBudgetAmountCoas();

                for (Object budgetAmountCoa : glBudgetAmountCoas) {

                    LocalGlBudgetAmountCoa glBudgetAmountCoa = (LocalGlBudgetAmountCoa) budgetAmountCoa;

                    GlModBudgetAmountCoaDetails bcDetails = new GlModBudgetAmountCoaDetails();
                    bcDetails.setBcCoaAccountNumber(glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountNumber());
                    bcDetails.setBcCoaAccountDescription(glBudgetAmountCoa.getGlChartOfAccount().getCoaAccountDescription());
                    glBgaBudgetAmountCoaList.add(bcDetails);
                }

                mdetails.setBgaBudgetAmountCoaList(glBgaBudgetAmountCoaList);

                bgaList.add(mdetails);
            }

            return bgaList;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public Integer getGlBgaSizeByCriteria(HashMap criteria, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFindBudgetControllerBean getGlBgaSizeByCriteria");

        try {

            StringBuilder jbossQl = new StringBuilder();
            jbossQl.append("SELECT OBJECT(bga) FROM GlBudgetAmount bga ");

            boolean firstArgument = true;
            short ctr = 0;
            Object[] obj = new Object[criteria.size()];

            if (criteria.containsKey("accountNumber")) {

                firstArgument = false;

                jbossQl.append(", In(bga.glBudgetAmountCoas) bc WHERE bc.glChartOfAccount.coaAccountNumber = ?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("accountNumber");
                ctr++;
            }

            if (criteria.containsKey("budgetOrganization")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("bga.glBudgetOrganization.boName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("budgetOrganization");
                ctr++;
            }

            if (criteria.containsKey("budgetName")) {

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("bga.glBudget.bgtName=?").append(ctr + 1).append(" ");
                obj[ctr] = criteria.get("budgetName");
                ctr++;
            }

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("bga.bgaAdCompany=").append(AD_CMPNY).append(" ");

            Collection glBudgetAmounts = null;

            try {

                glBudgetAmounts = glBudgetAmountHome.getBgaByCriteria(jbossQl.toString(), obj, 0, 0);

            } catch (Exception ex) {

                throw new EJBException(ex.getMessage());
            }

            if (glBudgetAmounts.isEmpty()) throw new GlobalNoRecordFoundException();

            return glBudgetAmounts.size();

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        }
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("GlFindBudgetControllerBean getGlFcPrecisionUnit");

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

        Debug.print("GlFindBudgetControllerBean ejbCreate");
    }

    // private methods

}
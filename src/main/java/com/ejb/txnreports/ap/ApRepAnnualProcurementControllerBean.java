package com.ejb.txnreports.ap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import jakarta.ejb.CreateException;;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;


import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.entities.ad.LocalAdUser;
import com.ejb.dao.ad.LocalAdUserHome;
import com.ejb.dao.ap.LocalApDistributionRecordHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.dao.gl.LocalGlFunctionalCurrencyRateHome;
import com.ejb.dao.gl.LocalGlJournalLineHome;
import com.ejb.dao.gl.LocalGlTaxInterfaceHome;
import com.ejb.entities.inv.LocalInvTransactionalBudget;
import com.ejb.dao.inv.LocalInvTransactionalBudgetHome;
import com.util.ad.AdBranchDetails;
import com.util.ad.AdCompanyDetails;
import com.util.reports.ap.ApRepAnnualProcurementDetails;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;

@Stateless(name = "ApRepAnnualProcurementControllerEJB")
public class ApRepAnnualProcurementControllerBean extends EJBContextClass implements ApRepAnnualProcurementController {

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
    private LocalAdUserHome adUserHome;
    @EJB
    private LocalApDistributionRecordHome apDistributionRecordHome;
    @EJB
    private LocalGlFunctionalCurrencyRateHome glFunctionalCurrencyRateHome;
    @EJB
    private LocalGlJournalLineHome glJournalHome;
    @EJB
    private LocalGlTaxInterfaceHome glTaxInterfaceHome;
    @EJB
    private LocalInvTransactionalBudgetHome invTransactionalBudgetHome;


  public ArrayList executeApRepAnnualProcurement(HashMap criteria, String month, ArrayList adBrnchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepAnnualProcurementControllerBean executeApRepAnnualProcurement");

        ArrayList list = new ArrayList();

        try {

            // get all available records
            StringBuilder jbossQl = new StringBuilder();

            jbossQl.append("SELECT OBJECT(tb) FROM InvTransactionalBudget tb ");

            boolean firstArgument = true;
            short ctr = 0;
            int criteriaSize = criteria.size();
            String department = " ";
            String year = null;
            Object[] obj;

            // Allocate the size of the object parameter

            if (criteria.containsKey("itemName")) {

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

                jbossQl.append("tb.tbName LIKE '%").append(criteria.get("itemName")).append("%' ");
            }

            if (criteria.containsKey("department")) {
                LocalAdLookUpValue adLkUpVl = adLookUpValueHome.findByLvName((String) criteria.get("department"), AD_CMPNY);

                if (!firstArgument) {

                    jbossQl.append("AND ");

                } else {

                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }

                jbossQl.append("tb.tbAdLookupValue=").append(adLkUpVl.getLvCode()).append(" ");
                obj[ctr] = criteria.get("department");
                ctr++;
                department = (String) criteria.get("department");
            }

            if (criteria.containsKey("year")) {

                if (!firstArgument) {
                    jbossQl.append("AND ");
                } else {
                    firstArgument = false;
                    jbossQl.append("WHERE ");
                }
                jbossQl.append("tb.tbYear=").append(criteria.get("year")).append(" ");
                obj[ctr] = criteria.get("year");
                Debug.print(obj[ctr] + "<== obj[ctr] year");
                ctr++;
            }

            year = (String) criteria.get("year");
            Debug.print(year + "<== year");
            Debug.print(criteriaSize + "<== criteria size");

            if (!firstArgument) {

                jbossQl.append("AND ");

            } else {

                firstArgument = false;
                jbossQl.append("WHERE ");
            }

            jbossQl.append("tb.tbAdCompany=").append(AD_CMPNY).append(" ");

            short PRECISION_UNIT = this.getGlFcPrecisionUnit(AD_CMPNY);
            Debug.print(criteria + "<== criteria");
            Debug.print(jbossQl + "<== query");
            Debug.print(obj + "<== object variable");
            Debug.print(month + "<== month");
            Collection invTransactionalBudget = invTransactionalBudgetHome.getTbByCriteria(jbossQl.toString(), obj);
            Debug.print(invTransactionalBudget + "<== collection transactional budget");
            ArrayList apSupplierList = new ArrayList();

            for (Object o : invTransactionalBudget) {

                //
                // LocalAdLookUpValue adLkUpVl = adLookUpValueHome.findByLvName(department, AD_CMPNY);

                LocalInvTransactionalBudget invTransactionalBudgetInterface = (LocalInvTransactionalBudget) o;

                ApRepAnnualProcurementDetails details = new ApRepAnnualProcurementDetails();

                details.setApItemName(invTransactionalBudgetInterface.getTbName());
                details.setApItemDesc(invTransactionalBudgetInterface.getTbDesc());
                details.setApInvItemCategory(invTransactionalBudgetInterface.getInvItem().getIiAdLvCategory());
                details.setApDepartment(department);
                details.setApUnit(invTransactionalBudgetInterface.getInvUnitOfMeasure().getUomShortName());
                details.setApQtyJan(invTransactionalBudgetInterface.getTbQuantityJan());
                details.setApQtyFeb(invTransactionalBudgetInterface.getTbQuantityFeb());
                details.setApQtyMrch(invTransactionalBudgetInterface.getTbQuantityMrch());
                details.setApQtyAprl(invTransactionalBudgetInterface.getTbQuantityAprl());
                details.setApQtyMay(invTransactionalBudgetInterface.getTbQuantityMay());
                details.setApQtyJun(invTransactionalBudgetInterface.getTbQuantityJun());
                details.setApQtyJul(invTransactionalBudgetInterface.getTbQuantityJul());
                details.setApQtyAug(invTransactionalBudgetInterface.getTbQuantityAug());
                details.setApQtySep(invTransactionalBudgetInterface.getTbQuantitySep());
                details.setApQtyOct(invTransactionalBudgetInterface.getTbQuantityOct());
                details.setApQtyNov(invTransactionalBudgetInterface.getTbQuantityNov());
                details.setApQtyDec(invTransactionalBudgetInterface.getTbQuantityDec());
                details.setApTotalCost(invTransactionalBudgetInterface.getTbTotalAmount());
                details.setApYear(invTransactionalBudgetInterface.getTbYear().toString());
                details.setApUnitCost(invTransactionalBudgetInterface.getTbAmount());

                list.add(details);
            }

            if (list.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

        } catch (GlobalNoRecordFoundException ex) {

            Debug.printStackTrace(ex);
            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }

        return list;
    }

    public short getGlFcPrecisionUnit(Integer AD_CMPNY) {

        Debug.print("ApRepAnnualProcurementControllerBean getGlFcPrecisionUnit");

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);

            return adCompany.getGlFunctionalCurrency().getFcPrecision();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public AdCompanyDetails getAdCompany(Integer AD_CMPNY) {

        Debug.print("ApRepAnnualProcurementControllerBean getAdCompany");

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

            return details;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdLvDEPARTMENT(Integer AD_CMPNY) {

        Debug.print("ApRepAnnualProcurementControllerBean getAdLvDEPARTMENT");

        ArrayList list = new ArrayList();

        try {

            Collection adLookUpValues = adLookUpValueHome.findByLuName("DEPARTMENT", AD_CMPNY);

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

    public String getAdUsrDeptartment(String USR_NM, Integer AD_CMPNY) {

        Debug.print("ApPurchaseRequisitionEntryControllerBean getAdUsrAll");

        LocalAdUser adUser = null;
        ArrayList list = new ArrayList();

        try {

            // Collection adUsers = adUserHome.findUsrByDepartment(USR_DEPT, AD_CMPNY);
            adUser = adUserHome.findByUsrName(USR_NM, AD_CMPNY);

            return adUser.getUsrDept();

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("ApRepAnnualProcurementControllerBean getAdBrResAll");

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

    private double convertForeignToFunctionalCurrency(Integer FC_CODE, String FC_NM, Date CONVERSION_DATE, double CONVERSION_RATE, double AMOUNT, Integer AD_CMPNY) {

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

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("ApRepAnnualProcurementControllerBean ejbCreate");
    }
}
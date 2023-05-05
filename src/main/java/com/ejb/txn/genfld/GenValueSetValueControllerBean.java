package com.ejb.txn.genfld;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.exception.gen.GenVSVNoValueSetValueFoundException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyAssignedException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.ejb.exception.global.GlobalRecordInvalidException;
import com.ejb.exception.global.GlobalSegmentValueInvalidException;
import com.ejb.entities.gen.LocalGenQualifier;
import com.ejb.dao.gen.LocalGenQualifierHome;
import com.ejb.entities.gen.LocalGenSegment;
import com.ejb.dao.gen.LocalGenSegmentHome;
import com.ejb.entities.gen.LocalGenValueSet;
import com.ejb.dao.gen.LocalGenValueSetHome;
import com.ejb.entities.gen.LocalGenValueSetValue;
import com.ejb.dao.gen.LocalGenValueSetValueHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.gl.LocalGlJournalLineInterface;
import com.ejb.dao.gl.LocalGlJournalLineInterfaceHome;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.gen.GenModValueSetValueDetails;
import com.util.gen.GenValueSetValueDetails;

@Stateless(name = "GenValueSetValueControllerEJB")
public class GenValueSetValueControllerBean extends EJBContextClass implements GenValueSetValueController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private ILocalGlChartOfAccountHome glChartOfAccountHome;
    @EJB
    private LocalGenQualifierHome genQualifierHome;
    @EJB
    private LocalGenSegmentHome genSegmentHome;
    @EJB
    private LocalGenValueSetHome genValueSetHome;
    @EJB
    private LocalGenValueSetValueHome genValueSetValueHome;
    @EJB
    private LocalGlJournalLineInterfaceHome glJournalLineInterfaceHome;

    public ArrayList getGenVsAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GenValueSetValueControllerBean getGenVsAll");

        Collection genValueSets = null;

        LocalGenValueSet genValueSet = null;

        ArrayList list = new ArrayList();


        try {

            genValueSets = genValueSetHome.findVsAll(AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (genValueSets.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }

        for (Object valueSet : genValueSets) {

            genValueSet = (LocalGenValueSet) valueSet;

            list.add(genValueSet.getVsName());
        }

        return list;
    }

    public ArrayList getGenQlfrAll(Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GenValueSetValueControllerBean getGenQlfrAll");

        Collection genQualifiers = null;

        LocalGenQualifier genQualifier = null;

        ArrayList list = new ArrayList();


        try {

            genQualifiers = genQualifierHome.findQlfrAll(AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (genQualifiers.isEmpty()) {

            throw new GlobalNoRecordFoundException();
        }

        for (Object qualifier : genQualifiers) {

            genQualifier = (LocalGenQualifier) qualifier;

            list.add(genQualifier.getQlAccountType());
        }

        return list;
    }

    public ArrayList getGenVsvByVsName(String VS_NM, Integer AD_CMPNY) throws GenVSVNoValueSetValueFoundException {

        Debug.print("GenValueSetValueControllerBean getGenVsvByVsName");

        ArrayList list = new ArrayList();

        Collection genValueSetValues = null;

        LocalGenQualifier genQualifier = null;


        try {
            Debug.print("VS_NM=" + VS_NM);
            Debug.print("AD_CMPNY=" + AD_CMPNY);
            genValueSetValues = genValueSetValueHome.findByVsName(VS_NM, AD_CMPNY);
            Debug.print("SIZE=" + genValueSetValues.size());

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        if (genValueSetValues.size() == 0) {

            throw new GenVSVNoValueSetValueFoundException();
        }

        for (Object valueSetValue : genValueSetValues) {

            LocalGenValueSetValue genValueSetValue = (LocalGenValueSetValue) valueSetValue;

            GenModValueSetValueDetails mdetails = new GenModValueSetValueDetails();

            mdetails.setVsvCode(genValueSetValue.getVsvCode());
            mdetails.setVsvValue(genValueSetValue.getVsvValue());
            mdetails.setVsvDescription(genValueSetValue.getVsvDescription());
            mdetails.setVsvParent(genValueSetValue.getVsvParent());
            mdetails.setVsvLevel(genValueSetValue.getVsvLevel());
            mdetails.setVsvEnable(genValueSetValue.getVsvEnable());
            mdetails.setVsvVsName(genValueSetValue.getGenValueSet().getVsName());

            Debug.print("genValueSetValue.getVsvValue()=" + genValueSetValue.getVsvValue());

            mdetails.setVsvQlAccountType(genValueSetValue.getGenQualifier() != null ? genValueSetValue.getGenQualifier().getQlAccountType() : null);
            Debug.print("TYPE=" + mdetails.getVsvQlAccountType());

            list.add(mdetails);
        }

        return list;
    }

    public char getGenSgSegmentTypeByVsName(String VS_NM, Integer AD_CMPNY) {

        Debug.print("GenValueSetValueControllerBean getGenSgSegmentTypeByVsName");

        char SG_SGMNT_TYP = 0;

        Collection genSegments = null;

        LocalGenValueSet genValueSet = null;
        LocalGenSegment genSegment = null;

        try {

            genValueSet = genValueSetHome.findByVsName(VS_NM, AD_CMPNY);

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            genSegments = genValueSet.getGenSegments();

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        for (Object segment : genSegments) {

            genSegment = (LocalGenSegment) segment;

            SG_SGMNT_TYP = genSegment.getSgSegmentType();
        }
        Debug.print("SG_SGMNT_TYP=" + SG_SGMNT_TYP);

        return SG_SGMNT_TYP;
    }

    public void addGenVsvEntry(GenValueSetValueDetails details, String VS_NM, String QL_ACCNT_TYP, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalRecordInvalidException, GlobalSegmentValueInvalidException {

        Debug.print("GenValueSetValueControllerBean addGenVsvEntry");

        Collection genSegments = null;

        try {

            LocalGenValueSetValue genValueSetValue = genValueSetValueHome.findByVsvValueAndVsName(details.getVsvValue(), VS_NM, AD_CMPNY);

            throw new GlobalRecordAlreadyExistException();

        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            String segmentSeparator = String.valueOf(adCompany.getGenField().getFlSegmentSeparator());

            if (details.getVsvValue().contains(segmentSeparator)) {

                throw new GlobalRecordInvalidException();
            }

        } catch (GlobalRecordInvalidException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // check segment value
        try {

            LocalGenValueSet genValueSet = genValueSetHome.findByVsName(VS_NM, AD_CMPNY);
            LocalGenSegment genSegment = genSegmentHome.findByVsCode(genValueSet.getVsCode(), AD_CMPNY);
            int segmentSize = details.getVsvValue().length();

            if (details.getVsvValue().length() != (int) genSegment.getSgMaxSize()) {

                throw new GlobalSegmentValueInvalidException();
            }

        } catch (GlobalSegmentValueInvalidException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            // create new value set values

            LocalGenValueSetValue genValueSetValue = genValueSetValueHome.create(details.getVsvValue(), details.getVsvDescription(), details.getVsvParent(), details.getVsvLevel(), details.getVsvEnable(), AD_CMPNY);

            LocalGenValueSet genValueSet = genValueSetHome.findByVsName(VS_NM, AD_CMPNY);
            genValueSetValue.setGenValueSet(genValueSet);

            if (QL_ACCNT_TYP != null && QL_ACCNT_TYP.length() > 0) {

                LocalGenQualifier genQualifier = genQualifierHome.findByQlAccountType(QL_ACCNT_TYP, AD_CMPNY);
                genValueSetValue.setGenQualifier(genQualifier);
            }

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void updateGenVsvEntry(GenValueSetValueDetails details, String VS_NM, String QL_ACCNT_TYP, Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalRecordAlreadyAssignedException, GlobalRecordInvalidException, GlobalSegmentValueInvalidException {

        Debug.print("GenValueSetValueControllerBean updateGenVsvEntry");

        LocalGenValueSetValue genValueSetValue = null;
        LocalAdCompany adCompany = null;
        String accountSeparator = null;
        LocalGenSegment genSegment = null;

        try {

            genValueSetValue = genValueSetValueHome.findByPrimaryKey(details.getVsvCode());
            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            accountSeparator = String.valueOf(adCompany.getGenField().getFlSegmentSeparator());
            genSegment = genSegmentHome.findByVsCode(genValueSetValue.getGenValueSet().getVsCode(), AD_CMPNY);

            if (details.getVsvValue().contains(accountSeparator)) {

                throw new GlobalRecordInvalidException();
            }

        } catch (GlobalRecordInvalidException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            LocalGenValueSetValue genExistingValueSetValue = genValueSetValueHome.findByVsvValueAndVsName(details.getVsvValue(), VS_NM, AD_CMPNY);

            if (genExistingValueSetValue != null && !genExistingValueSetValue.getVsvCode().equals(details.getVsvCode())) {

                throw new GlobalRecordAlreadyExistException();
            }

        } catch (GlobalRecordAlreadyExistException ex) {

            throw ex;

        } catch (FinderException ex) {

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // check segment value
        try {

            LocalGenValueSet genValueSet = genValueSetHome.findByVsName(VS_NM, AD_CMPNY);
            LocalGenSegment genSegment2 = genSegmentHome.findByVsCode(genValueSet.getVsCode(), AD_CMPNY);
            int segmentSize = details.getVsvValue().length();

            if (details.getVsvValue().length() != (int) genSegment2.getSgMaxSize()) {

                throw new GlobalSegmentValueInvalidException();
            }

        } catch (GlobalSegmentValueInvalidException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // validate if qualifer is not updated

        try {

            Collection glChartOfAccounts = glChartOfAccountHome.getCoaByCriteria("SELECT OBJECT(coa) FROM GlChartOfAccount coa WHERE coa.coaSegment" + genSegment.getSgSegmentNumber() + "='" + genValueSetValue.getVsvValue() + "' AND coa.coaAdCompany=" + AD_CMPNY + " ", new Object[0], 0, 0);
            if (QL_ACCNT_TYP != null) {
                if (!glChartOfAccounts.isEmpty() && QL_ACCNT_TYP != null && QL_ACCNT_TYP.length() > 0 && !genValueSetValue.getGenQualifier().getQlAccountType().equals(QL_ACCNT_TYP))
                    throw new GlobalRecordAlreadyAssignedException();
            }

        } catch (GlobalRecordAlreadyAssignedException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // Find and Update Value Set Value

        try {

            // propagate changes to value to other tables if necessary
            if (QL_ACCNT_TYP != null) {
                if (!details.getVsvValue().equals(genValueSetValue.getVsvValue()) || !details.getVsvDescription().equals(genValueSetValue.getVsvDescription()) || !genValueSetValue.getGenQualifier().getQlAccountType().equals(QL_ACCNT_TYP)) {
                }

                Collection glChartOfAccounts = glChartOfAccountHome.getCoaByCriteria("SELECT OBJECT(coa) FROM GlChartOfAccount coa WHERE coa.coaSegment" + genSegment.getSgSegmentNumber() + "='" + genValueSetValue.getVsvValue() + "' AND coa.coaAdCompany=" + AD_CMPNY + " ", new Object[0], 0, 0);

                Iterator i = glChartOfAccounts.iterator();

                while (i.hasNext()) {

                    LocalGlChartOfAccount glChartOfAccount = (LocalGlChartOfAccount) i.next();

                    switch (genSegment.getSgSegmentNumber()) {
                        case 1:
                            glChartOfAccount.setCoaSegment1(details.getVsvValue());
                            break;
                        case 2:
                            glChartOfAccount.setCoaSegment2(details.getVsvValue());
                            break;
                        case 3:
                            glChartOfAccount.setCoaSegment3(details.getVsvValue());
                            break;
                        case 4:
                            glChartOfAccount.setCoaSegment4(details.getVsvValue());
                            break;
                        case 5:
                            glChartOfAccount.setCoaSegment5(details.getVsvValue());
                            break;
                        case 6:
                            glChartOfAccount.setCoaSegment6(details.getVsvValue());
                            break;
                        case 7:
                            glChartOfAccount.setCoaSegment7(details.getVsvValue());
                            break;
                        case 8:
                            glChartOfAccount.setCoaSegment8(details.getVsvValue());
                            break;
                        case 9:
                            glChartOfAccount.setCoaSegment9(details.getVsvValue());
                            break;
                        case 10:
                            glChartOfAccount.setCoaSegment10(details.getVsvValue());
                            break;
                        default:
                            break;
                    }

                    StringBuilder newAccountNumber = new StringBuilder();
                    StringBuilder newAccountDescription = new StringBuilder();

                    int j = 1;
                    StringTokenizer st = new StringTokenizer(glChartOfAccount.getCoaAccountNumber(), accountSeparator);

                    while (st.hasMoreTokens()) {

                        if (j == genSegment.getSgSegmentNumber()) {

                            newAccountNumber.append(details.getVsvValue());
                            newAccountDescription.append(details.getVsvDescription());
                            st.nextToken();

                        } else {

                            String vsvValue = st.nextToken();
                            newAccountNumber.append(vsvValue);

                            LocalGenSegment genOtherSegment = genSegmentHome.findByFlCodeAndSgSegmentNumber(adCompany.getGenField().getFlCode(), (short) j, AD_CMPNY);
                            LocalGenValueSetValue genOtherValueSetValue = genValueSetValueHome.findByVsCodeAndVsvValue(genOtherSegment.getGenValueSet().getVsCode(), vsvValue, AD_CMPNY);
                            newAccountDescription.append(genOtherValueSetValue.getVsvDescription());
                        }

                        j++;

                        if (st.hasMoreTokens()) {

                            newAccountNumber.append(accountSeparator);
                            newAccountDescription.append(accountSeparator);
                        }
                    }

                    // replace coa in ad company if necessary

                    if (glChartOfAccount.getCoaAccountNumber().equals(adCompany.getCmpRetainedEarnings())) {

                        adCompany.setCmpRetainedEarnings(newAccountNumber.toString());
                    }

                    // replace coa in gl journal interface table if necessary

                    Collection glJournalLineInterfaces = glJournalLineInterfaceHome.findJliByJliCoaAccountNumber(glChartOfAccount.getCoaAccountNumber(), AD_CMPNY);

                    Iterator jliIter = glJournalLineInterfaces.iterator();

                    while (jliIter.hasNext()) {

                        LocalGlJournalLineInterface glJournalLineInterface = (LocalGlJournalLineInterface) jliIter.next();

                        glJournalLineInterface.setJliCoaAccountNumber(newAccountNumber.toString());
                    }

                    glChartOfAccount.setCoaAccountNumber(newAccountNumber.toString());
                    glChartOfAccount.setCoaAccountDescription(newAccountDescription.toString());

                    if (QL_ACCNT_TYP != null && QL_ACCNT_TYP.length() > 0) {

                        glChartOfAccount.setCoaAccountType(QL_ACCNT_TYP);
                    }
                }
            }

            genValueSetValue.setVsvValue(details.getVsvValue());
            genValueSetValue.setVsvDescription(details.getVsvDescription());
            genValueSetValue.setVsvParent(details.getVsvParent());
            genValueSetValue.setVsvLevel(details.getVsvLevel());
            genValueSetValue.setVsvEnable(details.getVsvEnable());

            LocalGenValueSet genValueSet = genValueSetHome.findByVsName(VS_NM, AD_CMPNY);
            genValueSetValue.setGenValueSet(genValueSet);

            if (QL_ACCNT_TYP != null && QL_ACCNT_TYP.length() > 0) {

                LocalGenQualifier genQualifier = genQualifierHome.findByQlAccountType(QL_ACCNT_TYP, AD_CMPNY);
                genValueSetValue.setGenQualifier(genQualifier);
            }
        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    public void deleteGenVsvEntry(Integer VSV_CODE, Integer AD_CMPNY) throws GlobalRecordAlreadyAssignedException {

        Debug.print("GenValueSetValueControllerBean deleteGenVsvEntry");

        LocalGenValueSetValue genValueSetValue = null;
        LocalAdCompany adCompany = null;
        String accountSeparator = null;
        LocalGenSegment genSegment = null;

        try {

            genValueSetValue = genValueSetValueHome.findByPrimaryKey(VSV_CODE);
            adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            accountSeparator = String.valueOf(adCompany.getGenField().getFlSegmentSeparator());
            genSegment = genSegmentHome.findByVsCode(genValueSetValue.getGenValueSet().getVsCode(), AD_CMPNY);

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        // validate if value set value still not assigned

        try {

            Collection glChartOfAccounts = glChartOfAccountHome.getCoaByCriteria("SELECT OBJECT(coa) FROM GlChartOfAccount coa WHERE coa.coaSegment" + genSegment.getSgSegmentNumber() + "='" + genValueSetValue.getVsvValue() + "' AND coa.coaAdCompany=" + AD_CMPNY + " ", new Object[0], 0, 0);

            if (!glChartOfAccounts.isEmpty()) throw new GlobalRecordAlreadyAssignedException();

        } catch (GlobalRecordAlreadyAssignedException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new EJBException(ex.getMessage());
        }

        try {

            // delete value set value
            //        	genValueSetValue.entityRemove();
            em.remove(genValueSetValue);

        } catch (Exception ex) {

            getSessionContext().setRollbackOnly();
            throw new EJBException(ex.getMessage());
        }
    }

    // SessionBean methods

    public void ejbCreate() throws CreateException {

        Debug.print("GenValueSetValueControllerBean ejbCreate");
    }
}
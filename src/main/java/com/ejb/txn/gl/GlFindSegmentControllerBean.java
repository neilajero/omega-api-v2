/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class GlFindSegmentControllerBean
 * @created
 * @author
 */
package com.ejb.txn.gl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.gen.LocalGenField;
import com.ejb.entities.gen.LocalGenSegment;
import com.ejb.dao.gen.LocalGenSegmentHome;
import com.ejb.entities.gen.LocalGenValueSet;
import com.ejb.dao.gen.LocalGenValueSetHome;
import com.ejb.entities.gen.LocalGenValueSetValue;
import com.ejb.dao.gen.LocalGenValueSetValueHome;
import com.util.Debug;
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.EJBHomeFactory;
import com.util.gen.GenModValueSetDetails;
import com.util.gen.GenModValueSetValueDetails;

@Stateless(name = "GlFindSegmentControllerEJB")
public class GlFindSegmentControllerBean extends EJBContextClass implements GlFindSegmentController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;
    @EJB
    private LocalGenSegmentHome genSegmentHome;
    @EJB
    private LocalGenValueSetHome genValueSetHome;
    @EJB
    private LocalGenValueSetValueHome genValueSetValueHome;

    public ArrayList getGlValueSetValueByCriteria(HashMap criteria, ArrayList vsvDescList, short selectedCoaSegmentNumber, ArrayList adBrnchList, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFindSegmentControllerBean getGlValueSetValueByCriteria");

        try {

            ArrayList list = new ArrayList();
            LocalAdCompany adCompany = adCompanyHome.findByPrimaryKey(AD_CMPNY);
            LocalGenField genField = adCompany.getGenField();
            LocalGenSegment genSegment = genSegmentHome.findByFlCodeAndSgSegmentNumber(genField.getFlCode(), selectedCoaSegmentNumber, AD_CMPNY);
            Collection genValueSetValues = null;
            if (vsvDescList.isEmpty()) {

                genValueSetValues = genValueSetValueHome.findByVsName(genSegment.getGenValueSet().getVsName(), AD_CMPNY);

            } else {

                Iterator vsvIter = vsvDescList.iterator();
                String vsvDescription = null;
                while (vsvIter.hasNext()) {

                    GenModValueSetValueDetails genVsvDetails = (GenModValueSetValueDetails) vsvIter.next();

                    if (genVsvDetails.getVsvVsName().equals(genSegment.getGenValueSet().getVsName())) {

                        vsvDescription = genVsvDetails.getVsvDescription();
                        break;
                    }
                }

                if (vsvDescription != null) {
                    Debug.print("pass 1 : desc " + vsvDescription);
                    Debug.print("pass 1 : name " + genSegment.getGenValueSet().getVsName());
                    genValueSetValues = genValueSetValueHome.findByVsvDescriptionAndVsName(vsvDescription, genSegment.getGenValueSet().getVsName(), AD_CMPNY);

                } else {
                    Debug.print("pass 2 : " + vsvDescription);
                    genValueSetValues = genValueSetValueHome.findByVsName(genSegment.getGenValueSet().getVsName(), AD_CMPNY);
                }
            }

            if (genValueSetValues.size() == 0) {

                throw new GlobalNoRecordFoundException();
            }

            for (Object valueSetValue : genValueSetValues) {

                LocalGenValueSetValue genValueSetValue = (LocalGenValueSetValue) valueSetValue;

                GenModValueSetValueDetails mdetails = new GenModValueSetValueDetails();

                mdetails.setVsvValue(genValueSetValue.getVsvValue());
                mdetails.setVsvDescription(genValueSetValue.getVsvDescription());

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

    public ArrayList getGenVsAll(Integer AD_CMPNY) {

        Debug.print("GlCoaGeneratorControllerBean getGenVsAll");

        ArrayList list = new ArrayList();

        try {

            Collection genValueSets = genValueSetHome.findVsAll(AD_CMPNY);

            for (Object valueSet : genValueSets) {

                LocalGenValueSet genValueSet = (LocalGenValueSet) valueSet;
                LocalGenSegment genSegment = genSegmentHome.findByVsCode(genValueSet.getVsCode(), AD_CMPNY);

                GenModValueSetDetails mdetails = new GenModValueSetDetails();
                mdetails.setVsName(genValueSet.getVsName());
                mdetails.setVsSegmentNumber(genSegment.getSgSegmentNumber());

                if (genSegment.getSgSegmentType() == 'N') {

                    mdetails.setVsSgNatural(EJBCommon.TRUE);

                } else {

                    mdetails.setVsSgNatural(EJBCommon.FALSE);
                }

                list.add(mdetails);
            }

            return list;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    public ArrayList getGenVsvAllByVsNameAndVsvDesc(String VS_NM, String VSV_DESC, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

        Debug.print("GlFindSegmentControllerBean getGenVsvAllByName");


        ArrayList list = new ArrayList();

        try {

            Collection genValueSetValues = null;

            try {

                genValueSetValues = genValueSetValueHome.findByVsvDescriptionAndVsName(VSV_DESC, VS_NM, AD_CMPNY);

            } catch (FinderException ex) {

                throw new GlobalNoRecordFoundException();
            }

            if (genValueSetValues.isEmpty()) {

                throw new GlobalNoRecordFoundException();
            }

            for (Object valueSetValue : genValueSetValues) {

                LocalGenValueSetValue genValueSetValue = (LocalGenValueSetValue) valueSetValue;

                GenModValueSetValueDetails details = new GenModValueSetValueDetails();

                details.setVsvValue(genValueSetValue.getVsvValue());
                details.setVsvDescription(genValueSetValue.getVsvDescription());

                list.add(details);
            }

            return list;

        } catch (GlobalNoRecordFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

    // private method

    private boolean containsSegment(ArrayList list, String vsvValue) {

        for (Object o : list) {

            GenModValueSetValueDetails details = (GenModValueSetValueDetails) o;

            if (details.getVsvValue().equals(vsvValue)) {

                return true;
            }
        }

        return false;
    }

    public void ejbCreate() throws CreateException {

        Debug.print("GlFindSegmentControllerBean ejbCreate");
    }
}
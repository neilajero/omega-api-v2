/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvUnitOfMeasureConversionControllerBean
 * @created March 22, 2004, 8:16 PM
 * @author Rey Limosenero
 */
package com.ejb.txn.inv;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdBranchItemLocation;
import com.ejb.dao.ad.LocalAdBranchItemLocationHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.entities.inv.LocalInvItem;
import com.ejb.dao.inv.LocalInvItemHome;
import com.ejb.entities.inv.LocalInvItemLocation;
import com.ejb.dao.inv.LocalInvItemLocationHome;
import com.ejb.entities.inv.LocalInvUnitOfMeasure;
import com.ejb.entities.inv.LocalInvUnitOfMeasureConversion;
import com.ejb.dao.inv.LocalInvUnitOfMeasureConversionHome;
import com.ejb.dao.inv.LocalInvUnitOfMeasureHome;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.mod.inv.InvModUnitOfMeasureConversionDetails;

@Stateless(name = "InvUnitOfMeasureConversionControllerEJB")
public class InvUnitOfMeasureConversionControllerBean extends EJBContextClass
    implements InvUnitOfMeasureConversionController {

  @EJB public PersistenceBeanClass em;
  @EJB private ILocalAdPreferenceHome adPreferenceHome;
  @EJB private LocalInvUnitOfMeasureConversionHome invUnitOfMeasureConversionHome;
  @EJB private LocalInvUnitOfMeasureHome invUnitOfMeasureHome;
  @EJB private LocalInvItemHome invItemHome;
  @EJB private LocalInvItemLocationHome invItemLocationHome;
  @EJB private LocalAdBranchItemLocationHome adBranchItemLocationHome;

  public short getAdPrfInvQuantityPrecisionUnit(Integer AD_CMPNY) {

    Debug.print("InvUnitOfMeasureConversionControllerBean getAdPrfInvQuantityPrecisionUnit");

    try {

      LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);

      return adPreference.getPrfInvQuantityPrecisionUnit();

    } catch (Exception ex) {

      Debug.printStackTrace(ex);
      throw new EJBException(ex.getMessage());
    }
  }

  public ArrayList getInvUmcByIiCode(Integer II_CODE, Integer AD_CMPNY)
      throws GlobalNoRecordFoundException {

    Debug.print("InvUnitOfMeasureConversionControllerBean getInvUmcByIiCode");

    try {

      LocalInvItem invItem = null;

      try {

        // get inv item
        invItem = invItemHome.findByPrimaryKey(II_CODE);

      } catch (FinderException ex) {

        throw new GlobalNoRecordFoundException();
      }

      ArrayList list = new ArrayList();

      // get unit of measure conversion

      Collection invUnitOfMeasureConversions = invItem.getInvUnitOfMeasureConversions();

        for (Object unitOfMeasureConversion : invUnitOfMeasureConversions) {

            LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion =
                    (LocalInvUnitOfMeasureConversion) unitOfMeasureConversion;

            InvModUnitOfMeasureConversionDetails mdetails = new InvModUnitOfMeasureConversionDetails();

            mdetails.setUomName(invUnitOfMeasureConversion.getInvUnitOfMeasure().getUomName());
            mdetails.setUomShortName(
                    invUnitOfMeasureConversion.getInvUnitOfMeasure().getUomShortName());
            mdetails.setUomAdLvClass(
                    invUnitOfMeasureConversion.getInvUnitOfMeasure().getUomAdLvClass());
            mdetails.setUmcConversionFactor(invUnitOfMeasureConversion.getUmcConversionFactor());
            mdetails.setUmcBaseUnit(invUnitOfMeasureConversion.getUmcBaseUnit());

            list.add(mdetails);
        }

      if (list.isEmpty()) {

        throw new GlobalNoRecordFoundException();
      }

      return list;

    } catch (GlobalNoRecordFoundException ex) {

      throw ex;

    } catch (Exception ex) {

      Debug.printStackTrace(ex);
      throw new EJBException(ex.getMessage());
    }
  }

  public void saveInvUMCEntry(Integer II_CODE, ArrayList umcList, Integer AD_CMPNY)
      throws GlobalNoRecordFoundException {

    Debug.print("InvUnitOfMeasureConversionControllerBean saveInvUOMConversionEntry");

    try {

      LocalInvItem invItem = invItemHome.findByPrimaryKey(II_CODE);

      // remove all unit of mesure conversion by item

      Collection unitOfMeasureConversions = invItem.getInvUnitOfMeasureConversions();

      Iterator i = unitOfMeasureConversions.iterator();

      while (i.hasNext()) {

        LocalInvUnitOfMeasureConversion unitOfMeasureConversion =
            (LocalInvUnitOfMeasureConversion) i.next();

        i.remove();

        //				unitOfMeasureConversion.entityRemove();
        em.remove(unitOfMeasureConversion);
      }

      // add new unit of mesure conversion

      i = umcList.iterator();

      while (i.hasNext()) {

        InvModUnitOfMeasureConversionDetails mdetails =
            (InvModUnitOfMeasureConversionDetails) i.next();

        double conversionFactor = mdetails.getUmcConversionFactor();
        byte umcBaseUnit = mdetails.getUmcBaseUnit();
        String uomName = mdetails.getUomName();
        String uomAdLvClass = mdetails.getUomAdLvClass();

        this.addInvUmcEntry(
            invItem, uomName, uomAdLvClass, conversionFactor, umcBaseUnit, AD_CMPNY);
      }

      try {
        LocalInvItemLocation invItemLocation = null;

        Collection invItemLocations =
            invItemLocationHome.findByIiName(invItem.getIiName(), AD_CMPNY);

          for (Object itemLocation : invItemLocations) {

              invItemLocation = (LocalInvItemLocation) itemLocation;

              Collection adBranchItemLocations =
                      adBranchItemLocationHome.findByInvIlAll(invItemLocation.getIlCode(), AD_CMPNY);

              for (Object branchItemLocation : adBranchItemLocations) {
                  LocalAdBranchItemLocation adBranchItemLocation =
                          (LocalAdBranchItemLocation) branchItemLocation;

                  if (adBranchItemLocation.getBilItemDownloadStatus() == 'N') {
                      adBranchItemLocation.setBilItemDownloadStatus('N');
                  } else if (adBranchItemLocation.getBilItemDownloadStatus() == 'D') {
                      adBranchItemLocation.setBilItemDownloadStatus('X');
                  } else if (adBranchItemLocation.getBilItemDownloadStatus() == 'U') {
                      adBranchItemLocation.setBilItemDownloadStatus('U');
                  } else if (adBranchItemLocation.getBilItemDownloadStatus() == 'X') {
                      adBranchItemLocation.setBilItemDownloadStatus('X');
                  }
              }
          }
      } catch (FinderException ex) {

      }

    } catch (GlobalNoRecordFoundException ex) {

      getSessionContext().setRollbackOnly();
      throw ex;

    } catch (Exception ex) {

      Debug.printStackTrace(ex);
      throw new EJBException(ex.getMessage());
    }
  }

  // private

  private void addInvUmcEntry(
      LocalInvItem invItem,
      String uomName,
      String uomAdLvClass,
      double conversionFactor,
      byte umcBaseUnit,
      Integer AD_CMPNY)
      throws GlobalNoRecordFoundException {

    Debug.print("InvUnitOfMeasureConversionControllerBean addInvUmcEntry");

    try {

      // create umc
      LocalInvUnitOfMeasureConversion invUnitOfMeasureConversion =
          invUnitOfMeasureConversionHome.create(conversionFactor, umcBaseUnit, AD_CMPNY);

      try {

        // map uom
        LocalInvUnitOfMeasure invUnitOfMeasure =
            invUnitOfMeasureHome.findByUomNameAndUomAdLvClass(uomName, uomAdLvClass, AD_CMPNY);
        invUnitOfMeasureConversion.setInvUnitOfMeasure(invUnitOfMeasure);

      } catch (FinderException ex) {

        throw new GlobalNoRecordFoundException();
      }

      // map item
      invUnitOfMeasureConversion.setInvItem(invItem);

    } catch (GlobalNoRecordFoundException ex) {

      getSessionContext().setRollbackOnly();
      throw ex;

    } catch (Exception ex) {

      Debug.printStackTrace(ex);
      throw new EJBException(ex.getMessage());
    }
  }

  // SessionBean methods

  public void ejbCreate() throws CreateException {

    Debug.print("InvUnitOfMeasureConversionControllerBean ejbCreate");
  }
}
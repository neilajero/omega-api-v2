package com.ejb.txnapi.inv;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdBranchItemLocation;
import com.ejb.dao.ad.LocalAdBranchItemLocationHome;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.inv.InvILCoaGlAccruedInventoryAccountNotFoundException;
import com.ejb.exception.inv.InvILCoaGlCostOfSalesAccountNotFoundException;
import com.ejb.exception.inv.InvILCoaGlInventoryAccountNotFoundException;
import com.ejb.exception.inv.InvILCoaGlSalesAccountNotFoundException;
import com.ejb.exception.inv.InvILCoaGlSalesReturnAccountNotFoundException;
import com.ejb.exception.inv.InvILCoaGlWipAccountNotFoundException;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.entities.inv.LocalInvItem;
import com.ejb.dao.inv.LocalInvItemHome;
import com.ejb.entities.inv.LocalInvItemLocation;
import com.ejb.dao.inv.LocalInvItemLocationHome;
import com.ejb.entities.inv.LocalInvLocation;
import com.ejb.dao.inv.LocalInvLocationHome;
import com.util.ad.AdBranchDetails;
import com.util.mod.ad.AdModBranchItemLocationDetails;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.mod.inv.InvModItemLocationDetails;

@Stateless(name = "InvItemLocationEntryApiControllerEJB")
public class InvItemLocationEntryApiControllerBean extends EJBContextClass
		implements InvItemLocationEntryApiController {

	@EJB
	private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
	@EJB
	private LocalAdBranchHome adBranchHome;
	@EJB
	private LocalAdBranchItemLocationHome adBranchItemLocationHome;
	@EJB
	private LocalInvLocationHome invLocationHome;
	@EJB
	private LocalInvItemHome invItemHome;
	@EJB
	private LocalInvItemLocationHome invItemLocationHome;
	@EJB
	private ILocalGlChartOfAccountHome glChartOfAccountHome;
	
	public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

		Debug.print("InvItemLocationEntryApiControllerBean getAdBrResAll");

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

                adBranch = adBranchHome.findByPrimaryKey(adBranchResponsibility.getAdBranch().getBrCode());

                AdBranchDetails details = new AdBranchDetails();
                details.setBrCode(adBranch.getBrCode());
                details.setBrBranchCode(adBranch.getBrBranchCode());
                details.setBrName(adBranch.getBrName());
                details.setBrCoaSegment(adBranch.getBrCoaSegment());
                details.setBrType(adBranch.getBrType());

                list.add(details);
            }

		} catch (FinderException ex) {

		} catch (Exception ex) {

			throw new EJBException(ex.getMessage());
		}

		return list;
	}

	@Override
	public void saveInvIlEntry(InvModItemLocationDetails mdetails, ArrayList itemList, ArrayList locationList,
			ArrayList branchItemLocationList, ArrayList categoryList, String II_CLSS, Integer AD_CMPNY)
			throws InvILCoaGlSalesAccountNotFoundException, InvILCoaGlSalesReturnAccountNotFoundException,
			InvILCoaGlInventoryAccountNotFoundException, InvILCoaGlCostOfSalesAccountNotFoundException,
			InvILCoaGlWipAccountNotFoundException, InvILCoaGlAccruedInventoryAccountNotFoundException {

		Debug.print("InvItemLocationEntryApiControllerBean saveInvIlEntry");

		try {

			String locationName = null;
            for (Object element : locationList) {
                locationName = (String) element;
                for (Object item : itemList) {
                    String itemName = (String) item;
                    for (Object value : categoryList) {
                        String category = (String) value;
                        LocalInvItem invItem = null;
                        try {
                            invItem = invItemHome.findByIiNameAndIiAdLvCategory(itemName, category, AD_CMPNY);
                        } catch (FinderException ex) {
                            continue;
                        }

                        LocalGlChartOfAccount glSalesAccount = null;
                        LocalGlChartOfAccount glInventoryAccount = null;
                        LocalGlChartOfAccount glCostOfSalesAccount = null;
                        LocalGlChartOfAccount glWipAccount = null;
                        LocalGlChartOfAccount glAccrdInvAccount = null;
                        LocalGlChartOfAccount glSalesReturnAccount = null;
                        LocalInvItemLocation invItemLocation = null;

                        // get sales account, inventory account and cost of sales account to validate accounts
                        try {
                            glSalesAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getIlCoaGlSalesAccountNumber(), AD_CMPNY);
                        } catch (FinderException ex) {
                            throw new InvILCoaGlSalesAccountNotFoundException("HO Sales");
                        }

                        try {
                            glSalesReturnAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getIlCoaGlSalesReturnAccountNumber(), AD_CMPNY);

                        } catch (FinderException ex) {
                            throw new InvILCoaGlSalesReturnAccountNotFoundException("HO Sales Return");
                        }

                        try {
                            glInventoryAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getIlCoaGlInventoryAccountNumber(), AD_CMPNY);
                        } catch (FinderException ex) {
                            throw new InvILCoaGlInventoryAccountNotFoundException("HO Inventory");
                        }

                        try {
                            glCostOfSalesAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getIlCoaGlCostOfSalesAccountNumber(), AD_CMPNY);
                        } catch (FinderException ex) {
                            throw new InvILCoaGlCostOfSalesAccountNotFoundException("HO COS");
                        }

                        try {
                            glAccrdInvAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getIlCoaGlAccruedInventoryAccountNumber(), AD_CMPNY);
                        } catch (FinderException ex) {
                            throw new InvILCoaGlAccruedInventoryAccountNotFoundException("HO Accrued Inventory");
                        }

                        try {
                            glWipAccount = glChartOfAccountHome.findByCoaAccountNumber(mdetails.getIlCoaGlWipAccountNumber(), AD_CMPNY);
                        } catch (FinderException ex) {
                            throw new InvILCoaGlWipAccountNotFoundException("HO Stock In-Transit");
                        }

                        if (II_CLSS != null && II_CLSS.length() > 0 && !invItem.getIiClass().equals(II_CLSS)) {
                            continue;
                        }

                        // create new item location
                        try {
                            invItemLocation = invItemLocationHome.findByLocNameAndIiName(locationName, itemName, AD_CMPNY);
                        } catch (FinderException ex) {
                        }

                        invItemLocation = invItemLocationHome.IlRack(mdetails.getIlRack()).IlBin(mdetails.getIlBin())
                                .IlReorderPoint(mdetails.getIlReorderPoint())
                                .IlReorderQuantity(mdetails.getIlReorderQuantity())
                                .IlReorderLevel(mdetails.getIlReorderLevel())
                                .IlGlCoaSalesAccount(glSalesAccount.getCoaCode())
                                .IlGlCoaInventoryAccount(glInventoryAccount.getCoaCode())
                                .IlGlCoaCostOfSalesAccount(glCostOfSalesAccount.getCoaCode())
                                .IlGlCoaWipAccount(glWipAccount.getCoaCode())
                                .IlGlCoaAccruedInventoryAccount(glAccrdInvAccount.getCoaCode())
                                .IlGlCoaSalesReturnAccount(glSalesReturnAccount.getCoaCode())
                                .IlSubjectToCommission(mdetails.getIlSubjectToCommission()).IlAdCompany(AD_CMPNY)
                                .buildItemLocation();

                        invItemLocation.setInvItem(invItem);

                        LocalInvLocation invLocation = invLocationHome.findByLocName(locationName, AD_CMPNY);
                        invItemLocation.setInvLocation(invLocation);

                        invItemLocation.getInvItem().setIiLastModifiedBy(mdetails.getIlLastModifiedBy());
                        invItemLocation.getInvItem().setIiDateLastModified(mdetails.getIlDateLastModified());

                        for (Object o : branchItemLocationList) {
                            AdModBranchItemLocationDetails details = (AdModBranchItemLocationDetails) o;

                            try {
                                glSalesAccount = glChartOfAccountHome.findByCoaAccountNumber(details.getBilCoaGlSalesAccountNumber(), AD_CMPNY);
                            } catch (FinderException ex) {
                                throw new InvILCoaGlSalesAccountNotFoundException("Branch Sales");
                            }

                            try {
                                glSalesReturnAccount = glChartOfAccountHome.findByCoaAccountNumber(details.getBilCoaGlSalesReturnAccountNumber(), AD_CMPNY);
                            } catch (FinderException ex) {
                                throw new InvILCoaGlSalesReturnAccountNotFoundException("Branch Sales Return");
                            }

                            try {
                                glInventoryAccount = glChartOfAccountHome.findByCoaAccountNumber(details.getBilCoaGlInventoryAccountNumber(), AD_CMPNY);
                            } catch (FinderException ex) {
                                throw new InvILCoaGlInventoryAccountNotFoundException("Branch Inventory");
                            }

                            try {
                                glCostOfSalesAccount = glChartOfAccountHome.findByCoaAccountNumber(details.getBilCoaGlCostOfSalesAccountNumber(), AD_CMPNY);
                            } catch (FinderException ex) {
                                throw new InvILCoaGlCostOfSalesAccountNotFoundException("Branch COS");
                            }

                            try {
                                glAccrdInvAccount = glChartOfAccountHome.findByCoaAccountNumber(details.getBilCoaGlAccruedInventoryAccountNumber(), AD_CMPNY);
                            } catch (FinderException ex) {
                                throw new InvILCoaGlAccruedInventoryAccountNotFoundException("Branch Accrued Inventory");
                            }

                            try {
                                glWipAccount = glChartOfAccountHome.findByCoaAccountNumber(details.getBilCoaGlWipAccountNumber(), AD_CMPNY);
                            } catch (FinderException ex) {
                                throw new InvILCoaGlWipAccountNotFoundException("Branch Stock In-Transit");
                            }

                            // create new branch item location
                            LocalAdBranchItemLocation adBranchItemLocation = adBranchItemLocationHome
                                    .BilRack(details.getBilRack()).BilBin(details.getBilBin())
                                    .BilReorderPoint(details.getBilReorderPoint())
                                    .BilReorderQuantity(details.getBilReorderQuantity())
                                    .BilGlCoaAccruedInventoryAccount(glAccrdInvAccount.getCoaCode())
                                    .BilGlCoaSalesAccount(glSalesAccount.getCoaCode())
                                    .BilGlCoaInventoryAccount(glInventoryAccount.getCoaCode())
                                    .BilGlCoaCostOfSalesAccount(glCostOfSalesAccount.getCoaCode())
                                    .BilGlCoaWipAccount(glWipAccount.getCoaCode())
                                    .BilGlCoaSalesReturnAccount(glSalesReturnAccount.getCoaCode())
                                    .BilSubjectToCommission(details.getBilSubjectToCommission())
                                    .BilHist1Sales(details.getBilHist1Sales()).BilHist2Sales(details.getBilHist2Sales())
                                    .BilProjectedSales(details.getBilProjectedSales())
                                    .BilDeliveryTime(details.getBilDeliveryTime())
                                    .BilDeliveryBuffer(details.getBilDeliveryBuffer())
                                    .BilOrderPerYear(details.getBilOrderPerYear()).BilAdCompany(AD_CMPNY)
                                    .buildBranchItemLocation();

                            adBranchItemLocation.setInvItemLocation(invItemLocation);

                            LocalAdBranch adBranch = adBranchHome.findByBrName(details.getBilBrName(), AD_CMPNY);
                            adBranchItemLocation.setAdBranch(adBranch);

                        }
                    }
                }
            }

		} catch (InvILCoaGlSalesAccountNotFoundException | InvILCoaGlWipAccountNotFoundException |
                InvILCoaGlCostOfSalesAccountNotFoundException | InvILCoaGlInventoryAccountNotFoundException |
                InvILCoaGlSalesReturnAccountNotFoundException ex) {

			getSessionContext().setRollbackOnly();
			throw ex;

		} catch (Exception ex) {

			Debug.printStackTrace(ex);
			getSessionContext().setRollbackOnly();
			throw new EJBException(ex.getMessage());
		}
	}
}
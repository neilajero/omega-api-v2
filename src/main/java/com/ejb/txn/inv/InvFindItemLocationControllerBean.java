/**
 * @copyright 2020 Omega Business Consulting, Inc.
 * @class InvFindItemLocationControllerBean
 * @created June 8, 2004 10:26 AM
 * @author Enrico C. Yap
 */
package com.ejb.txn.inv;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdPreferenceHome;
import com.ejb.entities.ad.LocalAdBranch;
import com.ejb.dao.ad.LocalAdBranchHome;
import com.ejb.entities.ad.LocalAdBranchResponsibility;
import com.ejb.dao.ad.LocalAdBranchResponsibilityHome;
import com.ejb.entities.ad.LocalAdLookUpValue;
import com.ejb.dao.ad.LocalAdLookUpValueHome;
import com.ejb.entities.ad.LocalAdPreference;
import com.ejb.exception.global.GlobalNoRecordFoundException;
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
import com.util.EJBCommon;
import com.util.EJBContextClass;
import com.util.mod.inv.InvModItemLocationDetails;

@Stateless(name = "InvFindItemLocationControllerEJB")
public class InvFindItemLocationControllerBean extends EJBContextClass
		implements InvFindItemLocationController {

	@EJB
	public PersistenceBeanClass em;
	@EJB
	private ILocalAdPreferenceHome adPreferenceHome;
	@EJB
	private ILocalGlChartOfAccountHome glChartOfAccountHome;
	@EJB
	private LocalAdLookUpValueHome adLookUpValueHome;
	@EJB
	private LocalInvItemHome invItemHome;
	@EJB
	private LocalInvLocationHome invLocationHome;
	@EJB
	private LocalAdBranchResponsibilityHome adBranchResponsibilityHome;
	@EJB
	private LocalAdBranchHome adBranchHome;
	@EJB
	private LocalInvItemLocationHome invItemLocationHome;

	public ArrayList getAdLvInvItemCategoryAll(Integer AD_CMPNY) {

		Debug.print("InvFindItemLocationControllerBean getAdLvInvItemCategoryAll");

		ArrayList list = new ArrayList();

		try {

			Collection adLookUpValues = adLookUpValueHome.findByLuName("INV ITEM CATEGORY", AD_CMPNY);
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

	public ArrayList getInvIiAll(Integer AD_CMPNY) {

		Debug.print("InvFindItemLocationControllerBean getInvIiAll");

		LocalInvItem invItem = null;
		ArrayList list = new ArrayList();

		try {

			Collection invItems = invItemHome.findEnabledIiAll(AD_CMPNY);

            for (Object item : invItems) {

                invItem = (LocalInvItem) item;
                list.add(invItem.getIiName());
            }

			return list;

		} catch (Exception ex) {

			Debug.printStackTrace(ex);
			throw new EJBException(ex.getMessage());
		}
	}

	public ArrayList getInvLocAll(Integer AD_CMPNY) {

		Debug.print("InvFindItemLocationControllerBean getInvLocAll");

		LocalInvLocation invLocation = null;
		ArrayList list = new ArrayList();

		try {

			Collection invLocations = invLocationHome.findLocAll(AD_CMPNY);

            for (Object location : invLocations) {
                invLocation = (LocalInvLocation) location;
                list.add(invLocation.getLocName());
            }
			return list;

		} catch (Exception ex) {

			Debug.printStackTrace(ex);
			throw new EJBException(ex.getMessage());
		}
	}

	public short getAdPrfInvQuantityPrecisionUnit(Integer AD_CMPNY) {

		Debug.print("InvFindItemLocationControllerBean getAdPrfInvQuantityPrecisionUnit");

		try {

			LocalAdPreference adPreference = adPreferenceHome.findByPrfAdCompany(AD_CMPNY);
			return adPreference.getPrfInvQuantityPrecisionUnit();

		} catch (Exception ex) {

			Debug.printStackTrace(ex);
			throw new EJBException(ex.getMessage());
		}
	}

	public ArrayList getAdBrResAll(Integer RS_CODE, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

		Debug.print("InvFindItemLocationControllerBean getAdBrResAll");

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

                list.add(details);
            }

		} catch (FinderException ex) {

		} catch (Exception ex) {

			throw new EJBException(ex.getMessage());
		}

		return list;
	}

	public ArrayList getInvIlByCriteria(HashMap criteria, ArrayList branchList, Integer OFFSET, Integer LIMIT,
			String ORDER_BY, Integer AD_CMPNY) throws GlobalNoRecordFoundException {

		Debug.print("InvFindItemLocationControllerBean getInvIlByCriteria");

		ArrayList list = new ArrayList();

		try {

			StringBuilder jbossQl = new StringBuilder();

			if (branchList.size() > 0) {

				jbossQl.append("SELECT DISTINCT OBJECT(il) FROM InvItemLocation il, IN(il.adBranchItemLocations)bil ");

			} else {

				jbossQl.append("SELECT OBJECT(il) FROM InvItemLocation il ");
			}

			boolean firstArgument = true;
			short ctr = 0;
			int criteriaSize = criteria.size() + branchList.size();

			Object[] obj = null;

			if (criteria.containsKey("category")) {

				criteriaSize--;
			}

			if (criteria.containsKey("itemName")) {

				criteriaSize--;
			}

			if (criteria.containsKey("itemDescription")) {

				criteriaSize--;
			}

			obj = new Object[criteriaSize];

			if (criteria.containsKey("category")) {

				if (!firstArgument) {
					jbossQl.append("AND ");
				} else {
					firstArgument = false;
					jbossQl.append(" WHERE ");
				}

				jbossQl.append("il.invItem.iiAdLvCategory = '").append(criteria.get("category")).append("' ");
			}

			if (criteria.containsKey("itemName")) {

				if (!firstArgument) {

					jbossQl.append("AND ");

				} else {

					firstArgument = false;
					jbossQl.append("WHERE ");
				}

				jbossQl.append("il.invItem.iiName LIKE '%").append(criteria.get("itemName")).append("%' ");
			}

			if (criteria.containsKey("itemDescription")) {

				if (!firstArgument) {

					jbossQl.append("AND ");

				} else {

					firstArgument = false;
					jbossQl.append("WHERE ");
				}

				jbossQl.append("il.invItem.iiDescription LIKE '%").append(criteria.get("itemDescription")).append("%' ");
			}

			if (criteria.containsKey("locationName")) {

				if (!firstArgument) {
					jbossQl.append("AND ");
				} else {
					firstArgument = false;
					jbossQl.append("WHERE ");
				}

				jbossQl.append("il.invLocation.locName=?").append(ctr + 1).append(" ");
				obj[ctr] = criteria.get("locationName");
				ctr++;
			}

			if (branchList.size() > 0) {

				if (!firstArgument) {

					jbossQl.append("AND ");

				} else {

					firstArgument = false;
					jbossQl.append("WHERE ");
				}

				AdModBranchItemLocationDetails details = null;

				Iterator i = branchList.iterator();

				details = (AdModBranchItemLocationDetails) i.next();

				jbossQl.append("(bil.adBranch.brCode=?").append(ctr + 1).append(" ");
				obj[ctr] = details.getBilBrCode();
				ctr++;

				while (i.hasNext()) {

					details = (AdModBranchItemLocationDetails) i.next();

					jbossQl.append("OR bil.adBranch.brCode=?").append(ctr + 1).append(" ");
					obj[ctr] = details.getBilBrCode();
					ctr++;
				}

				jbossQl.append(") ");
			}

			if (!firstArgument) {

				jbossQl.append("AND ");

			} else {

				firstArgument = false;
				jbossQl.append("WHERE ");
			}

			jbossQl.append("il.ilAdCompany=").append(AD_CMPNY).append(" ");

			String orderBy = null;

			if (ORDER_BY.equals("ITEM NAME")) {

				orderBy = "il.invItem.iiName";

			} else {

				orderBy = "il.invLocation.locName";
			}

			jbossQl.append("ORDER BY ").append(orderBy);

			Collection invItemLocations = invItemLocationHome.getIlByCriteria(jbossQl.toString(), obj, LIMIT, OFFSET);

			if (invItemLocations.size() == 0)
				throw new GlobalNoRecordFoundException();

            for (Object itemLocation : invItemLocations) {

                LocalInvItemLocation invItemLocation = (LocalInvItemLocation) itemLocation;

                InvModItemLocationDetails mdetails = new InvModItemLocationDetails();
                mdetails.setIlCode(invItemLocation.getIlCode());
                mdetails.setIlReorderPoint(invItemLocation.getIlReorderPoint());
                mdetails.setIlReorderQuantity(invItemLocation.getIlReorderQuantity());
                mdetails.setIlIiName(invItemLocation.getInvItem().getIiName());
                mdetails.setIlLocName(invItemLocation.getInvLocation().getLocName());

                if (!invItemLocation.getInvItem().getIiAdLvCategory().equals(EJBCommon.DEFAULT)) {

                    LocalGlChartOfAccount glSalesAccount = glChartOfAccountHome
                            .findByPrimaryKey(invItemLocation.getIlGlCoaSalesAccount());
                    LocalGlChartOfAccount glInventoryAccount = glChartOfAccountHome
                            .findByPrimaryKey(invItemLocation.getIlGlCoaInventoryAccount());
                    LocalGlChartOfAccount glCostOfSalesAccount = glChartOfAccountHome
                            .findByPrimaryKey(invItemLocation.getIlGlCoaCostOfSalesAccount());

                    mdetails.setIlCoaGlSalesAccountNumber(glSalesAccount.getCoaAccountNumber());
                    mdetails.setIlCoaGlSalesAccountDescription(glSalesAccount.getCoaAccountDescription());
                    mdetails.setIlCoaGlInventoryAccountNumber(glInventoryAccount.getCoaAccountNumber());
                    mdetails.setIlCoaGlInventoryAccountDescription(glInventoryAccount.getCoaAccountDescription());
                    mdetails.setIlCoaGlCostOfSalesAccountNumber(glCostOfSalesAccount.getCoaAccountNumber());
                    mdetails.setIlCoaGlCostOfSalesAccountDescription(glCostOfSalesAccount.getCoaAccountDescription());

                } else {

                    LocalGlChartOfAccount glExpenseAccount = glChartOfAccountHome
                            .findByPrimaryKey(invItemLocation.getIlGlCoaExpenseAccount());

                    mdetails.setIlCoaGlExpenseAccountNumber(glExpenseAccount.getCoaAccountNumber());
                    mdetails.setIlCoaGlExpenseAccountDescription(glExpenseAccount.getCoaAccountDescription());

                }

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

	public Integer getInvIlSizeByCriteria(HashMap criteria, ArrayList branchList, Integer AD_CMPNY)
			throws GlobalNoRecordFoundException {

		Debug.print("InvFindItemLocationControllerBean getInvIlSizeByCriteria");

		try {

			StringBuilder jbossQl = new StringBuilder();

			if (branchList.size() > 0) {

				jbossQl.append("SELECT DISTINCT OBJECT(il) FROM InvItemLocation il, IN(il.adBranchItemLocations)bil ");

			} else {

				jbossQl.append("SELECT OBJECT(il) FROM InvItemLocation il ");
			}

			boolean firstArgument = true;
			short ctr = 0;
			int criteriaSize = criteria.size() + branchList.size();
			Object[] obj = null;

			if (criteria.containsKey("category")) {

				criteriaSize--;
			}

			if (criteria.containsKey("itemName")) {

				criteriaSize--;
			}

			if (criteria.containsKey("itemDescription")) {

				criteriaSize--;
			}
			obj = new Object[criteriaSize];

			if (criteria.containsKey("category")) {

				if (!firstArgument) {
					jbossQl.append("AND ");
				} else {
					firstArgument = false;
					jbossQl.append(" WHERE ");
				}

				jbossQl.append("il.invItem.iiAdLvCategory = '").append(criteria.get("category")).append("' ");
			}

			if (criteria.containsKey("itemName")) {

				if (!firstArgument) {

					jbossQl.append("AND ");

				} else {

					firstArgument = false;
					jbossQl.append("WHERE ");
				}

				jbossQl.append("il.invItem.iiName LIKE '%").append(criteria.get("itemName")).append("%' ");
			}

			if (criteria.containsKey("itemDescription")) {

				if (!firstArgument) {

					jbossQl.append("AND ");

				} else {

					firstArgument = false;
					jbossQl.append("WHERE ");
				}

				jbossQl.append("il.invItem.iiDescription LIKE '%").append(criteria.get("itemDescription")).append("%' ");
			}

			if (criteria.containsKey("locationName")) {

				if (!firstArgument) {
					jbossQl.append("AND ");
				} else {
					firstArgument = false;
					jbossQl.append("WHERE ");
				}

				jbossQl.append("il.invLocation.locName=?").append(ctr + 1).append(" ");
				obj[ctr] = criteria.get("locationName");
				ctr++;
			}

			if (branchList.size() > 0) {

				if (!firstArgument) {

					jbossQl.append("AND ");

				} else {

					firstArgument = false;
					jbossQl.append("WHERE ");
				}

				AdModBranchItemLocationDetails details = null;

				Iterator i = branchList.iterator();

				details = (AdModBranchItemLocationDetails) i.next();

				jbossQl.append("(bil.adBranch.brCode=?").append(ctr + 1).append(" ");
				obj[ctr] = details.getBilBrCode();
				ctr++;

				while (i.hasNext()) {

					details = (AdModBranchItemLocationDetails) i.next();

					jbossQl.append("OR bil.adBranch.brCode=?").append(ctr + 1).append(" ");
					obj[ctr] = details.getBilBrCode();
					ctr++;
				}

				jbossQl.append(") ");
			}

			if (!firstArgument) {

				jbossQl.append("AND ");

			} else {

				firstArgument = false;
				jbossQl.append("WHERE ");
			}

			jbossQl.append("il.ilAdCompany=").append(AD_CMPNY).append(" ");

			Collection invItemLocations = invItemLocationHome.getIlByCriteria(jbossQl.toString(), obj, 0, 0);

			if (invItemLocations.size() == 0)
				throw new GlobalNoRecordFoundException();

			return invItemLocations.size();

		} catch (GlobalNoRecordFoundException ex) {

			throw ex;

		} catch (Exception ex) {

			ex.printStackTrace();
			throw new EJBException(ex.getMessage());
		}
	}

	// SessionBean methods
	public void ejbCreate() throws CreateException {

		Debug.print("InvFindItemLocationControllerBean ejbCreate");
	}
}
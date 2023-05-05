package com.ejb.txnapi.ar;

import com.ejb.PersistenceBeanClass;
import com.ejb.dao.ad.ILocalAdCompanyHome;
import com.ejb.dao.ad.LocalAdAmountLimitHome;
import com.ejb.dao.ar.LocalArStandardMemoLineHome;
import com.ejb.dao.gl.ILocalGlChartOfAccountHome;
import com.ejb.entities.ad.LocalAdCompany;
import com.ejb.entities.ar.LocalArStandardMemoLine;
import com.ejb.entities.gl.LocalGlChartOfAccount;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.Debug;
import com.util.EJBContextClass;
import com.util.mod.ar.ArModStandardMemoLineDetails;

import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collection;

@Stateless(name = "ArStandardMemoLineApiControllerEJB")
public class ArStandardMemoLineApiControllerBean extends EJBContextClass implements ArStandardMemoLineApiController {

    @EJB
    public PersistenceBeanClass em;
    @EJB
    private LocalArStandardMemoLineHome arStandardMemoLineHome;
    @EJB
    private ILocalAdCompanyHome adCompanyHome;

    @Override
    public ArrayList getArSmlAll(String companyShortName) throws GlobalNoRecordFoundException {
        Debug.print("ArStandardMemoLineApiControllerBean getArSmlAll");
        ArrayList<String> list = new ArrayList();
        try {

            LocalAdCompany company = adCompanyHome.findByCmpShortName(companyShortName);
            Collection arStandardMemoLines = arStandardMemoLineHome.findSmlAll(company.getCmpCode(), companyShortName);
            if (arStandardMemoLines.isEmpty()) {
                throw new GlobalNoRecordFoundException();
            }
            for (Object standardMemoLine : arStandardMemoLines) {
                LocalArStandardMemoLine arStandardMemoLine = (LocalArStandardMemoLine) standardMemoLine;
                list.add(arStandardMemoLine.getSmlName());
            }
            return list;
        } catch (GlobalNoRecordFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            Debug.printStackTrace(ex);
            throw new EJBException(ex.getMessage());
        }
    }

}
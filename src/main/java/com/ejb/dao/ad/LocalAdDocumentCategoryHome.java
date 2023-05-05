package com.ejb.dao.ad;


import com.ejb.PersistenceBeanClass;

import com.ejb.entities.ad.LocalAdDocumentCategory;
import jakarta.ejb.EJB;

import com.util.Debug;

import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;
import jakarta.persistence.NoResultException;

@Stateless
public class LocalAdDocumentCategoryHome {

    public static final String JNDI_NAME="LocalAdDocumentCategoryHome!com.ejb.ad.LocalAdDocumentCategoryHome";
    
    @EJB
    public PersistenceBeanClass em;
    
    public LocalAdDocumentCategoryHome(){}

    //FINDER METHODS

    public LocalAdDocumentCategory findByPrimaryKey(java.lang.Integer pk)
    throws FinderException {
        
        try{
            
            LocalAdDocumentCategory entity = (LocalAdDocumentCategory)em.find(new LocalAdDocumentCategory(), pk);
            if(entity==null){ throw new FinderException();}
            return entity;
        }
        catch(FinderException ex){
            throw new FinderException(ex.getMessage());
        }
        catch(Exception ex){
            throw ex;
        }
    }


    public java.util.Collection findDcAll(java.lang.Integer DC_AD_CMPNY)
    throws FinderException {
        
        try{
            Query query = em.createQuery("SELECT OBJECT(dc) FROM AdDocumentCategory dc WHERE dc.dcAdCompany = ?1");
            query.setParameter(1,DC_AD_CMPNY);
            return query.getResultList();
        }catch(Exception ex){
            Debug.print("EXCEPTION: Exception com.ejb.gl.LocalAdDocumentCategoryHome.findDcAll(java.lang.Integer DC_AD_CMPNY)");
            throw ex;
        }
    }




    public LocalAdDocumentCategory findByDcName(java.lang.String DC_NM, java.lang.Integer DC_AD_CMPNY)
    throws FinderException {
        
        try{
            Query query = em.createQuery("SELECT OBJECT(dc) FROM AdDocumentCategory dc WHERE dc.dcName=?1 AND dc.dcAdCompany = ?2");
            query.setParameter(1,DC_NM);
            query.setParameter(2,DC_AD_CMPNY);
            return (LocalAdDocumentCategory)query.getSingleResult();
        }catch(NoResultException ex){
            Debug.print("EXCEPTION: NoResultException com.ejb.gl.LocalAdDocumentCategoryHome.findByDcName(java.lang.String DC_NM, java.lang.Integer DC_AD_CMPNY)");
            throw new FinderException(ex.getMessage());
        }catch(Exception ex){
            Debug.print("EXCEPTION: Exception com.ejb.gl.LocalAdDocumentCategoryHome.findByDcName(java.lang.String DC_NM, java.lang.Integer DC_AD_CMPNY)");
            throw ex;
        }
    }




    //OTHER METHODS



    //CREATE METHODS

	public LocalAdDocumentCategory create (java.lang.Integer DC_CODE, java.lang.String DC_NM, java.lang.String DC_DESC,
                                           java.lang.Integer DC_AD_CMPNY)
		throws CreateException {
		try{

			LocalAdDocumentCategory entity = new LocalAdDocumentCategory();


			Debug.print("AdDocumentCategoryBean create");
			entity.setDcCode(DC_CODE);
			entity.setDcName(DC_NM);
			entity.setDcDescription(DC_DESC);
			entity.setDcAdCompany(DC_AD_CMPNY);

			em.persist(entity);
			return entity;

		}catch(Exception ex){throw new CreateException(ex.getMessage());}
	}
	public LocalAdDocumentCategory create (java.lang.String DC_NM, java.lang.String DC_DESC,
                                           java.lang.Integer DC_AD_CMPNY)
		throws CreateException {
		try{

			LocalAdDocumentCategory entity = new LocalAdDocumentCategory();


			Debug.print("AdDocumentCategoryBean create");
			entity.setDcName(DC_NM);
			entity.setDcDescription(DC_DESC);
			entity.setDcAdCompany(DC_AD_CMPNY);

			em.persist(entity);
			return entity;

		}catch(Exception ex){throw new CreateException(ex.getMessage());}
	}


}
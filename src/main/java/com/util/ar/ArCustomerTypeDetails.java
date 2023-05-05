package com.util.ar;

import com.ejb.entities.ar.LocalArCustomerType;

public class ArCustomerTypeDetails extends LocalArCustomerType implements java.io.Serializable {

  public ArCustomerTypeDetails() {}

  public ArCustomerTypeDetails(Object entityClass) {

    super.setValues(entityClass);
  }

  private String CT_BA_NM;

  public String getCtBaName() {

    return CT_BA_NM;
  }

  public void setCtBaName(String CT_BA_NM) {

    this.CT_BA_NM = CT_BA_NM;
  }
}
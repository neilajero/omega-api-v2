package com.util.ad;

import com.ejb.entities.ad.LocalAdUser;

public class AdUserDetails extends LocalAdUser implements java.io.Serializable {

  public AdUserDetails() {}

  public String CMP_SHRT_NM = null;
  public String CMP_NM = null;
  public String CMP_WLCM_NT = null;
  public String USR_DCRPTD_PSSWRD = null;

  public String getCmpShortName() {
    return CMP_SHRT_NM;
  }

  public String getCmpName() {
    return CMP_NM;
  }

  public String getCmpWelcomeNote() {
    return CMP_WLCM_NT;
  }

  public void setCmpShortName(String CMP_SHRT_NM) {
    this.CMP_SHRT_NM = CMP_SHRT_NM;
  }

  public void setCmpName(String CMP_NM) {
    this.CMP_NM = CMP_NM;
  }

  public void setCmpWelcomeNote(String CMP_WLCM_NT) {
    this.CMP_WLCM_NT = CMP_WLCM_NT;
  }

  public String getDecriptedPassword() {
    return USR_DCRPTD_PSSWRD;
  }

  public void setDecriptedPassword(String USR_DCRPTD_PSSWRD) {
    this.USR_DCRPTD_PSSWRD = USR_DCRPTD_PSSWRD;
  }
}
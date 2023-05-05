package com.ejb.entities.gl;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "GlFrgAccountAssignment")
@Table(name = "GL_FRG_ACCNT_ASSGNMNT")
public class LocalGlFrgAccountAssignment extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GL_AA_CODE", nullable = false)
    private Integer aaCode;

    @Column(name = "AA_ACCNT_LOW", columnDefinition = "VARCHAR")
    private String aaAccountLow;

    @Column(name = "AA_ACCNT_HGH", columnDefinition = "VARCHAR")
    private String aaAccountHigh;

    @Column(name = "AA_ACTVTY_TYP", columnDefinition = "VARCHAR")
    private String aaActivityType;

    @Column(name = "AA_AD_CMPNY", columnDefinition = "INT")
    private Integer aaAdCompany;

    @JoinColumn(name = "GL_FRG_ROW", referencedColumnName = "ROW_CODE")
    @ManyToOne
    private LocalGlFrgRow glFrgRow;

    public Integer getAaCode() {

        return aaCode;
    }

    public void setAaCode(Integer GL_AA_CODE) {

        this.aaCode = GL_AA_CODE;
    }

    public String getAaAccountLow() {

        return aaAccountLow;
    }

    public void setAaAccountLow(String AA_ACCNT_LOW) {

        this.aaAccountLow = AA_ACCNT_LOW;
    }

    public String getAaAccountHigh() {

        return aaAccountHigh;
    }

    public void setAaAccountHigh(String AA_ACCNT_HGH) {

        this.aaAccountHigh = AA_ACCNT_HGH;
    }

    public String getAaActivityType() {

        return aaActivityType;
    }

    public void setAaActivityType(String AA_ACTVTY_TYP) {

        this.aaActivityType = AA_ACTVTY_TYP;
    }

    public Integer getAaAdCompany() {

        return aaAdCompany;
    }

    public void setAaAdCompany(Integer AA_AD_CMPNY) {

        this.aaAdCompany = AA_AD_CMPNY;
    }

    public LocalGlFrgRow getGlFrgRow() {

        return glFrgRow;
    }

    public void setGlFrgRow(LocalGlFrgRow glFrgRow) {

        this.glFrgRow = glFrgRow;
    }

}
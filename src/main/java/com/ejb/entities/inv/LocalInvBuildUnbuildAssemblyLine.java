package com.ejb.entities.inv;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity(name = "InvBuildUnbuildAssemlyLine")
@Table(name = "INV_BLD_UNBLD_ASSMBLY_LN")
public class LocalInvBuildUnbuildAssemblyLine extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BL_CODE", nullable = false)
    private Integer blCode;
    @Column(name = "BL_BLD_QTY", columnDefinition = "DOUBLE")
    private double blBuildQuantity;
    @Column(name = "BL_BLD_SPCFC_GRVTY", columnDefinition = "DOUBLE")
    private double blBuildSpecificGravity;
    @Column(name = "BL_BLD_STD_FLL_SZ", columnDefinition = "DOUBLE")
    private double blBuildStandardFillSize;
    @Column(name = "BL_BL_CODE", columnDefinition = "INT")
    private Integer blBlCode;
    @Column(name = "BL_EXPRY_DT", columnDefinition = "VARCHAR")
    private String blMisc;
    @Column(name = "BL_AD_CMPNY", columnDefinition = "INT")
    private String blAdCompany;
    @JoinColumn(name = "INV_BUILD_UNBUILD_ASSEMBLY", referencedColumnName = "BUA_CODE")
    @ManyToOne
    private LocalInvBuildUnbuildAssembly invBuildUnbuildAssembly;
    @JoinColumn(name = "INV_UNIT_OF_MEASURE", referencedColumnName = "UOM_CODE")
    @ManyToOne
    private LocalInvUnitOfMeasure invUnitOfMeasure;
    @JoinColumn(name = "INV_ITEM_LOCATION", referencedColumnName = "INV_IL_CODE")
    @ManyToOne
    private LocalInvItemLocation invItemLocation;

    public String getBlAdCompany() {

        return blAdCompany;
    }

    public void setBlAdCompany(String blAdCompany) {

        this.blAdCompany = blAdCompany;
    }

    public Integer getBlCode() {

        return blCode;
    }

    public void setBlCode(Integer blCode) {

        this.blCode = blCode;
    }

    public double getBlBuildQuantity() {

        return blBuildQuantity;
    }

    public void setBlBuildQuantity(double blBuildQuantity) {

        this.blBuildQuantity = blBuildQuantity;
    }

    public double getBlBuildSpecificGravity() {

        return blBuildSpecificGravity;
    }

    public void setBlBuildSpecificGravity(double blBuildSpecificGravity) {

        this.blBuildSpecificGravity = blBuildSpecificGravity;
    }

    public double getBlBuildStandardFillSize() {

        return blBuildStandardFillSize;
    }

    public void setBlBuildStandardFillSize(double blBuildStandardFillSize) {

        this.blBuildStandardFillSize = blBuildStandardFillSize;
    }

    public Integer getBlBlCode() {

        return blBlCode;
    }

    public void setBlBlCode(Integer blBlCode) {

        this.blBlCode = blBlCode;
    }

    public String getBlMisc() {

        return blMisc;
    }

    public void setBlMisc(String blMisc) {

        this.blMisc = blMisc;
    }

    public LocalInvBuildUnbuildAssembly getInvBuildUnbuildAssembly() {

        return invBuildUnbuildAssembly;
    }

    public void setInvBuildUnbuildAssembly(LocalInvBuildUnbuildAssembly invBuildUnbuildAssembly) {

        this.invBuildUnbuildAssembly = invBuildUnbuildAssembly;
    }

    public LocalInvUnitOfMeasure getInvUnitOfMeasure() {

        return invUnitOfMeasure;
    }

    public void setInvUnitOfMeasure(LocalInvUnitOfMeasure invUnitOfMeasure) {

        this.invUnitOfMeasure = invUnitOfMeasure;
    }

    public LocalInvItemLocation getInvItemLocation() {

        return invItemLocation;
    }

    public void setInvItemLocation(LocalInvItemLocation invItemLocation) {

        this.invItemLocation = invItemLocation;
    }

}
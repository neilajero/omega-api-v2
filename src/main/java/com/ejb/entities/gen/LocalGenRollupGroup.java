package com.ejb.entities.gen;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity(name = "GenRollupGroup")
@Table(name = "GEN_RLLP_GRP")
public class LocalGenRollupGroup extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RLG_CODE", nullable = false)
    private Integer rlgCode;

    @Column(name = "RLG_NM", columnDefinition = "VARCHAR")
    private String rlgName;

    @Column(name = "RLG_DESC", columnDefinition = "VARCHAR")
    private String rlgDescription;

    @Column(name = "RLG_AD_CMPNY", columnDefinition = "INT")
    private Integer rlgAdCompany;

    @OneToMany(mappedBy = "genRollupGroup", fetch = FetchType.LAZY)
    private List<LocalGenRollupGroupAssignment> genRollupGroupAssignments;

    public Integer getRlgCode() {

        return rlgCode;
    }

    public void setRlgCode(Integer RLG_CODE) {

        this.rlgCode = RLG_CODE;
    }

    public String getRlgName() {

        return rlgName;
    }

    public void setRlgName(String RLG_NM) {

        this.rlgName = RLG_NM;
    }

    public String getRlgDescription() {

        return rlgDescription;
    }

    public void setRlgDescription(String RLG_DESC) {

        this.rlgDescription = RLG_DESC;
    }

    public Integer getRlgAdCompany() {

        return rlgAdCompany;
    }

    public void setRlgAdCompany(Integer RLG_AD_CMPNY) {

        this.rlgAdCompany = RLG_AD_CMPNY;
    }

    @XmlTransient
    public List getGenRollupGroupAssignments() {

        return genRollupGroupAssignments;
    }

    public void setGenRollupGroupAssignments(List genRollupGroupAssignments) {

        this.genRollupGroupAssignments = genRollupGroupAssignments;
    }

    public void addGenRollupGroupAssignment(LocalGenRollupGroupAssignment entity) {

        try {
            entity.setGenRollupGroup(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void dropGenRollupGroupAssignment(LocalGenRollupGroupAssignment entity) {

        try {
            entity.setGenRollupGroup(null);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
package com.ejb.entities.ad;

import com.ejb.NativeQueryHome;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "AdRole")
@Table(name = "AD_ROLE")
public class LocalAdRole extends NativeQueryHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RL_CODE", nullable = false)
    private Integer rlCode;

    @Column(name = "RL_USR_NM", columnDefinition = "VARCHAR")
    private String rlUserName;

    @Column(name = "RL_RL_NM", columnDefinition = "VARCHAR")
    private String rlRoleName;

    public Integer getRlCode() {

        return rlCode;
    }

    public void setRlCode(Integer RL_CODE) {

        this.rlCode = RL_CODE;
    }

    public String getRlUserName() {

        return rlUserName;
    }

    public void setRlUserName(String RL_USR_NM) {

        this.rlUserName = RL_USR_NM;
    }

    public String getRlRoleName() {

        return rlRoleName;
    }

    public void setRlRoleName(String RL_RL_NM) {

        this.rlRoleName = RL_RL_NM;
    }

}
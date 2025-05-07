package com.buihien.datn.domain;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "tbl_administrative_unit")
public class AdministrativeUnit extends AuditableEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "code", unique = true)
    private String code;
    @Column(name = "level")
    private Integer level; // DatnConstants.AdministrativeUnitLevel
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private AdministrativeUnit parent;
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Set<AdministrativeUnit> subAdministrativeUnits;

    public AdministrativeUnit() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public AdministrativeUnit getParent() {
        return parent;
    }

    public void setParent(AdministrativeUnit parent) {
        this.parent = parent;
    }

    public Set<AdministrativeUnit> getSubAdministrativeUnits() {
        return subAdministrativeUnits;
    }

    public void setSubAdministrativeUnits(Set<AdministrativeUnit> subAdministrativeUnits) {
        this.subAdministrativeUnits = subAdministrativeUnits;
    }
}

package com.buihien.datn.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Table(name = "tbl_profile")
@Entity
public class Person extends AuditableEntity {
    private static final long serialVersionUID = 1L;
}

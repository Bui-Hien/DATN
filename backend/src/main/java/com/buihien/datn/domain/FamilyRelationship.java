package com.buihien.datn.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
// Mối quan hệ gia đình (Ví dụ: Cha, Mẹ, Anh, Chị, Em... )

@Table(name = "tbl_family_relationship")
@Entity
public class FamilyRelationship extends BaseObject {
}

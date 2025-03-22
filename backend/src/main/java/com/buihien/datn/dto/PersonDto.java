package com.buihien.datn.dto;

import com.buihien.datn.domain.Person;

public class PersonDto extends AuditableEntityDto {
    public PersonDto() {
    }

    public PersonDto(Person entity) {
        super(entity);
        if (entity != null) {
            //todo
        }
    }
}

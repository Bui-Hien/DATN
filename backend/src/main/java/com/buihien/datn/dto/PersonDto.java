package com.buihien.datn.dto;

import com.buihien.datn.domain.Person;

public class PersonDto extends AuditableEntityDto {
    public PersonDto() {
    }

    public PersonDto(Person entity, Boolean isGetFull) {
        super(entity);
        if (entity != null) {
            //Todo
            if (isGetFull) {
                //Todo
            }
        }
    }

    public PersonDto(Person entity) {
        this(entity, true);
    }
}

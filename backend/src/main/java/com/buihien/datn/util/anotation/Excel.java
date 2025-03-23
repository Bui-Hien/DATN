package com.buihien.datn.util.anotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Excel {

    int index() default 0;

    int startRow() default 0;

    String name() default "Sheet 1";

}
package com.buihien.datn.util.anotation;

//vào đây để lấy mã màu tham khảo
//import org.apache.poi.ss.usermodel.IndexedColors;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Excel {

    int index() default 0;

    int startRow() default 0;

    String name() default "Sheet 1";

    // Màu nền toàn bảng
    short backgroundColor() default -1;

    // Màu chữ toàn bảng
    short textColor() default -1;

    // Màu nền của header
    short headerBackgroundColor() default -1;

    // Màu chữ của header
    short headerTextColor() default -1;
}
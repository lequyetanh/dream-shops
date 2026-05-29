package com.dailycodework.dreamshops.aop;

import java.lang.annotation.*;

//dùng cho method hoặc type (controller, service, ...)
@Target({ ElementType.METHOD, ElementType.TYPE })
// annotation được giữ lại trong runtime
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface WriteSystemLog {
//    Khai báo các biến đầu vào cho annotation
    String type();

    String actionType() default "";

    boolean isMultipleData() default false;
}

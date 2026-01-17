package com.dailycodework.dreamshops.aop;

import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface WriteSystemLog {
    String type();

    String actionType() default "";

    boolean isMultipleData() default false;
}

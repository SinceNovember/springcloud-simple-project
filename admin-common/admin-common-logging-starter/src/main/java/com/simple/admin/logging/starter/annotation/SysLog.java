package com.simple.admin.logging.starter.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    /**
     * ÈÕÖ¾ÄÚÈİ
     * @return {String}
     */
    String value();
}
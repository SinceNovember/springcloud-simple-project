package com.simple.admin.logging.starter.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    /**
     * ��־����
     * @return {String}
     */
    String value();
}
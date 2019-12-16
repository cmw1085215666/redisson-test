package com.cmw.annotation;

import java.lang.annotation.*;

/**
 * @author chengmingwen
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedissonLockAnnotation {

    String lockKey() default "";

    /**
     * 超时时间 默认 -1 : 大于0时看门狗机制才会生效。
     *
     * @return
     */
    int leaseTime() default -1;

    /**
     * 等待时间时间 默认 30秒 ： 小于0时lock会被重复获取（还不知道原因）
     * @return
     */
    int waitTimeout() default 30000;

}

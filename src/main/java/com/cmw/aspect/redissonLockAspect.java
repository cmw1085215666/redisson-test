package com.cmw.aspect;

import com.cmw.annotation.RedissonLockAnnotation;
import com.cmw.util.RedissLockUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author chengmingwen
 * Create Time : 2019/12/16 10:10
 */
@Component
@Aspect
public class redissonLockAspect {

    private final Logger log = LoggerFactory.getLogger(getClass());
/*

    @Pointcut("@annotation(redissonLockAnnotation)")
    public void lockAspect(RedissonLockAnnotation redissonLockAnnotation) {
    }
*/

    @Around("@annotation(redissonLockAnnotation)")
    public Object around(ProceedingJoinPoint joinPoint, RedissonLockAnnotation redissonLockAnnotation) throws Throwable {
        System.out.println("进入切面~~~~~~~~~~~~~~~~~~~~~~~~~~");
        redissonLockAnnotation = AnnotationUtils.getAnnotation(redissonLockAnnotation, RedissonLockAnnotation.class);
        boolean res = false;
        String lockKey = redissonLockAnnotation.lockKey();

        Object obj = null;
        boolean isLocked = false;
        try {
            lockKey += "_" + joinPoint.getSignature().getDeclaringTypeName() ;

            System.out.println("切面 ~~~~ "+lockKey);

            isLocked = RedissLockUtil.tryLock(lockKey, redissonLockAnnotation.waitTimeout(), redissonLockAnnotation.leaseTime());
            if (isLocked){
                obj = joinPoint.proceed();
            }else {
                System.out.println("redis getLock is failure ......");
                //这里建议换成项目自定义的错误返回对象
            }

        } catch (Throwable e) {
            log.error("redis lock -> ", e);
            throw e;
        } finally {
            if (isLocked){
                RedissLockUtil.unlock(lockKey);
            }

        }
        return obj;
    }

}

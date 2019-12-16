package com.cmw.api;

import cn.hutool.core.date.DateUtil;
import com.cmw.annotation.RedissonLockAnnotation;
import com.cmw.util.RedissLockUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author chengmingwen
 */
@RestController
public class testApi {

    @Autowired
    RedissonClient redissonClient;

    @RequestMapping("/test")
    public String testApi(){

        String name = Thread.currentThread().getName();
        String uuid = "uuid";
        boolean res=false;
        try {
            System.out.println(name + "  start tryLook..." + DateUtil.now());
//            res = RedissLockUtil.lock(uuid/*, TimeUnit.SECONDS, 5*/).isLocked();
            res = RedissLockUtil.tryLock(uuid, TimeUnit.SECONDS, 5,-1);
            if(res){
                System.out.println(name + " getLook..... " + DateUtil.now());
                Thread.sleep(10000);
                System.out.println(name + "  doing somthing ......" + DateUtil.now());
            }else{
                System.out.println(name + "  tryLock failure ......");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(res ){//释放锁
                RedissLockUtil.unlock(uuid);
            }
        }

        return "hello world";
    }

    @RequestMapping("/test1")
//    @RedissonLockAnnotation(lockKey = "testApi1",waitTimeout = 5000)
    public String testApi1(){
        boolean testApi1 = false;
        try{
            testApi1 = RedissLockUtil.tryLock("testApi1", 5000, -1);
            if (testApi1){

                System.out.println(Thread.currentThread().getName() + " getLook..... " + DateUtil.now());

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "  doing somthing ......" + DateUtil.now());
            }else {
                System.out.println(Thread.currentThread().getName() + "!!!!!!!!!!!!!!!!!" + DateUtil.now());
            }

        }finally {
            if (testApi1){
                RedissLockUtil.unlock("testApi1");
            }
        }

        return "hello world";
    }

    @RequestMapping("/test2")
    @RedissonLockAnnotation(lockKey = "test2",waitTimeout = 5000)
    public String testApi2(){

        System.out.println(Thread.currentThread().getName() + " getLook..... " + DateUtil.now());

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "  doing somthing ......" + DateUtil.now());


        return "hello world";
    }
}

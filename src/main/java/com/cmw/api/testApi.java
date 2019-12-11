package com.cmw.api;

import cn.hutool.core.date.DateUtil;
import com.cmw.util.RedissLockUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
//        RLock lock = redissonClient.getLock(uuid);
        boolean res=false;
        try {
            System.out.println(name + "  start tryLook..." + DateUtil.now());
            res = RedissLockUtil.tryLock(uuid, TimeUnit.SECONDS, 10, 10);
//            res = lock.tryLock(10, 10, TimeUnit.SECONDS);
            if(res){
                System.out.println(name + " getLook..... " + DateUtil.now());
                Thread.sleep(5000);
                System.out.println(name + "  doing somthing ......" + DateUtil.now());
            }else{
                System.out.println(name + "  tryLock failure ......");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            boolean locked = lock.isLocked();
//            System.out.println(locked);
            if(res ){//释放锁
                RedissLockUtil.unlock(uuid);
//                lock.unlock();
            }
        }

        return "hello world";
    }

}

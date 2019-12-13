package com.cmw;

import cn.hutool.core.date.DateUtil;
import com.cmw.util.RedissLockUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chengmingwen
 */
@RunWith(SpringRunner.class) //作用：让当前类在容器环境下进行测试
//作用：声明当前类是springboot的测试类并且获取入口类上的相关信息
@SpringBootTest(classes = RedissonApplicationConfig.class)
public class testRedisson {



    @Test
    public void startSeckilRedisLock2() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5,5,10,
                TimeUnit.SECONDS,new ArrayBlockingQueue<>(10)
        );
        for (int i = 1; i < 3 ; i++){
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("star test,,,,,,,,,,,,");
                    String name = Thread.currentThread().getName();
                    String uuid = "uuid";
                    RLock lock = redissonClient.getLock(uuid);
                    boolean res=false;
                    try {
//                        System.out.println(name + "  start tryLook..." + DateUtil.now());
//            res = RedissLockUtil.tryLock(uuid, TimeUnit.SECONDS, 10, 5);
                        res = lock.tryLock(10, 10, TimeUnit.SECONDS);
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
                        boolean locked = lock.isLocked();
                        System.out.println(locked);
                        if(res && locked){//释放锁
//                RedissLockUtil.unlock(uuid);
                            lock.unlock();
                        }
                    }

                }
            });
        }
    }


    @Test
    public void startSeckilRedisLock() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
//                testLook();
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
//                testLook();

            }
        });
        thread.start();
        thread2.start();
    }


    @Test
    public void startRedisLock() {
        testLook();
    }

    private void testLook() {
        String name = Thread.currentThread().getName();
        String uuid = "uuid";
        boolean res=false;
        try {
            System.out.println(name + "  start tryLook..." + DateUtil.now());
            res = RedissLockUtil.tryLock(uuid, TimeUnit.SECONDS, 10, 10);
            if(res){
                Thread.sleep(5000);
                System.out.println(name + "  doing somthing ......" + DateUtil.now());
            }else{
                System.out.println(name + "  tryLock failure ......");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(res){//释放锁
                RedissLockUtil.unlock(uuid);
            }
        }
    }

    @Autowired
    private RedissonClient redissonClient;

    private void testLook2() {
        String name = Thread.currentThread().getName();
        String uuid = "uuid";
        RLock lock = redissonClient.getLock(uuid);
        boolean res=false;
        try {
            System.out.println(name + "  start tryLook..." + DateUtil.now());
//            res = RedissLockUtil.tryLock(uuid, TimeUnit.SECONDS, 10, 5);
            res = lock.tryLock(10, 35, TimeUnit.SECONDS);
            if(res){
                Thread.sleep(50000);
                System.out.println(name + "  doing somthing ......" + DateUtil.now());
            }else{
                System.out.println(name + "  tryLock failure ......");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boolean locked = lock.isLocked();
            System.out.println(locked);
            if(res && locked){//释放锁
//                RedissLockUtil.unlock(uuid);
                lock.unlock();
            }
        }

    }

    public void testStor(){

    }


}

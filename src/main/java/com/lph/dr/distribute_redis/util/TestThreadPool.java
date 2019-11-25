package com.lph.dr.distribute_redis.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestThreadPool{
    public static void main(String[] args){
        // 1. 创建线程池
        ExecutorService pool = Executors.newFixedThreadPool(5);

        ThreadPoolDemo tpd = new ThreadPoolDemo();

        // 2. 为线程池中线程分配任务
        //    submit(Callable<T> task)
        //    submit(Runnable task)

        for(int i=0; i<10; i++){
            pool.submit(tpd);
        }

        // 3. 关闭线程池
        pool.shutdown();
    }
}

class ThreadPoolDemo implements Runnable{

    private volatile int i=0;

    public void run(){
        while(i <= 100){
            System.out.println(Thread.currentThread().getName() + " : " + i++);
        }
    }
}
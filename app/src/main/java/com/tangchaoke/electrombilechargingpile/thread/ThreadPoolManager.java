package com.tangchaoke.electrombilechargingpile.thread;

import android.annotation.SuppressLint;


import com.tangchaoke.electrombilechargingpile.util.LoggerUtil;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理类
 */
public class ThreadPoolManager {

    private ThreadPoolManager() {
    }

    private static ThreadPoolManager instance = new ThreadPoolManager();

    public static ThreadPoolManager getInstance() {
        return instance;
    }

    //创建线程池
    //cpu*2+1
    public ThreadPoolProxy getNetThreadPool() {
        return new ThreadPoolProxy(5, 5, 60 * 1000);//当前管理5个线程，如果满员在创建5个，如果空闲就存活1m
    }


    public ThreadPoolProxy getFileThreadPool() {
        return new ThreadPoolProxy(3, 3, 60 * 1000);//当前管理3个线程，如果满员在创建3个，如果空闲就存活1m
    }

    //线程池代理类
    public class ThreadPoolProxy {

        private int maximumPoolSize;
        private long keepAliveTime;
        private int corePoolSize;

        public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            super();
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
            this.corePoolSize = corePoolSize;
        }

        private ThreadPoolExecutor threadPoolExecutor;

        @SuppressLint("NewApi")
        public void execute(Runnable runnable) {
            if (threadPoolExecutor == null) {
                /**
                 * corePoolSize:当前线程池内管理几个线程，3
                 * maximumPoolSize：当前线程满员时，会重新开启的新线程个数
                 * keepAliveTime：当前线程池如果没有可执行的任务，他会存活的时间
                 * unit：存活时间单位
                 * workQueue:存储Runnable的集合，10大小
                 */
                threadPoolExecutor = new ThreadPoolExecutor(
                        corePoolSize,
                        maximumPoolSize,
                        keepAliveTime,
                        TimeUnit.MILLISECONDS,
                        new LinkedBlockingDeque<Runnable>(10));
            }
            //执行
            try{
                threadPoolExecutor.execute(runnable);
            }catch (Exception e){
                e.printStackTrace();
                LoggerUtil.e("ThreadPoolManager","threadPoolExecutor.execute-error");
            }


        }

    }

}

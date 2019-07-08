package com.dingmk.comm.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 时间发生器
 * 减少System.currentTimeMillis()系统调用
 * @author Tony.Lau
 * @createTime 2018-05-28
 */
public enum TimeGen {
	
	INSTANCE;
	
	private final Logger log = LoggerFactory.getLogger(TimeGen.class);
	
	private ScheduledExecutorService ES = Executors.newScheduledThreadPool(1);
	
	private final TimeUpdater updater = new TimeUpdater();
	
	private int period = 1000;
	
	private long currMills = System.currentTimeMillis();
	
	private long currSecond = currMills / 1000;
	
	private final ReentrantLock TIME_LOCK = new ReentrantLock();
	
	private TimeGen(){
	    ES.scheduleAtFixedRate(updater, 0, period, TimeUnit.MILLISECONDS);
	}
	
	public long currMills(){
		return currMills; 
	}
	
	public long currSecond(){
		return currSecond;
	}
	
	long nextMills(long lastTime){
		try{
			TIME_LOCK.lock();
			if(currMills > lastTime){
				return currMills;
			}
			while(currMills <= lastTime){
				currMills = System.currentTimeMillis();
			}
			currSecond = currMills / 1000;
			return currMills;
		} finally{
			TIME_LOCK.unlock();
		}
	}
	
	private class TimeUpdater implements Runnable {

		@Override
		public void run() {
			try {
				nextMills(System.currentTimeMillis()-1);
			} catch (Exception e) {
				log.error("TimeUpdater is error", e);
			}
		}
	}

}

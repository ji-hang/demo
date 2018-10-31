package com.example.demo.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
@EnableScheduling
public class AynamicSchedulerTasks {

	@Scheduled(cron = "0/3 * *  * * * ")
	public void test() throws InterruptedException {
		System.err.println(Thread.currentThread().getName() + ": " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		Thread.currentThread().sleep(10000);
	}
	
	/**
	 * 以固定频率执行线程任务，可能设定的固定时间不足以完成线程任务，但是达到设定的延迟时间就要执行下一次了
	 */
	/*@Scheduled(fixedRate = 1000)
	public void test1() {
		System.err.println(Thread.currentThread().getName() + "： " + new SimpleDateFormat("yyyy-MM-ss HH:mm:ss").format(new Date()));
	}*/
}

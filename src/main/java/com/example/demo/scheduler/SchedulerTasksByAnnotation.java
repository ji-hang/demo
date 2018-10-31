package com.example.demo.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

/**
 * @author admin
 * SpringBoot本身默认的执行方式是串行执行，无论有多少task, 就算task在其他类也是用的同一个线程池中的同一个一个线程，都是一个线程串行执行，并行需要手动配置
 * 
 * 并行任务 
 * 继承SchedulingConfigurer类并重写其 configureTasks 方法
 *
 */
//@Component
@EnableScheduling
public class SchedulerTasksByAnnotation implements SchedulingConfigurer {
	
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar regist) {
		regist.setScheduler(Executors.newScheduledThreadPool(3));
	}
	

	/*@Scheduled(cron = "3/5 * *  * * * ")
	public void reportCurrentByCron() {
		System.out.println("job 从3秒开始每5秒执行一次: " + new SimpleDateFormat("yyyy-MM-ss HH:mm:ss").format(new Date()));
	}*/
	
	@Scheduled(cron = "0/3 * *  * * * ")
	public void test() throws InterruptedException {
		System.out.println(Thread.currentThread().getName() + ": " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		Thread.currentThread().sleep(10000);
	}
	
	/**
	 * 以固定频率执行线程任务，可能设定的固定时间不足以完成线程任务，但是达到设定的延迟时间就要执行下一次了
	 */
	/*@Scheduled(fixedRate = 1000)
	public void test1() {
		System.out.println(Thread.currentThread().getName() + ": " + new SimpleDateFormat("yyyy-MM-ss HH:mm:ss").format(new Date()));
	}*/

	/**
	 * 以固定延迟（时间）来执行线程任务，每次都要把任务执行完成后再延迟固定时间后再执行下一次
	 */
	/*@Scheduled(fixedDelay = 10 * 1000)
	public void test2() {
		System.out.println("job 延迟10秒执行一次: " + new SimpleDateFormat("yyyy-MM-ss HH:mm:ss").format(new Date()));
	}*/
}

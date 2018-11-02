package com.example.demo.scheduler.quartz;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author admin 
 * 1.一个jobDetail可以用多个trigger，但是一个trigger不能有多个jobDetail,会报（Trigger
 *         does not reference given job!）
 * 
 */
@Component
public class InitTasks {

	@Autowired
	private SchedulerFactory schedulerFactoryBean;
	
	/**
	 * TriggerBuilder.startAt(getStart()) 参数，在什么时候启用调度器，启用后，任务不会立即出发，而是根据配置的触发时间触发
	 * Trigger trigger = TriggerBuilder.newTrigger().startAt(getStart()).endAt(getEnd()).withIdentity("name_" + id, "group_" + id).withSchedule(cron)
				.build();
	 * @return
	 */
	public Date getStart(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.SECOND, 30);
		return c.getTime();
	}
	
	/**
	 * TriggerBuilder.endAt(getEnd()) 参数，在什么时候停用调度器
	 * @return
	 */
	public Date getEnd(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MINUTE, 1);
		return c.getTime();
	}
	
	/**
	 * 每个任务都有自己的触发器，触发器设置了触发时间
	 * 
	 * @param id
	 * @param cronStr
	 * @throws SchedulerException
	 */
	public void addTask(String id, String cronStr) throws SchedulerException {
		Task task = new Task();
		task.setId(id);
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		// 表达式调度构建器
		CronScheduleBuilder cron = CronScheduleBuilder.cronSchedule(cronStr);

		// 调度器
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("name_" + id, "group_" + id).withSchedule(cron)
				.build();

		// 添加具体的任务方法
		JobDetail job = JobBuilder.newJob(QuartzJob.class).withIdentity("name_" + id, "group_" + id).build();
		job.getJobDataMap().put("task", task);

		scheduler.scheduleJob(job, trigger);
		// 启动，不调用此方法，定时任务不会执行
		scheduler.start();
	}

	/**
	 * 多个jobDetail设置同一个触发器,会报错(Trigger does not reference given job!)
	 * @param cronStr
	 * @throws SchedulerException
	 * @throws InterruptedException 
	 */
	public void addTaskTrigger(String id, String cronStr) throws SchedulerException, InterruptedException {
		
		//得到调度器
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		
		//构建cron表达式
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronStr);
		
		//构建触发器key
		TriggerKey key = TriggerKey.triggerKey("jj", "admin");
		
		//根据triggerKey得到
		Trigger trigger = null;
		
		if(scheduler.checkExists(key)){
			trigger = scheduler.getTrigger(key);
		}else{
			trigger = TriggerBuilder.newTrigger().withIdentity(key).withSchedule(cronScheduleBuilder).build();
		}
		
		//构建工作细节
		JobKey jobKey = JobKey.jobKey("name_" + id, "group_" + id);
		JobDetail job = JobBuilder.newJob(QuartzJob.class).withIdentity(jobKey).build();
		
		//传递的参数
		Task task = new Task();
		task.setId(id);
		job.getJobDataMap().put("task", task);
		
		//加入调度器
		scheduler.scheduleJob(job, trigger);
	}
	
	/**
	 * @param id
	 * 暂停任务
	 * 经测试，就算在队列中的任务 也会被移除，不会执行
	 * 但是正在执行的任务，不会停止，会执行完
	 * @throws SchedulerException 
	 */
	public void suspendTask(String id) throws SchedulerException{
		
		//得到调度器
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		
		JobKey jobKey = JobKey.jobKey("name_" + id, "group_" + id);
		
		scheduler.pauseJob(jobKey);
		
	}
	
	/**
	 * 恢复暂停的任务
	 * 恢复后任务不会立即执行一次，而是要等到执行时间后被触发
	 * @param string
	 * @throws SchedulerException 
	 */
	public void recoveryTask(String id) throws SchedulerException {
		//得到调度器
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		
		JobKey jobKey = JobKey.jobKey("name_" + id, "group_" + id);
		
		scheduler.resumeJob(jobKey);
	}
	
	
	/**
	 * 删除任务
	 * 删除的任务是不能恢复的，只能重新添加
	 * @param string
	 * @throws SchedulerException 
	 */
	public void delTask(String id) throws SchedulerException {
		//得到调度器
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		
		JobKey jobKey = JobKey.jobKey("name_" + id, "group_" + id);
		
		scheduler.deleteJob(jobKey);
	}


	/**
	 * 在用JobBuilder创建JobDetail的时候，有一个storeDurably()方法或者是storeDurably(true),传入false与不使用这个方法一样，
	 * 可以在没有触发器指向任务的时候，使用sched.addJob(job, true) 将任务保存在队列中了。而后使用 sched.scheduleJob 触发。如果不使用
	 * storeDurably ，则在添加 Job 到引擎的时候会抛异常，意思就是该 Job 没有对应的 Trigger。
	 * @param id
	 * @throws SchedulerException
	 * @throws InterruptedException
	 */
	public void noTrigger(String id) throws SchedulerException, InterruptedException {
		
		//得到调度器
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		//构建工作细节
		JobKey jobKey = JobKey.jobKey("name_" + id, "group_" + id);
		JobDetail job = JobBuilder.newJob(QuartzJob.class).withIdentity(jobKey).storeDurably(true).build();
		
		//传递的参数
		Task task = new Task();
		task.setId(id);
		job.getJobDataMap().put("task", task);
		
		scheduler.addJob(job, false);
		//虽然要用scheduler.triggerJob(jobKey);触发该任务，但是不调用scheduler.start()方法，依旧不能触发
		scheduler.start();
	}
	
	/**
	 * 一个jobDetail对应多个trigger
	 * 配置多个trigger时，第二个及其以上的必须 TriggerBuilder.newTrigger().forJob(jobKey) 使用forJob()方法
	 * 详情看代码，否则会报错
	 * 
	 * 由于是同一个jobDetail，又配置了@DisallowConcurrentExecution，所以依旧时同一时刻只有一个线程执行，是串行的
	 * 
	 * @param string
	 * @throws SchedulerException 
	 */
	public void testMoreTrigger(String id) throws SchedulerException {
		
		//得到调度器
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		
		//构建jobDetail
		JobKey jobKey = JobKey.jobKey("name_" + id, "group_" + id);
		JobDetail job = JobBuilder.newJob(QuartzJob.class).withIdentity(jobKey).build();
		
		Task task = new Task();
		task.setId(id);
		job.getJobDataMap().put("task", task);
		
		//构建多个trigger
		CronScheduleBuilder cronScheduleBuilder1 = CronScheduleBuilder.cronSchedule("0/5 * * * * ?");
		TriggerKey triggerKey1 = TriggerKey.triggerKey("name_1" + id, "group_1" + id);
		Trigger trigger1 = TriggerBuilder.newTrigger().withDescription("trigger1").withSchedule(cronScheduleBuilder1).withIdentity(triggerKey1).build();
		
		CronScheduleBuilder cronScheduleBuilder2 = CronScheduleBuilder.cronSchedule("0/1 * * * * ?");
		TriggerKey triggerKey2 = TriggerKey.triggerKey("name_2" + id, "group_2" + id);
		Trigger trigger2 = TriggerBuilder.newTrigger().forJob(jobKey).withDescription("trigger2").withSchedule(cronScheduleBuilder2).withIdentity(triggerKey2).build();
		
		scheduler.scheduleJob(job, trigger1);
		scheduler.scheduleJob(trigger2);
		
		//必须调用启动方法
		scheduler.start();
	}

	@PostConstruct
	public void test() throws SchedulerException, InterruptedException {
		
		//
		//addTask("11111", "0/5 * * * * ?");
		
		//测试新增任务
		//addTask("11111", "0/5 * * * * ?"); 
		//addTask("22222", "0/5 * * * * ?");
		 

		//测试多个 jobDetail公用一个trigger,回报错
		//addTaskTrigger("111111", "0/5 * * * * ?"); 
		//addTaskTrigger("222222", "0/5 * * * * ?");
		
		//测试一个 jobDetail 使用多个trigger
		//testMoreTrigger("111111");
		
		// 测试没有trgger的任务
		/*noTrigger("1111");
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey("name_1111", "group_1111");
		scheduler.triggerJob(jobKey);*/
		
		
		// 测试暂停 恢复 删除任务
		// 1. 先添加几个任务
		//addTask("11111", "0/5 * * * * ?");
		//addTask("22222", "0/5 * * * * ?");
		//addTask("33333", "0/5 * * * * ?");
		//addTask("44444", "0/5 * * * * ?");
//		Thread.currentThread().sleep(5000);
//		// 2.暂停任务
//		System.out.println("暂停任务22222");
//		suspendTask("22222");
//		
//		// 3.恢复任务 
//		Thread.currentThread().sleep(20000);
//		System.out.println("恢复任务22222");
//		recoveryTask("22222");
//		
//		// 4.删除任务
//		Thread.currentThread().sleep(20000);
//		System.out.println("删除任务22222");
//		delTask("22222");
		
	}

}

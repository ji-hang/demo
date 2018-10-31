package com.example.demo.scheduler.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author admin
 * @DisallowConcurrentExecution 作用：
 * 如果任务到时被触发，但是上一次的任务还未执行完，则该任务进入等待状态，执行完后立即执行
 * 对同一个jobDetail而已，不同的jobDetail仍旧可以并行
 *
 */
@DisallowConcurrentExecution
public class QuartzJob implements Job  {

	private static final Logger logger = LoggerFactory.getLogger(QuartzJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String result = "";
		JobDetail job = context.getJobDetail();
		Task task = (Task) job.getJobDataMap().get("task");
		String desc = context.getTrigger().getDescription();
		if(StringUtils.isEmpty(desc)){
			result = Thread.currentThread().getName() + ": " + task.getId() + " - " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		}else{
			result = Thread.currentThread().getName() + ": trigger desc【"+ desc + " 】: " + task.getId() + " - " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		}
		logger.info(result);
		
		String a = null;
		/*try {
			Thread.currentThread().sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*result += "结束***************";
		System.out.println(result);*/
	}

}

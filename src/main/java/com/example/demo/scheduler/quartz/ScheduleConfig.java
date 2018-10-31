package com.example.demo.scheduler.quartz;

import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScheduleConfig {

	@Bean(name="schedulerFactoryBean")
	public SchedulerFactory getSchedulerFactoryBean(){
		return new StdSchedulerFactory();
	}

}

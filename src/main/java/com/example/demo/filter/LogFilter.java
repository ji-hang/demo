package com.example.demo.filter;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Service;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;


@Service
public class LogFilter extends Filter<ILoggingEvent> {
	
	public static ThreadLocal<SimpleDateFormat> local = new ThreadLocal<SimpleDateFormat>(){
		
		@Override
		public SimpleDateFormat initialValue() {
	        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    }
	};
	

	@Override
	public FilterReply decide(ILoggingEvent event) {

		String exception = "";
		IThrowableProxy proxy = event.getThrowableProxy();
		if (proxy != null) {
			exception = proxy.getClassName() + "：" + proxy.getMessage() + "\n";
			for (int i = 0; i < proxy.getStackTraceElementProxyArray().length; i++) {
				exception += proxy.getStackTraceElementProxyArray()[i].toString() + "\n";
			}
		}
		LogMessage message = new LogMessage(event.getMessage(), 
				local.get().format(event.getTimeStamp()), 
				event.getThreadName(), event.getLoggerName(), 
				event.getLevel().levelStr, 
				exception, "");
		LogQueen.getInstance().push(message);
		return FilterReply.ACCEPT;
	}

}

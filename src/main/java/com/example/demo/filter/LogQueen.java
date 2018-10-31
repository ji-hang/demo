package com.example.demo.filter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class LogQueen {

	/**
	 * 队列最大大小
	 */
	private static final Integer QUEEN_MAX_SIZE = 10000;

	private static LogQueen logQueen = new LogQueen();

	private BlockingQueue<LogMessage> blockingQueue = new LinkedBlockingDeque<>(QUEEN_MAX_SIZE);

	public static LogQueen getInstance() {
		return logQueen;
	}

	public boolean push(LogMessage msg) {
		return this.blockingQueue.add(msg);
	}

	public LogMessage poll() {
		LogMessage result = null;
		try {
			result = (LogMessage) this.blockingQueue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}
}

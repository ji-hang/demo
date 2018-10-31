package com.example.demo.lock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Data;

/**
 * 基于zookeeper的分布式锁
 * @author admin
 *
 */
@Data
public class DistributedLock implements Lock, Watcher {
	
	private static final Logger logger = LoggerFactory.getLogger(DistributedLock.class);
	
	private String url = "118.24.160.133:2181";
	
	//分割符
	private String split = "_split_";
	
	private ZooKeeper zk;
	
	//根节点
	private String root = "/locks";

	//竞争的资源名
	private String lockName;
	
	//等待的前一个锁
	private String waitLock;
	
	//当前锁
	private String currentLock;
	
	//当前线程名
	private String threadName = Thread.currentThread().getName();
	
	//计数器
	private CountDownLatch countDownLatch;
	
	private int sessionTimeout = 30000;
	
	private List<Exception> exceptions = new ArrayList<>();
	
	
	/**
	 * 配置分布式锁
	 * @param lockName
	 */
	public DistributedLock(String lockName) {
		if(StringUtils.isBlank(lockName)){
			throw new RuntimeException("锁名不能为空...");
		}
		this.lockName = lockName;
		try{
			// 连接zookeeper
			this.zk = new ZooKeeper(url, sessionTimeout, this);
			Stat stat = zk.exists(root, false);
			if(stat == null){
				//如果根节点不存在，则创建永久的根节点
				zk.create(root, "root".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		
	}
	
	@Override
	public void process(WatchedEvent arg0) {
		if (this.countDownLatch != null) {
            this.countDownLatch.countDown();
        }
	}
	
	
	@Override
	public void lock() {
		try{
			if(this.tryLock()){
				logger.info(String.format("当前线程【%s】获得了锁【%s】", threadName, lockName));
			}else{
				// 等待锁
				waitForLock(waitLock, sessionTimeout);
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
	}

	

	@Override
	public void lockInterruptibly() throws InterruptedException {
		this.lock();
	}

	@Override
	public boolean tryLock() {
		try{
			// 创建临时有序节点
			this.currentLock = zk.create(root + "/" + lockName + split, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			logger.info(String.format("当前节点【%s】已创建,当前线程【%s】的锁是【%s】", currentLock, threadName, currentLock));
			//取出所有子节点
			List<String> childNodes = zk.getChildren(root, false);
			//取出所有 lockName 的锁
			List<String> lockObjects = new ArrayList<String>();
			for (String child : childNodes) {
				String ch = child.split(split)[0];
				if(lockName.equals(ch)){
					lockObjects.add(child);
				}
			}
			Collections.sort(lockObjects);
			//若当前创建的节点为最小节点则获取锁成功
			if(currentLock.equals(root + "/" + lockObjects.get(0))){
				return true;
			}
			// 若不是最小节点，则找到自己的前一个节点
			String prev = currentLock.substring(currentLock.lastIndexOf("/") + 1);
			this.waitLock = lockObjects.get(Collections.binarySearch(lockObjects, prev) - 1);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return false;
	}
	
	private boolean waitForLock(String prev, int sessionTimeout2) throws KeeperException, InterruptedException {
		String node = root + "/" + prev;
		Stat stat = zk.exists(node, true);
		if(stat != null){
			logger.info(String.format("线程【%s】等待锁【%s】", threadName, node));
			this.countDownLatch = new CountDownLatch(1);
			this.countDownLatch.await();
			this.countDownLatch = null;
			logger.info(String.format("线程【%s】等到了锁【%s】", threadName, node));
		}
		return true;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		try{
			if(this.tryLock()){
				return true;
			}else{
				return waitForLock(waitLock, sessionTimeout);
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	@Override
	public void unlock() {
		try{
			logger.info(String.format("线程【%s】释放锁【%s】", threadName, currentLock));
			this.zk.delete(currentLock, -1);
			this.currentLock = null;
			zk.close();
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public Condition newCondition() {
		return null;
	}
	
	public static void main(String[] args) {
		
		for (int i = 0; i < 20; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					DistributedLock lock = new DistributedLock("test");
					if(lock.tryLock()){
						System.err.println("\n"+Thread.currentThread().getName() + "获得了锁并执行...\n");
						try {
							Thread.currentThread().sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}else{
						System.err.println("\n"+Thread.currentThread().getName() + "没有得到锁，结束...\n");
					}
					lock.unlock();
					
				}
			}).start();
		}
	}

}

package SychronizedResources;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


public class AttemptLocking {
	/**
	 * 允许尝试获取着获取锁，如果没有获取锁，可以离开做其他事情，而不是等待这个锁释放
	 */
	private ReentrantLock lock = new ReentrantLock();
	public void untimed(){
		boolean captured = lock.tryLock();
		try {
			System.out.println("tryLock() = "+captured);
		} finally {
			if(captured){
				lock.unlock();
			}
		}
	}
	public void timed(){
		boolean captured = false;
		try {
			captured = lock.tryLock(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		try {
			System.out.println("tryLock(2,second): "+captured);
		} finally {
			if(captured){
				lock.unlock();
			}
		}
	}
	public static void main(String[] args) throws InterruptedException {
		final AttemptLocking al = new AttemptLocking();
		al.untimed();//True -- lock is available
		al.timed();// True -- lock is available
		//now careate a separate task to grab the lock
		new Thread(){
			{
				setDaemon(true);
			}
			public void run(){
				al.lock.lock();
				System.out.println("acquired");
			}
		}.start();
		TimeUnit.MILLISECONDS.sleep(50);//Give the 2nd task a chance
		Thread.yield();	//Give the 2nd task a chance
		al.untimed();	//False -- lock grable by task
		al.timed();		//False -- lock grabbed by task
	}
}

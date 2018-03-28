package Interrupted;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BlockedMutex {
	//
	private Lock lock = new ReentrantLock();

	//构造器获得reentrantlock锁，不释放
	public BlockedMutex() {
		lock.lock();
	}

	public void f() {
		try {
			//请求锁
			lock.lockInterruptibly();
			System.out.println("lock acquired in f()");
		} catch (InterruptedException e) {
			System.out.println("Interrupted from lock acquisition in f()");
		}
	}
}

class Blocked2 implements Runnable {
	BlockedMutex blocked = new BlockedMutex();

	@Override
	public void run() {
		System.out.println("Waiting for f() in BlockedMutex()");
		blocked.f();
		System.out.println("Broken out of blocked call");
	}
}

public class Interrupting2 {
	public static void main(String[] args) throws InterruptedException {
		Thread t = new Thread(new Blocked2());
		//线程会一直请求锁，得不到然后就阻塞了
		t.start();
		TimeUnit.SECONDS.sleep(1);
		System.out.println("Issuing t.interrupt()");
		//发送interrupt
		t.interrupt();
	}
}

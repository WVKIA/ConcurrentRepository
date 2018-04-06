package SychronizedResources;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * 使用concurrent包的Lock锁
 * Lock对象必须被显式的创建、锁定和释放
 * @author wk
 * 
 *
 */
public class MutexEvenGenerator extends IntGenerator {
	private int currentEvenValue = 0;
	private Lock lock = new ReentrantLock();
	

	@Override
	public int next() {
		lock.lock();
		try {
			++currentEvenValue;
			Thread.yield();
			++currentEvenValue;
			//return语句必须在try字句出现，确保unlock不会过早发生
			return currentEvenValue;
		}finally {
			lock.unlock();
		}
	}
	public static void main(String[] args) {
		EvenChecker.test(new MutexEvenGenerator());
	}

}

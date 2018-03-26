package SychronizedResources;

public class SynchronizedEvenGenerator extends IntGenerator {

	private int currentEvenValue = 0;
	/**
	 * 使用synchronized内建锁关键字实现同步
	 */
	@Override
	public synchronized int next() {
		++currentEvenValue;
		Thread.yield();
		++currentEvenValue;
		return currentEvenValue;
	}
	public static void main(String[] args) {
		EvenChecker.test(new SynchronizedEvenGenerator());
	}

}

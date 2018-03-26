package SychronizedResources;
/**
 * 线程不安全的版本
 * @author wk
 *
 */
public class EvenGenerator extends IntGenerator {

	private int currentEvenValue = 0;
	/**
	 * 采用不安全的写法，可能引起线程同步问题
	 */
	@Override
	public int next() {
		++currentEvenValue;	//	danger point here
		Thread.yield();
		++currentEvenValue;
		return currentEvenValue;
	}
	public static void main(String[] args) {
		EvenChecker.test(new EvenGenerator());
	}

}

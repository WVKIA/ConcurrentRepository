package Atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AtomicityTest implements Runnable{
	private  int i=0;
	//虽然evenIncrement是线程同步，而且return i也是原子操作，但是缺少同步使其数值可以处于不稳定的中间态时被读取，
	//而且“i”也不是volatile，因此也存在可视性问题
	//就是有可能i已经被改变，但只是在线程的本地内存，并没有被刷入主存，导致其他线程无法读取最新值
	public int getValue(){
		return i;
	}
	private synchronized void evenIncrement(){
		i++;
		i++;
	}
	public void run(){
		while(true){
			evenIncrement();
		}
	}
	public static void main(String[] args) {
		ExecutorService excu = Executors.newCachedThreadPool();
		AtomicityTest at = new AtomicityTest();
		excu.execute(at);
		while(true){
			int val = at.getValue();
			if(val % 2 !=0){
				System.out.println(val);
				System.exit(0);
			}
		}
	}
}

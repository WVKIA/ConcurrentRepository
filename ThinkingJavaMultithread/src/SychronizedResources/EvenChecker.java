package SychronizedResources;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//偶数检查
public class EvenChecker implements Runnable {
	private IntGenerator generator;
	private final int id;
	public EvenChecker(IntGenerator i,int id) {
		generator = i;
		this.id = id;
	}
	public void run() {
		//取消标志表示任务完成
		while(!generator.isCanceled()){
			int val = generator.next();
			if(val % 2 != 0){
				System.out.println(val + " not even");
				generator.cancel();//cancels all evencheckers
			}
		}
	}
	public static void test(IntGenerator gp,int count){
		System.out.println("press controll -c to exit");
		ExecutorService exce = Executors.newCachedThreadPool();
		for(int i=0;i < count;i++){
			exce.execute(new EvenChecker(gp, i));
		}
		exce.shutdown();
	}
	public static void test(IntGenerator gp){
		test(gp,10);
	}
}



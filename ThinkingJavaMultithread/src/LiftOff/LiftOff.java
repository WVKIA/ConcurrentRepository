package LiftOff;

/**
 * 倒计时
 * 
 * @author wk
 *关灯任务
 */
public class LiftOff implements Runnable {

	protected int countDown = 10;
	private static int taskCount = 0;
	private final int id = taskCount++;
	public LiftOff() {
	}
	public LiftOff(int countDown){
		this.countDown = countDown;
	}
	public String status(){
		return "#"+id+" " + (countDown > 0 ? countDown:"LIftoff!");
	}


	@Override
	public void run() {
		while(countDown-- > 0){
			System.out.println(status());
			Thread.yield();		//线程调度器的建议，表示：当前线程已经完成生命周期最重要的部分，可以把CPU执行权交给其他线程
		}
	}

}

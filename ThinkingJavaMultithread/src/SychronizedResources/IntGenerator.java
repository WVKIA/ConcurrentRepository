package SychronizedResources;

/**
 * 消费者任务
 */
public abstract class IntGenerator {
	//保证可见性
	private volatile boolean canceled =false;
	public abstract int next();
	//allow this to be canceled
	public void cancel(){
		canceled = true;
	}
	public boolean isCanceled(){
		return canceled;
	}
}

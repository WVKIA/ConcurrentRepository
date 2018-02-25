package Sychronized;

public abstract class IntGenerator {
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

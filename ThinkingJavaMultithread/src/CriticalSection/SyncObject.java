package CriticalSection;

class DualSynch {
	private Object sysncObject = new Object();

	public synchronized void f() {
		for (int i = 0; i < 5; i++) {
			System.out.println("f()");
			Thread.yield();
		}
	}

	public void g() {
		synchronized(sysncObject){
			
			for (int i = 0; i < 5; i++) {
				System.out.println("g()");
				Thread.yield();
			}
		}
	}
}

public class SyncObject {
	public static void main(String[] args) {
		final DualSynch sd = new DualSynch();
		new Thread(){
			public void run(){
				sd.f();
			}
		}.start();
		sd.g();
	}
}

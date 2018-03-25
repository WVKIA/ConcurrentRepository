package LiftOff;

public class BasicThread {
	public static void main(String[] args) {
		Thread t = new Thread(new LiftOff());
		t.start();	//此时 t线程和main线程同时执行
		System.out.println("waiting for Liftoff");
	}
}

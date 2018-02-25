package Interrupted;

import java.util.concurrent.TimeUnit;

class NeedsCleanUp {
	private final int id;

	public NeedsCleanUp(int id) {
		this.id = id;
		System.out.println("needscleanip " + id);
	}

	public void cleanup() {
		// TODO Auto-generated method stub
		System.out.println("Cleaning up " + id);
	}

}

class Blocked3 implements Runnable {
	private volatile double d = 0.0;

	public void run() {
		try {
			while (!Thread.interrupted()) {
				// point1
				NeedsCleanUp n1 = new NeedsCleanUp(1);
				// start try finally immediate after definition
				// of n1,to guaranteeproper cleanup of n1
				try {
					System.out.println("Sleeping");
					TimeUnit.SECONDS.sleep(1);
					// point2
					NeedsCleanUp n2 = new NeedsCleanUp(2);
					try {
						System.out.println("Calculating");
						for (int i = 0; i < 250000; i++) {
							d = d + (Math.PI + Math.E) / d;
						}
						System.out.println("Finished time-consuming operation");
					} finally {
						n2.cleanup();
					}
				} finally {
					n1.cleanup();
				}
			}
			System.out.println("Exiting via while() test");
		} catch (InterruptedException e) {
			System.out.println("Exiting via interruptedException");
		}
	}
}

public class InterruptingIdiom {
	public static void main(String[] args) {
		if(args.length != 1){
			
		}
	}
}

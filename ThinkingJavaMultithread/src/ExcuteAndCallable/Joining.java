package ExcuteAndCallable;

class Sleeper extends Thread {
	private int duration;

	public Sleeper(String name, int sleeptime) {
		super(name);
		duration = sleeptime;
		start();
	}

	public void run() {
		try {
			sleep(duration);
		} catch (InterruptedException e) {
			System.out.println(getName() + " was interrupted " + "isInterrupted(): " + isInterrupted());
		}
		System.out.println(getName() + " has awakened");
	}
}

class Joiner extends Thread {
	private Sleeper sleeper;

	public Joiner(String name, Sleeper sleeper) {
		super(name);
		this.sleeper = sleeper;
		start();
	}

	public void run() {
		try {
			sleeper.join();
		} catch (InterruptedException e) {
			System.out.println(getName() + " was interrupted " + "isInterrupted(): " + isInterrupted());
		}
		System.out.println(getName() + " join compeleted");
	}
}

public class Joining {
	public static void main(String[] args) {
		Sleeper sleepy = new Sleeper("sleeper", 1500), grumy = new Sleeper("grumy", 15000);
		Joiner dopey = new Joiner("doppey", sleepy), doc = new Joiner("doc", grumy);
		grumy.interrupt();
	}
}

package Sychronized;

public class ThreadTestA {
	public static void main(String[] args) {
		Object o = new Object();
		final printEvenOdd pe = new printEvenOdd();
		Thread a= new Thread(){
			@Override
			public void run() {
				pe.Even();
			}
		};
		Thread b = new Thread() {
			@Override
			public void run() {
				pe.Odd();
			}
		};
		a.start();
		b.start();
	}
}

// 交替输出奇偶数
class printEvenOdd {
	private  Object LOCK = new Object();

	private int num = 0;

	// 同理，这个synchronized括号里面的可以是this、TA.class等等
	public void Even() {
		while (num < 50) {
			synchronized (LOCK) {
				if (num % 2 == 0) {
					System.out.println(Thread.currentThread().getName() + " run " + num++);

					LOCK.notify();
				} else {
					try {
						System.out.println(Thread.currentThread().getName() + " wait ");
						LOCK.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void Odd() {
		while (num < 50) {
			synchronized (LOCK) {
				if (num % 2 != 0) {
					System.out.println(Thread.currentThread().getName() + " run " + num++);
					LOCK.notify();
				} else {
					try {
						System.out.println(Thread.currentThread().getName() + " wait ");
						LOCK.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}

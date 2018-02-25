package Excute;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class TaskWithResult implements Callable<String> {

	private int id;

	public TaskWithResult(int id) {
		this.id = id;
	}

	@Override
	public String call() throws Exception {

		return "result of TaskWithResult" + id;
	}

}

public class CallableDemo {
	public static void main(String[] args) {
		ExecutorService excu  = Executors.newCachedThreadPool();
		List<Future<String>> results = new ArrayList<>();
		for(int i=0;i < 5;i++){
			results.add(excu.submit(new TaskWithResult(i)));
		}
		for (Future<String> future : results) {
			try {
				System.out.println(future.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			} catch (ExecutionException e) {
				e.printStackTrace();
				return;
			}finally {
				excu.shutdown();
			}
		}
	}
}

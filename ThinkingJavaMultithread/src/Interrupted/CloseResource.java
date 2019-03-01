package Interrupted;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 用来关闭IO的阻塞
 */
public class CloseResource {
	public static void main(String[] args) throws Exception {
		ExecutorService ex = Executors.newCachedThreadPool();
		ServerSocket server = new ServerSocket(8080);
		InputStream socketInput = new Socket("localhost", 8080).getInputStream();
		ex.execute(new IOBlocked(socketInput));
		ex.execute(new IOBlocked(System.in));
		System.out.println("IO资源被锁定 -> ");
		TimeUnit.MILLISECONDS.sleep(100);
		System.out.println("shutting down al threads");
		//发出中断
		ex.shutdownNow();

		TimeUnit.SECONDS.sleep(1);
		System.out.println("Closing " + socketInput.getClass().getName());
		socketInput.close();
		TimeUnit.SECONDS.sleep(1);
		System.out.println("Closing " + System.in.getClass().getName());
		System.in.close();
	}
}

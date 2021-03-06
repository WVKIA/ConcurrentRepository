package Interrupted;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class NIOBlocked implements Runnable {
	private final SocketChannel sc;

	public NIOBlocked(SocketChannel sc) {
		this.sc = sc;
	}

	@Override
	public void run() {
		try {
			System.out.println("waiting for read() in " + this);
			sc.read(ByteBuffer.allocate(1));
		} catch (ClosedByInterruptException e) {
			System.out.println("closedbyinterruptexception");
		} catch (AsynchronousCloseException e) {
			System.out.println("asynchronousCLoseException");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		System.out.println("Exiting NIOBlocked run()" + this);
	}
}

public class NIOInterruption {
	public static void main(String[] args) throws Exception {
		ExecutorService e = Executors.newCachedThreadPool();
		ServerSocket server = new ServerSocket(8080);
		InetSocketAddress isa = new InetSocketAddress("localhost", 8080);
		SocketChannel sc1 = SocketChannel.open(isa);
		SocketChannel sc2 = SocketChannel.open(isa);
		//通过submit执行
		Future<?> f = e.submit(new NIOBlocked(sc1));
		//通过execute执行
		e.execute(new NIOBlocked(sc2));
		e.shutdown();
		TimeUnit.SECONDS.sleep(1);


		System.out.println("submit 执行的NIO通过调用future.cancel中断 --> ");
		//通过cancel可以中断
		f.cancel(true);


		TimeUnit.SECONDS.sleep(1);
		//通过close也可以中断
		System.out.println("execute 执行的NIO通过调用资源close中断 --> ");

		sc2.close();


	}
}

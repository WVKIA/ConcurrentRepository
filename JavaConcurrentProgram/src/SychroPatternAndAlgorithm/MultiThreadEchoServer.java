package SychroPatternAndAlgorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wukai
 * @date 2019/5/5
 */
public class MultiThreadEchoServer {
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    static class HandleMsg implements Runnable {
        Socket clientSocket;

        public HandleMsg(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }


        @Override
        public void run() {
            BufferedReader bufferedReader = null;
            PrintWriter printWriter = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                printWriter = new PrintWriter(clientSocket.getOutputStream(), true);

                String line = null;
                long time = System.currentTimeMillis();
                while ((line = bufferedReader.readLine()) != null) {
                    printWriter.println(line);
                }

                System.out.println("用时 " + (System.currentTimeMillis() - time));
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                printWriter.close();
            }
        }
    }

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket clientsocket = null;
        try {
            serverSocket = new ServerSocket(8080);

        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                clientsocket = serverSocket.accept();
                System.out.println(clientsocket.getRemoteSocketAddress() + " connet");
                executorService.execute(new HandleMsg(clientsocket));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

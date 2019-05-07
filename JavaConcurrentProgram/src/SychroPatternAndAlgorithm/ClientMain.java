package SychroPatternAndAlgorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author wukai
 * @date 2019/5/5
 */
public class ClientMain {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;
        Socket client = null;
        client = new Socket();
        client.connect(new InetSocketAddress("localhost", 8080));
        printWriter = new PrintWriter(client.getOutputStream(), true);
        printWriter.write("hello");

        printWriter.flush();

        bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        System.out.println("服务器读取 " + bufferedReader.readLine());
    }
}

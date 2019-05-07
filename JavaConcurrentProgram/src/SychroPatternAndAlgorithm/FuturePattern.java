package SychroPatternAndAlgorithm;

import java.util.concurrent.TimeUnit;

/**
 * @author wukai
 * @date 2019/5/5
 */
interface Data{
    public String getResult();
}

/**
 * Future模式获取数据，realData的代理，封装realdata的等待过程
 */
class FutureData implements Data {
    private RealData realData;
    private boolean ready = false;

    public synchronized void setRealData(RealData realData) {
        if (ready) {
            return;
        }
        this.realData = realData;
        ready = true;
        notifyAll();
    }

    @Override
    public synchronized String getResult() {
        while (!ready) {
            try {
                wait();     //一直等待，直到realdata被注入
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return realData.getResult();//由realdata返回真实数据
    }
}

/**
 * 真正处理数据的部分，但处理速度很慢
 */
class RealData implements Data {
    private String result;
    public RealData(String param) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append(param).append("_");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        result = sb.toString();
    }

    @Override
    public String getResult() {

        return result;
    }
}
class Client {
    public Data request(final String string) {
        final FutureData futureData = new FutureData();
        new Thread() {
            @Override
            public void run() {
                RealData realData = new RealData(string);
                futureData.setRealData(realData);
            }
        }.start();
        return futureData;
    }
}
public class FuturePattern {
    public static void main(String[] args) throws InterruptedException {
        Client client = new Client();
        Data data = client.request("haha");
        System.out.println("请求结束");
        TimeUnit.SECONDS.sleep(1);//做自己的其他事情
        System.out.println("再来获取数据 + "+data.getResult());

    }
}

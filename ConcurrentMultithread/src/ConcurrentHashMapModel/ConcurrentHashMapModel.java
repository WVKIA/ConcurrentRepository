package ConcurrentHashMapModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wukai
 * @date 2019/3/10
 */
public class ConcurrentHashMapModel {
    public static void main(String[] args) {
        final Map<String, Integer> map = new ConcurrentHashMap<>();

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            final int finalI = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    map.put("" + finalI, finalI/2);
                }
            });
        }

        final Map<String, Integer> nonConMap = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            final int finalI = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    nonConMap.put("" + finalI, finalI/2);
                }
            });
        }

        executorService.shutdown();
        while (!executorService.isTerminated()) {

        }
        System.out.println(map);
        System.out.println(nonConMap);

    }

    public static void test() {

    }
}

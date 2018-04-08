package ActiveObject;

import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class ActiveObjectDemo {
    private ExecutorService ex = Executors.newSingleThreadExecutor();
    private Random random = new Random(47);

    private void pause(int factor) {
        try {
            TimeUnit.MILLISECONDS.sleep(100 + random.nextInt(factor));
        } catch (InterruptedException e) {
            System.out.println(" Sleep() interrupted");
        }
    }

    public Future<Integer> calculateInt(final int x, final int y) {
        return ex.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("Starting " + x + " + " + y);
                pause(200);
                return x + y;
            }
        });
    }

    public Future<Float> calculateFloat(final float x, final float y) {
        return ex.submit(new Callable<Float>() {
            @Override
            public Float call() throws Exception {
                System.out.println("Starting " + x + " + " + y);
                pause(200);
                return x + y;
            }
        });
    }

    public void shutdown(){
        ex.shutdown();
    }

    public static void main(String[] args) {
        ActiveObjectDemo demo = new ActiveObjectDemo();

        //
        List<Future<?>> results = new CopyOnWriteArrayList<>();
        for(float f=0.0f; f < 1.0f; f+=0.2f) {
            results.add(demo.calculateFloat(f, f));
        }
        for (int i = 0; i < 5; i++) {
            results.add(demo.calculateInt(i, i));
        }

        System.out.println("All asynch calls made.....................");
        while (results.size() > 0) {
            for (Future<?> future : results) {
                if (future.isDone()) {
                    try {
                        System.out.println(future.get());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    results.remove(future);
                }
            }
        }
        demo.shutdown();

    }
}
